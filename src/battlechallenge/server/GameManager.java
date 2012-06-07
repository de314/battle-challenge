package battlechallenge.server;

import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
	
	private int playerCount;
	
	/**
	 * Instantiates a new game manager.
	 */
	public GameManager() {
		games = new HashSet<Game>();
		waitingPlayers = new LinkedList<ServerPlayer>();
		playerCount = 0;
	}
	
	/**
	 * Adds the player.
	 *
	 * @param conn the conn
	 */
	public void addPlayer(Socket conn) {
		System.out.println("Adding Player");
		// TODO: more robust socket validation/
		if (conn != null) {
			waitingPlayers.add(new ServerPlayer(conn, playerCount++));
			System.out.println("Added Player: " + playerCount);
			// FIXME: remove magic number "2"
			while (waitingPlayers.size() >= 2) {
				List<ServerPlayer> players = new LinkedList<ServerPlayer>();
				// FIXME: allow for more players later
				players.add(waitingPlayers.poll());
				players.add(waitingPlayers.poll());
				// Game constructor start its own thread
				System.out.println("Starting new Game");
				games.add(new Game(players));
			}
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
