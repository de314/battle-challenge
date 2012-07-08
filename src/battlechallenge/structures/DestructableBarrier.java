package battlechallenge.structures;

import battlechallenge.ActionResult;
import battlechallenge.Coordinate;
import battlechallenge.ActionResult.ShotResult;
import battlechallenge.ship.Ship;

// TODO: Auto-generated Javadoc
/**
 * The Class DestructableBarrier.
 */
public class DestructableBarrier extends Barrier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The health. */
	private int health;
	
	/**
	 * Gets the health.
	 *
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * Instantiates a new destructable barrier.
	 *
	 * @param location the location
	 * @param health the health
	 */
	public DestructableBarrier(Coordinate location, int health) {
		super(location);
		this.health = health;
	}
	
	/**
	 * Checks if is hit.
	 *
	 * @param c the c
	 * @param attacker the attacker
	 * @param damage the damage
	 * @return the action result
	 */
	public ActionResult isHit(Coordinate c, Ship attacker, int damage) {
		if (c == null)
			return new ActionResult(c, attacker.getLocation(), ShotResult.MISS, -1, -1);
		if (location.equals(c)) {
			health -= damage;
			return new ActionResult(c, attacker.getLocation(), health<=0 ? ShotResult.SUNK : ShotResult.HIT, this.health, -1);
		}
		return new ActionResult(c, attacker.getLocation(), ShotResult.MISS, -1, -1);
	}
}
