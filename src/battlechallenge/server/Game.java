package battlechallenge.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import battlechallenge.ActionResult;
import battlechallenge.Coordinate;
import battlechallenge.Ship;
import battlechallenge.ActionResult.ShotResult;
import battlechallenge.Ship.Direction;

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
	 *  
	 * @param players2 the player
	 */
	public Game(List<ServerPlayer> players) {
		if (players == null || players.size() < 2)
			throw new IllegalArgumentException();
		// TODO: ensure no null entries in list
		this.players = players;
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
		for(ServerPlayer p : players) {
			p.requestPlaceShips(this.getShips());
		}
		try {
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
		
		/*
		 * 			[[ PLAY GAME ]]
		 */
		int livePlayers = players.size();
		while (livePlayers > 1) {
			for(ServerPlayer p : players) {
				if (!p.hasShipsLeft()) { // Player is dead, don't request their turn
					continue;
				}
				if (!p.requestTurn()) {
					// TODO: handle lost socket connection
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO: handle thread failure
			}
			
			// TODO: figure out what results you are sending to each client and how to store them
			// should all players get access to the health? Coordinate?
			
			for(int j=0;j<players.size();j++) {
				ServerPlayer p = players.get(j);
				if (!p.hasShipsLeft()) { // Player is dead no need to get actions when they cannot act
					continue;
				}
				List<Coordinate> coords = p.getTurn(); // these are shot coordinates...
				// FIXME: remove magic number: "1"
				List<ActionResult> results = new LinkedList<ActionResult>();
				for(int i=0;i<coords.size() && i < 1;i++) {
					if (!coords.get(i).inBoundsInclusive(0, boardHeight-1, 0, boardWidth-1)) { // check if shot is within game boundries
						// TODO: handle shot out of bounds. ignore?
					} else {
						// valid shot location received
						// TODO: record and return results
						int resultsSize = coords.size(); // saving so I know if any hits are added to results
						for(ServerPlayer opp : players) {
							if (opp != p) {
								// FIXME: get rid of magic number for damage
								ActionResult ar = opp.isHit(coords.get(i), 1);
								if (ar.getResult() == ShotResult.HIT) {
									results.add(ar);
								}
							}
						}
						if (results.size() == resultsSize)
							results.add(new ActionResult(coords.get(i), ShotResult.MISS, -1, j));
					}
					// TODO: respond with action results
				}
			}
			livePlayers = 0;
			for (ServerPlayer player: players) {
				if (player.hasShipsLeft()) {
					livePlayers++;
				}
			}
		}
		getWinner();
		
		
		/*
		 * 			[[ END GAME ]]
		 */
		
		
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
