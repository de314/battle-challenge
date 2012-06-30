package battlechallenge.server;

import java.net.Socket;
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
import battlechallenge.network.ConnectionLostException;
import battlechallenge.ship.Ship;
import battlechallenge.ship.Ship.Direction;
import battlechallenge.structures.City;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerPlayer.
 */
public class ServerPlayer {

	/** The name. */
	private String name;

	/** The ships. */
	private List<Ship> ships;

	/** The conn. */
	private ClientConnection conn;

	/** The score. */
	private int score;
	
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
	
	/** The ship map. */
	private Map<String, Ship> shipMap = new HashMap();
	
	/** The last ship positions. */
	private Map<String, Coordinate> lastShipPositions = new HashMap();
	
	/** The minerals. */
	private int minerals;
	
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
	 * Gets the ships.
	 *
	 * @return the ships
	 */
	public List<Ship> getShips() {
		return ships;
	}
	
	/**
	 * Gets the ships copy.
	 *
	 * @return the ships copy
	 */
	public List<Ship> getShipsCopy() {
		List<Ship> temp = new LinkedList<Ship>();
		synchronized(ships) {
			for(Ship s : ships)
				temp.add(s.deepCopy());
			return temp;
		}
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
	 * Gets the num live ships.
	 *
	 * @return the num live ships
	 */
	public int getNumLiveShips() {
		int count = 0;
		for (Ship s : ships)
			if (!s.isSunken())
				count++;
		return count;
	}
	
	/**
	 * Gets the ship.
	 *
	 * @param si the si
	 * @return the ship
	 */
	public Ship getShip(ShipIdentifier si) {
		return shipMap.get(si.toString());
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
		this.name = conn.setCredentials(id, boardWidth, boardHeight);
		return this.name != null;
	}
	
	/**
	 * Move ships.
	 *
	 * @param shipAction the ship action
	 * @param boardWidth the board width
	 * @param boardHeight the board height
	 * @return the list
	 */
	private void moveShips(List<ShipAction> shipAction, int boardWidth, int boardHeight) {
		for (ShipAction shipAct: shipAction) {
			if (this.id != shipAct.getShipIdentifier().playerId) { // playerId does not match shipId
				continue;
			}
			Ship s = shipMap.get(shipAct.getShipIdentifier().toString());
			if (s != null && shipAct.getMoveDir() != null && !s.isSunken()) {
				lastShipPositions.put(s.getIdentifier().toString(), s.getLocation());
				Coordinate newCoord = move(shipAct.getMoveDir(), s.getLocation());
				s.setLocation(newCoord);
				if (!s.inBoundsInclusive(0, boardHeight-1, 0, boardWidth-1)) { // Check if ship remains on map
					s.setLocation(lastShipPositions.get(s.getIdentifier().toString()));
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
		Ship s = shipMap.get(shipId.toString());
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
	public boolean requestTurn(Map<Integer, List<Ship>> allPlayersShips, Map<Integer, List<ActionResult>> actionResults, List<City> structures) {
		setLastActionResults(actionResults.get(id));
		this.actionLog.addAll(actionResults.get(this.id));
		return conn.requestTurn(allPlayersShips, actionResults, structures);
	}

	/**
	 * Reads the socket.
	 *
	 * @param boardWidth the game board width
	 * @param boardHeight the game board height
	 * @return a list of ship actions from the ClientPlayer
	 */
	public List<ShipAction> getTurn(int boardWidth, int boardHeight) {
		List<ShipAction> shipActions = new LinkedList<ShipAction>();
		try {
			shipActions = conn.getTurn(); // get the ship action list from the client player
			moveShips(shipActions, boardWidth, boardHeight);
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
		return score;
	}

	/**
	 * Win.
	 */
	public void win() {
		// TODO: decide how to score a win
		score++;
	}
	
	/**
	 * Increments the players score for sinking an enemy ship.
	 */
	public void incrementScore() {
		score++;
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
			for (Ship s : ships) {
				if (!s.isSunken()) {
					return true;
				}
			}
			hasShips = false;
		}
		return hasShips;
	}
	
	/**
	 * Lose.
	 */
	public void lose() {
		// TODO: decide how to score a loss
		score--;
	}
	
	
	/**
	 * Checks if the ship is hit as a result of an action.
	 *
	 * @param c the coordinate affected by the action
	 * @param damage the damage incurred as a result of the action
	 * @return the action result
	 */
	public ActionResult isHit(Coordinate c, int damage) {
		if (c == null)
			return new ActionResult(c, ShotResult.MISS, -1, id);
		for (Ship s : ships) {
			if (s.isHit(c, damage)) {
				return new ActionResult(c, s.isSunken() ? ShotResult.SUNK : ShotResult.HIT, s.getHealth(), id);
			}
		}
		return new ActionResult(c, ShotResult.MISS, -1, id);
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
	public List<Ship> getShipsOpponnent(ServerPlayer opp) {
		// TODO: return not all ships
		// currently returns all unsunken ships
		List<Ship> unsunkShips = new LinkedList();
		for (Ship ship: ships)
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
	}
	
}
