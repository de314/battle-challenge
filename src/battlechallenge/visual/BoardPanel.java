package battlechallenge.visual;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.List;

import battlechallenge.server.ServerPlayer;
import battlechallenge.ship.Ship;

public class BoardPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Color OCEAN = Color.cyan;
	public static final Color MISS = Color.red;
	public static final Color HIT = Color.green;
	public static final Color SUNKEN = Color.lightGray;
	public static final Color SHIP_BORDER = Color.black;

	// TODO: need 8 player colors
	public static final Color[] PLAYER_COLORS = new Color[] { Color.blue,
			Color.orange, Color.yellow, Color.magenta, Color.white, Color.pink };

	private int heightPx;
	private int widthPx;
	private int heightSpaces;
	private int widthSpaces;
	private int colPx;
	private int rowPx;
	private List<ServerPlayer> players;

	public int getRowPx(int row) {
		return row * rowPx;
	}

	public int getColPx(int col) {
		return col * colPx;
	}

	public int getRowPx() {
		return rowPx;
	}

	public int getColPx() {
		return colPx;
	}

	private void updateSizeVariables() {
		this.widthPx = this.getWidth();
		this.heightPx = this.getHeight();
		colPx = this.widthPx / this.widthSpaces;
		rowPx = this.heightPx / this.heightSpaces;
	}

	public BoardPanel(int widthSpaces, int heightSpaces,
			List<ServerPlayer> players) {
		this.widthSpaces = widthSpaces;
		this.heightSpaces = heightSpaces;
		this.players = players;
	}

	@Override
	public void paint(Graphics g) {
		updateSizeVariables();
		g.setColor(OCEAN);
		g.fillRect(0, 0, widthPx, heightPx);
		List<List<Ship>> shipsCollection = new ArrayList<List<Ship>>();
		for (ServerPlayer p : players)
			shipsCollection.add(p.getShipsCopy());
		// draw sunken ships
		for (List<Ship> ships : shipsCollection)
			ShipPainter.paintSunkenShips(this, ships);
		// draw ships with health
		for (int i = 0; i < players.size(); i++)
			ShipPainter.paintShips(this, shipsCollection.get(i),
					PLAYER_COLORS[i]);
		// draw shots
		for (ServerPlayer p : players)
			ActionPainter.paintResults(this, p.getLastActionResults());
	}
}
