package battlechallenge.server;

import java.util.ArrayList;
import java.util.List;

import battlechallenge.Coordinate;
import battlechallenge.Ship;
import battlechallenge.Ship.Direction;

public class Game {
	
	public static int DEFAULT_HEIGHT = 15;
	public static int DEFAULT_WIDTH = 15;
	
	private ServerPlayer playerOne;
	private ServerPlayer playerTwo;

	public Game() {
		this(null);
	}
	
	public Game(ServerPlayer player) {
		this.playerOne = player;
	}
	
	public boolean addPlayer(ServerPlayer player) {
		if (playerOne == null) {
			playerOne = player;
			return true;
		} else if (playerTwo == null) {
			playerTwo = player;
			return true;
		}
		return false;
	}
	
	public ServerPlayer getWinner() {
		return playerTwo.getScore() > playerOne.getScore() ? playerTwo : playerOne;
	}
	
	public static List<Ship> getShips() {
		List<Ship> ships = new ArrayList<Ship>();
		ships.add(new Ship(2,new Coordinate(-1, -1), Direction.NORTH));
		ships.add(new Ship(3,new Coordinate(-1, -1), Direction.NORTH));
		ships.add(new Ship(3,new Coordinate(-1, -1), Direction.NORTH));
		ships.add(new Ship(4,new Coordinate(-1, -1), Direction.NORTH));
		ships.add(new Ship(5,new Coordinate(-1, -1), Direction.NORTH));
		return ships;
	}
}
