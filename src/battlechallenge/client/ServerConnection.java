package battlechallenge.client;

import java.util.List;
import java.util.Map;

import battlechallenge.ActionResult;
import battlechallenge.CommunicationConstants;
import battlechallenge.ShipAction;
import battlechallenge.bot.ClientPlayer;
import battlechallenge.network.ConnectionLostException;
import battlechallenge.network.NetworkSocket;
import battlechallenge.ship.Ship;

/**
 * The Class ServerConnection.
 */
public class ServerConnection {

	/** The id. */
	private int id;
	
	/** The socket. */
	private NetworkSocket socket;
	
	/** The bot. */
	private ClientPlayer bot;
	
	/** The name. */
	private String name;
	
	/**
	 * Constructor that creates a client socket and request and set
	 * the name of the ClientPlayer. The server connection will then
	 * call the run method
	 *
	 * @param port The port to connect to the server
	 * @param ip The ip address of the server
	 * @param name The name to identify the player
	 * @param bot the bot
	 */
	public ServerConnection(int port, String ip, String name, ClientPlayer bot) {
		socket = new NetworkSocket(ip, port);
		this.bot = bot;
		this.name = name;
		this.run();
	}

	/**
	 * Ends the socket connection.
	 * @see battlechallenge.client.ServerConnection.kill
	 */
	public void kill() {
		socket.kill();
	}

	/**
	 * Goes into an infinite loop waiting for server to give command. Each 
	 * command puts ServerConnection into a different state.
	 */
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

	/**
	 * Setup hand shake. Confirms that server and client versions are compatible.
	 */
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

	/**
	 * Sets the credentials.
	 *
	 * @param name the name of the player
	 */
	public void setCredentials(String name) {
		try {
			id = socket.readInt(true);
			bot.setBoardWidth(socket.readInt(true));
			bot.setBoardHeight(socket.readInt(true));
			bot.setNetworkID(id);
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

	/**
	 * Passes a list of ships to the player with a call to the client players
	 * placeShips method.
	 */
	public void placeShips() {
		try {
			@SuppressWarnings("unchecked")
			List<Ship> ships = (List<Ship>)socket.readObject(true);
			// place ships and send resulting ships to server
			List<Ship> shipsList = bot.placeShips(ships);
			socket.writeObject(shipsList);
			bot.setBoardWidth(socket.readInt(true));
			bot.setBoardHeight(socket.readInt(true));
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

	/**
	 * Will pass the ship list and action results list in a call 
	 * to the client players doTurn method
	 * Kill the connection if a call to the doTurn method fails
	 */
	public void doTurn() {
		try {
			@SuppressWarnings("unchecked")
			List<Ship> myShips = (List<Ship>)socket.readObject(true);
			@SuppressWarnings("unchecked")
			Map<Integer, List<ActionResult>> actionResults = (Map<Integer, List<ActionResult>>)socket.readObject(true);
			// doTurn and send resulting coordinates to server
			List<ShipAction> coords = bot.doTurn(myShips, actionResults);
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
