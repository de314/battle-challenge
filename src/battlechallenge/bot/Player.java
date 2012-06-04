package battlechallenge.bot;

import java.util.List;

import battlechallenge.Coordinate;
import battlechallenge.Ship;
import battlechallenge.client.Game;
import battlechallenge.client.PlayerBoard;


public class Player {
	
	private String name;
	private Game game;
	
	public Player(String name, Game game) {
		this.name = name;
		this.game = game;
	}
	
	public void placeShips(List<Ship> ship) {
		
	}
	
	public Coordinate doTurn(PlayerBoard myBoard, PlayerBoard oppBoard) {
		return null;
	}
}
