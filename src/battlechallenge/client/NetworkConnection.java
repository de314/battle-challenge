package battlechallenge.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkConnection {
	
	public static final String ip = "127.0.0.1";
	public static final int port = 3000;
	
	private Socket conn;
	private int id;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Game game;
	
	public void run() {
		
	}
	
	public void setupHandShake() {
		
	}
	
	public void setPlayerCredentials(String name) {
		
	}
	
	public void doTurn() {
		
	}
}
