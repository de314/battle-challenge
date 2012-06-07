package battlechallenge.server;

import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * The Class GameManager.
 */
public class GameManager {

	/** The games. */
	private Set<Game> games;
	
	/** The waiting players. */
	private Queue<ServerPlayer> waitingPlayers;
	
	/**
	 * Instantiates a new game manager.
	 */
	public GameManager() {
		games = new HashSet<Game>();
		waitingPlayers = new LinkedList<ServerPlayer>();
	}
	
	/**
	 * Adds the player.
	 *
	 * @param conn the conn
	 */
	public void addPlayer(Socket conn) {
		if (conn != null) {
			// TODO: methodology for adding players and and handling games
		}
	}
	
	/**
	 * Removes the game.
	 *
	 * @param g the g
	 */
	public void removeGame(Game g) {
		if (games.contains(g)) {
			games.remove(g);
			
		}
	}
}
