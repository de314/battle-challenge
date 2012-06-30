package battlechallenge.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import battlechallenge.ActionResult;
import battlechallenge.ship.Ship;
import battlechallenge.structures.Base;
import battlechallenge.structures.City;

public class ClientGame {
	
	private int networkID;
	private Map<Integer, List<Ship>> shipMap;
	private Map<Integer, List<ActionResult>> actionResults;
	private List<City> structures;
	
	public int getNetworkID() {
		return networkID;
	}

	public void setNetworkID(int networkID) {
		this.networkID = networkID;
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

	public List<Ship> getMyShips() {
		return null;
	}
	
	public List<List<Ship>> getOpponentShips() {
		return null;
	}

	public List<ActionResult> getMyActionResults() {
		return null;
	}
	
	public List<List<ActionResult>> getOpponentActionResults() {
		return null;
	}
	
	public Base getMyBase() {
		for (City c : structures) {
			if (c instanceof Base) {
				if (c.getOwner().getId() == networkID)
					return (Base)c;
			}
		}
		return null;
	}
	
	public List<Base> getOponnentBases() {
		List<Base> bases = new ArrayList<Base>();
		for (City c : structures) {
			if (c instanceof Base) {
				if (c.getOwner().getId() != networkID)
					bases.add((Base)c);
			}
		}
		return bases;
	}
	
	public List<City> getMyCities() {
		List<City> cities = new ArrayList<City>();
		for (City c : structures) {
			if (!(c instanceof Base)) {
				if (c.getOwner().getId() == networkID)
					cities.add(c);
			}
		}
		return cities;
	}
	
	public List<City> getAllCities() {
		List<City> cities = new ArrayList<City>();
		for (City c : structures) {
			if (!(c instanceof Base)) {
				if (c.getOwner().getId() != networkID)
					cities.add(c);
			}
		}
		return cities;
	}
}
