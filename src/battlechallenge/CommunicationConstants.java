package battlechallenge;

import java.util.HashSet;
import java.util.Set;

public class CommunicationConstants {

	/** The SERVE r_ version. */
	public static String SERVER_VERSION;
	
	/** The SERVE r_ version. */
	public static String CLIENT_VERSION;

	/** The SUPPORTE d_ clients. */
	public static Set<String> SUPPORTED_CLIENTS;
	
	/** The SUPPORTE d_ clients. */
	public static Set<String> SUPPORTED_SERVERS;

	/** The Constant SERVER_REQUEST_NAME. */
	public static final String REQUEST_HANDSHAKE;

	/** The Constant SERVER_REQUEST_NAME. */
	public static final String REQUEST_CREDENTIALS;

	/** The Constant SERVER_REQUEST_PLACE_SHIPS. */
	public static final String REQUEST_PLACE_SHIPS;

	/** The Constant SERVER_REQUEST_TURN. */
	public static final String REQUEST_DO_TURN;

	/** The Constant SERVER_RESULT_DISQUALIFIED. */
	public static final String RESULT_DISQUALIFIED;

	/** The Constant SERVER_RESULT_WINNER. */
	public static final String RESULT_WIN;

	/** The Constant SERVER_RESULT_LOSER. */
	public static final String RESULT_LOSE;
	
	/** The Constant SERVER_RESULT_DRAW. */
	public static final String RESULT_DRAW;
	
	/** The Constant SERVER_RESULT_RANKED. */
	public static final String RESULT_RANKED;
	
	public static final int SOCKET_WAIT_TIME;

	static {
		// Server Credentials
		SERVER_VERSION = "battleship_S-v0.1";
		SUPPORTED_CLIENTS = new HashSet<String>();
		SUPPORTED_CLIENTS.add("battleship_C-v0.1");
		// Client Credentials
		CLIENT_VERSION = "battleship_C-v0.1";
		SUPPORTED_SERVERS = new HashSet<String>();
		SUPPORTED_SERVERS.add("battleship_S-v0.1");
		// Requests
		REQUEST_HANDSHAKE = "H";
		REQUEST_CREDENTIALS = "C";
		REQUEST_PLACE_SHIPS = "P";
		REQUEST_DO_TURN = "T";
		// Results
		RESULT_DISQUALIFIED = "Q";
		RESULT_WIN = "W";
		RESULT_LOSE = "L";
		RESULT_DRAW = "D";
		RESULT_RANKED = "R";
		SOCKET_WAIT_TIME = 1000;
	}
	
	public CommunicationConstants() {
		throw new RuntimeException("Class cannot be instanciated");
	}
	
}
