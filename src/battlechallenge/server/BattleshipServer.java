package battlechallenge.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BattleshipServer extends Thread {

	private ServerSocket socket;
	private GameManager manager;
	
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
	
	public void run() {
		while(true) {
			try {
			Socket client = socket.accept();
			manager.addPlayer(client);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param args
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
