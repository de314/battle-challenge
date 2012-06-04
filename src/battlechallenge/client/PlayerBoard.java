package battlechallenge.client;
import java.util.*;

import battlechallenge.Coordinate;
import battlechallenge.Tile;

public class PlayerBoard {
	
	private final int height;
	private final int width;
	private Tile[][] board;
	private List<Coordinate> hits;
	private List<Coordinate> misses;
	
	public PlayerBoard(int height, int width, Tile[][] board,
			List<Coordinate> hits, List<Coordinate> misses) {
		this.height = height;
		this.width = width;
		this.board = board;
		this.hits = hits;
		this.misses = misses;
	}
	
	public Tile[][] getBoard() {
		return board;
	}
	public void setBoard(Tile[][] board) {
		this.board = board;
	}
	public List<Coordinate> getHits() {
		return hits;
	}
	public void setHits(List<Coordinate> hits) {
		this.hits = hits;
	}
	public List<Coordinate> getMisses() {
		return misses;
	}
	public void setMisses(List<Coordinate> misses) {
		this.misses = misses;
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
}

