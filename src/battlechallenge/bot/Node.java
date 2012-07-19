package battlechallenge.bot;

import battlechallenge.Coordinate;
import battlechallenge.ship.Ship.Direction;

public class Node {
	private Coordinate c1;
	private Direction dir;
	private Node prev;
	
	public Node(Coordinate c1, Node prev, Direction dir) {
		this.c1 = c1;
		this.dir = dir;
		this.prev = prev;
	}

	public Node getPrev() {
		return prev;
	}

	public void setPrev(Node prev) {
		this.prev = prev;
	}

	public Coordinate getLocation() {
		return c1;
	}

	public void setC1(Coordinate c1) {
		this.c1 = c1;
	}

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public Node(Coordinate c1, Direction dir) {
		this.c1 = c1;
		this.dir = dir;
	}
}
