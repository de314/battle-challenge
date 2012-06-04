package battlechallenge.client;

import java.util.List;

import battlechallenge.Ship;

public class Game {
	private PlayerBoard myBoard;
	private PlayerBoard oppBoard;
	private List<Ship> ships;
	
	public PlayerBoard getMyBoard() {
		return myBoard;
	}
	public PlayerBoard getOppBoard() {
		return oppBoard;
	}
	/* This method will be changed when
	 * ships can individually fire
	 */
	private int getNumberOfShots() {
		return 1;
	}
	
	
}
