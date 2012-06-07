package battlechallenge;

public class ActionResult {

	public static enum ShotResult {
		HIT, SUNK, MISS
	}
	
	private Coordinate coordinate;
	private ShotResult result;
	private int health;
	
	public Coordinate getCoordinate() {
		return coordinate;
	}



	public ShotResult getResult() {
		return result;
	}



	public int getHealth() {
		return health;
	}



	public ActionResult(Coordinate c, ActionResult result, int health) {
		// TODO: Action Result constructor
	}
	
}
