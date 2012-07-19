package battlechallenge.bot;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import battlechallenge.Coordinate;
import battlechallenge.ShipAction;
import battlechallenge.ShipIdentifier;
import battlechallenge.client.ClientGame;
import battlechallenge.ship.Ship;
import battlechallenge.ship.Ship.Direction;
import battlechallenge.structures.Base;
import battlechallenge.structures.City;

public class DavidBot2 extends ClientPlayer {

	private static final Set<String> moves;
	private static final Set<String> cityCoords;
	private static final DoubleMap dMap;
	private static final PriorityQueue<City> sortedCities;
	private static final Direction[] dirs = new Direction[] { Direction.NORTH,
			Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.STOP };
	private static final Map<String, AssignedShip> ships;
	private static final Map<Ship, Direction> shipMoves;
	
	static {
		moves = new HashSet<String>();
		cityCoords = new HashSet<String>();
		dMap = new DoubleMap();
		sortedCities = new PriorityQueue<City>(5, new Comparator<City>() {
			@Override
			public int compare(City arg0, City arg1) {
				Coordinate base = ClientGame.getMyBase().getLocation();
				return base.distanceTo(arg0.getLocation()) < base
						.distanceTo(arg1.getLocation()) ? -1 : 1;
			}
		});
		ships = new HashMap<String, AssignedShip>();
		shipMoves = new HashMap<Ship, Direction>();
	}

	public DavidBot2(String playerName, int mapWidth, int mapHeight,
			int networkID) {
		super(playerName, mapWidth, mapHeight, networkID);
	}

	@Override
	public List<ShipAction> doTurn() {
		if (sortedCities.isEmpty()) {
			sortedCities.addAll(ClientGame.getAllCities());
		}
		// clear list of moves
		moves.clear();
		shipMoves.clear();
		List<Ship> myShips = ClientGame.getMyShips();
		
		// move assigned ships
		for(int i=0;i<myShips.size();) {
			Ship s = myShips.get(i);
			if (ships.containsKey(s.getIdentifier().toString())) {
				shipMoves.put(s, ships.get(s.getIdentifier().toString()).getMove(s));
				myShips.remove(i);
			} else
				i++;
		}
		
		// assign and move other ships
		for (Ship s : myShips) {
			AssignedShip as = AssignedShip.getAssignment(s);
			ships.put(s.getIdentifier().toString(), as);
			shipMoves.put(s, as.getMove(s));
		}
		
		// choose shots and build action list
		List<ShipAction> actions = new LinkedList<ShipAction>();
		for (Entry<Ship,Direction> e : shipMoves.entrySet()) {
			
		}
		return actions;
	}
	
	private Coordinate getShot(Ship s) {
		if (cityCoords.isEmpty()) {
			for (City c : ClientGame.getAllCities())
				cityCoords.add(c.getLocation().toString());
		}
		
		// TODO
		return null;
	}
	
	private Coordinate getClosestEnemy() {
		Coordinate minCoord = null;
		double minDist = Double.MAX_VALUE;
		for (Entry<Integer, List<Ship>> e : ClientGame.getShipMap().entrySet()) {
			if (e.getKey() != ClientGame.getNetworkID()) {
				for (Ship s : e.getValue()) {
					// TODO
				}
			}
		}
		return minCoord;
	}

	private static class AssignedShip {
		private static Coordinate[] oppBases;
		public ShipIdentifier sid;
		public Coordinate goal;
		public AssignedShip(ShipIdentifier sid, Coordinate goal) {
			this.sid = sid;
			this.goal = goal;
		}
		public Direction getMove(Ship s) {
			Coordinate origin = s.getLocation();
			PriorityQueue<SortedMove> sortedMoves = new PriorityQueue<SortedMove>();
			for (Direction d : dirs) {
				Coordinate temp = getMovedCoordinate(origin, d);
				sortedMoves.add(new SortedMove(temp, d, temp.distanceTo(goal)));
			}
			while (!moves.isEmpty()) {
				SortedMove move = sortedMoves.poll(); 
				if (!moves.contains(move.c.toString())) {
					moves.add(move.c.toString());
					return move.d;
				}
			}
			moves.add(origin.toString());
			return Direction.STOP;
		}
		private Coordinate getMovedCoordinate(Coordinate c, Direction d) {
			if (d == Direction.NORTH)
				return new Coordinate(c.getRow()-1, c.getCol());
			if (d == Direction.SOUTH)
				return new Coordinate(c.getRow()+1, c.getCol());
			if (d == Direction.EAST)
				return new Coordinate(c.getRow(), c.getCol()+1);
			return new Coordinate(c.getRow(), c.getCol()-1);
		}
		public static void setBases() {
			Collection<Base> bases = ClientGame.getOpponentBases();
			oppBases = new Coordinate[bases.size()];
			int i = 0;
			for (Base b : bases)
				oppBases[i++] = b.getLocation();
		}
		public static AssignedShip getAssignment(Ship s) {
			if (!sortedCities.isEmpty())
				return new AssignedShip(s.getIdentifier(), sortedCities.poll().getLocation());
			if (oppBases == null)
				setBases();
			return new AssignedShip(s.getIdentifier(), oppBases[(int)(Math.random()*oppBases.length)]);
		}
	}

 	private static class DoubleMap {
		private Map<Ship, City> shipMap;
		private Map<City, Ship> cityMap;
	
		public DoubleMap() {
			this.shipMap = new HashMap<Ship, City>();
			this.cityMap = new HashMap<City, Ship>();
		}
	
		public void clear() {
			this.shipMap.clear();
			this.cityMap.clear();
		}
	
		public void put(Ship s, City c) {
			shipMap.put(s, c);
			cityMap.put(c, s);
		}
	
		public Ship getShip(City c) {
			return cityMap.get(c);
		}
	
		public Collection<Ship> getAllShips() {
			return shipMap.keySet();
		}
	
		public boolean containsShip(Ship s) {
			return shipMap.containsKey(s);
		}
	
		public City getCity(Ship s) {
			return shipMap.get(s);
		}
	
		public boolean containsCity(City c) {
			return cityMap.containsKey(c);
		}
	
		public void update(List<Ship> ships) {
			for (Entry<Ship, City> e : shipMap.entrySet()) {
				if (!ships.contains(e.getKey())) {
					shipMap.remove(e.getKey());
					cityMap.remove(e.getValue());
				}
			}
		}
	}

	private static class SortedMove implements Comparable<SortedMove> {
		public Coordinate c;
		public Direction d;
		public double dist;
		public SortedMove(Coordinate c, Direction d, double dist) {
			this.c = c;
			this.d = d;
			this.dist = dist;
		}
		@Override
		public int compareTo(SortedMove that) {
			return this.dist < that.dist ? -1 : this.dist == that.dist ? 0 : 1;
		}
		
	}
}
