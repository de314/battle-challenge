package battlechallenge.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import battlechallenge.Coordinate;
import battlechallenge.Ship;

public class NetworkConnection {

	private Socket conn;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public NetworkConnection(Socket conn) {
		this.conn = conn;
	}
	
	public void setupHandshake() {
		// TODO
	}
	
	public void setPlayerStatus() {
		// TODO
	}
	
	public void placeShips(List<Ship> ships) {
		// TODO
	}
	
	public boolean requestTurn() {
		// TODO
		return false;
	}
	
	public Coordinate getTurn() {
		// TODO
		return null;
	}
}
