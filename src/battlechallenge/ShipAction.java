package battlechallenge;
import java.io.Serializable;

import battlechallenge.Ship.Direction;

/**
 * The Class ShipAction.
 */
public class ShipAction implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 0L;
	
	/** The ship identifier. */
	private ShipIdentifier shipID;
	
	/** The shot coord. */
	private Coordinate shotCoord;

	/** The move direction. */
	private Ship.Direction moveDir;
	
	/**
	 * Instantiates a new ship action.
	 *
	 * @param shipID the ship id
	 * @param shotCoord the shot coord
	 * @param moveDir the move direction
	 */
	public ShipAction(ShipIdentifier shipID, Coordinate shotCoord,
			Direction moveDir) {
		this.shipID = shipID;
		this.shotCoord = shotCoord;
		this.moveDir = moveDir;
	}
	
	/**
	 * Gets the ship id.
	 *
	 * @return the ship id
	 */
	public ShipIdentifier getShipID() {
		return shipID;
	}

	/**
	 * Gets the shot coord.
	 *
	 * @return the shot coord
	 */
	public Coordinate getShotCoord() {
		return shotCoord;
	}

	/**
	 * Gets the move dir.
	 *
	 * @return the move dir
	 */
	public Ship.Direction getMoveDir() {
		return moveDir;
	}
	
}
