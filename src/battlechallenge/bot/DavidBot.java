package battlechallenge.bot;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import battlechallenge.ActionResult;
import battlechallenge.ActionResult.ShotResult;
import battlechallenge.Coordinate;
import battlechallenge.ShipAction;
import battlechallenge.ship.Ship;
import battlechallenge.ship.Ship.Direction;


public class DavidBot extends ClientPlayer {

	private Set<String> guessed;
	private Queue<Coordinate> adjacentList;
	private List<MyCoordinate> nextGuesses;
	
	public DavidBot(String playerName, int boardWidth, int boardHeight,
			int networkID) {
		super(playerName, boardWidth, boardHeight, networkID);
		this.guessed = new HashSet<String>();
		adjacentList = new LinkedList<Coordinate>();
	}
	
	@Override
	public List<Ship> placeShips(List<Ship> shipList) {
		
		Set<String> boatCoords = new HashSet<String>();
		for (Ship s : shipList) {
			int row = -1;
			int col = -1;
			while (row < 0 || col < 0) {
				Direction dir;
				int rowLimit;
				int colLimit;
				if (Math.random() > 0.5) {
					dir= Direction.NORTH;
					rowLimit = s.getLength();
					colLimit = 0;
				} else {
					dir = Direction.EAST;
					rowLimit = 0;
					colLimit = s.getLength();
				}
				row = (int)(Math.random() * (boardHeight - rowLimit)) + rowLimit;
				col = (int)(Math.random() * (boardWidth - colLimit));
				s.setStartPosition(new Coordinate(row, col));
				s.setDirection(dir);
				Set<String> temp = s.getCoordinateStrings();
				for (String c : temp) {
					if (boatCoords.contains(c)) {
						row = -1;
						col = -1;
						break;
					}
				}
				boatCoords.addAll(temp);
			}
		}
		
		
//		List<Integer> shipRow = new ArrayList<Integer>();
//		int row = 0;
//		for (Ship ship: shipList) {
//			while (shipRow.contains(row)) {
//				row = (int) (Math.random() * (boardHeight-1));
//			}
//			shipRow.add(row);
//			ship.setStartPosition(new Coordinate(row,0));
//			ship.setDirection(Ship.Direction.EAST);
//		}
//		System.out.println("placed ships");
		
		
		return shipList;
	}

	public List<ShipAction> doTurn(List<Ship> myShips, Map<Integer, List<ActionResult>> actionResults) {
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
			c = new Coordinate((int)(Math.random() * super.boardHeight), (int)(Math.random() * super.boardWidth));
			if (guessed.contains(c.toString()))
				c = null;
		}
		System.out.println("#guesses: " + guessed.size() + "  ||  #adjacent: " + adjacentList.size());
		guessed.add(c.toString());
		List<Coordinate> ret = new LinkedList<Coordinate>();
		ret.add(c);
//		return ret;
		return null;
	}
	
	public void addAdjacent(int row, int col) {
		if (col < super.boardWidth-1)
			adjacentList.offer(new Coordinate(row, col+1));
		if (col > 0)
			adjacentList.offer(new Coordinate(row, col-1));
		if (row < super.boardHeight-1)
			adjacentList.offer(new Coordinate(row+1, col));
		if (row > 0)
			adjacentList.offer(new Coordinate(row-1, col));
	}
	
	
	/*
	 * TODO: halfway through implementation
	 */
	private static class MyCoordinate {
		
		public enum Orientation { HORIZONTAL, VERTICLE }
		
		private Orientation dir;
		private Coordinate coord;
		private MyCoordinate referrer;
		private List<MyCoordinate> vert;
		private List<MyCoordinate> horz;
		
		public MyCoordinate(Coordinate coord, Orientation dir, MyCoordinate referrer) {
			this.coord = coord;
			this.dir = dir;
			this.referrer = referrer;
		}
		
		private List<MyCoordinate> getHorzGuesses(Set<String> guessed, int width, int height) {
			if (horz != null)
				return horz;
			horz = new LinkedList<MyCoordinate>();
			if (this.coord.getCol() < width-1) {
				Coordinate coord = new Coordinate(this.coord.getRow(), this.coord.getCol()+1);
				if (!guessed.contains(coord))
					horz.add(new MyCoordinate(coord, Orientation.HORIZONTAL, this));
			}
			if (this.coord.getCol() > 0) {
				Coordinate coord = new Coordinate(this.coord.getRow(), this.coord.getCol()-1);
				if (!guessed.contains(coord))
					horz.add(new MyCoordinate(coord, Orientation.HORIZONTAL, this));
			}
			return horz;
		}
			
		private List<MyCoordinate> getVertGuesses(Set<String> guessed, int width, int height) {
			if (vert != null)
				return vert;
			vert = new LinkedList<MyCoordinate>();
			if (this.coord.getRow() < height-1) {
				Coordinate coord = new Coordinate(this.coord.getRow()+1, this.coord.getCol());
				if (!guessed.contains(coord))
					vert.add(new MyCoordinate(coord, Orientation.HORIZONTAL, this));
			}
			if (this.coord.getRow() > 0) {
				Coordinate coord = new Coordinate(this.coord.getRow()-1, this.coord.getCol());
				if (!guessed.contains(coord))
					vert.add(new MyCoordinate(coord, Orientation.HORIZONTAL, this));
			}
			return vert;
		}
		
		public List<MyCoordinate> getGuesses(Set<String> guessed, int width, int height) {
			List<MyCoordinate> temp = new LinkedList<MyCoordinate>();
			temp.addAll(getHorzGuesses(guessed, width, height));
			temp.addAll(getVertGuesses(guessed, width, height));
			return temp;
		}
		
		public void remove(List<MyCoordinate> nextMoves) {
			if (dir == Orientation.VERTICLE)
				nextMoves.removeAll(referrer.getHorzGuesses(null, -1, -1));
			else 
				nextMoves.removeAll(referrer.getVertGuesses(null, -1, -1));
		}
		
		@Override
		public String toString() {
			return coord.toString();
		}
	}
	
}
