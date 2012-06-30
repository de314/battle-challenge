package battlechallenge.ship;

import battlechallenge.Coordinate;

public class Cruiser extends Ship {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// TODO: set values
	public static final int DAMAGE = 5;
	public static final int HEALTH = 5;
	public static final int NUMSHOTS = 1;
	public static final int RANGE = 5;
	public static final int MOVEMENT = 5;
	
	public Cruiser() {
		this(new Coordinate(-1, -1));
	}
	
	public Cruiser(Coordinate startPosition) {
		super(DAMAGE, HEALTH, RANGE, MOVEMENT, NUMSHOTS, startPosition);		
	}
}
