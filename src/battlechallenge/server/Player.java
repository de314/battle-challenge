package battlechallenge.server;

import java.net.Socket;
import java.util.List;

import battlechallenge.Coordinate;
import battlechallenge.Ship;

public class Player {

	private String name;
	private Board board;
	private ClientConnection conn;
	private int score;
	
	
	
	public Player(Socket socket, Board board) {
		this.conn = new ClientConnection(socket);
		// TODO
	}
	
	public void placeShips(List<Ship> ships) {
		conn.placeShips(ships);
	}
	
	public boolean requestTurn() {
		return conn.requestTurn();
	}
	
	public Coordinate getTurn() {
		return conn.getTurn();
	}
	
	public int getScore() {
		return score;
	}
	
	public void win() {
		// TODO
		score++;
	}
	
	public void lose() {
		// TODO
		score--;
	}
}
