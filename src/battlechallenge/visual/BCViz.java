package battlechallenge.visual;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JFrame;

import battlechallenge.ActionResult;
import battlechallenge.ActionResult.ShotResult;
import battlechallenge.server.ServerPlayer;
import battlechallenge.ship.Ship;

public class BCViz extends JFrame {

	public static final int DEFAULT_HEIGHT_PX = 400;
	public static final int DEFAULT_WIDTH_PX = 400;
	public static final int DEFAULT_ROW_OFFSET_PX = 30;
	public static final int DEFAULT_COL_OFFSET_PX = 0;
	public final int TOTAL_HEIGHT_PX;
	public final int TOTAL_WIDTH_PX;

	private ServerPlayer p1;
	private ServerPlayer p2;
	private final int totalWidth;
	private final int totalHeight;

	public BCViz(ServerPlayer p1, ServerPlayer p2, int boardWidth,
			int boardHeight) {
		super("Battle Challenge Viz 0.1");
		this.p1 = p1;
		this.p2 = p2;
		this.totalWidth = boardWidth;
		this.totalHeight = boardHeight;
		this.TOTAL_WIDTH_PX = DEFAULT_WIDTH_PX * 2;
		this.TOTAL_HEIGHT_PX = DEFAULT_HEIGHT_PX;

		this.setSize(DEFAULT_WIDTH_PX * 2, DEFAULT_HEIGHT_PX);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(true);
		System.out.println(boardWidth + " : " + boardHeight);
		System.out.println(totalWidth + " : " + totalHeight);
		System.out.println(TOTAL_WIDTH_PX + " : " + TOTAL_HEIGHT_PX);
	}

	public void updateGraphics() {
		this.repaint();
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(DEFAULT_COL_OFFSET_PX + 0, DEFAULT_ROW_OFFSET_PX + 0, TOTAL_WIDTH_PX,
				TOTAL_HEIGHT_PX);
		synchronized (p1) {
			synchronized (p2) {
				addSunkenShips(g, p1.getShips());
				addSunkenShips(g, p2.getShips());
				addShips(g, p1.getShips(), Color.BLUE);
				addShips(g, p2.getShips(), Color.ORANGE);
				addActionResults(g, p1.getLastActionResultsCopy());
				addActionResults(g, p2.getLastActionResultsCopy());
			}
		}
	}

	private void addSunkenShips(Graphics g, List<Ship> ships) {
		int colPix = TOTAL_WIDTH_PX / totalWidth;
		int rowPix = TOTAL_HEIGHT_PX / totalHeight;
		for (Ship s : ships) {
			for (String c : s.getCoordinateStrings()) {
				String[] arr = c.split(",");
				int row = Integer.parseInt(arr[0]);
				int col = Integer.parseInt(arr[1]);
				if (s.isSunken())
					g.setColor(Color.lightGray);
				else
					continue;
				g.fillRect(DEFAULT_COL_OFFSET_PX + col * colPix,
						DEFAULT_ROW_OFFSET_PX + row * rowPix, colPix, rowPix);
			}
		}
	}

	private void addShips(Graphics g, List<Ship> ships, Color teamColor) {
		int colPix = TOTAL_WIDTH_PX / totalWidth;
		int rowPix = TOTAL_HEIGHT_PX / totalHeight;
		for (Ship s : ships) {
			for (String c : s.getCoordinateStrings()) {
				String[] arr = c.split(",");
				int row = Integer.parseInt(arr[0]);
				int col = Integer.parseInt(arr[1]);
				if (s.isSunken())
					continue;
				else
					g.setColor(teamColor);
				g.fillRect(DEFAULT_COL_OFFSET_PX + col * colPix,
						DEFAULT_ROW_OFFSET_PX + row * rowPix, colPix, rowPix);
			}
		}
	}

	private void addActionResults(Graphics g, List<ActionResult> ar) {
		int colPix = TOTAL_WIDTH_PX / totalWidth;
		int rowPix = TOTAL_HEIGHT_PX / totalHeight;
		for (ActionResult a : ar) {
			if (a.getResult() == ShotResult.HIT
					|| a.getResult() == ShotResult.SUNK)
				g.setColor(Color.green);
			else
				g.setColor(Color.red);
			g.fillRect(
					DEFAULT_COL_OFFSET_PX + a.getCoordinate().getCol() * colPix,
					DEFAULT_ROW_OFFSET_PX + a.getCoordinate().getRow() * rowPix,
					colPix, rowPix);
		}
	}
}
