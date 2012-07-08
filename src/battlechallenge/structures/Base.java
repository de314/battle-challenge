package battlechallenge.structures;

import java.io.Serializable;

import battlechallenge.Coordinate;
import battlechallenge.server.ServerPlayer;

/**
 * The Class Base.
 */
public class Base extends City implements Serializable {
	private static final long serialVersionUID = 0L;
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
		this(10, 1, -1, null);
	}
	
	public Base(int ownerId, Coordinate coord) {
		this(10, 1, ownerId, coord);	
	}
	
	/**
	 * Instantiates a new base.
	 *
	 * @param health the health
	 * @param mineralGenerationSpeed the mineral generation speed
	 * @param owner the owner
	 * @param location the location
	 */
	public Base(int health, int mineralGenerationSpeed, int ownerId,
			Coordinate location) {
		super(mineralGenerationSpeed, ownerId, location);
		this.health = health;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Base: ");
		sb.append(getLocation());
		return sb.toString();
	}
}
