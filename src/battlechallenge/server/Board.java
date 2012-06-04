package battlechallenge.server;

import java.util.ArrayList;
import java.util.List;

import battlechallenge.Coordinate;
import battlechallenge.Ship;
import battlechallenge.Tile;

public class Board {

	private final int width;
	private final int height;
	
	private Tile[][] board;
	private List<Coordinate> hits;
	private List<Coordinate> misses;
	private List<Ship> ships;
	
	public Board(int width, int height, List<Ship> ships) {
		this.width = width;
		this.height = height;
		this.ships = ships;
		hits = new ArrayList<Coordinate>();
		misses = new ArrayList<Coordinate>();
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public List<Coordinate> getHits() {
		return hits;
	}
	
	public List<Coordinate> getMisses() {
		return misses;
	}
	
	public List<Ship> getShips() {
		return ships;
	}
}
