package battlechallenge.client;

import java.io.BufferedInputStream;
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
	private BufferedInputStream bis;
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
			bis = new BufferedInputStream(conn.getInputStream());
			ois = new ObjectInputStream(bis);
		} catch (Exception e) {
			// TODO: Handle client cannot connect to server
//			e.printStackTrace();
			System.err.println("Application Exception: Invalid network connection parameters.");
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
//				e.printStackTrace();
				System.err.println("Socket Exception: Cannot read from socket.");
			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
				System.err.println("Network Exception: Cannot receive game object from server. Check server version.");
			} catch (ClassCastException e) {
//				e.printStackTrace();
				System.err.println("Network Exception: Unexpected game object from server. Check server version.");
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
				// Ensure objects are sent.
				oos.flush();
				return;
			}
		} catch (IOException e) {
//			e.printStackTrace();
			System.err.println("Socket Exception: Cannot read from socket.");
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
			System.err.println("Network Exception: Cannot receive game object from server. Check server version.");
		} catch (ClassCastException e) {
//			e.printStackTrace();
			System.err.println("Network Exception: Unexpected game object from server. Check server version.");
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
			// Ensure objects are sent.
			oos.flush();
			return;
		} catch (IOException e) {
//			e.printStackTrace();
			System.err.println("Socket Exception: Cannot read from socket.");
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
			// Clear socket object cache. Causes problems when sending same object with different data.
			oos.reset();
			oos.writeObject(shipsList);
			// Ensure objects are sent.
			oos.flush();
			return;
		} catch (IOException e) {
//			e.printStackTrace();
			System.err.println("Socket Exception: Cannot read from socket.");
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
			System.err.println("Network Exception: Cannot receive game object from server. Check server version.");
		} catch (ClassCastException e) {
//			e.printStackTrace();
			System.err.println("Network Exception: Unexpected game object from server. Check server version.");
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
			@SuppressWarnings("unchecked")
			Map<Integer, List<ActionResult>> actionResults = (Map<Integer, List<ActionResult>>)ois.readObject();
			// doTurn and send resulting coordinates to server
			List<Coordinate> coords = bot.doTurn(myShips, actionResults);
			if (coords != null) {
				// Clear socket object cache. Causes problems when sending same object with different data.
				oos.reset();
				oos.writeObject(coords);
				// Ensure objects are sent.
				oos.flush();
			}
			return;
		} catch (IOException e) {
//			e.printStackTrace();
			System.err.println("Socket Exception: Cannot read from socket.");
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
			System.err.println("Network Exception: Cannot receive game object from server. Check server version.");
		} catch (ClassCastException e) {
//			e.printStackTrace();
			System.err.println("Network Exception: Unexpected game object from server. Check server version.");
		}
		// TODO: handle error setting ID
		System.out.println("Lost socket connection during doTurn");
		this.kill();
		System.exit(0);
	}
}
