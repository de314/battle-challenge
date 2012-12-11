
package battlechallenge.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Map.Entry;

import com.google.gdata.util.ServiceException;

import battlechallenge.ActionResult;
import battlechallenge.ActionResult.ShotResult;
import battlechallenge.CommunicationConstants;
import battlechallenge.Coordinate;
import battlechallenge.ShipAction;
import battlechallenge.maps.BattleMap;
import battlechallenge.maps.MapImporter;
import battlechallenge.settings.Config;
import battlechallenge.ship.Ship;
import battlechallenge.ship.ShipCollection;
import battlechallenge.structures.Barrier;
import battlechallenge.structures.Base;
import battlechallenge.structures.City;
import battlechallenge.structures.Structure;
import battlechallenge.visual.BCViz;
import battlechallenge.visual.GenerateVideo;
import battlechallenge.visual.YoutubeUploader;

/**
 * The Class Game.
 */
public class Game extends Thread {
	
	public static final boolean DEBUG = true;
	
	public static final int DEFAULT_WIDTH;
	public static final int DEFAULT_HEIGHT;
	public static final int DEFAULT_SPEED;
	public static final int MAX_NUM_TURNS;
	public static final String WEBSITE_URL;
	
	static {
		DEFAULT_WIDTH = 15;
		DEFAULT_HEIGHT = 15;
		DEFAULT_SPEED = 1000; // number of milliseconds to sleep between turns
		MAX_NUM_TURNS = 300; //300; // 5 minutes if 1 turn per second
		WEBSITE_URL = "http://autonomousarmada.azurewebsites.net/Match/Record";
	}
	
	/** Game State**/
	private GameState prevGameState;
	
	/** The players. */
	private Map<Integer, ServerPlayer> players;
	
	private GameManager manager;
	
	private BCViz viz;
	
	private BattleMap map;
	
	private ShipCollection ships;
	
	private int minsPerShip = 10; // TODO: Make variable per game
	
	private String videoURL;
	
	/**
	 * Used to check how many players are in the game
	 * @return The number of players in the game
	 */
	public int numberOfPlayers() {
		return players.size();
	}
	
	/**
	 * Instantiates a new game.
	 *  
	 * @param players the player
	 */
	public Game(List<ServerPlayer> players, GameManager manager) {
		this(players, DEFAULT_WIDTH, DEFAULT_HEIGHT, manager);
	}
	
	/**
	 * Constructor for a game that requires at least two players
	 * @param players The players that make up the game
	 * @param height The height of the game board
	 * @param width  The width of the game board
	 * @param manager The GameManager that creates the game
	 */
	public Game(List<ServerPlayer> players, int height, int width, GameManager manager) {
		if (players == null || players.size() < 2)
			throw new IllegalArgumentException();
		// TODO: ensure no null entries in list
		this.players = new HashMap<Integer, ServerPlayer>();
		for (ServerPlayer p : players)
			this.players.put(p.getId(), p);
		this.manager = manager;
		ships = new ShipCollection();
		for (ServerPlayer p : players) {
			ships.addPlayer(p);
			p.setShipCollection(ships);
		}
		
		// DEBUG INFO
		StringBuilder sb = new StringBuilder("Starting new Game with players { ");
		for(ServerPlayer p : players)
			sb.append(p.getId()).append(", ");
		sb.delete(sb.length()-2, sb.length()).append(" }");
		System.out.println(sb.toString());
		
		this.start();
	}
	
	
	/**
	 * Thread that runs a game
	 */
	public void run() {
		// FIXME: Do not allow other players to be added.
		/*
		 * 			[[ INITIALIZE GAME ]]
		 */
		map = MapImporter.getMap(); // get default map
		setPlayerCredentials();
		map.assignPlayersToBases(players.values(), getInitialShips()); // set bases
		setGameConstants();
		viz = new BCViz(players.values(), map.getStructures(), map.getNumCols(), map.getNumRows());
		
		//record the initial game settings into the transcript file
		recordGameSettings();
		
		/*
		 * 			[[ PLAY GAME ]]
		 */
		Map<Integer, List<ActionResult>> actionResults = new HashMap<Integer, List<ActionResult>>();
		for(ServerPlayer p : players.values())
			actionResults.put(p.getId(), new LinkedList<ActionResult>());
		int livePlayers = 0;
		for (ServerPlayer player: players.values()) {
			if (player.isAlive()) {
				livePlayers++;
			}
		}
		for (int turnCount = 0; turnCount<MAX_NUM_TURNS && livePlayers > 1;turnCount++) {
			doTurn(actionResults, turnCount);
			viz.updateGraphics(turnCount);
			livePlayers = 0;
			for (ServerPlayer player: players.values()) {
				if (player.isAlive()) {
					livePlayers++;
				}
			}
			recordGameChange(turnCount);
		}
		
		/*
		 * 			[[ END GAME ]]
		 */
		
		recordGameEnd();
		endGameGenerateResults();
		manager.removeGame(this);
	}
	
	private void setPlayerCredentials() {
		for(ServerPlayer p : players.values()) {
			if (p.setCredentials(map.getNumCols(), map.getNumRows()))
				System.out.println("Set player credentials: " + p.toString());
			else {
				// TODO: remove player and pause game if necessary
			}
		}
	}
	
	public void setGameConstants() {
		for(ServerPlayer p : players.values()) {
			p.setMinsPerShip(minsPerShip);
		}
	}
	
	
	/**
	 * Will sink all ships that are overlapping after moving
	 */
	public void handleCollisions() {
		Map<Coordinate, Ship> allShipCoords = new HashMap<Coordinate, Ship>(); // Stores coords of all players ships
		HashSet<Ship> shipsToSink = new HashSet<Ship>();
		for(ServerPlayer p : players.values()) {
			for (Ship s: p.getUnsunkenShips(p)) {
					Coordinate coord = s.getLocation();
					if (allShipCoords.get(coord) != null) {
						shipsToSink.add(allShipCoords.get(coord)); // sink ship that had coordinates in allShipCoords
						shipsToSink.add(s); // sink ship colliding with another ship
					}
					allShipCoords.put(coord, s);
				}
			}
		for (Ship s: shipsToSink) {
			s.setHealth(0); // Sink ship as it has collided with another ship
		}
	}

	
	/**
	 * 
	 * @param actionResults The results of last turns actions
	 */
	private void doTurn(Map<Integer, List<ActionResult>> actionResults, int turnCount) {
		if (DEBUG) {


			// DEGBUG INFO: print each users guess
			/*
			StringBuilder sb = new StringBuilder();
			for (Entry<Integer, List<ActionResult>> e : actionResults.entrySet()) {
				sb.append("\t{ ").append(e.getKey()).append(" ");
				for (ActionResult a : e.getValue())
					sb.append(a.getResult().toString()).append(" ");
				sb.append(" }\n");
			}
			System.out.println(sb.toString());
			*/


		}
		// hash map of ships to  reveal entire map
		// TODO: alter for fog of war
		Map<Integer, List<Ship>> allPlayersShips = new HashMap<Integer, List<Ship>>();
		for (ServerPlayer p : players.values()) {
			allPlayersShips.put(p.getId(), p.getUnsunkenShips(p)); // List of all players ships to send to each player	
		}
		for(ServerPlayer p : players.values()) {
			if (!p.isAlive()) // Player is dead, don't request their turn
				continue;
			if (!p.requestTurn(allPlayersShips, actionResults, map, turnCount)) {
				// TODO: handle lost socket connection
			}
		}
		try {
			// FIXME: Change speed depending on how fast players return results from calls
			// to their doTurn method. Or game specific value (slower time, harder game)
			Thread.sleep(DEFAULT_SPEED);
		} catch (InterruptedException e) {
			// TODO: handle thread failure
		}
		// Get all player actions and save them into a hash map by network id
		// This is where all the ships are moved.
		Map<Integer, List<ShipAction>> playerActions = new HashMap<Integer, List<ShipAction>>();
		for(ServerPlayer p : players.values()) {
			if (!p.isAlive()) // Player is dead no need to get actions when they cannot act
				continue;
			// shot coordinates and moves ships
			List<ShipAction> shipActions = p.getTurn(map.getNumCols(), map.getNumRows(), map.getStructures(), turnCount);
			// reset action results for current user
			actionResults.get(p.getId()).clear();
			playerActions.put(p.getId(), shipActions);
		}
		handleCollisions(); // if ships are overlapping after moving
		// Now that all ships are moved, evaluate shots for each player
		Map<String, String> shipIDMap = new HashMap<String, String>(); // keep track of ships already given actions
		for(ServerPlayer p : players.values()) {
			if (playerActions.get(p.getId()) == null) // Player is dead no need to get actions when they cannot act
				continue;
			for(ShipAction sa : playerActions.get(p.getId())) {
				// NOTE: moves are processed in: ServerPlayer.getTurn(...);
				// check if shot is within game boundaries
				if (sa.getShipIdentifier() == null) { // Player passed in null shipIdentifier
					continue;
				}
				// Prevent same ship from getting multiple move commands
				if (shipIDMap.get(sa.getShipIdentifier().toString()) != null) {
					continue;
				}
				
				shipIDMap.put(sa.getShipIdentifier().toString(), sa.getShipIdentifier().toString());
				
				Ship s = ships.getShip(sa.getShipIdentifier());
				if (s == null || sa.getShotCoordList().isEmpty()) // Handle null ships and empty shot list
					continue;
				for (int k=0;k<s.getNumShots() && k<sa.getShotCoordList().size(); k++) {
					Coordinate c = sa.getShotCoordList().get(k);
					if (c == null || (s.distanceFromCoord(c) > s.getRange()) || 
							!c.inBoundsInclusive(0, map.getNumRows()-1, 0, map.getNumCols()-1)) {// || 
							//s.isSunken()) {
						// ignore shot out of bounds or invalid shot range
						continue;
					} else {
						ActionResult hit = null;
						ActionResult temp = null;
						// valid shot location received
						for(ServerPlayer opp : players.values()) {
							// add all action results
							// beware of friendly fire				
							temp = (opp.isHit(c, s, s.getDamage()));
							if (temp.getResult() == ShotResult.HIT)
								hit = temp;
							else if (temp.getResult() == ShotResult.SUNK) {
								hit = temp;
								if (opp != p) {
									p.recordSunkenShip();
								}
							}
						}
						actionResults.get(p.getId()).add(hit == null ? temp : hit);
					}
				}
			}
		}
		updateCities();
		allocateIncome();
		ships.removeSunkenShips();
		ships.updateCoordinates();
		spawnShips();
		ships.updateCoordinates();
	}
	
	/**
	 * Adds the player.
	 *
	 * @param player the player to add to the game
	 * @return true, if player is successfully added to the game
	 */
	public boolean addPlayer(ServerPlayer player) {
		// FIXME: handle null players
		// TODO: how to add players
		return false;
	}
	
	/**
	 * Gets the winner by checking each player to see if they
	 * have any ships left
	 *
	 * @return the winner who is the only player with at least one ship left unsunk.
	 */
	public List<ServerPlayer> getWinner() {
		List<ServerPlayer> maxPlayerList = new LinkedList<ServerPlayer>();
		PriorityQueue<ServerPlayer> playerList = new PriorityQueue<ServerPlayer>(8, comp);

		
		int maxScore = -1;

		for (ServerPlayer p : players.values()) {
			playerList.offer(p); // Sorted by player Score
		}
		
		int currRank = 1;
		for (ServerPlayer p: playerList) { // assign player ranks
			if (p.getScore() < maxScore) {
				currRank++;
			}
			else {
				maxScore = p.getScore();
			}
			p.setEndGameRank(currRank);
			maxPlayerList.add(p); 
		}
		return maxPlayerList; // TODO: Change to return a priority queue
	}
	
	public void allocateIncome() {
		Map<Integer,Integer> incomes = new HashMap<Integer,Integer>();
		for(Integer id : players.keySet())
			incomes.put(id, 0);
		for (Structure str: map.getStructures()) {
			// there are no bases in map.getStructures
			if (str instanceof City) {
				City city = (City)str;
				int ownerId = city.getOwnerId();
				if (ownerId >= 0) { // all non-neutral cities
					// increment players minerals by the amount the city generates
					incomes.put(ownerId, incomes.get(ownerId) + city.getMineralGenerationSpeed());
				}
			}
		}
		for (Entry<Integer,Integer> e : incomes.entrySet())
			players.get(e.getKey()).incrementMinerals(e.getValue());
	}
	
	/**
	 * Updates cities owners to players that have ships in the cities
	 */
	public void updateCities() {
		Map<Coordinate, Ship> allShipCoords = new HashMap<Coordinate, Ship>(); // Stores coords of all players ships
		Ship ship;
		for(ServerPlayer p : players.values()) {
			for (Ship s: p.getUnsunkenShips(p)) {
					Coordinate coord = s.getLocation();
					allShipCoords.put(coord, s);
				}
		}
		for (Structure str: map.getStructures()) {
			// there are no bases in map.getStructures
			if (str instanceof City) {
				City city = (City)str;
				ship = allShipCoords.get(city.getLocation());
				if (ship != null) // There is a ship on the city
					city.setOwner(ship.getPlayerId());
				else {
					city.setOwner(-1); // Neutral City
				}
			}
		}
		
	}
	
	public void spawnShips() {
		for (ServerPlayer player: players.values()) {
			player.spawnShip();
		}
	}
	
	public List<Ship> getInitialShips() {
		List<Ship> ships = new LinkedList<Ship>();
		ships.add(new Ship());
		return ships;
	}
	
	
	static Comparator<ServerPlayer> comp = new Comparator<ServerPlayer>() { // frames are created with IDs in order, sort them this way
		  public int compare(ServerPlayer p1, ServerPlayer p2) {
			Integer p1Score = p1.getScore();
			Integer p2Score = p2.getScore();
		    return p2Score.compareTo(p1Score);
		  }
	};
	
	public void recordGameSettings(){
		BattleMap bm = map;
		String a = "BattleMap\n";
		a = a.concat(bm.getNumRows() + "," + bm.getNumCols() + "\n");
		
		a=a.concat("Bases\n");
		for(Base b : map.getBases()){
			a = a.concat("player " + b.getOwnerId() + ": " + b.getLocation().getRow() + "," + b.getLocation().getCol() + "\n");
		}
		a = a.concat("Cities\n");
		for(Structure c : map.getStructures()){
			if(c instanceof City )
				a = a.concat(c.getLocation().getRow() + "," + c.getLocation().getCol() + "\n");
		}
		a = a.concat("Barriers\n");
		int barriers = 0;
		for(Structure b : map.getStructures()){
			if(b instanceof Barrier){
				a = a.concat(b.getLocation().getRow() + "," + b.getLocation().getCol() + "\n");
				barriers++;
			}
		}
		if(barriers == 0){a = a.concat("there are no barriers!\n");}
		
//		make the game state and set it
		HashMap<String, List<BoardLocation>> gplayerShipLocations = new HashMap<String, List<BoardLocation>>(); 
		HashMap<String, int[]> gplayerScoreMinerals = new HashMap<String, int[]>();
		for (ServerPlayer p : players.values()) {
			List<Ship> ships = p.getUnsunkenShips(p);
			List<BoardLocation> locations = new ArrayList<BoardLocation>();
			for(Ship s : ships){locations.add(new BoardLocation(s.getLocation()));}
			gplayerShipLocations.put(String.valueOf(p.getId()), locations); // List of all players ships to send to each player	
		}
		
		for (ServerPlayer p : players.values()) {
			int[] scoreMineral = new int[2];
			scoreMineral[0] = p.getScore();
			scoreMineral[1] = p.getMinerals();
			gplayerScoreMinerals.put(String.valueOf(p.getId()), scoreMineral); // List of all players ships to send to each player	
		}
		
		GameState gs = new GameState(gplayerShipLocations,gplayerScoreMinerals);
//		now call get game state string
		initializeTranscriptFile();
		writeTranscriptFile(a);
		writeTranscriptFile("Initial Game State:\n" + gs.toString());
		this.prevGameState = gs;
	}
	
	/**
	 * Each turn, call this. At the end, set the new created game state to be prevGameState
	 */
	public void recordGameChange(int turn){
		HashMap<String, List<BoardLocation>> gplayerShipLocations = new HashMap<String, List<BoardLocation>>(); 
		HashMap<String, int[]> gplayerScoreMinerals = new HashMap<String, int[]>();
		for (ServerPlayer p : players.values()) {
			List<Ship> ships = p.getUnsunkenShips(p);
			List<BoardLocation> locations = new ArrayList<BoardLocation>();
			for(Ship s : ships){locations.add(new BoardLocation(s.getLocation()));}
			gplayerShipLocations.put(String.valueOf(p.getId()), locations); // List of all players ships to send to each player	
		}
		
		for (ServerPlayer p : players.values()) {
			int[] scoreMineral = new int[2];
			scoreMineral[0] = p.getScore();
			scoreMineral[1] = p.getMinerals();
			gplayerScoreMinerals.put(String.valueOf(p.getId()), scoreMineral); // List of all players ships to send to each player	
		}
		
		GameState gs = new GameState(gplayerShipLocations,gplayerScoreMinerals);
		try {
			String diff = gs.getDifferenceToGameState(prevGameState);
			writeTranscriptFile("t:" + turn + "\n" + diff);
			prevGameState = gs;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(prevGameState);
			System.out.println(gs);
		}
	}
	
	public void recordGameEnd(){
		HashMap<String, List<BoardLocation>> gplayerShipLocations = new HashMap<String, List<BoardLocation>>(); 
		HashMap<String, int[]> gplayerScoreMinerals = new HashMap<String, int[]>();
		for (ServerPlayer p : players.values()) {
			List<Ship> ships = p.getUnsunkenShips(p);
			List<BoardLocation> locations = new ArrayList<BoardLocation>();
			for(Ship s : ships){locations.add(new BoardLocation(s.getLocation()));}
			gplayerShipLocations.put(String.valueOf(p.getId()), locations); // List of all players ships to send to each player	
		}
		for (ServerPlayer p : players.values()) {
			int[] scoreMineral = new int[2];
			scoreMineral[0] = p.getScore();
			scoreMineral[1] = p.getMinerals();
			gplayerScoreMinerals.put(String.valueOf(p.getId()), scoreMineral); // List of all players ships to send to each player	
		}
		
		GameState gs = new GameState(gplayerShipLocations,gplayerScoreMinerals);
		writeTranscriptFile("Final Game State:\n" + gs.toString());
	}
	
	public void writeTranscriptFile(String s){
		try {
			String currentDir = System.getProperty("user.dir");
			//System.out.println(currentDir);
			File file = new File(currentDir + "/transcript.txt");
			// if file doesnt exists, then create it
			if (!file.exists()) {
				System.out.println(currentDir + "/transcript.txt created!");
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(s);
			bw.close();
		} catch (IOException e) {
			System.out.println("not writting!");
			e.printStackTrace();
		}
	}
	
	public void initializeTranscriptFile(){
		try {
			String currentDir = System.getProperty("user.dir");
			File file = new File(currentDir + "/transcript.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("");
			bw.close();
		} catch (IOException e) {
			System.out.println("not writting!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Will generate the youtube video and send a query with the results of the game to the web server
	 */
	public void endGameGenerateResults() {
		List<ServerPlayer> winnerList = getWinner();
		List<String> rankList = new ArrayList<String>();

		String charset = "UTF-8";
		String param = "";
		String query = "";
		Boolean draw = false;
			
		if (winnerList != null) { 
			// Generate video because the game returned a winner implying a successful game
			GenerateVideo video = new GenerateVideo(viz.getCurrentFolderName());
			try {
				if (video.genVideo()) { // if video was generated successfully
					YoutubeUploader uploader = new YoutubeUploader(viz.getCurrentFolderName() + Config.sep + viz.getiExp().getTimeStamp() + ".mp4");
					uploader.uploadVideo(); 
					videoURL = uploader.getVideoURL();
					System.out.println(videoURL);
					query += "link=" + videoURL + "&"; // add video link query string
					viz.getiExp().deleteCurrDir(); // delete the game directory with screen shots of frames to make video;
				}
				else {
					System.out.println("Video was unable to be created");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			int count = 0;
			//Contestant.1=first place player name
			for (ServerPlayer p: winnerList) { // store params as "Contestant.#=playerName"
				rankList.add("Rank: " + (p.getEndGameRank()-1) + " = " + p.getName());
				param = "Contestant[" + (p.getEndGameRank()-1) + "]=" + p.getName();	
				query += param;
				if (count < winnerList.size() -1) {
					query += "&"; // params must be seperated by '&'
				}
				count++;
			}
			
			query += "&mapName=" + map.getName();
			
			URLConnection connection = null;
			
			try {
				connection = new URL(WEBSITE_URL).openConnection();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
			OutputStream output = null;
			try {
			     output = connection.getOutputStream();
			     output.write(query.getBytes(charset));
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			     if (output != null) try { output.close(); } catch (IOException logOrIgnore) {
			    	 System.out.println("error on output");
			     }
			}
			InputStream response = null;
			try {
				response = connection.getInputStream();
			} catch (IOException e1) {
			}
		}
		// Prints the final game rankings
		for (String s: rankList) {
			System.out.println(s);
		}
		
		for(ServerPlayer p : winnerList) {
			if (draw == true) {
				p.endGame(CommunicationConstants.RESULT_DRAW);
				continue;
			}
			p.endGame(CommunicationConstants.RESULT_RANKED + "You finished ranked: " + p.getEndGameRank() + "/" + winnerList.size()
					+ " Video url: " + videoURL);			
		}
	}
}
