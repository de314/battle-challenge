package battlechallenge.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import battlechallenge.ActionResult;
import battlechallenge.CommunicationConstants;
import battlechallenge.Coordinate;
import battlechallenge.Ship;
import battlechallenge.network.ConnectionLostException;

/**
 * The Class ClientConnection.
 */
public class ClientConnection {

	
	
	/** The Socket conn. */
	private Socket conn;
	
	/** ObjectOutputStream object */
	private ObjectOutputStream oos;

	/** The bis. */
	private BufferedInputStream bis;
	
	/** The ois. */
	private ObjectInputStream ois;
	
	/**
	 * Instantiates a new client connection.
	 *
	 * @param conn the conn
	 */
	public ClientConnection(Socket conn, int id) {
		this.conn = conn;
		if (conn == null)
			throw new IllegalArgumentException("Socket cannot be null");
		try {
			oos = new ObjectOutputStream(conn.getOutputStream());
			bis = new BufferedInputStream(conn.getInputStream());
			ois = new ObjectInputStream(bis);
			setupHandshake();
			System.out.println("Client connection confirmed:" + id);
		} catch (IOException e) {
			// TODO: handle client socket exception
			e.printStackTrace();
		} catch (ConnectionLostException e) {
			// TODO: handle socket lost connection
			e.printStackTrace();
		}
	}
	
	public void endGame(String result) {
		try {
			oos.writeObject(result);
			// Ensure objects are sent.
			oos.flush();
			// Clear socket object cache. Causes problems when sending same object with different data.
			oos.reset();
		} catch (IOException e) { }
		  catch (NullPointerException e) { }
		kill();
	}
	
	/**
	 * Kill the client connection
	 */
	public void kill() {
		if (this.conn != null) {
			/* don't care if these mess up */
			try { this.oos.close(); } catch (IOException e) { }catch (NullPointerException e) { }
			try { this.ois.close(); } catch (IOException e) { }catch (NullPointerException e) { }
			try { this.conn.close(); } catch (IOException e) { }catch (NullPointerException e) { }
			this.conn = null;
		}
	}
	
	/**
	 * Setup handshake.
	 *
	 * @return true, if successful
	 * @throws ConnectionLostException the connection lost exception
	 */
	public boolean setupHandshake() throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			oos.writeObject(CommunicationConstants.REQUEST_HANDSHAKE);
			oos.writeObject(CommunicationConstants.SERVER_VERSION);
			// Ensure objects are sent.
			oos.flush();
			// Clear socket object cache. Causes problems when sending same object with different data.
			oos.reset();
			// TODO: hack a non-blocking read
			String client_version = (String)ois.readObject();
			if (client_version != null && CommunicationConstants.SUPPORTED_CLIENTS.contains(client_version)) {
				return true;
			} else {
				// TODO: handle unsupported client version
				return false;
			}
		} catch (IOException e) {
			// TODO: cannot send server objects over the socket
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle server attack
			e.printStackTrace();
		} catch (ClassCastException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle invalid input
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Sets the player credentials.
	 * @param id The id that identifies a player
	 * @param height the height of the board
	 * @param width the width of the board
	 * @return the name of the client if successful, otherwise null
	 * @throws ConnectionLostException the connection was lost throw an exception
	 */
	public String setCredentials(int id, int height, int width) throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			oos.writeObject(CommunicationConstants.REQUEST_CREDENTIALS);
			oos.writeInt(id);
			oos.writeInt(width);
			oos.writeInt(height);
			// Ensure objects are sent.
			oos.flush();
			// Clear socket object cache. Causes problems when sending same object with different data.
			oos.reset();
			String client_name = (String)ois.readObject();
			return client_name;
		} catch (IOException e) {
			// TODO: cannot send server objects over the socket
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle server attack
			e.printStackTrace();
		} catch (ClassCastException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle invalid input
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Request place ships.
	 *
	 * @param ships the ships
	 * @return true, if successful
	 * @throws ConnectionLostException the connection lost exception
	 */
	public boolean requestPlaceShips(List<Ship> ships) throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			oos.writeObject(CommunicationConstants.REQUEST_PLACE_SHIPS);
			oos.writeObject(ships);
			// Ensure objects are sent.
			oos.flush();
			// Clear socket object cache. Causes problems when sending same object with different data.
			oos.reset();
			System.out.println("Ships sent");
			return true;
		} catch (IOException e) {
			// TODO: cannot send server objects over the socket
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets the updated ship locations returned by the client
	 *
	 * @return the list of ships passed back from the client
	 * @throws ConnectionLostException the connection lost exception
	 */
	public List<Ship> getPlaceShips() throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			/*
			 * check to see if something is arrived in time. Otherwise, assume
			 * no input. 
			 */
			List<Ship> temp = bis.available() == 0 ? new LinkedList<Ship>() : (List<Ship>)ois.readObject();
			// validate that no ships in returned list are null
			for (Ship s : temp) {
				if (s == null)
					return new LinkedList<Ship>();
			}
			return temp;
		} catch (IOException e) {
			// TODO: cannot send server objects over the socket
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle server attack
			e.printStackTrace();
		} catch (ClassCastException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle invalid input
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Tells the client to expect turn parameters then passes 
	 * the current list of ships and actionResult list to the client
	 * @param ships the current list of ships as of last turn
	 * @param actionResults list of actionResults from last turn
	 * @return true, if successful
	 * @throws ConnectionLostException a lost connection exception
	 */
	public boolean requestTurn(List<Ship> ships, Map<Integer, List<ActionResult>> actionResults) throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			// send command
			oos.writeObject(CommunicationConstants.REQUEST_DO_TURN);
			// Clear socket object cache. Causes problems when sending same object with different data.
			oos.reset();
			// send current players boats
			oos.writeObject(ships);
			// send action results hash table
			oos.writeObject(actionResults);
			// Ensure objects are sent.
			oos.flush();
			return true;
		} catch (IOException e) {
			// TODO: cannot send server objects over the socket
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets the coordinates passed by the client at the end of a turn
	 *
	 * @return the list of coordinates returned by the ClientPlayer's doTurn method
	 * @throws ConnectionLostException the connection lost exception
	 */
	public List<Coordinate> getTurn() throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			/*
			 * check to see if something is arrived in time. Otherwise, assume
			 * no input. 
			 */
			System.out.println("Available: " + bis.available());
			List<Coordinate> temp = bis.available() == 0 ? new LinkedList<Coordinate>() : (List<Coordinate>)ois.readObject();
			// validate that no ships in returned list are null
			for (Coordinate c : temp) {
				if (c == null)
					return new LinkedList<Coordinate>();
			}
			return temp;
			
		} catch (IOException e) {
			// TODO: cannot send server objects over the socket
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle server attack
			e.printStackTrace();
		} catch (ClassCastException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle invalid input
			e.printStackTrace();
		}
		return null;
	}
}
