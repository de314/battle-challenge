package battlechallenge;
import java.io.Serializable;
/**
 * The Class ShipIdentifier.
 */
public class ShipIdentifier implements Serializable {
	
	private static final long serialVersionUID = 0L;

	/** The ship id. */
	private int shipID;
	
	/** The player id. */
	private int playerID;
	
	/**
	 * Instantiates a new ship identifier.
	 *
	 * @param shipID the ship id
	 * @param playerID the player id
	 */
	public ShipIdentifier(int shipID, int playerID) {
		this.shipID = shipID;
		this.playerID = playerID;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + playerID + ": " + shipID+ "]";
	}

}
