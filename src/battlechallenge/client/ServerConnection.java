package battlechallenge.client;

import java.util.List;
import java.util.Map;

import battlechallenge.ActionResult;
import battlechallenge.CommunicationConstants;
import battlechallenge.Coordinate;
import battlechallenge.Ship;
import battlechallenge.bot.ClientPlayer;
import battlechallenge.bot.DavidBot;
import battlechallenge.network.ConnectionLostException;
import battlechallenge.network.NetworkSocket;

public class ServerConnection {

	private int id;
	private NetworkSocket socket;
	private ClientPlayer bot;
	private String name;
	
	/**
	 * Constructor that creates a client socket and request and set
	 * the name of the ClientPlayer. The server connection will then 
	 * call the run method
	 * 
	 * @param port The port to connect to the server
	 * @param ip The ip address of the server
	 * @param name The name to identify the player
	 */
	public ServerConnection(int port, String ip, String name) {
		socket = new NetworkSocket(ip, port);
		this.name = name;
		this.run();
	}

	public void kill() {
		socket.kill();
	}

	public void run() {
		String req;
		while (true) {
			try {
				req = (String) socket.readObject(true);
				/*
				 * [[ HANDLE REQUESTS ]]
				 */
				if (req.equals(CommunicationConstants.REQUEST_HANDSHAKE))
					setupHandShake();
				if (req.equals(CommunicationConstants.REQUEST_CREDENTIALS))
					setCredentials(name);
				if (req.equals(CommunicationConstants.REQUEST_PLACE_SHIPS))
					placeShips();
				if (req.equals(CommunicationConstants.REQUEST_DO_TURN))
					doTurn();
				/*
				 * [[ HANDLE RESULTS ]]
				 */
				if (req.equals(CommunicationConstants.RESULT_DISQUALIFIED)) {
					System.out.println("You have been disqualified from the game");
					break;
				}
				if (req.equals(CommunicationConstants.RESULT_WIN)) {
					System.out.println("You have WON the game!!");
					break;
				}
				if (req.equals(CommunicationConstants.RESULT_LOSE)) {
					System.out.println("You have lost the game.");
					break;
				}
			} catch (ConnectionLostException e) {
				System.err.println("Socket Exception: Connection lost, disconnecting.");
				this.kill();
				break;
			} catch (ClassCastException e) {
				System.err.println("Network Exception: Unexpected game object from server. Check server version.");
			}
		}
		System.out.println("The game is over.");
		this.kill();
	}

	public void setupHandShake() {
		try {
			// check that server version is supported
			if (CommunicationConstants.SUPPORTED_SERVERS.contains((String)socket.readObject(true))) {
				socket.writeObject(CommunicationConstants.CLIENT_VERSION);
				return;
			}
		} catch (ConnectionLostException e) {
			System.err.println("Socket Exception: Connection lost, disconnecting.");
			this.kill();
		} catch (ClassCastException e) {
			System.err.println("Network Exception: Unexpected game object from server. Check server version.");
		}
		System.err.println("Server is not supported");
		this.kill();
		System.exit(0);
	}

	public void setCredentials(String name) {
		try {
			id = socket.readInt(true);
			int width = socket.readInt(true);
			int height = socket.readInt(true);
			bot = new DavidBot(name, width, height, id);
			System.out.println("bot generated with network ID: " + id);
			// send name to server
			socket.writeObject(name);
			return;
		} catch (ConnectionLostException e) {
			System.err.println("Socket Exception: Connection lost, disconnecting.");
			this.kill();
		}
		System.err.println("Could not set ID");
		this.kill();
		System.exit(0);
	}

	public void placeShips() {
		try {
			@SuppressWarnings("unchecked")
			List<Ship> ships = (List<Ship>)socket.readObject(true);
			// place ships and send resulting ships to server
			List<Ship> shipsList = bot.placeShips(ships);
			if (shipsList != null) {
				socket.writeObject(shipsList);
			}
			return;
		} catch (ClassCastException e) {
			System.err.println("Network Exception: Unexpected game object from server. Check server version.");
		} catch (ConnectionLostException e) {
			System.err.println("Socket Exception: Connection lost, disconnecting.");
			this.kill();
		}
		System.err.println("Lost socket connection during placeShips");
		this.kill();
		System.exit(0);
	}

	public void doTurn() {
		try {
			@SuppressWarnings("unchecked")
			List<Ship> myShips = (List<Ship>)socket.readObject(true);
			@SuppressWarnings("unchecked")
			Map<Integer, List<ActionResult>> actionResults = (Map<Integer, List<ActionResult>>)socket.readObject(true);
			// doTurn and send resulting coordinates to server
			List<Coordinate> coords = bot.doTurn(myShips, actionResults);
			if (coords != null)
				socket.writeObject(coords);
			return;
		} catch (ClassCastException e) {
			System.err.println("Network Exception: Unexpected game object from server. Check server version.");
		} catch (ConnectionLostException e) {
			System.err.println("Socket Exception: Connection lost, disconnecting.");
			this.kill();
		}
		System.err.println("Lost socket connection during doTurn");
		this.kill();
		System.exit(0);
	}
}
