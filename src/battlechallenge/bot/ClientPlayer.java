package battlechallenge.bot;

import java.util.List;

import battlechallenge.Coordinate;
import battlechallenge.Ship;
import battlechallenge.client.Game;
import battlechallenge.client.PlayerBoard;


public class ClientPlayer {
	
	private String name;
	
	public ClientPlayer(String name) {
		this.name = name;
	}
	
	public void placeShips(List<Ship> ship) {
		
	}
	
	public Coordinate doTurn(PlayerBoard myBoard, PlayerBoard oppBoard) {
		return null;
	}
}
