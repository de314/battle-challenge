package battlechallenge.visual;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;

import battlechallenge.server.ServerPlayer;
import battlechallenge.ship.Ship;
import battlechallenge.structures.Structure;

public class BoardPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Color OCEAN = Color.cyan;
	public static final Color SHOT = Color.red;
	public static final Color SHIP_BORDER = Color.black;

	// TODO: need 8 player colors
	public static final Color[] PLAYER_COLORS = new Color[] { Color.blue,
			Color.orange, Color.yellow, Color.magenta, Color.white, Color.pink };

	private int heightPx;
	private int widthPx;
	private int heightSpaces;
	private int widthSpaces;
	private double colPx;
	private double rowPx;
	private Collection<ServerPlayer> players;
	private List<Structure> structures;
	BufferedImage grid;
	
	public int getRowPx(int row) {
		return (int)(row * rowPx);
	}

	public int getColPx(int col) {
		return (int)(col * colPx);
	}

	public int getRowPx() {
		return (int)rowPx;
	}

	public int getColPx() {
		return (int)colPx;
	}

	public BoardPanel(int widthSpaces, int heightSpaces,
			Collection<ServerPlayer> players,List<Structure> structures) {
		this.widthSpaces = widthSpaces;
		this.heightSpaces = heightSpaces;
		this.players = players;
		this.structures = structures;
	}

	private void updateSizeVariables() {
		this.widthPx = this.getWidth();
		this.heightPx = this.getHeight();
		colPx = (double)this.widthPx / (double)this.widthSpaces;
		rowPx = (double)this.heightPx / (double)this.heightSpaces;
	}
	@Override
	public Graphics getGraphics() {
		Graphics2D gc = grid.createGraphics();
		return gc;
	}

	private void drawGrid(Graphics g) {
		g.setColor(OCEAN);
		g.fillRect(0, 0, widthPx, heightPx);
//		 draw ocean grids
		g.setColor(Color.black);
		for (int i = 1; i <= this.widthSpaces; i++)
			g.drawLine((int)(i * colPx), 0, (int)(i * colPx), heightPx);
		for (int i = 1; i < this.heightSpaces; i++)
			g.drawLine(0, (int)(i * rowPx), widthPx, (int)(i * rowPx));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);       // paint background
        Graphics2D g2 = (Graphics2D)g; // we need a Graphics2D context
		List<List<Ship>> shipsCollection = new ArrayList<List<Ship>>();
		for (ServerPlayer p : players)
			shipsCollection.add(p.getShipsCopy());
		updateSizeVariables();


		grid = (BufferedImage)(this.createImage(this.widthPx ,this.heightPx));
		Graphics2D gc = grid.createGraphics();
		drawGrid(gc);

//		 draw structures
		int colorIndex = 0;
		for (ServerPlayer p : players)
			StructurePainter.paintBase(this, p.getBase(),
					PLAYER_COLORS[colorIndex++]);
		StructurePainter.paintStructures(this, structures);
		// draw ships
		for (int i = 0; i < players.size(); i++) 
			ShipPainter.paintShips(this, shipsCollection.get(i),
					PLAYER_COLORS[i]);
		// draw shots
		
		for (ServerPlayer p : players)
			ActionPainter.paintResults(this, p.getLastActionResults());
				
		g2.drawImage(grid, null, 0, 0);
	}
}
