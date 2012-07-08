package battlechallenge.structures;

import java.io.Serializable;

import battlechallenge.Coordinate;
import battlechallenge.server.ServerPlayer;

public class City implements Serializable {
	private static final long serialVersionUID = 0L;

	/** The mineral generation speed. */
	private int mineralGenerationSpeed;
	
	/** The owner. */
	private int ownerId;
	
	/** The location. */
	private Coordinate location;
	
	/**
	 * Gets the mineral generation speed.
	 *
	 * @return the mineral generation speed
	 */
	public int getMineralGenerationSpeed() {
		return mineralGenerationSpeed;
	}
	
	/**
	 * Sets the mineral generation speed.
	 *
	 * @param mineralGenerationSpeed the new mineral generation speed
	 */
	public void setMineralGenerationSpeed(int mineralGenerationSpeed) {
		this.mineralGenerationSpeed = mineralGenerationSpeed;
	}
	
	/**
	 * Gets the owner.
	 *
	 * @return the owner
	 */
	public int getOwnerId() {
		return ownerId;
	}
	
	/**
	 * Sets the owner.
	 *
	 * @param owner the new owner
	 */
	public void setOwner(int ownerId) {
		this.ownerId = ownerId;
	}
	
	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Coordinate getLocation() {
		return location;
	}
	
	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(Coordinate location) {
		this.location = location;
	}
	
	public City() {
		this(1, -1, null);
	}
	
	public City(Coordinate coord) {
		this(1, -1, coord);
	}

	public City(int mineralGenerationSpeed, int ownerId,
			Coordinate location) {
		this.mineralGenerationSpeed = mineralGenerationSpeed;
		this.ownerId = ownerId;
		this.location = location;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("City: ");
		sb.append(location);
		return sb.toString();
	}
}
