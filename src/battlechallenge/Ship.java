package battlechallenge;

public class Ship {

	public static enum Direction {
		NORTH, EAST, SOUTH, WEST
	}
	
	private int length;
	private Coordinate startPosition;
	private Direction direction;
	
	public Ship(int length, Coordinate startPosition, Direction direction) {
		this.length = length;
		this.startPosition = startPosition;
		this.direction = direction;
	}
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public Coordinate getStartPosition() {
		return startPosition;
	}
	public void setStartPosition(Coordinate startPosition) {
		this.startPosition = startPosition;
	}
	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public Coordinate getEndPosition() {
		switch(direction) {
		case NORTH:
			return new Coordinate(startPosition.getX(), startPosition.getY() + length);
		case SOUTH:
			return new Coordinate(startPosition.getX(), startPosition.getY() - length);
		case EAST:
			return new Coordinate(startPosition.getX() + length, startPosition.getY());
		case WEST:
			return new Coordinate(startPosition.getX() - length, startPosition.getY());
		}
		return null; // Should not reach here, will only be true if the ship direction is invalid
	}
	
	public Ship deepCopy() {
		return new Ship(length, startPosition, direction);
	}
		
}
