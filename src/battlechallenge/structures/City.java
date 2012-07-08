package battlechallenge.structures;

import java.io.Serializable;

import battlechallenge.Coordinate;
import battlechallenge.server.ServerPlayer;

// TODO: Auto-generated Javadoc
/**
 * The Class City.
 */
public class City extends Structure implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 0L;

	/** The mineral generation speed. */
	private int mineralGenerationSpeed;
	
	/** The owner. */
	private int ownerId;
	
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
	 * @param ownerId the new owner
	 */
	public void setOwner(int ownerId) {
		this.ownerId = ownerId;
	}
	
	/**
	 * Instantiates a new city.
	 */
	public City() {
		this(1, -1, null);
	}
	
	/**
	 * Instantiates a new city.
	 *
	 * @param coord the coord
	 */
	public City(Coordinate coord) {
		this(1, -1, coord);
	}

	/**
	 * Instantiates a new city.
	 *
	 * @param mineralGenerationSpeed the mineral generation speed
	 * @param ownerId the owner id
	 * @param location the location
	 */
	public City(int mineralGenerationSpeed, int ownerId,
			Coordinate location) {
		super(location);
		this.mineralGenerationSpeed = mineralGenerationSpeed;
		this.ownerId = ownerId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("City: ");
		sb.append(location);
		return sb.toString();
	}
}
