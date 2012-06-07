package battlechallenge.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import battlechallenge.ConnectionLostException;
import battlechallenge.Coordinate;
import battlechallenge.Ship;

/**
 * The Class ClientConnection.
 */
public class ClientConnection {

	
	
	/** The conn. */
	private Socket conn;
	
	/** The oos. */
	private ObjectOutputStream oos;
	
	/** The ois. */
	private ObjectInputStream ois;
	
	/**
	 * Instantiates a new client connection.
	 *
	 * @param conn the conn
	 */
	public ClientConnection(Socket conn) {
		this.conn = conn;
		if (conn == null)
			throw new IllegalArgumentException("Socket cannot be null");
		try {
			oos = new ObjectOutputStream(conn.getOutputStream());
			ois = new ObjectInputStream(conn.getInputStream());
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
	}
	
	/**
	 * Kill.
	 */
	public void kill() {
		if (this.conn != null) {
			try {
				this.conn.close();
			} catch (IOException e) { /* don't care if this messes up */}
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
			oos.writeObject(BattleshipServer.SERVER_VERSION);
			String client_version = (String)ois.readObject();
			if (client_version != null && BattleshipServer.SUPPORTED_CLIENTS.contains(client_version)) {
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
	 *
	 * @return the string
	 * @throws ConnectionLostException the connection lost exception
	 */
	public String setPlayerCredentials() throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			oos.writeObject(BattleshipServer.SERVER_VERSION.getBytes());
			String client_name = (String)ois.readObject();
			if (client_name != null && client_name.contains("Name:")) {
				return client_name.replace("Name:", "");
			} else {
				// TODO: handle unsupported client version
				return null;
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
		return null;
	}
	
	/**
	 * Place ships.
	 *
	 * @param ships the ships
	 * @return the list
	 * @throws ConnectionLostException the connection lost exception
	 */
	public List<Ship> placeShips(List<Ship> ships) throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			oos.writeObject(ships);
			@SuppressWarnings("unchecked")
			List<Ship> newShips = (List<Ship>)ois.readObject();
			if (newShips != null) {
				for (Ship s : newShips) {
					if (s == null) {
						// TODO: handle invalid input
					}
				}
				return newShips;
			} else {
				// TODO: handle null input from socket (I don't think this will happen)
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
		return null;
	}
	
	/**
	 * Request turn.
	 *
	 * @return true, if successful
	 * @throws ConnectionLostException the connection lost exception
	 */
	public boolean requestTurn() throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			oos.writeObject(BattleshipServer.SERVER_REQUEST_TURN);
			return true;
		} catch (IOException e) {
			// TODO: cannot send server objects over the socket
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets the turn.
	 *
	 * @return the turn
	 * @throws ConnectionLostException the connection lost exception
	 */
	public List<Coordinate> getTurn() throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			// check to see if something is arrived in time. Otherwise, assume
			// no input
			if (ois.available() == 0)
				return null;
			List<Coordinate> c = (List<Coordinate>)ois.readObject();
			if (c != null) {
				return c;
			} else {
				// TODO: handle null input from socket (I don't think this will happen)
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
		return null;
	}
}
