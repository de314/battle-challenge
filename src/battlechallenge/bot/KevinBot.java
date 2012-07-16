package battlechallenge.bot;

import java.util.List;
import java.util.Map;

import battlechallenge.ActionResult;
import battlechallenge.ShipAction;
import battlechallenge.ship.Ship;



/**
 * The Class ClientPlayer.
 */
public class KevinBot extends ClientPlayer {
	
	/**
	 * Instantiates a new client player.
	 *
	 * @param playerName the player name
	 * @param mapWidth the map width
	 * @param boardHeight the map height
	 */
	public KevinBot(final String playerName, final int mapWidth, final int boardHeight, final int networkID) {
		super(playerName, mapWidth, boardHeight, networkID);
	}

	public List<ShipAction> doTurn(Map<Integer, List<Ship>> ships, Map<Integer, List<ActionResult>> actionResults) {
		return null;
	}
}
