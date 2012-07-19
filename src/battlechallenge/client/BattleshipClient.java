package battlechallenge.client;

import uk.co.flamingpenguin.jewel.cli.ArgumentValidationException;
import uk.co.flamingpenguin.jewel.cli.CliFactory;
import battlechallenge.bot.ClientPlayer;
import battlechallenge.bot.DavidBot;
import battlechallenge.bot.DavidBot2;
import battlechallenge.bot.KevinBot;

/**
 * The Class BattleshipClient.
 */
public class BattleshipClient {
	
	/**
	 * Bot to play. Allows user to develop multiple AI bots.
	 *
	 * @param botName the bot name
	 * @return the client player
	 */
	public static ClientPlayer botToPlay(String botName) {
		if (botName.equals("StarterBot")) {
			return new DavidBot(botName, 0, 0, 0);
		}
		if (botName.equals("David")) {
			return new DavidBot(botName, 0, 0, 0);
		}
		if (botName.equals("DavidBot2")) {
			return new DavidBot2(botName, 0, 0, 0);
		}
		else if (botName.equals("KevinBot")) {
			return new KevinBot(botName, 0, 0, 0);
		}
		else {
			return new DavidBot(botName, 0, 0, 0);
		}
	}
	
	
	/**
	 * The main method.
	 *
	 * Example
	 * 		./runClient 'Name' 3000, '127.0.0.1' 'BotName'
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		ClientRun runParams = null;
		String name;
		ClientPlayer bot;
		int port;
		String ip;
		try {
			runParams = CliFactory.parseArguments(ClientRun.class, args);
			name = runParams.getPlayerName();
			if (name == null)
				name = "player" + ((int)(Math.random()*Integer.MAX_VALUE));
			bot = botToPlay(runParams.getBotName());
			port = runParams.getPort();
			ip = runParams.getIP();
			System.out.println("Starting: " + name + "@"+ip+":"+port + " with bot " + bot.getClass().toString());
			new ServerConnection(port, ip, name, bot);
		} catch (ArgumentValidationException e1) {
			System.out.println(e1.getMessage());
			System.out.println("Command line arguments exception. Please try again.");
		}
	}
}
