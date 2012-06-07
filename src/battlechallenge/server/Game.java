package battlechallenge.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import battlechallenge.Coordinate;
import battlechallenge.Ship;
import battlechallenge.Ship.Direction;
import battlechallenge.bot.ClientPlayer;

// TODO: Auto-generated Javadoc
/**
 * The Class Game.
 */
public class Game extends Thread {
	
	/** The board width. */
	private int boardWidth;
	
	/** The board height. */
	private int boardHeight;
	
	/** The players. */
	private List<ServerPlayer> players;

	/**
	 * Instantiates a new game.
	 */
	public Game() {
		this(null);
	}
	
	/**
	 * Instantiates a new game.
	 *
	 * @param player the player
	 */
	public Game(ServerPlayer player) {
		this.players = new LinkedList<ServerPlayer>();
		addPlayer(player);
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		// TODO: Start playing game
		// Assume all players have been added. Do not allow other players to be added.
	}
	
	/**
	 * Adds the player.
	 *
	 * @param player the player
	 * @return true, if successful
	 */
	public boolean addPlayer(ServerPlayer player) {
		// FIXME: handle null players
		// TODO: how to add players
		return false;
	}
	
	/**
	 * Gets the winner.
	 *
	 * @return the winner
	 */
	public ServerPlayer getWinner() {
		// TODO: get player with max score
		return null;
	}
	
	/**
	 * Gets the default ships for this game.
	 *
	 * @return the ships
	 */
	public static List<Ship> getShips() {
		List<Ship> ships = new ArrayList<Ship>();
		ships.add(new Ship(2,new Coordinate(-1, -1), Direction.NORTH));
		ships.add(new Ship(3,new Coordinate(-1, -1), Direction.NORTH));
		ships.add(new Ship(3,new Coordinate(-1, -1), Direction.NORTH));
		ships.add(new Ship(4,new Coordinate(-1, -1), Direction.NORTH));
		ships.add(new Ship(5,new Coordinate(-1, -1), Direction.NORTH));
		return ships;
	}
}
