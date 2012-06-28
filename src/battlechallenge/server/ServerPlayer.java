package battlechallenge.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
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
	
	private int hits;
	
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
	
	private int rowOffset = 0;
	private int colOffset = 0;
	
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
	
	public List<ActionResult> getLastActionResults() {
		return lastActionResults;
	}
	
	public List<ActionResult> getLastActionResultsCopy() {
		List<ActionResult> temp = new LinkedList<ActionResult>();
		temp.addAll(lastActionResults);
		return temp;
	}
	
	public void setLastActionResults(List<ActionResult> actionResults) {
		// FIXME: this might need to be fixed with more than two players
		for (ActionResult result : actionResults) {
			if (result.getResult() != ShotResult.MISS) {
				this.hits++;
				if (result.getResult() == ShotResult.SUNK)
					this.score++;
			}
			this.totalShots++;
		}
		lastActionResults = actionResults;
	}
	
	public double getShotAccuracy() {
		return (double)hits/(double)totalShots;
	}
	
	public int getNumLiveShips() {
		int count = 0;
		for (Ship s : ships)
			if (!s.isSunken())
				count++;
		return count;
	}
	
	public int getRowOffset() {
		return rowOffset;
	}

	public void setRowOffset(int rowOffset) {
		this.rowOffset = rowOffset;
	}

	public int getColOffset() {
		return colOffset;
	}

	public void setColOffset(int colOffset) {
		this.colOffset = colOffset;
	}
	
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
	 * Requests ships from the ClientPlayer through the ClientConnection.
	 *
	 * @param ships the original ships to be updated by the ClientPlayer's
	 * @return true, if successful
	 */
	public boolean requestPlaceShips(List<Ship> ships) {
		try {
			this.ships = ships;
			for (Ship ship: ships) {
				ship.setPlayerId(id);
			}
			return conn.requestPlaceShips(ships);
		} catch (ConnectionLostException e) {
			// TODO: handle lost connection
		}
		return false;
	}
	
	/**
	 * Will request the ships updated by the ClientPlayers placeShips
	 * method and update the default list of ships with the updated
	 * player ships.
	 *
	 * @param ships the ships
	 * @param boardWidth the board width
	 * @param boardHeight the board height
	 * @return the list of ships with updated starting coordinates
	 */
	public List<Ship> getPlaceShips(int boardWidth, int boardHeight) {
		Map<String, Ship> shipSet = new HashMap<String, Ship>();
		for (Ship s : ships)
			shipSet.put(s.getIdentifier().toString(), s);
		List<Ship> temp = conn.getPlaceShips(boardWidth, boardHeight);
		Map<String, Ship> coords = new HashMap<String, Ship>();
		for(Ship s : temp) {
			// check that client did not change ship attributes e.g. id, length, health...
			if (shipSet.get(s.getIdentifier().toString()) == null) {
				// client changed ship id or player id
				s.setHealth(-1);
				continue;
			} else {
				// id's are the same, now checking ship attributes
				Ship serverShip = shipSet.get(s.getIdentifier().toString());
				if (!(serverShip.getDamage() == s.getDamage() && serverShip.getHealth() == s.getHealth() && 
						serverShip.getLength() == s.getLength() && serverShip.getMovement() == s.getMovement() && 
						serverShip.getRange() == s.getRange())) {
					// client changed ship attributes
					s.setHealth(-1); // 
					continue;
				}
			}
			// check on board
			if (!s.inBoundsInclusive(0, boardHeight-1, 0, boardWidth-1)) {
				// sink invalid placed ship
				s.setHealth(-1);
				continue;
			}
			// check overlap
			Set<String> coordStrings = s.getCoordinateStrings();
			for(String c : coordStrings) {
				if (coords.get(c) != null) {
					// handle invalid ship placement (overlap)
					s.setHealth(-1);
					coords.get(c).setHealth(-1);
				}
				// add all coordinates to ensure all overlapped ships are sunk
				coords.put(c, s);
			}
		}
		this.ships = temp;
		for (Ship ship: ships) {
			// calculate new start position with offset
			Coordinate newStart = new Coordinate(ship.getStartPosition().getRow() + rowOffset, ship.getStartPosition().getCol() + colOffset);
			ship.setStartPosition(newStart);
			ship.getCoordinateStrings();
			shipMap.put(ship.getIdentifier().toString(), ship);
		}
		return ships; // return instance ships for placement verification by game
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
				lastShipPositions.put(s.getIdentifier().toString(), s.getStartPosition());
				Coordinate newCoord = move(shipAct.getMoveDir(), s.getStartPosition());
				s.setStartPosition(newCoord);
				if (!s.inBoundsInclusive(0, boardHeight-1, 0, boardWidth-1)) { // Check if ship remains on map
					s.setStartPosition(lastShipPositions.get(s.getIdentifier().toString()));
				}
				s.getCoordinateStrings();
			}
		}
	}
	
	/**
	 * Will revert the movement of a ship to where the ship was
	 * before it moved
	 * @param shipId The Id of the ship
	 */
	public void revertMovement(ShipIdentifier shipId) {
		Ship s = shipMap.get(shipId.toString());
		s.setStartPosition(lastShipPositions.get(s.getIdentifier().toString()));
		s.getCoordinateStrings();
	}
	
	/**
	 * Move.
	 *
	 * @param dir the dir
	 * @param coor the coor
	 * @return the coordinate
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
	 * @param actionResults the action results
	 * @return true, if successful
	 */
	public boolean requestTurn(Map<Integer, List<Ship>> allPlayersShips, Map<Integer, List<ActionResult>> actionResults) {
		setLastActionResults(actionResults.get(id));
		this.actionLog.addAll(actionResults.get(this.id));
		return conn.requestTurn(allPlayersShips, actionResults);
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
	
	public List<Ship> getShipsOpponnent(ServerPlayer opp) {
		// TODO: return not all ships
		// currently returns all unsunken ships
		List<Ship> unsunkShips = new LinkedList();
		for (Ship ship: ships)
			if (!ship.isSunken())
				unsunkShips.add(ship);
		return unsunkShips;
	}
}
