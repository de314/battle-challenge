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

	public static final int DEFAULT_NUM_PLAYERS;
	
	static {
		DEFAULT_NUM_PLAYERS = 4;
	}
	
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
		// TODO: more robust socket validation/
		if (conn != null) {
			waitingPlayers.add(new ServerPlayer(conn, playerCount++));
			// FIXME: remove magic number "2"
			while (waitingPlayers.size() >= DEFAULT_NUM_PLAYERS) {
				List<ServerPlayer> players = new LinkedList<ServerPlayer>();
				// FIXME: allow for more players later
				for(int i=0;i<DEFAULT_NUM_PLAYERS;i++)
					players.add(waitingPlayers.poll());
				// Game constructor start its own thread
				games.add(new Game(players, this));
			}
		}
	}
	
	/**
	 * Removes the game from the GameManager
	 *
	 * @param g the game
	 */
	public void removeGame(Game g) {
		if (games.contains(g)) {
			games.remove(g);
		}
	}
}
