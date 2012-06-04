package bot;

public class Player {
	
	private String name;
	private PlayerGame game;
	
	public Player(String name, Game game) {
		this.name = name;
		this.game = game;
	}
	
	public void placeShips(List<Ship> ship) {
		
	}
	
	public Coordinate doTurn(PlayerBoard myBoard, PlayerBoard oppBoard) {
		return null;
	}
}
