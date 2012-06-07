package battlechallenge.server;

import java.net.Socket;
import java.util.List;
import java.util.Set;

import battlechallenge.ActionResult;
import battlechallenge.ConnectionLostException;
import battlechallenge.Coordinate;
import battlechallenge.Ship;

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
	
	/** The action log. */
	private List<ActionResult> actionLog;
	
	/** The hit log. */
	private Set<String> hitLog;

	/**
	 * Instantiates a new server player.
	 *
	 * @param socket the socket
	 */
	public ServerPlayer(Socket socket) {
		this.conn = new ClientConnection(socket);
		// TODO: initialize player
	}

	/**
	 * Place ships.
	 *
	 * @param ships the ships
	 */
	public void placeShips(List<Ship> ships) {
		try {
			conn.placeShips(ships);
		} catch (ConnectionLostException e) {
			// TODO: handle lost connection
		}
	}

	/**
	 * Request turn.
	 *
	 * @return true, if successful
	 */
	public boolean requestTurn() {
		try {
			return conn.requestTurn();
		} catch (ConnectionLostException e) {
			// TODO: handle lost connection
		}
		return false;
	}

	/**
	 * Gets the turn.
	 *
	 * @return the turn
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
	 * Lose.
	 */
	public void lose() {
		// TODO: decide how to score a loss
		score--;
	}
}
