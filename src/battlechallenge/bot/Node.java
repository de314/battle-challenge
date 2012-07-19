package battlechallenge.bot;

import battlechallenge.Coordinate;
import battlechallenge.ship.Ship.Direction;

public class Node implements Comparable<Node> {
	private Coordinate c1;
	private Direction dir;
	private Node prev;
	private Double distance;
	
	public Node(Coordinate c1, Node prev, Direction dir, Double distance) {
		this.c1 = c1;
		this.dir = dir;
		this.prev = prev;
		this.distance = distance;
	}
	@Override
	public int compareTo(Node other) {
		return this.distance < other.distance ? -1 : this.distance == other.distance ? 0 : 1; // fix
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
