package battlechallenge.server;

import java.util.List;

import battlechallenge.Ship;

public class Board {

	private final int width;
	private final int height;
	
	private Tile[][] board;
	private List<Coordinate> hits;
	private List<Coordinate> misses;
	private List<Ship> ships;
	
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		// TODO
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
}
