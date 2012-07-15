package battlechallenge.ship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sun.awt.geom.AreaOp.AddOp;

import battlechallenge.Coordinate;
import battlechallenge.ShipIdentifier;
import battlechallenge.server.ServerPlayer;

public class ShipCollection {

	private Map<String, Ship> coordMap;
	private Map<Integer, Map<Integer, Ship>> idenMap;
	private boolean needsCoordUpdate;
	private int shipIdCount;
	
	public int getNextShipId() {
		return shipIdCount++;
	}
	
	public ShipCollection() {
		coordMap = new HashMap<String, Ship>();
		idenMap = new HashMap<Integer, Map<Integer,Ship>>();
		this.needsCoordUpdate = false;
		this.shipIdCount = 0;
	}
	
	public void addPlayer(ServerPlayer player) {
		idenMap.put(player.getId(), new HashMap<Integer, Ship>());
	}
	
	public void addShip(Ship s) {
		idenMap.get(s.getPlayerId()).put(s.getShipId(), s);
		needsCoordUpdate = true;
	}
	
	public Ship getShip(ShipIdentifier si) {
		return getShip(si.playerId, si.shipId);
	}
	
	public Collection<Ship> getPlayerShips(ServerPlayer player) {
		return getPlayerShips(player.getId());
	}
	
	public Collection<Ship> getPlayerShips(int playerId) {
		return idenMap.get(playerId).values();
	}
	
	public Map<String, Ship> getCoordMap() {
		return coordMap;

	}
	
	public Ship getShip(ServerPlayer player, int shipId) {
		return getShip(player.getId(), shipId);
	}
	
	public Ship getShip(int playerId, int shipId) {
		return idenMap.get(playerId).get(shipId);
	}
	
	public void updateCoordinates() {
		coordMap.clear();
		List<Ship> shipsToSink = new LinkedList<Ship>();
		for (Map<Integer, Ship> map : idenMap.values()) {
			for (Ship s : map.values()) 
				if (coordMap.containsKey(s.getLocation().toString())) {
					shipsToSink.add(s);
					shipsToSink.add(coordMap.get(s.getLocation().toString()));
				} else
					coordMap.put(s.getLocation().toString(), s);
		}
		for (Ship s : shipsToSink) {
			if (coordMap.containsKey(s.getLocation().toString()))
				coordMap.remove(s.getLocation().toString());
			s.setHealth(0);
		}
	}
	
	public Ship getShip(Coordinate location) {
		return getShip(location.toString());
	}
	
	public Ship getShip(String coordinateString) {
		if (needsCoordUpdate) {
			updateCoordinates();
			needsCoordUpdate = false;
		}
		return coordMap.get(coordinateString);
	}
	
	public void removeShip(Ship ship) {
		if (idenMap.get(ship.getPlayerId()).containsKey(ship.getShipId()))
			idenMap.get(ship.getPlayerId()).remove(ship.getShipId());
		if (coordMap.containsKey(ship.getLocation().toString()) && coordMap.get(ship.getLocation().toString()).equals(ship))
			coordMap.remove(ship.getLocation().toString());
	}
	
	public void removeSunkenShips() {
		List<Ship> shipsToDelete = new LinkedList<Ship>();
		for (Map<Integer, Ship> map : idenMap.values()) {
			for (Ship s : map.values()) 
				if (s.isSunken())
					shipsToDelete.add(s);
		}
		for (Ship s : shipsToDelete)
			removeShip(s);
	}
	
	@Override
	public String toString() {
		return idenMap.toString();
	}
}
