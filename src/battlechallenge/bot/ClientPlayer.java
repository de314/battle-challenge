package battlechallenge.bot;

import java.util.List;

import battlechallenge.ShipAction;

/**
 * The Class ClientPlayer.
 */
public class ClientPlayer {

	/** The minerals. */
	protected int minerals;
	
	/** The player name. */
	protected final String playerName;
	
	/** The id to identify the player. */
	protected int networkID;

	/** The board width. */
	protected int boardWidth;

	/** The board height. */
	protected int boardHeight;
	
	/**
	 * Gets the minerals.
	 *
	 * @return the minerals
	 */
	public int getMinerals() {
		return minerals;
	}

	/**
	 * Sets the minerals.
	 *
	 * @param minerals the new minerals
	 */
	public void setMinerals(int minerals) {
		this.minerals = minerals;
	}

	/**
	 * Save the width of the game board.
	 *
	 * @param boardWidth the width of the game board
	 */
	public void setBoardWidth(int boardWidth) {
		this.boardWidth = boardWidth;
	}
	
	/**
	 * Save the height of the game board.
	 *
	 * @param boardHeight the height of the game board
	 */
	public void setBoardHeight(int boardHeight) {
		this.boardHeight = boardHeight;
	}
	
	/**
	 * Sets the network id.
	 *
	 * @param networkID the new network id
	 */
	public void setNetworkID(int networkID) {
		this.networkID = networkID;
	}
	
	/**
	 * Instantiates a new client player.
	 *
	 * @param playerName the player name
	 * @param mapWidth the map width
	 * @param mapHeight the map height
	 * @param networkID the network id
	 */
	public ClientPlayer(final String playerName, final int mapWidth, final int mapHeight, final int networkID) {
		this.playerName = playerName;
		this.boardWidth = mapWidth;
		this.boardHeight = mapHeight;
		this.networkID = networkID;
	}
	
	/**
	 * This class will be filled in by the player. All logic regarding in game decisions to be
	 * made by your bot should be put in here. This class will be called every turn until the
	 * end of the game
	 *
	 * @return a List of coordinates corresponding to where you wish to fire
	 */
	public List<ShipAction> doTurn() {
		return null;
	}
}

