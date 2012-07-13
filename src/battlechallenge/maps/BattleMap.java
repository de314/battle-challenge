package battlechallenge.maps;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import battlechallenge.server.ServerPlayer;
import battlechallenge.ship.Ship;
import battlechallenge.structures.Base;
import battlechallenge.structures.Structure;


/**
 * The Class Map.
 */
public class BattleMap implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The name. */
	private final String name;

	/** The num cols. */
	private final int numCols;
	
	/** The num rows. */
	private final int numRows;
	
	/** The structures. */
	private final List<Structure> structures;
	
	/** The bases. */
	private final List<Base> bases;
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the num cols.
	 *
	 * @return the num cols
	 */
	public int getNumCols() {
		return numCols;
	}
	
	/**
	 * Gets the num rows.
	 *
	 * @return the num rows
	 */
	public int getNumRows() {
		return numRows;
	}
	
	/**
	 * Gets the structures.
	 *
	 * @return the structures
	 */
	public List<Structure> getStructures() {
		return structures;
	}
	
	/**
	 * Gets the bases.
	 *
	 * @return the bases
	 */
	public List<Base> getBases() {
		return bases;
	}
	
	/**
	 * Instantiates a new map.
	 *
	 * @param name the name
	 * @param numCols the num cols
	 * @param numRows the num rows
	 * @param structures the structures
	 * @param bases the bases
	 */
	public BattleMap(String name, int numCols, int numRows, List<Structure> structures, List<Base> bases) {
		this.name = name;
		this.numCols = numCols;
		this.numRows = numRows;
		this.structures = structures;
		this.bases = bases;
	}
	
	/**
	 * Assign players to bases.
	 *
	 * @param players the players
	 * @return true, if successful
	 */
	public boolean assignPlayersToBases(Collection<ServerPlayer> players, List<Ship> ships) {
		if (players.size() > bases.size())
			return false;
		List<Ship> temp = new LinkedList<Ship>();
		int count = 0;
		for(ServerPlayer p : players) {
			temp.clear();
			for (Ship s : ships)
				temp.add(s.deepCopy());
			p.setBase(bases.get(count++), temp);
		}
		return true;
	}
}
