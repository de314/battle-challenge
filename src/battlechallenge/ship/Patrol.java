package battlechallenge.ship;

import battlechallenge.Coordinate;

public class Patrol extends Ship {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// TODO: set values
	public static final int DAMAGE = 5;
	public static final int HEALTH = 5;
	public static final int LENGTH = 5;
	public static final int RANGE = 5;
	public static final int MOVEMENT = 5;
	
	public Patrol() {
		this(new Coordinate(-1, -1), Direction.NORTH);
	}
	
	public Patrol(Coordinate startPosition, Direction direction) {
		super(DAMAGE, HEALTH, LENGTH, RANGE, MOVEMENT, startPosition, direction);		
	}
}
