package battlechallenge.client;

import battlechallenge.bot.ClientPlayer;
import battlechallenge.bot.DavidBot;
import battlechallenge.bot.KevinBot;

public class BattleshipClient {
	
	public static final String IP = "127.0.0.1";
	public static final int PORT = 3000;
	
	public static ClientPlayer botToPlay(String botName) {
		if (botName.equals("DavidBot")) {
			return new DavidBot(botName, 0, 0, 0);
		}
		else if (botName.equals("KevinBot")) {
			return new KevinBot(botName, 0, 0, 0);
		}
		else {
			return new ClientPlayer(botName, 0, 0, 0);
		}
	}
	
	
	public static void main(String[] args) {
		String name;
		ClientPlayer bot;
		if (args.length > 0)
			name = args[0];
		else
			name = "player" + ((int)(Math.random()*Integer.MAX_VALUE));
		int port = PORT;
		if (args.length > 1) 
			try {
			port = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) { /* ignore exception */ }
		String ip = IP;
		if (args.length > 2)
			ip = args[2];
		if (args.length > 3) {
			bot = botToPlay(args[3]);
		}
		else {
			bot = botToPlay(name);
		}		
		System.out.println(port + " " + ip);
		new ServerConnection(port, ip, name, bot);
	}
}
