package battlechallenge.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import battlechallenge.ActionResult;
import battlechallenge.maps.BattleMap;
import battlechallenge.ship.Ship;
import battlechallenge.structures.Base;
import battlechallenge.structures.City;
import battlechallenge.structures.Structure;

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
	
	/** The map. */
	private static BattleMap map;
	
	/**
	 * Gets the network id.
	 *
	 * @return the network id
	 */
	public static int getNetworkID() {
		return networkId;
	}

	/**
	 * Sets the network id.
	 *
	 * @param networkId the new network id
	 */
	public static void setNetworkID(int networkId) {
		ClientGame.networkId = networkId;
	}

	/**
	 * Gets the ship map.
	 *
	 * @return the ship map
	 */
	public static Map<Integer, List<Ship>> getShipMap() {
		return new HashMap<Integer, List<Ship>>(shipMap);
	}

	/**
	 * Sets the ship map.
	 *
	 * @param shipMap the ship map
	 */
	public static void setShipMap(Map<Integer, List<Ship>> shipMap) {
		ClientGame.shipMap = shipMap;
	}

	/**
	 * Gets the action results.
	 *
	 * @return the action results
	 */
	public static Map<Integer, List<ActionResult>> getActionResults() {
		return new HashMap<Integer, List<ActionResult>>(actionResults);
	}

	/**
	 * Sets the action results.
	 *
	 * @param newActionResults the new action results
	 */
	public static void setActionResults(Map<Integer, List<ActionResult>> newActionResults) {
		actionResults = newActionResults;
	}

	/**
	 * Gets the map.
	 *
	 * @return the map
	 */
	public static BattleMap getMap() {
		return map;
	}

	/**
	 * Sets the map.
	 *
	 * @param newMap the new map
	 */
	public static void setMap(BattleMap newMap) {
		map = newMap;
	}

	/**
	 * Gets the my shMps.
	 *
	 * @return the my ships
	 */
	public static List<Ship> getMyShips() {
		return new LinkedList<Ship>(shipMap.get(networkId));
	}
	
	/**
	 * Gets the opponent ships.
	 *
	 * @return the opponent ships
	 */
	public static List<Ship> getOpponentShips() {
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
	public static List<ActionResult> getMyActionResults() {
		return new LinkedList<ActionResult>(actionResults.get(networkId));
	}
	
	/**
	 * Gets the opponent action results.
	 *
	 * @return the opponent action results
	 */
	public static List<ActionResult> getOpponentActionResults() {
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
	public static Base getMyBase() {
		for (Base b : map.getBases()) {
			if (b.getOwnerId() == networkId)
				return b;
		}
		return null;
	}
	
	public static List<Base> getAllBases() {
		return map.getBases();
	}
	
	/**
	 * Gets the opponent bases.
	 *
	 * @return the opponent bases
	 */
	public static List<Base> getOpponentBases() {
		List<Base> bases = new ArrayList<Base>();
		for (Base b : map.getBases()) {
			if (b.getOwnerId() != networkId)
				bases.add(b);
		}
		return bases;
	}
	
	/**
	 * Gets the my cities.
	 *
	 * @return the my cities
	 */
	public static List<City> getMyCities() {
		List<City> cities = new ArrayList<City>();
		for (Structure str : map.getStructures()) {
			if (str instanceof City) {
				City c = (City)str;
				if (c.getOwnerId() == networkId)
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
	public static List<City> getAllCities() {
		List<City> cities = new ArrayList<City>();
		for (Structure str : map.getStructures()) {
			if (str instanceof City) {
				City c = (City) str;
				if (c.getOwnerId() != networkId)
					cities.add(c);
			}
		}
		return cities;
	}
}
