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
