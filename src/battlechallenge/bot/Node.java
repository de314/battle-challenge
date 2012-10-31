package battlechallenge.bot;

import java.util.Comparator;

import battlechallenge.Coordinate;
import battlechallenge.ship.Ship.Direction;

public class Node implements Comparable<Node>, Comparator<Node> {
	private Coordinate c1;
	private Direction dir;
	private Node prev;
	private Double distance;
	private int gScore;
	
	public Integer getgScore() {
		return gScore;
	}
	public void setgScore(int gScore) {
		this.gScore = gScore;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public Node(Coordinate c1, Node prev, Direction dir, Double distance, int gScore) {
		this.c1 = c1;
		this.dir = dir;
		this.prev = prev;
		this.distance = distance;
		this.gScore = gScore;
	}
	
	public Node(Coordinate c1, Node prev, Direction dir) {
		this.c1 = c1;
		this.dir = dir;
		this.prev = prev;
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
	@Override
	public String toString() {
		return "Node [c1=" + c1 + ", distance=" + distance + "]";
	}
	@Override
	public int compare(Node o1, Node o2) {
		return o1.distance < o2.distance ? -1 : o1.distance == o2.distance ? 0 : 1;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((c1 == null) ? 0 : c1.hashCode());
		result = prime * result + ((dir == null) ? 0 : dir.hashCode());
		result = prime * result
				+ ((distance == null) ? 0 : distance.hashCode());
		result = prime * result + ((prev == null) ? 0 : prev.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		Node other = (Node) obj;
		if (this.getLocation() == other.getLocation()) {
			return true;
		}

		return false;
	}
	

}
