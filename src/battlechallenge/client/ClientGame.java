package battlechallenge.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import battlechallenge.ActionResult;
import battlechallenge.ship.Ship;
import battlechallenge.structures.Base;
import battlechallenge.structures.City;

/**
 * The Class ClientGame.
 */
public class ClientGame {
	
	/** The network id. */
	private static int networkId;
	
	/** The ship map. */
	private static Map<Integer, List<Ship>> shipMap;
	
	/** The action results. */
	private static Map<Integer, List<ActionResult>> actionResults;
	
	/** The structures. */
	private List<City> structures;
	
	public static int getNetworkID() {
		return networkId;
	}

	public void setNetworkID(int networkId) {
		this.networkId = networkId;
	}

	public Map<Integer, List<Ship>> getShipMap() {
		return shipMap;
	}

	public void setShipMap(Map<Integer, List<Ship>> shipMap) {
		this.shipMap = shipMap;
	}

	public Map<Integer, List<ActionResult>> getActionResults() {
		return actionResults;
	}

	public void setActionResults(Map<Integer, List<ActionResult>> actionResults) {
		this.actionResults = actionResults;
	}

	public List<City> getStructures() {
		return structures;
	}

	public void setStructures(List<City> structures) {
		this.structures = structures;
	}

	/**
	 * Gets the my ships.
	 *
	 * @return the my ships
	 */
	public static List<Ship> getMyShips() {
		List<Ship> myShips = shipMap.get(networkId);
		return myShips;
	}
	
	/**
	 * Gets the opponent ships.
	 *
	 * @return the opponent ships
	 */
	public List<Ship> getOpponentShips() {
		List<Ship> enemyShips = new LinkedList<Ship>();
		for (Entry<Integer, List<Ship>> e: shipMap.entrySet()) { 
			if (e.getKey() == networkId)
				continue;
			for (Ship s : shipMap.get(e.getKey())) {
				enemyShips.add(s);
			}	
		}
		return enemyShips;
	}

	/**
	 * Gets the my action results.
	 *
	 * @return the my action results
	 */
	public List<ActionResult> getMyActionResults() {
		List<ActionResult> myActionResults = actionResults.get(networkId);
		return myActionResults;
	}
	
	/**
	 * Gets the opponent action results.
	 *
	 * @return the opponent action results
	 */
	public List<ActionResult> getOpponentActionResults() {
		List<ActionResult> enemyActions = new LinkedList<ActionResult>();
		for (Entry<Integer, List<ActionResult>> e: actionResults.entrySet()) { 
			if (e.getKey() == networkId)
				continue;
			for (ActionResult a: actionResults.get(e.getKey())) {
				enemyActions.add(a);
			}	
		}
		return enemyActions;
	}
	
	/**
	 * Gets the my base.
	 *
	 * @return the my base
	 */
	public Base getMyBase() {
		for (City c : structures) {
			if (c instanceof Base) {
				if (c.getOwner().getId() == networkId)
					return (Base)c;
			}
		}
		return null;
	}
	
	/**
	 * Gets the oponnent bases.
	 *
	 * @return the oponnent bases
	 */
	public List<Base> getOponnentBases() {
		List<Base> bases = new ArrayList<Base>();
		for (City c : structures) {
			if (c instanceof Base) {
				if (c.getOwner().getId() != networkId)
					bases.add((Base)c);
			}
		}
		return bases;
	}
	
	/**
	 * Gets the my cities.
	 *
	 * @return the my cities
	 */
	public List<City> getMyCities() {
		List<City> cities = new ArrayList<City>();
		for (City c : structures) {
			if (!(c instanceof Base)) {
				if (c.getOwner().getId() == networkId)
					cities.add(c);
			}
		}
		return cities;
	}
	
	/**
	 * Gets the all cities.
	 *
	 * @return the all cities
	 */
	public List<City> getAllCities() {
		List<City> cities = new ArrayList<City>();
		for (City c : structures) {
			if (!(c instanceof Base)) {
				if (c.getOwner().getId() != networkId)
					cities.add(c);
			}
		}
		return cities;
	}
}
