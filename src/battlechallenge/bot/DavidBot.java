package battlechallenge.bot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import battlechallenge.ActionResult;
import battlechallenge.ActionResult.ShotResult;
import battlechallenge.Ship.Direction;
import battlechallenge.Coordinate;
import battlechallenge.Ship;


public class DavidBot extends ClientPlayer {

	private Set<String> guessed;
	private Queue<Coordinate> adjacentList;
	
	public DavidBot(String playerName, int mapWidth, int mapHeight,
			int networkID) {
		super(playerName, mapWidth, mapHeight, networkID);
		this.guessed = new HashSet<String>();
		adjacentList = new LinkedList<Coordinate>();
	}
	
	@Override
	public List<Ship> placeShips(List<Ship> shipList) {
		
		/*
		Set<String> boatCoords = new HashSet<String>();
		for (Ship s : shipList) {
			int row = -1;
			int col = -1;
			while (row < 0 || col < 0) {
				Direction dir = Direction.NORTH;
				if (Math.random() > 0.5)
					dir = Direction.EAST;
				row = (int)(Math.random() * mapHeight);
				col = (int)(Math.random() * mapWidth);
				
			}
		}
		*/
		
		
		List<Integer> shipRow = new ArrayList<Integer>();
		int row = 0;
		for (Ship ship: shipList) {
			while (shipRow.contains(row)) {
				row = (int) (Math.random() * (mapHeight-1));
			}
			shipRow.add(row);
			ship.setStartPosition(new Coordinate(row,0));
			ship.setDirection(Ship.Direction.EAST);
		}
		System.out.println("placed ships");
		return shipList;
	}

	public List<Coordinate> doTurn(List<Ship> myShips, Map<Integer, List<ActionResult>> actionResults) {
		List<ActionResult> results = actionResults.get(super.networkID);
		for (ActionResult ar : results) {
			if (ar.getResult() == ShotResult.HIT) {
				addAdjacent(ar.getCoordinate().getRow(), ar.getCoordinate().getCol());
				System.out.println("Recording hit at " + ar.getCoordinate().toString() + " with size " + adjacentList.size());
			}
		}
		Coordinate c = null;
		while (c == null && !adjacentList.isEmpty()) {
			c = adjacentList.poll();
			if (guessed.contains(c.toString()))
				c = null;
		}
		while (c == null) {
			c = new Coordinate((int)(Math.random() * super.mapHeight), (int)(Math.random() * super.mapWidth));
			if (guessed.contains(c.toString()))
				c = null;
		}
		System.out.println("#guesses: " + guessed.size() + "  ||  #adjacent: " + adjacentList.size());
		guessed.add(c.toString());
		List<Coordinate> ret = new LinkedList<Coordinate>();
		ret.add(c);
		return ret;
	}
	
	public void addAdjacent(int row, int col) {
		if (col < super.mapWidth-1)
			adjacentList.offer(new Coordinate(row, col+1));
		if (col > 0)
			adjacentList.offer(new Coordinate(row, col-1));
		if (row < super.mapHeight-1)
			adjacentList.offer(new Coordinate(row+1, col));
		if (col > 0)
			adjacentList.offer(new Coordinate(row-1, col));
	}
	
}
