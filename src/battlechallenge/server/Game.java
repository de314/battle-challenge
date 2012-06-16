package battlechallenge.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import battlechallenge.ActionResult;
import battlechallenge.ActionResult.ShotResult;
import battlechallenge.CommunicationConstants;
import battlechallenge.Coordinate;
import battlechallenge.ShipAction;
import battlechallenge.ship.Ship;
import battlechallenge.ship.Ship.Direction;
import battlechallenge.visual.BoardExporter;

/**
 * The Class Game.
 */
public class Game extends Thread {
	
	public static final int DEFAULT_WIDTH;
	public static final int DEFAULT_HEIGHT;
	public static final int DEFAULT_SPEED;
	
	static {
		DEFAULT_WIDTH = 10;
		DEFAULT_HEIGHT = 10;
		DEFAULT_SPEED = 400;
	}
	
	/** The board width. */
	private int boardWidth;
	
	/** The board height. */
	private int boardHeight;
	
	/** The players. */
	private List<ServerPlayer> players;
	
	private GameManager manager;
	
	/**
	 * Used to check how many players are in the game
	 * @return The number of players in the game
	 */
	public int numberOfPlayers() {
		return players.size();
	}
	
	/**
	 * Instantiates a new game.
	 *  
	 * @param players the player
	 */
	public Game(List<ServerPlayer> players, GameManager manager) {
		this(players, DEFAULT_WIDTH, DEFAULT_HEIGHT, manager);
	}
	
	/**
	 * Constructor for a game that requires at least two players
	 * @param players The players that make up the game
	 * @param height The height of the game board
	 * @param width  The width of the game board
	 * @param manager The GameManager that creates the game
	 */
	public Game(List<ServerPlayer> players, int height, int width, GameManager manager) {
		if (players == null || players.size() < 2)
			throw new IllegalArgumentException();
		// TODO: ensure no null entries in list
		this.players = players;
		this.boardHeight = height;
		this.boardWidth = width;
		this.manager = manager;
		
		// DEBUG INFO
		StringBuilder sb = new StringBuilder("Starting new Game with players { ");
		for(ServerPlayer p : players)
			sb.append(p.getId()).append(", ");
		sb.delete(sb.length()-2, sb.length()).append(" }");
		System.out.println(sb.toString());
		
		this.start();
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		// TODO: Start playing game
		// FIXME: Do not allow other players to be added.
		/*
		 * 			[[ INITIALIZE GAME ]]
		 */
		setPlayerCredentials();
		placeShips();
		
		/*
		 * 			[[ PLAY GAME ]]
		 */
		Map<Integer, List<ActionResult>> actionResults = new HashMap<Integer, List<ActionResult>>();
		for(ServerPlayer p : players)
			actionResults.put(p.getId(), new LinkedList<ActionResult>());
		int livePlayers = 0;
		for (ServerPlayer player: players) {
			if (player.isAlive()) {
				livePlayers++;
			}
		}
		while (livePlayers > 1) {
			doTurn(actionResults);
			livePlayers = 0;
			for (ServerPlayer player: players) {
				if (player.isAlive()) {
					livePlayers++;
				}
			}
			BoardExporter.exportBoards(players.get(0), players.get(1), boardWidth, boardHeight);
		}
		
		
		/*
		 * 			[[ END GAME ]]
		 */
		ServerPlayer winner = getWinner();
		if (winner != null)
			System.out.println("Winner is " + winner.getName());
		else
			System.out.println("Error: Winner not found.");
		
		for(ServerPlayer p : players) {
			if (winner == p)
				p.endGame(CommunicationConstants.RESULT_WIN);
			else {
				p.endGame(CommunicationConstants.RESULT_LOSE);
			}
		}
		manager.removeGame(this);
	}
	
	private void setPlayerCredentials() {
		for(ServerPlayer p : players) {
			if (p.setCredentials(boardWidth, boardHeight))
				System.out.println("Set player credentials: " + p.toString());
			else {
				// TODO: remove player and pause game if necessary
			}
		}
	}
	
	/**
	 * The method will pass the initial ships to be updated by each player's
	 * placeShip method. The method then checks for invalid placements of 
	 * ships for each player
	 */
	private void placeShips() {
		for(ServerPlayer p : players) {
			p.requestPlaceShips(Game.getShips());
		}
		try {
			// FIXME: remove magic number
			Thread.sleep(CommunicationConstants.SOCKET_WAIT_TIME);
		} catch (InterruptedException e) {
			// FIXME: handle thread failure
		}
		for(ServerPlayer p : players) {
			// FIXME: take away the magic number
			p.getPlaceShips(boardWidth * 2, boardHeight);
		}
		System.out.println("All player ships placed");
	}
	
	/**
	 * 
	 * @param actionResults The results of last turns actions
	 */
	private void doTurn(Map<Integer, List<ActionResult>> actionResults) {
		// DEGBUG INFO: print each users guess
		StringBuilder sb = new StringBuilder();
		for (Entry<Integer, List<ActionResult>> e : actionResults.entrySet()) {
			sb.append("\t{ ").append(e.getKey()).append(" size:").append(e.getValue().size());
			for (ActionResult a : e.getValue())
				sb.append(a).append(" ");
			sb.append(" }\n");
		}
		System.out.println(sb.toString());
				
		for(ServerPlayer p : players) {
			if (!p.isAlive()) // Player is dead, don't request their turn
				continue;
			if (!p.requestTurn(actionResults)) {
				// TODO: handle lost socket connection
			}
		}
		try {
			// FIXME: Change speed depending on how fast players return results from calls
			// to their doTurn method.
			Thread.sleep(DEFAULT_SPEED);
		} catch (InterruptedException e) {
			// TODO: handle thread failure
		}
		
		for(int j=0;j<players.size();j++) {
			ServerPlayer p = players.get(j);
			if (!p.isAlive()) // Player is dead no need to get actions when they cannot act
				continue;
			// shot coordinates
			List<ShipAction> shipActions = p.getTurn(boardWidth, boardHeight);
			// reset action results for current user
			actionResults.get(p.getId()).clear();
			// FIXME: remove magic number for allowed shots: "1"
			for(int i=0;i<shipActions.size() && i < 1;i++) {
				// check if shot is within game boundries
				if (!shipActions.get(i).getShotCoord().inBoundsInclusive(0, boardHeight-1, 0, boardWidth-1)) { 
					// TODO: handle shot out of bounds. ignore?
				} else {
					// valid shot location received
					for(ServerPlayer opp : players) {
						if (opp != p) {
							// FIXME: get rid of magic number for damage
							// This is done for multiple opponents, recording multiple hits
							// on multiple opponents if necessary, otherwise recording
							// a single miss
							ActionResult ar = opp.isHit(shipActions.get(i).getShotCoord(), 1);
							if (ar.getResult() == ShotResult.HIT) {
								actionResults.get(p.getId()).add(ar);
							}
						}
					}
					// If shot did not hit any enemies, then record a miss
					if (actionResults.get(p.getId()).isEmpty())
						actionResults.get(p.getId()).add(new ActionResult(shipActions.get(i).getShotCoord(), ShotResult.MISS, -1, j));
				}
			}
		}
	}
	
	/**
	 * Adds the player.
	 *
	 * @param player the player to add to the game
	 * @return true, if player is successfully added to the game
	 */
	public boolean addPlayer(ServerPlayer player) {
		// FIXME: handle null players
		// TODO: how to add players
		return false;
	}
	
	/**
	 * Gets the winner by checking each player to see if they
	 * have any ships left
	 *
	 * @return the winner who is the only player with at least one ship left unsunk.
	 */
	public ServerPlayer getWinner() {
		// TODO: get player with max score
		for (ServerPlayer player: players) {
			if (player.isAlive()) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Creates the default ships for this game.
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
		// Setting the original ship Ids
		for (int i = 0; i < ships.size(); i++) {
			ships.get(0).setShipId(i);
		}
		return ships;
	}
}
