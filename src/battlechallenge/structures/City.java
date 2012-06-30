package battlechallenge.structures;

import battlechallenge.Coordinate;
import battlechallenge.server.ServerPlayer;

public class City {

	/** The mineral generation speed. */
	private int mineralGenerationSpeed;
	
	/** The owner. */
	private ServerPlayer owner;
	
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
	public ServerPlayer getOwner() {
		return owner;
	}
	
	/**
	 * Sets the owner.
	 *
	 * @param owner the new owner
	 */
	public void setOwner(ServerPlayer owner) {
		this.owner = owner;
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
		this(1, null, null);
	}

	public City(int mineralGenerationSpeed, ServerPlayer owner,
			Coordinate location) {
		super();
		this.mineralGenerationSpeed = mineralGenerationSpeed;
		this.owner = owner;
		this.location = location;
	}
}
