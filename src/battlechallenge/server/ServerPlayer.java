package battlechallenge.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import battlechallenge.ActionResult;
import battlechallenge.ActionResult.ShotResult;
import battlechallenge.Coordinate;
import battlechallenge.ShipAction;
import battlechallenge.ShipIdentifier;
import battlechallenge.maps.BattleMap;
import battlechallenge.network.ConnectionLostException;
import battlechallenge.ship.Ship;
import battlechallenge.ship.Ship.Direction;
import battlechallenge.ship.ShipCollection;
import battlechallenge.structures.Base;
import battlechallenge.structures.Structure;
import battlechallenge.structures.Barrier;

/**
 * The Class ServerPlayer.
 */
public class ServerPlayer {

	/** The name. */
	private String name;

	/** The ships. */
	private ShipCollection ships;

	/** The conn. */
	private ClientConnection conn;

	/** The score. */
	private ScoreKeeper score = new ScoreKeeper();
	
	/** The hits. */
	private int hits;
	
	/** The total shots. */
	private int totalShots;

	/** The action log. */
	private List<ActionResult> actionLog;

	/** The action log. */
	private List<ActionResult> lastActionResults;

	/** The hit log. */
	private Set<String> hitLog;
	
	/** The id. */
	private final int id;

	/** The has ships. */
	private boolean hasShips = true;
	
	/** The last ship positions. */
	private Map<String, Coordinate> lastShipPositions = new HashMap<String, Coordinate>();
	
	/** The minerals. */
	private int minerals;
	
	private int totalMinerals = 0;
	
	private int boardHeight;
	
	private int boardWidth;
	
	public int getTotalMinerals() {
		return totalMinerals;
	}
	
	private Base base;
	
	private int minsPerShip;
	
	private int totalShips = 1;
	
	public int getTotalShips() {
		return totalShips;
	}
	
	public int getMinsPerShip() {
		return minsPerShip;
	}

	public void setMinsPerShip(int minsForShip) {
		this.minsPerShip = minsForShip;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the ships copy.
	 *
	 * @return the ships copy
	 */
	public List<Ship> getShipsCopy() {
		List<Ship> temp = new LinkedList<Ship>();
			for(Ship s : ships.getPlayerShips(this))
				temp.add(s.deepCopy());
			return temp;
	}
	
	/**
	 * Gets the action log.
	 *
	 * @return a list of action results returned over the
	 * period of a game
	 */
	public List<ActionResult> getActionLog() {
		return actionLog;
	}
	
	/**
	 * Gets the last action results.
	 *
	 * @return the last action results
	 */
	public List<ActionResult> getLastActionResults() {
		return lastActionResults;
	}
	
	/**
	 * Gets the last action results copy.
	 *
	 * @return the last action results copy
	 */
	public List<ActionResult> getLastActionResultsCopy() {
		List<ActionResult> temp = new LinkedList<ActionResult>();
		temp.addAll(lastActionResults);
		return temp;
	}
	
	/**
	 * Sets the last action results.
	 *
	 * @param actionResults the new last action results
	 */
	public void setLastActionResults(List<ActionResult> actionResults) {
		// FIXME: this might need to be fixed with more than two players
		for (ActionResult result : actionResults) {
			if (result.getResult() != ShotResult.MISS)
				this.hits++;
			this.totalShots++;
		}
		lastActionResults = actionResults;
	}
	
	/**
	 * Gets the shot accuracy.
	 *
	 * @return the shot accuracy
	 */
	public double getShotAccuracy() {
		return (double)hits/(double)totalShots;
	}
	
	/**
	 * Gets the hit count.
	 *
	 * @return the hit count
	 */
	public int getHitCount() {
		return hits;
	}
	
	/**
	 * Gets the total shot count.
	 *
	 * @return the total shot count
	 */
	public int getTotalShotCount() {
		return totalShots;
	}
	
	/**
	 * Gets the number of live ships.
	 *
	 * @return the number of live ships
	 */
	public int getNumLiveShips() {
		int count = 0;
		for (Ship s : ships.getPlayerShips(this))
			if (!s.isSunken())
				count++;
		return count;
	}

	/**
	 * Instantiates a new server player.
	 *
	 * @param socket the socket
	 * @param id the id
	 */
	public ServerPlayer(Socket socket, int id) {
		this.id = id;
		this.conn = new ClientConnection(socket, id);
		// TODO: initialize player
		if (!conn.setupHandshake()) {
			// TODO: handle invalid player
			throw new IllegalArgumentException();
		}
		this.actionLog = new LinkedList<ActionResult>();
		this.lastActionResults = new LinkedList<ActionResult>();
	}
	
	/**
	 * End game.
	 *
	 * @param result the result
	 */
	public void endGame(String result) {
		conn.endGame(result);
	}

	/**
	 * Kills the player and socket connection.
	 * 
	 * DO NOT FORGET TO DO THIS!!!
	 */
	public void kill() {
		this.conn.kill();
	}

	/**
	 * Sets the credentials.
	 *
	 * @param boardWidth the board width
	 * @param boardHeight the board height
	 * @return true, if successful
	 */
	public boolean setCredentials(int boardWidth, int boardHeight) {
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
		this.name = conn.setCredentials(id, boardWidth, boardHeight);
		return this.name != null;
	}
	
	public void setShipCollection(ShipCollection shipCollection) {
		this.ships = shipCollection;
	}
	
	/**
	 * Move ships.
	 *
	 * @param shipAction the ship action
	 * @param boardWidth the board width
	 * @param boardHeight the board height
	 * @return the list
	 */
	private void moveShips(List<ShipAction> shipAction, int boardWidth, int boardHeight, List<Structure> structures) {
		for (ShipAction shipAct: shipAction) {
			if (this.id != shipAct.getShipIdentifier().playerId) { // playerId does not match shipId
				System.out.println("Attempted to act on ship that isn't players");
				continue;
			}
			Ship s = ships.getShip(shipAct.getShipIdentifier());
			if (s != null && shipAct.getMoveDir() != null && !s.isSunken()) {
				lastShipPositions.put(s.getIdentifier().toString(), s.getLocation());
				Coordinate newCoord = move(shipAct.getMoveDir(), s.getLocation());
				s.setLocation(newCoord);
				if (!s.inBoundsInclusive(0, boardHeight-1, 0, boardWidth-1)) { // Check if ship remains on map
					s.setLocation(lastShipPositions.get(s.getIdentifier().toString()));
				}
				for (Structure str: structures) {
					if (str instanceof Barrier && str.getLocation().equals(newCoord)) { // Is the new coordinate on top of a barrier?
						s.setLocation(lastShipPositions.get(s.getIdentifier().toString())); // reverting the movement because attempting to move into a barrier
					}
				}
			}
		}
	}
	
	/**
	 * Will revert the movement of a ship to where the ship was
	 * before it moved.
	 *
	 * @param shipId The Id of the ship
	 */
	public void revertMovement(ShipIdentifier shipId) {
		Ship s = ships.getShip(shipId);
		s.setLocation(lastShipPositions.get(s.getIdentifier().toString()));
	}
	
	/**
	 * Move.
	 *
	 * @param dir the direction to move
	 * @param coor the ship location
	 * @return the new ship coordinate
	 */
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

	/**
	 * Request turn.
	 *
	 * @param allPlayersShips the all players ships
	 * @param actionResults the action results
	 * @return true, if successful
	 */
	public boolean requestTurn(Map<Integer, List<Ship>> allPlayersShips, Map<Integer, List<ActionResult>> actionResults, BattleMap map) {
		setLastActionResults(actionResults.get(id));
		this.actionLog.addAll(actionResults.get(this.id));
		return conn.requestTurn(allPlayersShips, actionResults, map);
	}

	/**
	 * Reads the socket.
	 *
	 * @param boardWidth the game board width
	 * @param boardHeight the game board height
	 * @return a list of ship actions from the ClientPlayer
	 */
	public List<ShipAction> getTurn(int boardWidth, int boardHeight, List<Structure> structures) {
		List<ShipAction> shipActions = new LinkedList<ShipAction>();
		try {
			shipActions = conn.getTurn(); // get the ship action list from the client player
			moveShips(shipActions, boardWidth, boardHeight, structures);
			return shipActions;
		} catch (ConnectionLostException e) {
			// TODO: handle lost connection
		}
		return shipActions;
	}

	/**
	 * Gets the score.
	 * 
	 * @return the score
	 */
	public int getScore() {
		return score.getScore();
	}
	
	/**
	 * Checks to see if the player has any ships not sunk.
	 *
	 * @return true, if player has ships left
	 */
	public boolean isAlive() {
		if (!conn.isOpen())
			return false;
		if (hasShips) {
			for (Ship s : ships.getPlayerShips(this)) {
				if (!s.isSunken()) {
					return true;
				}
			}
			hasShips = false;
		}
		return hasShips;
	}
	
	/**
	 * Checks if the ship is hit as a result of an action.
	 *
	 * @param c the coordinate affected by the action
	 * @param damage the damage incurred as a result of the action
	 * @return the action result
	 */
	public ActionResult isHit(Coordinate c, Ship attacker, int damage) {
		if (c == null)
			return new ActionResult(c, attacker.getLocation(), ShotResult.MISS, -1, id);
		for (Ship s : ships.getPlayerShips(this)) {
			if (s.isHit(c, damage)) {
				return new ActionResult(c, attacker.getLocation(), s.isSunken() ? ShotResult.SUNK : ShotResult.HIT, s.getHealth(), id);
			}
		}
		return new ActionResult(c, attacker.getLocation(), ShotResult.MISS, -1, id);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.id+"");
		sb.append(":").append(this.name).append("(");
		return sb.append(this.score+"").append(")").toString();
	}
	
	/**
	 * Gets the ships opponnent.
	 *
	 * @param opp the opp
	 * @return the ships opponnent
	 */
	public List<Ship> getUnsunkenShips(ServerPlayer opp) {
		// TODO: return not all ships
		// currently returns all unsunken ships
		List<Ship> unsunkShips = new LinkedList<Ship>();
		for (Ship ship: ships.getPlayerShips(this))
			if (!ship.isSunken())
				unsunkShips.add(ship);
		return unsunkShips;
	}

	/**
	 * Gets the minerals.
	 *
	 * @return the minerals
	 */
	public int getMinerals() {
		return minerals;
	}

	/**
	 * Sets the minerals.
	 *
	 * @param minerals the new minerals
	 */
	public void setMinerals(int minerals) {
		this.minerals = minerals;
	}
	
	/**
	 * Increment minerals.
	 *
	 * @param income the income
	 */
	public void incrementMinerals(int income) {
		this.minerals += income;
		this.totalMinerals += income;
		this.score.addMineralsEarned(income);
	}

	public Base getBase() {
		return base;
	}

	public void setBase(Base base, List<Ship> ships) {
		this.base = base;
		for (Ship ship: ships) {
			ship.setPlayerId(id);
		}
		// place first ship like normal
		Ship s = ships.get(0);
		s.setLocation(base.getLocation());
		s.setShipId(this.ships.getNextShipId());
		s.setPlayerId(this.id);
		this.ships.addShip(s);
		// FIXME: place ships correctly
		this.minerals += (ships.size() - 1)*this.minsPerShip; // allocate minerals for any remaining ships.
	}
	
	public boolean baseBlocked() {
		if (ships.getShip(base.getLocation()) != null) // no ship on base
			return true;
		return false;
	}
	
	/**
	 * Spawn a new ship at the base if the player has enough
	 * minerals. Spawn ships adjacent to base if spawning more than one ship at a time
	 */
	public void spawnShip() {
		List<Coordinate> spawnLocations = genSpawnCoords(base.getLocation());
		int i = 0;
		if (minerals >= minsPerShip && minerals - minsPerShip < minsPerShip) { // Only enough minerals to spawn one ship so spawn on base if possi ble
			if (!baseBlocked()) {
				totalShips++;
				Ship ship = new Ship(spawnLocations.get(i));
				ship.setPlayerId(id);
				ship.setShipId(ships.getNextShipId()); 
				ships.addShip(ship, true);
				minerals -= minsPerShip; // Subtract cost to make a ship
				return;
			}
		}
		while (minerals >= minsPerShip  && i < spawnLocations.size()) {
			totalShips++;
			Ship ship = new Ship(spawnLocations.get(i));  // get first available spawn location
			ship.setPlayerId(id);
			ship.setShipId(ships.getNextShipId()); // TODO: Keep track of number of ships created thus far instead
			ships.addShip(ship, true);
			minerals -= minsPerShip; // Subtract cost to make a ship
			i++;
		}
	}
	
	public List<Coordinate> genSpawnCoords(Coordinate coord) {
		List<Coordinate> adjCoords = new ArrayList<Coordinate>();
		Coordinate North = new Coordinate(coord.getRow()-1, coord.getCol());
		Coordinate South = new Coordinate(coord.getRow()+1, coord.getCol());
		Coordinate East = new Coordinate(coord.getRow(), coord.getCol() + 1);
		Coordinate West = new Coordinate(coord.getRow(), coord.getCol() - 1);
		Coordinate NorthEast = new Coordinate(coord.getRow()-1, coord.getCol() + 1);
		Coordinate NorthWest = new Coordinate(coord.getRow()-1, coord.getCol() - 1);
		Coordinate SouthEast = new Coordinate(coord.getRow()+1, coord.getCol() + 1);
		Coordinate SouthWest = new Coordinate(coord.getRow()+1, coord.getCol() - 1);
		Coordinate Base = base.getLocation();
		adjCoords.add(North);
		adjCoords.add(South);
		adjCoords.add(East);
		adjCoords.add(West);
		adjCoords.add(NorthEast);
		adjCoords.add(SouthWest);
		adjCoords.add(NorthWest);
		adjCoords.add(SouthEast);
		adjCoords.add(Base);
		adjCoords = validateCoordinateList(adjCoords);
		return adjCoords;
}
	/**
	 * Gets the adjacent coordinates
	 * @param coord The coordinate to get the adjacent coordinates of
	 * @return A list of adjacent coordinates to the coordinate
	 */
	public List<Coordinate> validateCoordinateList(Collection<Coordinate> coordList) {
		Map<String, Ship> allShipsLocations = ships.getCoordMap();
		List<Coordinate> validSpawnCoordinates = new LinkedList<Coordinate>();
		for (Coordinate coord: coordList) {
			if (allShipsLocations.get(coord.toString()) == null) {
				if (!(coord.getRow() < 0 || coord.getRow() >= boardHeight || coord.getCol() < 0 || coord.getCol() >= boardWidth)) {
					validSpawnCoordinates.add(coord);
				}
			}
		}
		return validSpawnCoordinates;
}
	
	
	public void recordSunkenShip() {
		score.addSunkShip();
	}

}
