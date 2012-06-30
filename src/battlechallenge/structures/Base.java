package battlechallenge.structures;

import battlechallenge.Coordinate;
import battlechallenge.server.ServerPlayer;

/**
 * The Class Base.
 */
public class Base extends City {

	/** The health. */
	private int health;
	
	/**
	 * Gets the health.
	 *
	 * @return the health
	 */
	public int getHealth() {
		return health;
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
	 * Instantiates a new base.
	 */
	public Base() {
		this(10, 1, null, null);
	}
	
	/**
	 * Instantiates a new base.
	 *
	 * @param health the health
	 * @param mineralGenerationSpeed the mineral generation speed
	 * @param owner the owner
	 * @param location the location
	 */
	public Base(int health, int mineralGenerationSpeed, ServerPlayer owner,
			Coordinate location) {
		super(mineralGenerationSpeed, owner, location);
		this.health = health;
	}
}
