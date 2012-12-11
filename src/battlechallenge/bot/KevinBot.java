package battlechallenge.bot;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

import battlechallenge.ActionResult;
import battlechallenge.Coordinate;
import battlechallenge.ShipAction;
import battlechallenge.ShipIdentifier;
import battlechallenge.client.ClientGame;
import battlechallenge.maps.BattleMap;
import battlechallenge.ship.Ship;
import battlechallenge.ship.Ship.Direction;
import battlechallenge.structures.Barrier;
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
		if (directionList.size() == 0) {
			directionList.add(Direction.NORTH);
			directionList.add(Direction.SOUTH);
			directionList.add(Direction.EAST);
			directionList.add(Direction.WEST);
			directionList.add(Direction.STOP);
		}
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
			Coordinate newCoord = new Coordinate(coor.getRow()-1, coor.getCol());
			if (validCoordinate(newCoord)) {
				//System.out.println("Old: " + coor + "New: " + newCoord);
				return newCoord;
			}
//			else {
//				System.out.println(newCoord + " Is Invalid");
//			}
		}
		case SOUTH: {
			Coordinate newCoord = new Coordinate(coor.getRow()+1, coor.getCol());
			if (validCoordinate(newCoord)) {
				return newCoord;
			}
		}
		case EAST: {
			Coordinate newCoord = new Coordinate(coor.getRow(), coor.getCol() + 1);
			if (validCoordinate(newCoord)) {
				return newCoord;
			}
		}
		case WEST: {
			Coordinate newCoord = new Coordinate(coor.getRow(), coor.getCol() - 1);
			if (validCoordinate(newCoord)) {
				return newCoord;
			}
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
		if (coord.getRow() < 0 || coord.getRow() > ClientGame.getMap().getNumRows() || coord.getCol() < 0 || 
				coord.getCol() > ClientGame.getMap().getNumCols())
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
				newDist = s2.getLocation().manhattanDistance(newCoord);
				if (newDist < s2.getLocation().manhattanDistance(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.NORTH;
					}
				}
				continue;
			}
			case SOUTH: {
				newCoord = new Coordinate(s1.getLocation().getRow()+1, s1.getLocation().getCol());
				newDist = s2.getLocation().manhattanDistance(newCoord);
				if (newDist < s2.getLocation().manhattanDistance(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.SOUTH;
					}
				}
				continue;
			}
			case EAST: {
				newCoord = new Coordinate(s1.getLocation().getRow(), s1.getLocation().getCol() + 1);
				newDist = s2.getLocation().manhattanDistance(newCoord);
				if (newDist < s2.getLocation().manhattanDistance(s1.getLocation())) {
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
				if (newDist < s2.getLocation().manhattanDistance(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.WEST;
					}
				}
				continue;
			}

			case STOP: {
				newCoord = s1.getLocation();
				newDist = s2.getLocation().manhattanDistance(newCoord);
				if (newDist < s2.getLocation().manhattanDistance(s1.getLocation())) {
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
		int minDistance = Integer.MAX_VALUE;
		for (Direction d : directionList) {
			Coordinate newCoord;
			int newDist = 0;
			switch (d) {
			case NORTH: {
				newCoord = new Coordinate(s1.getLocation().getRow()-1, s1.getLocation().getCol());
				newDist = coord.manhattanDistance(newCoord);
				if (newDist < coord.manhattanDistance(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.NORTH;
					}
				}
				break;
			}
			case SOUTH: {
				newCoord = new Coordinate(s1.getLocation().getRow()+1, s1.getLocation().getCol());
				newDist = coord.manhattanDistance(newCoord);
				if (newDist < coord.manhattanDistance(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.SOUTH;
					}
				}
				break;
			}
			case EAST: {
				newCoord = new Coordinate(s1.getLocation().getRow(), s1.getLocation().getCol() + 1);
				newDist = coord.manhattanDistance(newCoord);
				if (newDist < coord.manhattanDistance(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.EAST;
					}
				}
				break;
			}
			case WEST: {
				newCoord = new Coordinate(s1.getLocation().getRow(), s1.getLocation().getCol() - 1);
				newDist = coord.manhattanDistance(newCoord);
				if (newDist < coord.manhattanDistance(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.WEST;
					}
				}
				break;
			}

			case STOP: {
				newCoord = s1.getLocation();
				newDist = coord.manhattanDistance(newCoord);
				if (newDist <= coord.manhattanDistance(s1.getLocation())) {
					if (newDist < minDistance) {
						minDistance = newDist;
						bestDirection = Direction.STOP;
					}
				}
				break;

			}
			}
		}
		return bestDirection;
	}

	public Ship closestEnemyShip(Ship myShip, List<Ship> enemyShips) {
		Ship closestShip = null;
		int currDist;
		int minDist = Integer.MAX_VALUE;
		for (Ship eShip: enemyShips) {
			currDist = myShip.getLocation().manhattanDistance(eShip.getLocation());
			if (currDist < minDist) {
				closestShip = eShip;
				minDist = currDist;
			}
		}
		return closestShip;
	}

	public City closestCity(Ship myShip, List<City> cities) {//, Map<Coordinate, Ship> moveMap) {
		City closestCity = null;
		int currDist;
		int minDist = Integer.MAX_VALUE;
		for (City city: cities) {
			if (city.getOwnerId() == ClientGame.getNetworkID()) { // I own the city
				continue;
			}
			currDist = myShip.getLocation().manhattanDistance(city.getLocation());
			if (currDist < minDist && currDist != 0) {
				closestCity = city;
				minDist = currDist;
			}
		}
		return closestCity;
	}

	public City theClosestCity(Ship ship, List<City> cities) {
		City closestCity = null;
		int currDist;
		int minDist = Integer.MAX_VALUE;
		for (City city: cities) {
			currDist = ship.getLocation().manhattanDistance(city.getLocation());
			if (currDist < minDist) {
				closestCity = city;
				minDist = currDist;
			}
		}
		return closestCity;
	}

	public boolean isOnMyCity(Coordinate coord) {
		for (City city: ClientGame.getMyCities()) {
			if (city.getLocation().equals(coord)) {
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
	
	public double manhattanDistance(Coordinate c1, Coordinate c2) {
		return (double) Math.abs(c2.getCol()-c1.getCol()) + Math.abs(c2.getRow()-c1.getRow());
	} 


	public Direction BFS(Ship s, Coordinate goal, List<Coordinate> myShipCoord, List<Coordinate> barriers) {
		//System.out.println("Goal: " + goal.toString());
		
		Map<Node, Node> prev = new HashMap<Node, Node>();
		Map<Node, String> visited = new HashMap<Node, String>();
		ArrayList<Node> visited2 = new ArrayList<Node>();
		int gScore = 0;
		Node currNode = new Node(s.getLocation(), null, null, 0.0, gScore); // Coordinate, Prev Node, Direction, Distance, gScore
		int maxNodes = 1500;
		int expandedNodes = 0;
		List<Direction> directions = new LinkedList<Direction>();
		Comparator<Node> comparator = new Node(null, null, null, null, 0);
		PriorityQueue<Node> q = new PriorityQueue<Node>(500, comp);
		q.add(currNode);
		while (!q.isEmpty() && expandedNodes <= maxNodes) {
			expandedNodes++;
			currNode = (Node) q.poll();
			visited.put(currNode, currNode.toString());
			visited2.add(currNode);
			if ((currNode.getLocation()).equals(goal)) { // Found where we want to go
				break;
			}
			for (Direction dir: directionList) { // look into
				if (dir == Direction.STOP) {
					continue;
				}
				Coordinate newCoord = move(dir, currNode.getLocation());
			
//				Node adjNode = new Node (newCoord, currNode, dir, currNode.getgScore() + 1 + manhattanDistance(newCoord, goal), currNode.getgScore() + 1);
				Node adjNode = new Node (newCoord, currNode, dir, manhattanDistance(newCoord, goal), currNode.getgScore() + 1);
				//adjNode.setDistance(adjNode.getDistance() + adjNode.getLocation().distanceTo(coord));
//				if (visited.get(adjNode) != null || myShipCoord.contains(adjNode.getLocation()) || barriers.contains(adjNode.getLocation())) { // don't use path along friendly ship
				if (visited2.contains(adjNode)|| barriers.contains(adjNode.getLocation())) {
					//System.out.println("Already visited");
					continue;
				}
				//visited.put(adjNode, adjNode.toString());
				if (!q.contains(adjNode)) { // || adjNode.getDistance() < gScore) {
					q.offer((adjNode));
//					visited.put(adjNode, adjNode.toString());
					prev.put(adjNode, currNode);	
				}
				if ((adjNode.getLocation()).equals(goal)) { // Found where we want to go
					currNode = adjNode;
					break;
				}
				expandedNodes++;
			}
		}
		if (!(currNode.getLocation().equals(goal))) {
//			System.out.println("Current Node: " + currNode.getLocation());
//			System.out.println("Goal Coord:: " + goal);
//			System.out.println("Expanded Nodes: " + expandedNodes);
			System.out.println("Failed to find a path");
			return null;
		}
				
		for(Node node = currNode; node != null; node = prev.get(node)) {
	        directions.add(node.getDir());
	    }
		//System.out.println(directions);
		if (directions.size() > 2) {
	    	return directions.get(directions.size()-2);
		}
		else return directions.get(directions.size()-1);
			
	}
	/*
	 * Used to find closest ship and fastest direction to move to get to that location
	 */
	static Comparator<Node> comp = new Comparator<Node>() { // frames are created with IDs in order, sort them this way
		  public int compare(Node n1, Node n2) {
//		    return n1.getgScore().compareTo(n2.getgScore());
			  return n1.getDistance().compareTo(n2.getDistance());
		  }
	};
	
	
	public Direction aStar(Coordinate c, List<Coordinate> myShipCoord, List<Coordinate> barriers) {
		Map<Node, Node> prev = new HashMap<Node, Node>();
		Map<Node, String> visited = new HashMap<Node, String>();
		Node currNode = new Node(c, null, null); // Coordinate, Node, Direction, Distance, gScore
		int maxNodes = 2000;
		Ship ship = null;
		int expandedNodes = 0;
		List<Direction> directions = new LinkedList<Direction>();
		Comparator<Node> comparator = new Node(null, null, null);
		PriorityQueue<Node> q = new PriorityQueue<Node>(300, comp);
		q.add(currNode);
		while (!q.isEmpty() && expandedNodes <= maxNodes) {
			expandedNodes++;
			currNode = (Node) q.poll();
			visited.put(currNode, currNode.toString());
			if ((myShipCoord.contains(currNode.getLocation())) && !isOnMyCity(currNode.getLocation())) { // Found a ship
				break;
			}
			for (Direction dir: directionList) { // look into
				if (dir == Direction.STOP) {
					continue;
				}
				Coordinate newCoord = move(dir, currNode.getLocation());
			
				Node adjNode = new Node (newCoord, currNode, dir);
				
				if (visited.get(adjNode) != null || barriers.contains(adjNode.getLocation())) { // don't use path along friendly ship
					continue;
				}
				if (!q.contains(adjNode)) {
					q.offer((adjNode));
//					visited.put(adjNode, adjNode.toString());
//					prev.put(adjNode, currNode);	
				}
				if (myShipCoord.contains(adjNode.getLocation()) & !isOnMyCity(adjNode.getLocation())) { // Found where we want to go
					currNode = adjNode;
					//Ship ship = 
//					break;
				}
				expandedNodes++;
			}
		}
		
		if (!myShipCoord.contains(currNode.getLocation()) && !isOnMyCity(currNode.getLocation())) { // didnt end up on a ship
			return null;
		}
		
		for(Node node = currNode; node != null; node = prev.get(node)) {
	        directions.add(node.getDir());
	    }
		
		if (directions.size() > 2) { // redundant fix
			//moveMap.add((currNode.getLocation())
	    	return directions.get(0);
		}
		else return directions.get(0);
			
	}
	
	public Map<Coordinate, City> getCityCoordMap() {
		Map<Coordinate, City> cityMap = new HashMap<Coordinate, City>();
		for (City city: ClientGame.getAllCities()) {
			cityMap.put(city.getLocation(), city);
		}
		return cityMap;
	}
	
//	public void floydWarshall() {
//		BattleMap map = ClientGame.getMap();
//		int numVertices = map.getNumCols() * map.getNumRows();
//		int[][] path = new int [map.getNumRows()][map.getNumCols()];
//	    for (int k = 0; k < numVertices; k++) {
//	      for (int i = 0; i < numVertices; i++) {
//	        for (int j = 0; j < numVertices; j++) {
//	          path[i][j] = Math.min(path[i][j], path[i][k] + path[k][j]);
//	        }
//	      }
//	    }
//	}
	Map<Coordinate, Ship> moveMap;
	
	Comparator<Ship> cityComp = new Comparator<Ship>() { // frames are created with IDs in order, sort them this way
		  public int compare(Ship s1, Ship s2) {
			  if (isOnMyCity(s1.getLocation())) {
				  return 1; 
			  }
			  if (isOnMyCity(s2.getLocation())) {
				  return 0;
			  }
			  return 1;
			//  return s1.getLocation()..compareTo(n2.getDistance());
		  }
	};
	
	public List<ShipAction> doTurn() {
//		System.out.println(ClientGame.getMap().getNumCols());
		List<ShipAction> actions = new LinkedList<ShipAction>();
		moveMap = new HashMap<Coordinate, Ship>();
		Map<Coordinate, Ship> shotMap = new HashMap<Coordinate, Ship>();
		List<Ship> myShips = ClientGame.getMyShips();
		Map<Coordinate, City> cityCoordMap = getCityCoordMap();
		List<Coordinate> myShipLocations = new LinkedList<Coordinate>();
		List<Coordinate> barriers = new LinkedList<Coordinate>(); 
		//ClientGame.getBarriers();
		fillDirectionList();
		for (Ship s : myShips) {
			myShipLocations.add(s.getLocation());
		}
		for (Barrier b : ClientGame.getBarriers()) {
			barriers.add(b.getLocation());
		}
		List<Ship> myMovedShips = new LinkedList<Ship>();
		List<Ship> enemyShips = ClientGame.getOpponentShips();
		List<City> cityList = ClientGame.getAllCities();
		List<Coordinate> moveTargets = new LinkedList<Coordinate>();
		List<Coordinate> shotTargets = new LinkedList<Coordinate>();
		Map<Ship, Direction> shipMoves = new HashMap<Ship, Direction>();

		City closeCity;
		boolean movingTowardsCity;
		boolean movingTowardsShip;
		
		Collections.sort(myShips, cityComp);
		
//		for (Ship s: myShips) {
//			if (isOnMyCity(s.getLocation())) {
//			System.out.println("On City " + s.getLocation());
//			}
//			else {
//				System.out.println("Not on city " + s.getLocation());
//			}
//		}
//		
		
		for (Ship s : myShips) {
			movingTowardsCity = false;
			movingTowardsShip = false;
			Ship closeEnemy = null;
			closeCity = null;
			Direction moveDirection = null;
			closeEnemy = closestEnemyShip(s, enemyShips);
			closeCity = closestCity(s, cityList);//, moveMap);
			if (isOnMyCity(s.getLocation())) {
			//	if (moveMap.get(s.getLocation()) == null) { // No ship is currently suppose to move to this location
				
//					Ship ship = moveMap.get(s.getLocation());
//					shipMoves.remove(moveMap.get(s.getLocation()));
//					if (moveMap.get(ship.getLocation()) != null) { //ship would collide with city
//						moveMap.put(ship.getLocation(), ship);
//						shipMoves.put(ship, Direction.STOP);
//					}
					moveDirection = moveTowardsCoord(s, closeCity.getLocation());
					Coordinate newCoord = move(moveDirection, s.getLocation());
					if (moveMap.get(newCoord) != null) { // someone already moving to new coord so stay on city
						moveDirection = Direction.STOP;
						moveMap.put(s.getLocation(), s); // stay on city
					}
					else if (moveMap.get(newCoord) == null && cityCoordMap.get(newCoord) != null) { // post move coordinate is a city
						moveMap.put(newCoord, s); // move to adjacent city
					}
					else {
						moveDirection = Direction.STOP;
						moveMap.put(s.getLocation(), s); // stay on city
					}
					shipMoves.put(s, moveDirection);
					continue;
			//	}
			}

		

			if (moveDirection == null) {
				if (closeCity != null && moveMap.get(closeCity.getLocation()) == null) { // no ship moving onto city already
					moveDirection = BFS(s, closeCity.getLocation(), myShipLocations, barriers);
					if (moveDirection == null) {
						moveDirection = moveTowardsCoord(s, closeCity.getLocation());
					}
					//System.out.println(moveDirection);
					movingTowardsCity = true;
				}
				else {
					moveDirection = BFS(s, closeEnemy.getLocation(), myShipLocations, barriers);
					if (moveDirection == null) {
						moveDirection = moveTowardsCoord(s, closeEnemy.getLocation());
					}
					movingTowardsCity = false;	
					movingTowardsShip = true;
				}
			}
			if (moveDirection != null) {
//				System.out.println(moveDirection);
				Coordinate newCoord = move(moveDirection, s.getLocation());
//				 if (moveMap.get(newCoord) != null) {
//					 if (movingTowardsShip == false) {
//						 moveDirection = moveTowardsShip(s, closeEnemy);
//						 newCoord = move(moveDirection, s.getLocation());
//					 }
//				 }
			// Avoid collisions
//				System.out.println(s.getLocation());
////				System.out.println(myShipLocations);
//				System.out.println(newCoord);
//				System.out.println(moveDirection);
			if (moveMap.get(newCoord) == null && !myShipLocations.contains(newCoord)) { // No Ship is moving here
				int index = myShipLocations.indexOf(s.getLocation());
				myShipLocations.remove(index);
				myShipLocations.add(newCoord); // update current ship locations
				shipMoves.put(s, moveDirection);
				moveMap.put(newCoord, s);
				}
			}
		
			
			if (shipMoves.get(s) == null) {
				shipMoves.put(s, Direction.STOP);
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

			// Decide where to shoot
			
					
			if (shotCoordinates.isEmpty() && !shotTargets.contains(closeEnemy.getLocation()) && s.inRange(closeEnemy.getLocation())) {
				shotCoordinates.add(closeEnemy.getLocation());
				shotTargets.add(closeEnemy.getLocation());
			}
					
			Coordinate secondCloseEnemyCoordGuess = move(moveTowardsCoord(closeEnemy, theClosestCity(closeEnemy, cityList).getLocation()), closeEnemy.getLocation());
			
			if (shotCoordinates.isEmpty() && (!shotTargets.contains(secondCloseEnemyCoordGuess)) && s.inRange(secondCloseEnemyCoordGuess)) {
				shotCoordinates.add(secondCloseEnemyCoordGuess);
				shotTargets.add(secondCloseEnemyCoordGuess);
			}
			
			Coordinate newCloseEnemyCoordGuess = move(moveTowardsCoord(closeEnemy, oldShipLocation), closeEnemy.getLocation());	
			
			if (shotCoordinates.isEmpty() && s.inRange(newCloseEnemyCoordGuess) && !shotTargets.contains(newCloseEnemyCoordGuess)) {
				shotCoordinates.add(newCloseEnemyCoordGuess);
				shotTargets.add(newCloseEnemyCoordGuess);
			}
			if (shotCoordinates.isEmpty() && s.inRange(closeEnemy.getLocation()) && (!shotTargets.contains(closeEnemy.getLocation()))) {
				shotCoordinates.add(closeEnemy.getLocation());
				shotTargets.add(closeEnemy.getLocation());
			}

			if (shotCoordinates.isEmpty()) {
				for (Ship eShip: enemyShips) {
					Coordinate coord = eShip.getLocation();
					if (s.inRange(coord) && (!shotTargets.contains(coord))) {
						//if (isOnCity(eShip)) {
							shotCoordinates.add(coord);
							shotTargets.add(coord);
						//}
					}
				}
			}
	
			// Decide where to shoot


			actions.add(new ShipAction(s.getIdentifier(), shotCoordinates, moves));
			//System.out.println(actions);
		}
		//System.out.println("Target List: " + shotTargets);
		cityCoordMap.clear();
		moveMap.clear();
		shipMoves.clear();
		return actions;
	}
}