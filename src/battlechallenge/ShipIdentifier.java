package battlechallenge;
import java.io.Serializable;
/**
 * The Class ShipIdentifier.
 */
public class ShipIdentifier implements Serializable {
	
	private static final long serialVersionUID = 0L;

	/** The ship id. */
	public final int shipId;
	
	/** The player id. */
	public final int playerId;
	
	/**
	 * Instantiates a new ship identifier.
	 *
	 * @param shipID the ship id
	 * @param playerID the player id
	 */
	public ShipIdentifier(int shipId, int playerId) {
		this.shipId = shipId;
		this.playerId = playerId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + playerId + " : " + shipId + "]";
	}

}
