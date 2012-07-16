package battlechallenge.server;

public class ScoreKeeper {

	public static final int MULTIPLIER_BATTLE;
	public static final int MULTIPLIER_ECONOMY;
	
	static {
		MULTIPLIER_BATTLE = 10;
		MULTIPLIER_ECONOMY = 50;
	}
	
	private int score;
	private int shipsSunk;
	private int mineralsEarned;
	
	public int getScore() {
		return score;
	}
	
	public void addSunkShip() {
		shipsSunk++;
		updateScore();
	}
	
	public void addMineralsEarned(int total) {
		mineralsEarned += total;
		updateScore();
	}
	
	private void updateScore() {
		score = shipsSunk * MULTIPLIER_BATTLE + mineralsEarned * MULTIPLIER_ECONOMY;
	}
}
