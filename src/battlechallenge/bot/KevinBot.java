package battlechallenge.bot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	
	private List<Coordinate> shotCoordinates;
	
	private List<Ship> enemyShips;
	
	private List<Direction> directionList = new LinkedList<Direction>();
	
	public void fillDirectionList() {
		directionList.add(Direction.NORTH);
		directionList.add(Direction.SOUTH);
		directionList.add(Direction.EAST);
		directionList.add(Direction.WEST);
	}
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
		//fillDirectionList(); // so direction list has N,E,S,W
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
	
	public Coordinate move(Direction dir, Coordinate coor) {
		switch (dir) {
			case NORTH: {
				return new Coordinate(coor.getRow()-1, coor.getCol());
			}
			case SOUTH: {
				return new Coordinate(coor.getRow()+1, coor.getCol());
			}
			case EAST: {
				return new Coordinate(coor.getRow(), coor.getCol() + 1);
			}
			case WEST: {
				return new Coordinate(coor.getRow(), coor.getCol() - 1);
			}
		}
		return null;
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

	enemyShips = new LinkedList<Ship>();
	for (Entry<Integer, List<Ship>> e: ships.entrySet()) { 
		if (e.getKey() == networkID)
			continue;
		for (Ship s : ships.get(e.getKey())) {
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
	
	public Direction moveTowardsShip(Ship s1, Ship s2) {
//		for (Direction d : directionList) {
			Coordinate newCoord; 
			newCoord = new Coordinate(s1.getCenter().getRow(), s1.getCenter().getCol()-1);
			if (s2.distanceFromCenter(newCoord) < s2.distanceFromCenter(s1.getCenter())) {
				return Direction.WEST;
			}
		return null;
	}
	

	public List<ShipAction> doTurn(Map<Integer, List<Ship>> ships, Map<Integer, List<ActionResult>> actionResults) {
		
		List<ShipAction> actions = new LinkedList<ShipAction>();
		List<Ship> myShips = ships.get(networkID);
		List<Ship> enemyShips = getEnemyShipList(ships);
		List<Coordinate> shotCoordinates = new ArrayList<Coordinate>();
		List<Coordinate> enemyShipCoord = getEnemyCoordinates(enemyShips);
//		System.out.println(enemyShips);
//		System.out.println(enemyShipCoord);
		
		for (Ship s : myShips) {
			List<Direction> moves = new LinkedList<Direction>();
			Direction toMove = null;
			for (Ship eShip: enemyShips) {
				toMove = moveTowardsShip(s, eShip);
				if (toMove != null) {
					moves.add(toMove);
					Coordinate newCoord = move(toMove, s.getStartPosition());
					s.setStartPosition(newCoord);
					break;
				}
			}
			
			for (Coordinate coord: enemyShipCoord) {
//				if (s.distanceFromCenter(coord) <= s.getRange()) {
				if (s.inRange(coord)) {
					shotCoordinates.add(coord);
					actions.add(new ShipAction(s.getIdentifier(), shotCoordinates, moves));
					break;
				}
			}
		}
		return actions;
	}
}
