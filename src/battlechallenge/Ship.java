package battlechallenge;

import java.io.Serializable;

/**
 * The Class Ship. A data holder representing ships from the original game battleship.
 */
public class Ship implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 0L;

	/**
	 * The Enum Direction. Which direction the ship will be extended.
	 */
	public static enum Direction {
		/** The NORTH. */
		NORTH, 
		/** The EAST. */
		EAST, 
		/** The SOUTH. */
		SOUTH, 
		/** The WEST. */
		WEST
	}
	
	/** The length. How many spaces the ship extends from the start position in the specified direction. */
	private int length;
	
	/** The health. Number of hits left before the ship is sunk.  */
	private int health;
	
	/** The start position. */
	private Coordinate startPosition;
	
	/** The direction. Which direction from the starting position the ship will be extended. */
	private Direction direction;
	
	/**
	 * Instantiates a new ship.
	 *
	 * @param length of the ship
	 * @param startPosition the start position
	 * @param direction the direction
	 */
	public Ship(int length, Coordinate startPosition, Direction direction) {
		this.length = length;
		this.health = length;
		this.startPosition = startPosition;
		this.direction = direction;
	}
	
	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Sets the length.
	 *
	 * @param length the new length
	 */
	public void setLength(int length) {
		this.length = length;
	}
	
	/**
	 * Gets the starting coordinate of the ship
	 *
	 * @return the start position
	 */
	public Coordinate getStartPosition() {
		return startPosition;
	}
	
	/**
	 * Sets the starting coordinate of the ship
	 *
	 * @param startPosition the new start position
	 */
	public void setStartPosition(Coordinate startPosition) {
		this.startPosition = startPosition;
	}
	
	/**
	 * Gets the direction in which the ship extends outward
	 *
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * Sets the direction in which the ship extends outward
	 *
	 * @param direction the new direction
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	/**
	 * Gets the end position based on the direction
	 * the ship extends outward from its starting position
	 *
	 * @return the end position
	 */
	public Coordinate getEndPosition() {
		switch(direction) {
		case NORTH:
			return new Coordinate(startPosition.getRow(), startPosition.getCol() + length);
		case SOUTH:
			return new Coordinate(startPosition.getRow(), startPosition.getCol() - length);
		case EAST:
			return new Coordinate(startPosition.getRow() + length, startPosition.getCol());
		case WEST:
			return new Coordinate(startPosition.getRow() - length, startPosition.getCol());
		}
		return null; // Should not reach here, will only be true if the ship direction is invalid
	}
	
	/**
	 * Gets the health. The number of hits left before sinking.
	 *
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}
	
	
	/**
	 * Checks if this ship is sunken.
	 *
	 * @return true, if is sunken
	 */
	public boolean isSunken() {
		return health <= 0;
	}
	
	/**
	 * Checks if the supplied coordinate is a hit. Health is automatically
	 * updated.
	 *
	 * @param c the coordinate
	 * @return true, if is hit
	 */
	public boolean isHit(Coordinate c, int damage) {
		// TODO: handle case where player hits same spot twice (force unique shot locations)
		if (c.isBetween(startPosition, getEndPosition())) {
			health -= damage; // reduce health on ship by damage
			return true; // Is a hit
		}
		return false;
	}
	
	
	/**
	 * Deep copy of the shipObject so that the player
	 * can use the ship object as necessary while the 
	 * server has keeps its own copy.
	 *
	 * @return the ship
	 */
	public Ship deepCopy() {
		return new Ship(length, startPosition, direction);
	}
}
