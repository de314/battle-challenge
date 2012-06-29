package battlechallenge.ship;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import battlechallenge.Coordinate;
import battlechallenge.ShipAction;
import battlechallenge.ShipIdentifier;


/**
 * The Class Ship. A data holder representing ships from the original game
 * battleship.
 */
public class Ship implements Serializable {

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

	/** The damage. */
	private int damage;

	/** The health. Number of hits left before the ship is sunk. */
	private int health;
	
	/**
	 * The length. How many spaces the ship extends from the start position in
	 * the specified direction.
	 */
	private int length;

	/** The start position. */
	private Coordinate startPosition;

	/** The range. */
	private int range;
	
	/** The movement. */
	private int movement;
	
	/** The player id. */
	private int playerId;

	/** The ship id. */
	private int shipId;
	
	/**
	 * The direction. Which direction from the starting position the ship will
	 * be extended.
	 */
	private Direction direction;
	
	/** The coordinates. */
	private Set<String> coords;
	
	/**
	 * Gets the damage.
	 *
	 * @return the damage
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Sets the damage.
	 *
	 * @param damage the new damage
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * Sets the health.
	 *
	 * @param health the new health
	 */
	public void setHealth(int health) {
		this.health = health;
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
	 * @param length
	 *            the new length
	 */
	public void setLength(int length) {
		this.length = length;
	}
	
	/**
	 * Gets the range.
	 *
	 * @return the range
	 */
	public int getRange() {
		return range;
	}

	/**
	 * Sets the range.
	 *
	 * @param range the new range
	 */
	public void setRange(int range) {
		this.range = range;
	}

	/**
	 * Gets the movement.
	 *
	 * @return the movement
	 */
	public int getMovement() {
		return movement;
	}

	/**
	 * Sets the movement.
	 *
	 * @param movement the new movement
	 */
	public void setMovement(int movement) {
		this.movement = movement;
	}

	/**
	 * Gets the player id.
	 *
	 * @return the player id
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * Sets the player id.
	 *
	 * @param playerId the new player id
	 */
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	/**
	 * Gets the ship id.
	 *
	 * @return the ship id
	 */
	public int getShipId() {
		return shipId;
	}

	/**
	 * Sets the ship id.
	 *
	 * @param shipId the new ship id
	 */
	public void setShipId(int shipId) {
		this.shipId = shipId;
	}
	
	public ShipIdentifier getIdentifier() {
		return new ShipIdentifier(shipId, playerId);
	}

	/**
	 * Instantiates a new ship.
	 *
	 * @param length the length
	 */
	public Ship(int length) {
		this(length, new Coordinate(-1, -1), Direction.NORTH);
	}
	
	/**
	 * Instantiates a new ship.
	 * 
	 * @param length
	 *            of the ship
	 * @param startPosition
	 *            the start position
	 * @param direction
	 *            the direction
	 */
	public Ship(int length, Coordinate startPosition, Direction direction) {
		this(1, length, length, 10, 1, startPosition, direction);
	}
	
	/**
	 * Instantiates a new ship.
	 *
	 * @param damage the damage
	 * @param health the health
	 * @param length the length
	 * @param range the range
	 * @param movement the movement
	 * @param startPosition the start position
	 * @param direction the direction
	 */
	public Ship(int damage, int health, int length, int range, int movement, Coordinate startPosition, Direction direction) {
		this.damage = damage;
		this.health = health;
		this.length = length;
		this.range = range;
		this.movement = movement;
		this.startPosition = startPosition;
		this.direction = direction;
		this.coords = getCoordinateStrings();
	}

	/**
	 * Gets the starting coordinate of the ship.
	 *
	 * @return the start position
	 */
	public Coordinate getStartPosition() {
		return startPosition;
	}

	/**
	 * Sets the starting coordinate of the ship.
	 *
	 * @param startPosition the new start position
	 */
	public void setStartPosition(Coordinate startPosition) {
		this.startPosition = startPosition;
	}

	/**
	 * Gets the direction in which the ship extends outward.
	 *
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Sets the direction in which the ship extends outward.
	 *
	 * @param direction the new direction
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * Gets the end position based on the direction the ship extends outward
	 * from its starting position.
	 *
	 * @return the end position
	 */
	public Coordinate getEndPosition() {
		switch (direction) {
		case NORTH:
			return new Coordinate(startPosition.getRow() - length,
					startPosition.getCol());
		case SOUTH:
			return new Coordinate(startPosition.getRow() + length,
					startPosition.getCol());
		case EAST:
			return new Coordinate(startPosition.getRow(),
					startPosition.getCol() + length);
		case WEST:
			return new Coordinate(startPosition.getRow(),
					startPosition.getCol() - length);
		}
		return null; // Should not reach here, will only be true if the ship
						// direction is invalid
	}

	/**
	 * Gets the coordinate strings.
	 *
	 * @return a Set of coordinates in String form of all
	 * coordinates that the ship is located on
	 */
	public Set<String> getCoordinateStrings() {
			coords = new HashSet<String>();
			for (int i = 0; i < length; i++) {
	
				switch (direction) {
				case NORTH: {
					coords.add((this.startPosition.getRow() - i) + ","
							+ this.startPosition.getCol());
				} break;
				case SOUTH: {
					coords.add((this.startPosition.getRow() + i) + ","
							+ this.startPosition.getCol());
				} break;
				case EAST: {
					coords.add(this.startPosition.getRow() + ","
							+ (this.startPosition.getCol() + i));
				} break;
				case WEST: {
					coords.add(this.startPosition.getRow() + ","
							+ (this.startPosition.getCol() - i));
				} break;
				}
			}
		return coords;
	}
	
	/**
	 * Gets a list of coordinates that comprises the ship 
	 *
	 * @return a Set of coordinates that comprises the ship
	 */
	public Set<Coordinate> getCoordinates() {
			
			HashSet<Coordinate> coordSet = new HashSet<Coordinate>();
			
			for (int i = 0; i < length; i++) {
	
				switch (direction) {
				case NORTH: {
					coordSet.add(new Coordinate(this.startPosition.getRow() - i,
							this.startPosition.getCol()));
				} break;
				case SOUTH: {
					coordSet.add(new Coordinate(this.startPosition.getRow() + i,
							this.startPosition.getCol()));
				} break;
				case EAST: {
					coordSet.add(new Coordinate(this.startPosition.getRow(),
							this.startPosition.getCol() + i));
				} break;
				case WEST: {
					coordSet.add(new Coordinate(this.startPosition.getRow(),
							this.startPosition.getCol() - i));
				} break;
				}
			}
		return coordSet;
	}
	
	/**
	 * Checks to see if any part of the ship is within the specified bounds.
	 *
	 * @param rowMin the row min
	 * @param rowMax the row max
	 * @param colMin the col min
	 * @param colMax the col max
	 * @return true, if successful
	 */
	public boolean inBoundsInclusive(int rowMin, int rowMax, int colMin, int colMax) {
		switch (direction) {
		case NORTH:
			return this.startPosition.getCol() >= colMin && this.startPosition.getCol() <= colMax && this.startPosition.getRow() <= rowMax && this.startPosition.getRow() >= (rowMin-length);
		case SOUTH:
			return this.startPosition.getCol() >= colMin && this.startPosition.getCol() <= colMax && this.startPosition.getRow() <= (rowMax+length) && this.startPosition.getRow() >= rowMin;
		case EAST:
			return this.startPosition.getCol() >= colMin && this.startPosition.getCol() <= (colMax-length) && this.startPosition.getRow() <= rowMax && this.startPosition.getRow() >= rowMin;
		case WEST:
			return this.startPosition.getCol() >= (colMin+length) && this.startPosition.getCol() <= colMax && this.startPosition.getRow() <= rowMax && this.startPosition.getRow() >= rowMin;
		}
		return false;
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
	 * @param damage the damage
	 * @return true, if is hit
	 */
	public boolean isHit(Coordinate c, int damage) {
		if (!isSunken() && coords.contains(c.toString())) {
			health -= damage;
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Ship: ");
		sb.append(this.startPosition).append(" ").append(" Health ").append(health).append(this.direction);
		return sb.toString();
	}
	
	/**
	 * Gets the ship action.
	 *
	 * @param move the move
	 * @param shot the shot
	 * @return the ship action
	 */
	public ShipAction getShipAction(Direction move, Coordinate shot) {
		return new ShipAction(new ShipIdentifier(shipId, playerId), shot, move);
	}
	
	/**
	 * Gets the ship action.
	 *
	 * @param moves the moves
	 * @param shots the shots
	 * @return the ship action
	 */
	public ShipAction getShipAction(List<Direction> moves, List<Coordinate> shots) {
		return new ShipAction(new ShipIdentifier(shipId, playerId), shots, moves);
	}

	@Override
	public int hashCode() {
		return this.getIdentifier().toString().hashCode();
	}

	/**
	 * Used to determine the center coordinate of a ship
	 * @return The center coordinate of a ship
	 */
	public Coordinate getCenter() {
		return new Coordinate ((int)Math.ceil((this.getStartPosition().getRow() + this.getEndPosition().getRow()) / 2.0),
				(int)Math.ceil((this.getStartPosition().getCol() + this.getEndPosition().getCol()) / 2.0));
	}
	
	/**
	 * Used to determine the distance from the center of the ship to a coordinate
	 * @param coord The coordinate to get the distance to
	 * @return The distance between a ships center and a coordinate
	 */
	public int distanceTo(Coordinate coord) {
		return this.getCenter().distanceTo(coord);
	}
	
	public Ship deepCopy() {
		//int damage, int health, int length, int range, int movement, Coordinate startPosition, Direction direction
		Ship temp = new Ship(damage, health, length, range, movement, startPosition, direction);
		return temp;
	}
}
