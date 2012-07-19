package battlechallenge.client;

import uk.co.flamingpenguin.jewel.cli.Option;

public interface ClientRun {

	@Option(helpRequest = true)
	boolean getHelp();

	@Option(shortName = "n", defaultToNull = true)
	public String getPlayerName();

	@Option(shortName = "p", defaultValue = "3000")
	public int getPort();

	@Option(shortName = "i", defaultValue = "127.0.0.1")
	public String getIP();

	@Option(shortName = "b", defaultValue = "StarterBot")
	public String getBotName();
}
