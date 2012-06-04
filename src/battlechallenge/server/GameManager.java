package battlechallenge.server;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class GameManager {

	private Set<Game> games;
	private Game lastGame;
	private Set<Player> players;
	
	public GameManager() {
		games = new HashSet<Game>();
		players = new HashSet<Player>();
	}
	
	public void addPlayer(Socket conn) {
		if (conn != null) {
			Player p = new Player(conn, new Board(Game.DEFAULT_WIDTH, Game.DEFAULT_HEIGHT, Game.getShips()));
			if (lastGame != null) {
				if (!lastGame.addPlayer(p)) {
					lastGame = new Game(p);
					games.add(lastGame);
				} // else player was successfully added
			} else
				lastGame = new Game(p);
		}
	}
	
	public void removeGame(Game g) {
		if (games.contains(g)) {
			games.remove(g);
			
		}
	}
}
