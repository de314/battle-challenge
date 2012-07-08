package battlechallenge.structures;

import java.io.Serializable;

import battlechallenge.Coordinate;

/**
 * The Class Structure.
 */
public abstract class Structure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** The location. */
	protected Coordinate location;

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
	 * @param location
	 *            the new location
	 */
	public void setLocation(Coordinate location) {
		this.location = location;
	}

	/**
	 * Instantiates a new structure.
	 * 
	 * @param location
	 *            the location
	 */
	public Structure(Coordinate location) {
		this.location = location;
	}

}
