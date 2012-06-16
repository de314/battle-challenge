package battlechallenge;
import java.io.Serializable;

import battlechallenge.Ship.Direction;

/**
 * The Class ShipAction.
 */
public class ShipAction implements Serializable {
	
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
	
}
