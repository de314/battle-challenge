package battlechallenge.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class BattleshipServer.
 */
public class BattleshipServer extends Thread {
	
	/** The SERVE r_ version. */
	public static String SERVER_VERSION;
	
	/** The SUPPORTE d_ clients. */
	public static Set<String> SUPPORTED_CLIENTS;
	
	/** The Constant SERVER_REQUEST_NAME. */
	public static final String SERVER_REQUEST_NAME;
	
	/** The Constant SERVER_REQUEST_PLACE_SHIPS. */
	public static final String SERVER_REQUEST_PLACE_SHIPS;
	
	/** The Constant SERVER_REQUEST_TURN. */
	public static final String SERVER_REQUEST_TURN;
	
	/** The Constant SERVER_RESULT_DISQUALIFIED. */
	public static final String SERVER_RESULT_DISQUALIFIED;
	
	/** The Constant SERVER_RESULT_WINNER. */
	public static final String SERVER_RESULT_WINNER;
	
	/** The Constant SERVER_RESULT_LOSER. */
	public static final String SERVER_RESULT_LOSER;
	
	static {
		SERVER_VERSION = "battleship_S-v0";
		SUPPORTED_CLIENTS = new HashSet<String>();
		SUPPORTED_CLIENTS.add("battleship_C-v0");
		SERVER_REQUEST_NAME = "N";
		SERVER_REQUEST_PLACE_SHIPS = "P";
		SERVER_REQUEST_TURN = "T";
		SERVER_RESULT_DISQUALIFIED = "D";
		SERVER_RESULT_WINNER = "W";
		SERVER_RESULT_LOSER= "L";
	}

	/** The socket. */
	private ServerSocket socket;
	
	/** The manager. */
	private GameManager manager;
	
	/**
	 * Instantiates a new battleship server.
	 *
	 * @param port the port
	 * @param manager the manager
	 */
	public BattleshipServer(int port, GameManager manager) {
		if (manager == null || port < 0)
			throw new IllegalArgumentException();
		this.manager = manager;
		try {
			socket = new ServerSocket(port);
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while(true) {
			try {
			Socket client = socket.accept(); // blocking
			manager.addPlayer(client);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		int port = 3000;
		if (args.length > 0)
			try {
			port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {}
		new BattleshipServer(port, new GameManager());
	}

}
