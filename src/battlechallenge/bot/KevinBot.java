package battlechallenge.bot;

import java.util.*;
import java.util.Map.Entry;

import battlechallenge.ActionResult;
import battlechallenge.Coordinate;
import battlechallenge.ShipAction;
import battlechallenge.ShipIdentifier;
import battlechallenge.client.ClientGame;
import battlechallenge.ship.Ship;
import battlechallenge.ship.Ship.Direction;
import battlechallenge.structures.City;
import battlechallenge.structures.Structure;
import battlechallenge.bot.Node;


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
		directionList.add(Direction.STOP);
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
		case STOP: {
			return coor;
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
				newCoord = new Coordinate(s1.getLocation().getRow(), s1.getLocation().getCol() - 1);
				newDist = s2.distanceFromCoord(newCoord);
				if (newDist < s2.distanceFromCoord(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.WEST;
					}
				}
				continue;
			}

			case STOP: {
				newCoord = s1.getLocation();
				newDist = s2.distanceFromCoord(newCoord);
				if (newDist < s2.distanceFromCoord(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.STOP;
					}
				}
				continue;

			}
			}
		}
		return bestDirection;
	}

	public Direction moveTowardsCoord(Ship s1, Coordinate coord) {
		Direction bestDirection = null;
		double minDistance = Double.MAX_VALUE;
		for (Direction d : directionList) {
			Coordinate newCoord;
			double newDist = 0.0;
			switch (d) {
			case NORTH: {
				newCoord = new Coordinate(s1.getLocation().getRow()-1, s1.getLocation().getCol());
				newDist = coord.distanceTo(newCoord);
				if (newDist < coord.distanceTo(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.NORTH;
					}
				}
				continue;
			}
			case SOUTH: {
				newCoord = new Coordinate(s1.getLocation().getRow()+1, s1.getLocation().getCol());
				newDist = coord.distanceTo(newCoord);
				if (newDist < coord.distanceTo(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.SOUTH;
					}
				}
				continue;
			}
			case EAST: {
				newCoord = new Coordinate(s1.getLocation().getRow(), s1.getLocation().getCol() + 1);
				newDist = coord.distanceTo(newCoord);
				if (newDist < coord.distanceTo(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.EAST;
					}
				}
				continue;
			}
			case WEST: {
				newCoord = new Coordinate(s1.getLocation().getRow(), s1.getLocation().getCol() - 1);
				newDist = coord.distanceTo(newCoord);
				if (newDist < coord.distanceTo(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.WEST;
					}
				}
				continue;
			}

			case STOP: {
				newCoord = s1.getLocation();
				newDist = coord.distanceTo(newCoord);
				if (newDist <= coord.distanceTo(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.STOP;
					}
				}
				continue;

			}
			}
		}
		return bestDirection;
	}

	public Ship closestEnemyShip(Ship myShip, List<Ship> enemyShips) {
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

	public City closestCity(Ship myShip, List<City> cities) {
		City closestCity = null;
		double currDist;
		double minDist = Double.MAX_VALUE;
		for (City city: cities) {
			if (city.getOwnerId() == ClientGame.getNetworkID()) { // I own the city
				continue;
			}
			currDist = myShip.distanceFromCoord(city.getLocation());
			if (currDist < minDist) {
				closestCity = city;
				minDist = currDist;
			}
		}
		return closestCity;
	}

	public City theClosestCity(Ship ship, List<City> cities) {
		City closestCity = null;
		double currDist;
		double minDist = Double.MAX_VALUE;
		for (City city: cities) {
			currDist = ship.distanceFromCoord(city.getLocation());
			if (currDist < minDist) {
				closestCity = city;
				minDist = currDist;
			}
		}
		return closestCity;
	}

	public boolean isOnMyCity(Ship s) {
		for (City city: ClientGame.getMyCities()) {
			if (city.getLocation().equals(s.getLocation())) {
				System.out.println(city);
				return true;
			}
		}
		return false;
	}

	public boolean isOnCity(Ship s) {
		for (City city: ClientGame.getAllCities()) {
			if (city.getOwnerId() == ClientGame.getNetworkID()) {
				continue;
			}
			if (city.getLocation().equals(s.getLocation())) {
				return true;
			}
		}
		return false;
	}

	public Direction BFS(Ship s, Coordinate coord, List<Coordinate> myShipCoord) {
		Node currNode = new Node(s.getLocation(), null, null, 0.0); // Coordinate, Node, Direction
		Map<Node, Node> prev = new HashMap<Node, Node>();
		Map<Node, String> visited = new HashMap<Node, String>();
		List<Direction> directions = new LinkedList<Direction>();
		PriorityQueue<Node> q = new PriorityQueue<Node>();
		q.add(currNode);//, currNode.getLocation().distanceTo(coord));
		while (!q.isEmpty()) {
			currNode = (Node) q.remove();
			visited.put(currNode, currNode.toString());
			if ((currNode.getLocation()).equals(coord)) { // Found where we want to go
				break;
			}
			for (Direction dir: directionList) {
				Node adjNode = new Node (move(dir, currNode.getLocation()), currNode, dir, coord.distanceTo(move(dir, currNode.getLocation())));
				
				if (visited.get(adjNode) != null || myShipCoord.contains(adjNode.getLocation())) { // don't use path along friendly ship
					continue;
				}
				if (visited.get(adjNode) == null) {
					q.add(adjNode);
					visited.put(adjNode, adjNode.toString());
					prev.put(adjNode, currNode);	
				}
			}
		}
		if (!(currNode.getLocation().equals(coord))) {
			System.out.println("Failed to find a path");
			return null;
		}
				
		for(Node node = currNode; node != null; node = prev.get(node)) {
	        directions.add(node.getDir());
	    }
		System.out.println(directions);
	    return directions.get(0);
		
	}


	public List<ShipAction> doTurn() {

		List<ShipAction> actions = new LinkedList<ShipAction>();
		Map<Coordinate, Ship> moveMap = new HashMap<Coordinate, Ship>();
		Map<Coordinate, Ship> shotMap = new HashMap<Coordinate, Ship>();
		List<Ship> myShips = ClientGame.getMyShips();
		List<Coordinate> myShipLocations = new LinkedList<Coordinate>();
		for (Ship s : myShips) {
			myShipLocations.add(s.getLocation());
		}
		List<Ship> myMovedShips = new LinkedList<Ship>();
		List<Ship> enemyShips = ClientGame.getOpponentShips();
		List<City> cityList = ClientGame.getAllCities();
		fillDirectionList();
		List<Coordinate> moveTargets = new LinkedList<Coordinate>();
		List<Coordinate> shotTargets = new LinkedList<Coordinate>();
		Map<Ship, Direction> shipMoves = new HashMap<Ship, Direction>();
//		for (City city: ClientGame.getMyCities()) {
//			moveTargets.add(city.getLocation());
//		}
		// System.out.println(enemyShips);
		// System.out.println(enemyShipCoord);
		City closeCity;
		boolean movingTowardsCity;
		boolean movingTowardsShip;
		for (Ship s : myShips) {
			movingTowardsCity = false;
			movingTowardsShip = false;
			Coordinate oldShipLocation = s.getLocation();
			Ship closeEnemy = null;
			closeCity = null;
			Direction moveDirection = null;
			closeEnemy = closestEnemyShip(s, enemyShips);
			closeCity = closestCity(s, cityList);
			if (isOnMyCity(s)) {
				if (moveMap.get(s.getLocation()) == null) {
				
//					Ship ship = moveMap.get(s.getLocation());
//					shipMoves.remove(moveMap.get(s.getLocation()));
//					if (moveMap.get(ship.getLocation()) != null) { //ship would collide with city
//						moveMap.put(ship.getLocation(), ship);
//						shipMoves.put(ship, Direction.STOP);
//					}
					moveDirection = Direction.STOP;
					moveMap.put(s.getLocation(), s);
					shipMoves.put(s, moveDirection); // will overwrite  
					continue;
				}
				else {
					moveDirection = BFS(s, closeCity.getLocation(), myShipLocations);
				}
				
			}

		

			if (moveDirection == null) {
				if (closeCity != null) {
//					moveDirection = moveTowardsCoord(s, closeCity.getLocation());
					moveDirection = BFS(s, closeCity.getLocation(), myShipLocations);
					System.out.println(moveDirection);
					movingTowardsCity = true;
				}
				else {
					moveDirection = moveTowardsShip(s, closeEnemy);
//					moveDirection = BFS(s, closeEnemy.getLocation());
					movingTowardsShip = true;
				}
			}
			if (moveDirection != null) {
				Coordinate newCoord = move(moveDirection, s.getLocation());
//				 if (moveMap.get(newCoord) != null) {
//					 if (movingTowardsShip == false) {
//						 moveDirection = moveTowardsShip(s, closeEnemy);
//						 newCoord = move(moveDirection, s.getLocation());
//					 }
//				 }
			
				 
				if (moveMap.get(newCoord) == null) { // Avoid collisions
					shipMoves.put(s, moveDirection);
					moveMap.put(newCoord, s);
				}
			}
			if (shipMoves.get(s) == null) {
				moveMap.put(s.getLocation(), s);
			}
		}
	
				for (Ship s: shipMoves.keySet()) { 
					List<Direction> moves = new LinkedList<Direction>();
					List<Coordinate> shotCoordinates = new ArrayList<Coordinate>();
					Direction dir = shipMoves.get(s);
					moves.add(dir);
					Coordinate newCoord = move(dir, s.getLocation());

					Coordinate oldShipLocation = s.getLocation();
					Ship closeEnemy = closestEnemyShip(s, enemyShips);
					s.setLocation(newCoord); // move ship
					

//				if (dir == null) {
//				continue;
//			}
			// Decide where to shoot
			
			Coordinate newCloseEnemyCoordGuess = move(moveTowardsCoord(closeEnemy, oldShipLocation), closeEnemy.getLocation());	
					
			if (s.inRange(newCloseEnemyCoordGuess) && !shotTargets.contains(newCloseEnemyCoordGuess)) {
				shotCoordinates.add(newCloseEnemyCoordGuess);
				shotTargets.add(newCloseEnemyCoordGuess);
			}
			if (shotCoordinates.isEmpty() && s.inRange(closeEnemy.getLocation()) && (!shotTargets.contains(closeEnemy.getLocation()))) {
				shotCoordinates.add(closeEnemy.getLocation());
				shotTargets.add(closeEnemy.getLocation());
			}

			Coordinate secondCloseEnemyCoordGuess = move(moveTowardsCoord(closeEnemy, theClosestCity(closeEnemy, cityList).getLocation()), closeEnemy.getLocation());

			if (shotCoordinates.isEmpty() && (!shotTargets.contains(secondCloseEnemyCoordGuess))) {
				shotCoordinates.add(secondCloseEnemyCoordGuess);
				shotTargets.add(secondCloseEnemyCoordGuess);
			}

			if (shotCoordinates.isEmpty()) {
				for (Ship eShip: enemyShips) {
					Coordinate coord = eShip.getLocation();
					if (s.inRange(coord) && (!shotTargets.contains(coord))) {
						if (isOnCity(eShip)) {
							shotCoordinates.add(coord);
							shotTargets.add(coord);
						}
					}
				}
			}
	
			// Decide where to shoot


//
//			// Shoot at closest enemy even if out of range
//			if (shotCoordinates.isEmpty()) {
//				//Coordinate newCoord = move(moveTowardsCoord(closeEnemy, s.getLocation()), closeEnemy.getLocation());
//				if (s.inRange(newCoord) && !shotTargets.contains(newCoord)) {
//					shotCoordinates.add(newCoord);
//					shotTargets.add(newCoord);
//				}
//				else {
//					if (!shotTargets.contains(closeEnemy.getLocation())) {
//						shotCoordinates.add(closeEnemy.getLocation()); // shoot at closest enemy anyway
//						shotTargets.add(closeEnemy.getLocation());
//					}
//				}	
//			}

			actions.add(new ShipAction(s.getIdentifier(), shotCoordinates, moves));
			// System.out.println(actions);
		}
		System.out.println("Target List: " + shotTargets);
		return actions;
	}
}