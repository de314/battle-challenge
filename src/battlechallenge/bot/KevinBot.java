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
import battlechallenge.ShipAction;
import battlechallenge.ship.Ship;
import battlechallenge.ship.Ship.Direction;

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
	
	List<Ship> enemyShips;
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
	
	public List<Ship> getEnemyShipList(Map<Integer, List<Ship>> ships) {
	for (int i = 0; i < ships.size(); i++) {
		enemyShips = new LinkedList<Ship>();
		if (i == networkID)
			continue;
		for (Ship s : ships.get(i)) {
			enemyShips.add(s);
		}	
	}
	return enemyShips;
	}
	
	public List<Coordinate> getEnemyCoordinates(List<Ship> eShips) {
		List<Coordinate> enemyShipCoord = new LinkedList<Coordinate>();
		for (Ship eShip : enemyShips) 
		{
			for (Coordinate coord: eShip.getCoordinates()) {
				enemyShipCoord.add(coord);
			}
		}
		return enemyShipCoord;
	}
	

	public List<ShipAction> doTurn(Map<Integer, List<Ship>> ships, Map<Integer, List<ActionResult>> actionResults) {
		
		List<ShipAction> actions = new LinkedList<ShipAction>();
		List<Ship> myShips = ships.get(networkID);
		List<Ship> enemyShips = getEnemyShipList(ships);
		List<Coordinate> shotCoordinates = new ArrayList<Coordinate>();
		List<Coordinate> enemyShipCoord = getEnemyCoordinates(enemyShips);
		
		
		for (Ship s : myShips) {
			List<Direction> moves = new LinkedList<Direction>();
			for (Coordinate coord: enemyShipCoord) {
				if (s.distanceTo(coord) <= s.getRange()) {
					shotCoordinates.add(coord);
					continue;
				}
			}
			
		
			actions.add(new ShipAction(s.getIdentifier(), shotCoordinates, moves));
		}
		return null;
	}
}
