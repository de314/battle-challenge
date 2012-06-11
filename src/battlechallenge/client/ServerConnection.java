package battlechallenge.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import battlechallenge.ActionResult;
import battlechallenge.CommunicationConstants;
import battlechallenge.Coordinate;
import battlechallenge.Ship;
import battlechallenge.bot.ClientPlayer;
import battlechallenge.bot.DavidBot;
import battlechallenge.bot.KevinBot;

public class ServerConnection {

	private Socket conn;
	private int id;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
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
		// TODO: validate port and IP
		try {
			// open a socket connection
			conn = new Socket(ip, port);
			// open I/O streams for objects
			oos = new ObjectOutputStream(conn.getOutputStream());
			ois = new ObjectInputStream(conn.getInputStream());
		} catch (Exception e) {
			// TODO: Handle client cannot connect to server
			e.printStackTrace();
		}
		this.name = name;
		this.run();
	}

	public void kill() {
		try {
			oos.close();
		} catch (IOException e) { /* ignore exceptions */
		}
		try {
			ois.close();
		} catch (IOException e) { /* ignore exceptions */
		}
		try {
			conn.close();
		} catch (IOException e) { /* ignore exceptions */
		}
	}

	public void run() {
		while (true) {
			try {
				String req = (String) ois.readObject();
				System.out.println(req);
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
					// TODO: handle disqualified player
					System.out.println("You have been disqualified from the game");
					break;
				}
				if (req.equals(CommunicationConstants.RESULT_WIN)) {
					// TODO: handle winner
					System.out.println("You have WON the game!!");
					break;
				}
				if (req.equals(CommunicationConstants.RESULT_LOSE)) {
					// TODO: handle loser
					System.out.println("You have lost the game.");
					break;
				}
			} catch (IOException e) {
				// TODO: handle client socket failure
			} catch (ClassNotFoundException e) {
				// ignore invalid requests
			} catch (ClassCastException e) {
				// ignore invalid requests
			}
		}
		System.out.println("The game is over.");
		this.kill();
	}

	public void setupHandShake() {
		try {
			// check that server version is supported
			if (CommunicationConstants.SUPPORTED_SERVERS.contains((String)ois.readObject())) {
				oos.writeObject(CommunicationConstants.CLIENT_VERSION);
				oos.flush();
				return;
			}
		} catch (IOException e) {
			
		} catch (ClassNotFoundException e) {
			
		} catch (ClassCastException e) {
			
		}
		// TODO handle unsupported server
		System.out.println("Server is not supported");
		this.kill();
		System.exit(0);
	}

	public void setCredentials(String name) {
		try {
			id = ois.readInt();
			int width = ois.readInt();
			int height = ois.readInt();
			bot = new DavidBot(name, width, height, id);
			System.out.println("bot generated with network ID: " + id);
			// send name to server
			System.out.print(System.currentTimeMillis() + " - ");
			oos.writeObject(name);
			oos.flush();
			return;
		} catch (IOException e) {
			
		}
		// TODO: handle error setting ID
		System.out.println("Could not set ID");
		this.kill();
		System.exit(0);
	}

	public void placeShips() {
		try {
			List<Ship> ships = (List<Ship>)ois.readObject();
			// place ships and send resulting ships to server
			// FIXME: handle null
			List<Ship> shipsList = bot.placeShips(ships);
			oos.writeObject(shipsList);
			oos.flush();
			return;
		} catch (IOException e) {
			
		} catch (ClassNotFoundException e) {
			
		} catch (ClassCastException e) {
			
		}
		// TODO: handle error setting ID
		System.out.println("Lost socket connection during placeShips");
		this.kill();
		System.exit(0);
	}

	public void doTurn() {
		try {
			@SuppressWarnings("unchecked")
			List<Ship> myShips = (List<Ship>)ois.readObject();
			// FIXME: send a list of ActionResults
			Map<Integer, ActionResult> temp = (Map<Integer, ActionResult>)ois.readObject();
			Map<Integer, List<ActionResult>> actionResults = new HashMap<Integer, List<ActionResult>>();
			for (Entry<Integer, ActionResult> e : temp.entrySet()) {
				actionResults.put(e.getKey(), new LinkedList<ActionResult>());
				if (e.getValue() != null)
					actionResults.get(e.getKey()).add(e.getValue());
			}
			// doTurn and send resulting coordinates to server
			// FIXME: handle null
			List<Coordinate> coords = bot.doTurn(myShips, actionResults);
			oos.writeObject(coords);
			oos.flush();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		// TODO: handle error setting ID
		System.out.println("Lost socket connection during doTurn");
		this.kill();
		System.exit(0);
	}
}
