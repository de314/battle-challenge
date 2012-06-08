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
import battlechallenge.Ship;
import battlechallenge.Ship.Direction;
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
	 * Instantiates a new game.
	 *  
	 * @param players2 the player
	 */
	public Game(List<ServerPlayer> players, GameManager manager) {
		this(players, DEFAULT_WIDTH, DEFAULT_HEIGHT, manager);
	}
	
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
		int livePlayers = players.size();
		while (livePlayers > 1) {
			doTurn(actionResults);
			livePlayers = 0;
			for (ServerPlayer player: players) {
				if (player.hasShipsLeft()) {
					livePlayers++;
				}
			}
			BoardExporter.exportBoards(players.get(0), players.get(1), boardWidth, boardHeight);
		}
		
		
		/*
		 * 			[[ END GAME ]]
		 */
		ServerPlayer winner = getWinner();
		System.out.println("Winner is " + winner.getName());
		for(ServerPlayer p : players) {
			if (winner == p)
				p.endGame(CommunicationConstants.RESULT_WIN);
			else
				p.endGame(CommunicationConstants.RESULT_LOSE);
		}
		manager.removeGame(this);
	}
	
	private void setPlayerCredentials() {
		for(ServerPlayer p : players) {
			p.setCredentials(boardWidth, boardHeight);
			System.out.println("Set player credentials: " + p.toString());
		}
	}
	
	private void placeShips() {
		for(ServerPlayer p : players) {
			p.requestPlaceShips(Game.getShips());
		}
		try {
			// FIXME: remove magic number
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// FIXME: handle thread failure
		}
		for(ServerPlayer p : players) {
			List<Ship> ships = p.getPlaceShips();
			// TODO: verify valid location
			Set<String> coords = new HashSet<String>();
			for(Ship s : p.getShips()) {
				if (!s.inBoundsInclusive(0, this.boardHeight-1, 0, boardWidth-1)) {
					// TODO: handle invalid ship placement (out of bounds)
				}
				Set<String> temp = s.getCoordinateStrings();
				for(String c : temp) {
					if (coords.contains(c)) {
						// TODO: handle invalid ship placement (overlap)
					} else {
						coords.add(c);
					}
				}
			}
		}
		System.out.println("All player ships placed");
	}
	
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
			if (!p.hasShipsLeft()) // Player is dead, don't request their turn
				continue;
			if (!p.requestTurn(actionResults)) {
				// TODO: handle lost socket connection
			}
		}
		try {
			// FIXME: remove magic number
			Thread.sleep(DEFAULT_SPEED);
		} catch (InterruptedException e) {
			// TODO: handle thread failure
		}
		
		for(int j=0;j<players.size();j++) {
			ServerPlayer p = players.get(j);
			if (!p.hasShipsLeft()) // Player is dead no need to get actions when they cannot act
				continue;
			// shot coordinates
			List<Coordinate> coords = p.getTurn();
			// reset action results for current user
			actionResults.get(p.getId()).clear();
			// FIXME: remove magic number for allowed shots: "1"
			for(int i=0;i<coords.size() && i < 1;i++) {
				// check if shot is within game boundries
				if (!coords.get(i).inBoundsInclusive(0, boardHeight-1, 0, boardWidth-1)) { 
					// TODO: handle shot out of bounds. ignore?
				} else {
					// valid shot location received
					for(ServerPlayer opp : players) {
						if (opp != p) {
							// FIXME: get rid of magic number for damage
							ActionResult ar = opp.isHit(coords.get(i), 1);
							if (ar.getResult() == ShotResult.HIT) {
								actionResults.get(p.getId()).add(ar);
							}
						}
					}
					// If shot did not hit any enemies, then record a miss
					if (actionResults.get(p.getId()).isEmpty())
						actionResults.get(p.getId()).add(new ActionResult(coords.get(i), ShotResult.MISS, -1, j));
				}
			}
		}
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
		for (ServerPlayer player: players) {
			if (player.hasShipsLeft()) {
				return player;
			}
		}
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
