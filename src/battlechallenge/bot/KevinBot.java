package battlechallenge.bot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import battlechallenge.ActionResult;
import battlechallenge.Coordinate;
import battlechallenge.ship.Ship;

/**
 * The Class ClientPlayer.
 */
public class KevinBot extends ClientPlayer {
	
	private Set<String> hitList;
	
	private Set<String> missList = new HashSet<String>();
	
	private Set<String> sunkList;
	
	private Set<String> shotList = new HashSet<String>();
	
	private Queue<Coordinate> shotQueue = new LinkedList<Coordinate>();
	
	List<Coordinate> shotCoordinates;
	/**
	 * Instantiates a new client player.
	 *
	 * @param playerName the player name
	 * @param mapWidth the map width
	 * @param boardHeight the map height
	 */
	public KevinBot(final String playerName, final int mapWidth, final int boardHeight, final int networkID) {
		super(playerName, mapWidth, boardHeight, networkID);
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
		int col = 0;
		for (Ship ship: shipList) {
			while (shipRow.contains(row)) {
				row = (int) (Math.random() * boardHeight);
				col = (int) (Math.random() * (boardWidth-5));
			}
		
			shipRow.add(row);
			shipRow.add(row+1);
			shipRow.add(row-1);
			ship.setStartPosition(new Coordinate(row,col));
			ship.setDirection(Ship.Direction.EAST);
		}
		System.out.println("placed ships");
		return shipList;		
	}
	
	public List<Coordinate> genAdjCoords(Coordinate coord) {
		List<Coordinate> adjCoords = new ArrayList<Coordinate>();
		Coordinate North = (new Coordinate(coord.getRow()-1, coord.getCol()));
		Coordinate South =(new Coordinate(coord.getRow()+1, coord.getCol()));
		Coordinate East = (new Coordinate(coord.getRow(), coord.getCol() + 1));
		Coordinate West = (new Coordinate(coord.getRow(), coord.getCol() - 1));
		if (validCoordinate(North))
			adjCoords.add(North);
		if (validCoordinate(South))
			adjCoords.add(South);
		if (validCoordinate(East))
			adjCoords.add(East);
		if (validCoordinate(West))
			adjCoords.add(West);		
		return adjCoords;
	}
	
	public boolean validCoordinate(Coordinate coord) {
		if (coord.getRow() < 0 || coord.getRow() >= boardHeight || coord.getCol() < 0 || coord.getCol() >= boardWidth)
			return false;
		return true;
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
		//shotCoordinates.add(new Coordinate((int) (Math.random() * (boardHeight -1)), (int) (Math.random() * (mapWidth - 1))));
		List<Coordinate> adjList = new ArrayList<Coordinate>();
		ActionResult.ShotResult shotResult;
		shotCoordinates = new ArrayList<Coordinate>();
		Coordinate coordinateResult;
		List<ActionResult> myResults = actionResults.get(this.networkID);
		System.out.println(myResults);
//		for (ActionResult action: myResults) {
		if  (myResults.size() > 0) {
			shotResult = myResults.get(0).getResult();
			coordinateResult = myResults.get(0).getCoordinate();
		
			switch (shotResult) {
			case HIT:
				shotList.add(coordinateResult.toString());
				//hitList.add(coordinateResult.toString());
				break;
			case MISS:
				shotList.add(coordinateResult.toString());
				missList.add(coordinateResult.toString());
				break;
			case SUNK:
				shotList.add(coordinateResult.toString());
				sunkList.add(coordinateResult.toString());
				break;
			}
			shotList.add(coordinateResult.toString());
			adjList = genAdjCoords(coordinateResult);
			
		if (shotResult == ActionResult.ShotResult.HIT) {
			for (Coordinate adjCoord: adjList) {
				if (!shotList.contains(adjCoord.toString())) {
					shotQueue.add(adjCoord);
				}
			}
		}	
		if (!shotQueue.isEmpty()) {
			Coordinate head = shotQueue.poll();
			while (!shotQueue.isEmpty()) {
				if (!shotList.contains(head.toString()))
					break;
				head = shotQueue.poll();
			}
			shotCoordinates.add(head);
		}
		
		}
		
		if (shotCoordinates.isEmpty()) {
			Coordinate randShot = new Coordinate((int) (Math.random() * (boardHeight)), (int) (Math.random() * (boardWidth)));
			while (!shotList.isEmpty()) {
				randShot = new Coordinate((int) (Math.random() * boardHeight), (int) (Math.random() * boardWidth));
				if (!shotList.contains(randShot.toString()))
					break;
			}
			
			shotCoordinates.add(randShot);
		}
		shotList.add(shotCoordinates.get(0).toString());
		return shotCoordinates;
	}
}
