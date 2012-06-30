package battlechallenge.ship;

import battlechallenge.Coordinate;

public class BattleShip extends Ship {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// TODO: set values
	public static final int DAMAGE = 5;
	public static final int HEALTH = 5;
	public static final int NUMSHOTS = 5;
	public static final int RANGE = 5;
	public static final int MOVEMENT = 5;
	
	public BattleShip() {
		this(new Coordinate(-1, -1));
	}
	
	public BattleShip(Coordinate startPosition) {
		super(DAMAGE, HEALTH, RANGE, MOVEMENT, NUMSHOTS, startPosition);		
	}
}
