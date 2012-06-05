package battlechallenge.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import battlechallenge.Coordinate;
import battlechallenge.Ship;
import battlechallenge.Tile;

/**
 * The Class Board.
 */
public class Board {

	/** The width. Number of columns in the board. */
	private final int width;
	
	/** The height. Number of rows in the board. */
	private final int height;
	
	/** The board. Place holder for each  */
	private Map<String, Tile> board;
	
	/** The hits. */
	private List<Coordinate> hits;
	
	/** The misses. */
	private List<Coordinate> misses;
	
	/** The ships. */
	private List<Ship> ships;
	
	/**
	 * Instantiates a new board.
	 *
	 * @param width the width
	 * @param height the height
	 * @param ships the ships
	 */
	public Board(int width, int height, List<Ship> ships) {
		this.width = width;
		this.height = height;
		this.ships = ships;
		hits = new ArrayList<Coordinate>();
		misses = new ArrayList<Coordinate>();
		this.board = new HashMap<String, Tile>();
	}
	
	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the hits.
	 *
	 * @return the hits
	 */
	public List<Coordinate> getHits() {
		return hits;
	}
	
	/**
	 * Gets the misses.
	 *
	 * @return the misses
	 */
	public List<Coordinate> getMisses() {
		return misses;
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
	 * Gets the sunken ships.
	 *
	 * @return the sunken ships
	 */
	public List<Ship> getSunkenShips() {
		List<Ship> sunkenShips = new ArrayList<Ship>();
		for (Ship s : ships)
			if (s.isSunken())
				sunkenShips.add(s);
		return sunkenShips;
	}
}
