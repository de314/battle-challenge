package battlechallenge.client;

public class BattleshipClient {
	
	public static final String IP = "127.0.0.1";
	public static final int PORT = 3000;
	
	
	
	public static void main(String[] args) {
		String name;
		if (args.length > 0)
			name = args[0];
		else
			name = "player" + ((int)(Math.random()*Integer.MAX_VALUE));
		int port = PORT;
		if (args.length > 1) 
			try {
			port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) { /* ignore exception */ }
		String ip = IP;
		if (args.length > 2)
			ip = args[1];
		new ServerConnection(port, ip, name);
	}
}
