package battlechallenge.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import battlechallenge.ActionResult;
import battlechallenge.CommunicationConstants;
import battlechallenge.Ship;
import battlechallenge.bot.ClientPlayer;

public class ServerConnection {

	private Socket conn;
	private int id;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ClientPlayer bot;
	private String name;

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
		}
		this.name = name;
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
				}
				if (req.equals(CommunicationConstants.RESULT_DISQUALIFIED)) {
					// TODO: handle winner
					System.out.println("You have WON the game!!");
				}
				if (req.equals(CommunicationConstants.RESULT_DISQUALIFIED)) {
					// TODO: handle loser
					System.out.println("You have lost the game.");
				}
			} catch (IOException e) {
				// TODO: handle client socket failure
			} catch (ClassNotFoundException e) {
				// ignore invalid requests
			} catch (ClassCastException e) {
				// ignore invalid requests
			}
		}
	}

	public void setupHandShake() {
		try {
			// check that server version is supported
			if (CommunicationConstants.SUPPORTED_SERVERS.contains((String)ois.readObject())) {
				oos.writeObject(CommunicationConstants.CLIENT_VERSION);
				return;
			}
		} catch (IOException e) {
			
		} catch (ClassNotFoundException e) {
			
		} catch (ClassCastException e) {
			
		}
		// TODO handle unsupported server
		System.out.println("Server is not supported");
		System.exit(0);
	}

	public void setCredentials(String name) {
		try {
			id = ois.readInt();
			int width = ois.readInt();
			int height = ois.readInt();
			bot = new ClientPlayer(name, width, height, id);
			// send name to server
			oos.writeObject(name);
			return;
		} catch (IOException e) {
			
		}
		// TODO: handle error setting ID
		System.out.println("Could not set ID");
		System.exit(0);
	}

	public void placeShips() {
		try {
			List<Ship> ships = (List<Ship>)ois.readObject();
			// place ships and send resulting ships to server
			// FIXME: handle null
			oos.writeObject(bot.placeShips(ships));
			return;
		} catch (IOException e) {
			
		} catch (ClassNotFoundException e) {
			
		} catch (ClassCastException e) {
			
		}
		// TODO: handle error setting ID
		System.out.println("Lost socket connection during doTurn");
		System.exit(0);
	}

	public void doTurn() {
		try {
			List<Ship> myShips = (List<Ship>)ois.readObject();
			List<Ship> oppShips = (List<Ship>)ois.readObject();
			List<ActionResult> myAR = (List<ActionResult>)ois.readObject();
			List<ActionResult> oppAR = (List<ActionResult>)ois.readObject();
			// doTurn and send resulting coordinates to server
			// FIXME: handle null
			oos.writeObject(bot.doTurn(myShips, oppShips, myAR, oppAR));
			return;
		} catch (IOException e) {
			
		} catch (ClassNotFoundException e) {
			
		} catch (ClassCastException e) {
			
		}
		// TODO: handle error setting ID
		System.out.println("Lost socket connection during doTurn");
		System.exit(0);
	}
}
