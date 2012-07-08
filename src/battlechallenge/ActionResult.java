package battlechallenge;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class ActionResult.
 */
public class ActionResult implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 0L;

	/**
	 * The Enum ShotResult.
	 */
	public static enum ShotResult {
		
		/** The HIT. */
		HIT, 
		/** The SUNK. */
		SUNK, 
		/** The MISS. */
		MISS
	}
	
	/** The coordinate. */
	private final Coordinate coordinate;
	
	private final Coordinate origin;
	
	/** The result. */
	private final ShotResult result;
	
	/** The health. */
	private final int health;
	
	/** The player id. */
	private final int playerId;
	
	/**
	 * Gets the coordinate.
	 *
	 * @return the coordinate
	 */
	public Coordinate getCoordinate() {
		return coordinate;
	}
	
	/**
	 * Gets the coordinate.
	 *
	 * @return the coordinate
	 */
	public Coordinate getOrigin() {
		return origin;
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public ShotResult getResult() {
		return result;
	}

	/**
	 * Gets the health.
	 *
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Gets the player id.
	 *
	 * @return the player id
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * Instantiates a new action result.
	 * 
	 * Note: Assumes good input. No validation.
	 *
	 * @param c the c
	 * @param result the result
	 * @param health the health
	 * @param playerId the player id
	 */
	public ActionResult(Coordinate target, Coordinate origin, ShotResult result, int health, int playerId) {
		this.coordinate = target;
		this.origin = origin;
		this.result = result;
		this.health = health;
		this.playerId = playerId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[ ");
		sb.append(this.result.toString()).append(" : ").append(this.coordinate);
		sb.append(" ]");
		return sb.append(" Health: ").append(this.health).toString();
	}
	
}
