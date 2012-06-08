package battlechallenge.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import battlechallenge.ActionResult;
import battlechallenge.CommunicationConstants;
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
	public ClientConnection(Socket conn, int id) {
		this.conn = conn;
		if (conn == null)
			throw new IllegalArgumentException("Socket cannot be null");
		try {
			oos = new ObjectOutputStream(conn.getOutputStream());
			ois = new ObjectInputStream(conn.getInputStream());
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
			oos.flush();
		} catch (IOException e) { }
		  catch (NullPointerException e) { }
		kill();
	}
	
	/**
	 * Kill.
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
			oos.flush();
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
	 *
	 * @return the string
	 * @throws ConnectionLostException the connection lost exception
	 */
	public String setCredentials(int id, int height, int width) throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			oos.writeObject(CommunicationConstants.REQUEST_CREDENTIALS);
			oos.writeInt(id);
			oos.writeInt(width);
			oos.writeInt(height);
			oos.flush();
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
			oos.flush();
			System.out.println("Ships sent");
			return true;
		} catch (IOException e) {
			// TODO: cannot send server objects over the socket
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets the place ships.
	 *
	 * @return the place ships
	 * @throws ConnectionLostException the connection lost exception
	 */
	public List<Ship> getPlaceShips() throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			// check to see if something is arrived in time. Otherwise, assume
			// no input
			// FIXME: validate input (no null ships)
			//List<Ship> temp = new LinkedList<Ship>();
			//System.out.print(System.currentTimeMillis() + " - ");
			//System.out.println(ois.available() > 0 ? "Ships returned" : "No ships returned");
			//return ois.available() == 0 ? temp : (List<Ship>)ois.readObject();
			// FIXME
			return (List<Ship>)ois.readObject();
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
	public boolean requestTurn(List<Ship> ships, Map<Integer, List<ActionResult>> actionResults) throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			oos.writeObject(CommunicationConstants.REQUEST_DO_TURN);
			oos.writeObject(ships);
			// FIXME: send a list of action results
			Map<Integer, ActionResult> temp = new HashMap<Integer, ActionResult>();
			for(Entry<Integer, List<ActionResult>> e : actionResults.entrySet()) {
				temp.put(e.getKey(), e.getValue().isEmpty() ? null : e.getValue().get(0));
			}
			oos.writeObject(temp);
			oos.flush();
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
			// FIXME: validate input (no null coordinates)
			//System.out.print(System.currentTimeMillis() + " - ");
			//System.out.println(ois.available() > 0 ? "Coordinates returned" : "No coordinates returned");
			//List<Coordinate> temp = new LinkedList<Coordinate>();
			// return ois.available() <= 0 ? temp : (List<Coordinate>)ois.readObject();
			// FIXME 
			return (List<Coordinate>)ois.readObject();
			
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
