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
	
	public Direction moveTowardsShip(Ship s1, Ship s2) {
		Direction bestDirection = null;
		double minDistance = Double.MAX_VALUE;
		for (Direction d : directionList) {
			Coordinate newCoord; 
			double newDist = 0.0;
			switch (d) {
			case NORTH: {
				newCoord = new Coordinate(s1.getLocation().getRow()-1, s1.getLocation().getCol());
				newDist = s2.distanceFromCoord(newCoord);
				if (newDist < s2.distanceFromCoord(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist; 
						bestDirection = Direction.NORTH;
					}
				}
				continue;
			}
			case SOUTH: {
				newCoord = new Coordinate(s1.getLocation().getRow()+1, s1.getLocation().getCol());
				newDist = s2.distanceFromCoord(newCoord);
				if (newDist < s2.distanceFromCoord(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist; 
						bestDirection = Direction.SOUTH;
					}
				}
				continue;
			}
			case EAST: {
				newCoord = new Coordinate(s1.getLocation().getRow(), s1.getLocation().getCol() + 1);
				newDist = s2.distanceFromCoord(newCoord);
				if (newDist < s2.distanceFromCoord(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist; 
						bestDirection = Direction.EAST;
					}
				}
				continue;
			}
			case WEST: {
				newCoord = new Coordinate(s1.getLocation().getRow()-1, s1.getLocation().getCol() - 1);
				newDist = s2.distanceFromCoord(newCoord);
				if (newDist < s2.distanceFromCoord(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist; 
						bestDirection = Direction.WEST;
					}
				}
				continue;
			}
			}
		}
		return bestDirection;
	}
	
	public Ship closestEnemy(Ship myShip, List<Ship> enemyShips) {
		Ship closestShip = null;
		double currDist;
		double minDist = Double.MAX_VALUE;
		for (Ship eShip: enemyShips) {
			currDist = myShip.distanceFromCoord(eShip.getLocation());
			if (currDist < minDist) {
				closestShip = eShip;
				minDist = currDist;
			}
		}
		return closestShip;
	}
	

	public List<ShipAction> doTurn(Map<Integer, List<Ship>> ships, Map<Integer, List<ActionResult>> actionResults) {
		
		List<ShipAction> actions = new LinkedList<ShipAction>();
		List<Ship> myShips = ships.get(networkID);
		List<Ship> enemyShips = getEnemyShipList(ships);
		List<Coordinate> shotCoordinates = new ArrayList<Coordinate>();
		List<Coordinate> enemyShipCoord = getEnemyCoordinates(enemyShips);
		fillDirectionList();
//		System.out.println(enemyShips);
//		System.out.println(enemyShipCoord);
		
		for (Ship s : myShips) {
			List<Direction> moves = new LinkedList<Direction>();
			Direction toMove = null;
			Ship closeEnemy = closestEnemy(s, enemyShips);
//			for (Ship eShip: enemyShips) {
				toMove = moveTowardsShip(s, closeEnemy);
				System.out.println(toMove);
				if (toMove != null) {
					moves.add(toMove);
//					Coordinate newCoord = move(toMove, s.getStartPosition());
//					s.setStartPosition(newCoord);
				}
//			}
			
			for (Coordinate coord: enemyShipCoord) {
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
