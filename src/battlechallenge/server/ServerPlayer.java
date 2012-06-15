package battlechallenge.server;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import battlechallenge.ActionResult;
import battlechallenge.ActionResult.ShotResult;
import battlechallenge.network.ConnectionLostException;
import battlechallenge.Coordinate;
import battlechallenge.Ship;

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

	/** The action log. */
	private List<ActionResult> actionLog;

	/** The hit log. */
	private Set<String> hitLog;
	
	/** The id. */
	private final int id;

	/** The has ships. */
	private boolean hasShips = true;
	
	public int getId() {
		return id;
	}
	
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
	 * 
	 * @return a list of action results returned over the
	 * period of a game
	 */
	public List<ActionResult> getActionLog() {
		return actionLog;
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
	}
	
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
	 */
	public boolean setCredentials(int boardWidth, int boardHeight) {
		this.name = conn.setCredentials(id, boardWidth, boardHeight);
		return this.name != null;
	}

	/**
	 * Requests ships from the ClientPlayer through the ClientConnection
	 *
	 * @param ships the original ships to be updated by the ClientPlayer's
	 * @return true, if successful
	 */
	public boolean requestPlaceShips(List<Ship> ships) {
		try {
			this.ships = ships;
			return conn.requestPlaceShips(ships);
		} catch (ConnectionLostException e) {
			// TODO: handle lost connection
		}
		return false;
	}

	/**
	 * Will request the ships updated by the ClientPlayers placeShips 
	 * method and update the default list of ships with the updated
	 * player ships
	 * 
	 * @return the list of ships with updated starting coordinates
	 */
	public List<Ship> getPlaceShips(List<Ship> ships) {
		List<Ship> temp = conn.getPlaceShips();
		for(Ship s : temp) {
			//ships.get(counter).setStartPosition(s.getStartPosition());
			//ships.get(counter).setDirection(s.getDirection());
			// FIXME: save new ship info into instance variables
			// DO NOT TRUST THE USER
		}
		this.ships = temp;
		return ships; // return instance ships for placement verification by game
	}

	/**
	 * Request turn.
	 * 
	 * @return true, if successful
	 */
	public boolean requestTurn(Map<Integer, List<ActionResult>> actionResults) {
		this.actionLog.addAll(actionResults.get(this.id));
		return conn.requestTurn(this.ships, actionResults);
	}

	/**
	 * Requests the result
	 * 
	 * @return the list of coordinates returned by the ClientPlayer
	 */
	public List<Coordinate> getTurn() {
		try {
			return conn.getTurn();
		} catch (ConnectionLostException e) {
			// TODO: handle lost connection
		}
		return null;
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
	 * Checks to see if the player has any ships not sunk
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
	 * Checks if the ship is hit as a result of an action
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.id+"");
		sb.append(":").append(this.name).append("(");
		return sb.append(this.score+"").append(")").toString();
	}
}
