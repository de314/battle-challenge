package battlechallenge.structures;

import java.io.Serializable;

import battlechallenge.Coordinate;

public class Barrier extends Structure implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Coordinate location;
	
	public Barrier(Coordinate location) {
		super(location);
		this.location = location;
	}

	public Coordinate getLocation() {
		return location;
	}

	public void setLocation(Coordinate location) {
		this.location = location;
	}
	
	
    
	
}
