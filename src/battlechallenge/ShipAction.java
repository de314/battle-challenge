package battlechallenge;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import battlechallenge.ship.Ship;
import battlechallenge.ship.Ship.Direction;

/**
 * The Class ShipAction.
 */
public class ShipAction implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 0L;
	
	/** The ship identifier. */
	private ShipIdentifier shipIdentifier;
	
	/** The shot coord. */
	private List<Coordinate> shots;

	/** The move direction. */
	private List<Direction> moves;
	
	/**
	 * Instantiates a new ship action.
	 *
	 * @param shipID the ship id
	 * @param shotCoord the shot coord
	 * @param moveDir the move dir
	 */
	public ShipAction(ShipIdentifier shipID, Coordinate shotCoord,
			Direction moveDir) {
		this.shipIdentifier = shipID;
		this.shots = new LinkedList<Coordinate>();
		this.shots.add(shotCoord);
		this.moves = new LinkedList<Direction>();
		this.moves.add(moveDir);
	}
	
	/**
	 * Instantiates a new ship action.
	 *
	 * @param shipID the ship id
	 * @param shots the shots
	 * @param moves the moves
	 */
	public ShipAction(ShipIdentifier shipID, List<Coordinate> shots,
			List<Direction> moves) {
		this.shipIdentifier = shipID;
		this.shots = shots;
		this.moves = moves;
	}
	
	/**
	 * Gets the ship id.
	 *
	 * @return the ship id
	 */
	public ShipIdentifier getShipIdentifier() {
		return shipIdentifier;
	}

	/**
	 * Gets the first shot.
	 *
	 * @return the shot coord
	 */
	public Coordinate getShotCoord() {
		return shots.size() > 0 ? shots.get(0) : null;
	}

	/**
	 * Gets the i-th shot.
	 *
	 * @param i the i
	 * @return the shot coord
	 */
	public Coordinate getShotCoord(int i) {
		return shots.size() > i ? shots.get(i) : null;
	}
	
	/**
	 * Gets the first move.
	 *
	 * @return the move dir
	 */
	public Ship.Direction getMoveDir() {
		return moves.size() > 0 ? moves.get(0) : null;
	}
	
	/**
	 * Gets the i-th move.
	 *
	 * @param i the i
	 * @return the move dir
	 */
	public Ship.Direction getMoveDir(int i) {
		return moves.size() > i ? moves.get(i) : null;
	}
	
	@Override
	public String toString() {
		return getShotCoord() == null ? "()" : getShotCoord().toString();
	}
	
}
