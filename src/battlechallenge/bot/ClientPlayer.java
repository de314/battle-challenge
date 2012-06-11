package battlechallenge.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import battlechallenge.ActionResult;
import battlechallenge.Coordinate;
import battlechallenge.Ship;

/**
 * The Class ClientPlayer.
 */
public class ClientPlayer {
	
	/** The player name. */
	protected final String playerName;
	
	/** The id to identify the player */
	protected int networkID;
	
	/** The board width. */
	protected int boardWidth;

	/** The board height. */
	
	protected int boardHeight;
	
	/**
	 * Save the width of the game board
	 * @param boardWidth the width of the game board
	 */
	public void setBoardWidth(int boardWidth) {
		this.boardWidth = boardWidth;
	}
	/**
	 * Save the height of the game board
	 * @param boardHeight the height of the game board
	 */
	public void setBoardHeight(int boardHeight) {
		this.boardHeight = boardHeight;
	}
	
	public ClientPlayer(final String playerName) {
		this(playerName, 0, 0, 0);
	}
	/**
	 * Instantiates a new client player.
	 *
	 * @param playerName the player name
	 * @param boardWidth the map width
	 * @param boardHeight the map height
	 */
	public ClientPlayer(final String playerName, final int mapWidth, final int mapHeight, final int networkID) {
		this.playerName = playerName;
		this.boardWidth = mapWidth;
		this.boardHeight = mapHeight;
		this.networkID = networkID;
	}
	/**
	 * This method is called at the beginning of the game to determine
	 * where the player wants to place his ships. For each ship set the
	 * starting position and the direction in which the ship's length will extend. Make sure
	 * ships are not overlapping and are within the defined bounds of the game map.
	 * @param shipList A list of ships with all null attributes
	 * @return the shipList with updated values for the starting position
	 * and direction the ship is facing
	 */
	public List<Ship> placeShips(List<Ship> shipList) {
		List<Integer> shipRow = new ArrayList<Integer>();
		int row = 0;
		for (Ship ship: shipList) {
			while (shipRow.contains(row)) {
				row = (int) (Math.random() * (boardHeight-1));
			}
			shipRow.add(row);
			ship.setStartPosition(new Coordinate(row,0));
			ship.setDirection(Ship.Direction.EAST);
		}
		System.out.println("placed ships");
		return shipList;		
	}
	
	/**
	 * This class will be filled in by the player. All logic regarding in game decisions to be
	 * made by your bot should be put in here. This class will be called every turn until the
	 * end of the game
	 * 
	 * @param myShips List of ships belonging to player
	 * @param oppShips List of ships belonging to opponent
	 * @param myLastTurn List of action results from the last turn
	 * @param oppLastTurn List of actions and their results of your opponent
	 * @return a List of coordinates corresponding to where you wish to fire
	 */
	public List<Coordinate> doTurn(List<Ship> myShips, Map<Integer, List<ActionResult>> actionResults) {
		List<Coordinate> shotCoordinates = new ArrayList<Coordinate>();
		shotCoordinates.add(new Coordinate((int) (Math.random() * (boardHeight -1)), (int) (Math.random() * (boardWidth - 1))));	
		return shotCoordinates;
	}
}

