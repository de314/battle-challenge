package battlechallenge.bot;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import battlechallenge.Coordinate;
import battlechallenge.ShipAction;
import battlechallenge.client.ClientGame;
import battlechallenge.ship.Ship;
import battlechallenge.ship.Ship.Direction;
import battlechallenge.structures.City;

public class DavidBot extends ClientPlayer {

	
	private Map<Coordinate, Ship> investments;
	private Map<Ship, List<Coordinate>> closestCities;
	private Map<Ship, Coordinate> closestEnemies;
	private Set<String> nextPositions;

	public DavidBot(String playerName, int boardWidth, int boardHeight,
			int networkID) {
		super(playerName, boardWidth, boardHeight, networkID);
		System.out.println("Initializing DavidBot");
		investments = new HashMap<Coordinate, Ship>();
		closestEnemies = new HashMap<Ship, Coordinate>();
		closestCities = new HashMap<Ship, List<Coordinate>>();
		nextPositions = new HashSet<String>();
	}

	@Override
	public List<ShipAction> doTurn() {
		//System.out.println("[[TURN]]");
		setClosestEnemies();
		setClosestCities();
		assignShipsToCities();
		Map<Ship, Direction> actions = moveShips();
		return fireShots(actions);
	}

	private void setClosestEnemies() {
		closestEnemies.clear();
		for (Ship mine : ClientGame.getMyShips()) {
			Coordinate c = mine.getLocation();
			Ship closest = null;
			double dist = -1;
			for (Ship enemy : ClientGame.getOpponentShips()) {
				double temp = c.distanceTo(enemy.getLocation());
				if (closest == null || temp < dist) {
					closest = enemy;
					dist = temp;
				}
			}
			closestEnemies.put(mine, closest.getLocation());
		}
	}

	private void setClosestCities() {
		closestCities.clear();
		for (final Ship mine : ClientGame.getMyShips()) {
			List<City> temp = ClientGame.getAllCities();
			temp.removeAll(ClientGame.getMyCities());
			Coordinate[] arr = new Coordinate[temp.size()];
			for (int i=0;i<arr.length;i++)
				arr[i] = temp.get(i).getLocation();
			Arrays.sort(arr, new Comparator<Coordinate>() {
				@Override
				public int compare(Coordinate a, Coordinate b) {
					double temp = mine.distanceFromCoord(a) - mine.distanceFromCoord(b);
					if (temp < 0)
						return -1;
					if (temp > 0)
						return 1;
					return 0;
				}
			});
			List<Coordinate> coords = new LinkedList<Coordinate>();
			for (int i=0;i<arr.length;i++)
				coords.add(arr[i]);
			closestCities.put(mine, coords);
		}
	}

	private void assignShipsToCities() {
		investments.clear();
		List<City> cities = ClientGame.getAllCities();
		cities.removeAll(ClientGame.getMyCities());
		List<Ship> ships = ClientGame.getMyShips();
		ships.removeAll(investments.keySet());
		for (Ship s : ClientGame.getMyShips()) {
			for (Coordinate c : closestCities.get(s)) {
				if (investments.get(c) == null) {
					investments.put(c, s);
					break;
				}
			}
		}
	}

	private Map<Ship, Direction> moveShips() {
		nextPositions.clear();
		Map<Ship, Direction> actions = new HashMap<Ship, Direction>();
		//System.out.println(investments.size());
		for (Entry<Coordinate, Ship> e : investments.entrySet()) {
			Ship s = e.getValue();
			Coordinate c = s.getLocation();
			Direction dir = Direction.EAST;
			if (e.getKey().getCol() > c.getCol()) {
				c = new Coordinate(c.getRow(), c.getCol()-1);
			} else if (e.getKey().getCol() < c.getCol()) {
				dir = Direction.WEST;
				c = new Coordinate(c.getRow(), c.getCol()+1);
			} else if (e.getKey().getRow() > c.getRow()) {
				dir = Direction.SOUTH;
				c = new Coordinate(c.getRow()+1, c.getCol());
			} else if (e.getKey().getRow() < c.getRow()) {
				dir = Direction.NORTH;
				c = new Coordinate(c.getRow()-1, c.getCol());
			} else {
				actions.put(s, Direction.STOP);
				continue;
			}
			if (!nextPositions.contains(c.toString())) {
				actions.put(s, dir);
				nextPositions.add(c.toString());
			}
		}
		for (Ship s : ClientGame.getMyShips()) {
			if (actions.containsKey(s))
				continue;
			Coordinate closest = closestEnemies.get(s);
			if (closest.getRow() > s.getLocation().getRow())
				actions.put(s, Direction.NORTH);
			else if (closest.getRow() < s.getLocation().getRow())
				actions.put(s, Direction.SOUTH);
			else if (closest.getCol() > s.getLocation().getCol())
				actions.put(s, Direction.EAST);
			else
				actions.put(s, Direction.WEST);
//			System.out.println(s.getLocation() + ":" + closest + ":"
//					+ actions.get(s));
		}
		return actions;
	}

	private List<ShipAction> fireShots(Map<Ship, Direction> directions) {
		List<ShipAction> actions = new LinkedList<ShipAction>();
		for (Ship s : directions.keySet())
			actions.add(new ShipAction(s.getIdentifier(),
					closestEnemies.get(s), directions.get(s)));
		return actions;
	}
}
