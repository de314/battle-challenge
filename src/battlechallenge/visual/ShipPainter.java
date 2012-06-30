package battlechallenge.visual;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import battlechallenge.ship.Ship;

public class ShipPainter {

	public static final int FONT_OFFSET_X = -5;
	public static final int FONT_OFFSET_Y = 5;

	public static void paintShips(BoardPanel bp, List<Ship> ships,
			Color teamColor) {
		for (Ship s : ships)
			paintShip(bp, s, teamColor);
	}

	public static void paintShip(BoardPanel bp, Ship s, Color teamColor) {
		if (s.isSunken())
			return;
		Graphics g = bp.getGraphics();
		// fill in ship color
		g.setColor(teamColor);
		g.fillRect(bp.getColPx(s.getLocation().getCol()),
				bp.getRowPx(s.getLocation().getRow()), bp.getColPx(),
				bp.getRowPx());
		// draw health box
		g.setColor(Color.white);
		int col = s.getLocation().getCol();
		int row = s.getLocation().getRow();
		g.fillRect(bp.getColPx(col + 2), bp.getRowPx(row + 2),
				bp.getColPx() - 4, bp.getRowPx() - 4);
		// write health
		g.setColor(Color.black);
		g.drawString("" + s.getHealth(), bp.getColPx(col) - bp.getColPx() / 2
				+ FONT_OFFSET_X, bp.getRowPx(row) + bp.getRowPx() / 2
				+ FONT_OFFSET_Y);
		// draw boarder last
		// try using outline from above
		// g.setColor(BoardPanel.SHIP_BORDER);
		// g.drawRect(bp.getColPx(s.getLocation().getRow()),
		// bp.getRowPx(minRow),
		// (maxCol - minCol + 1) * bp.getColPx(), (maxRow - minRow + 1)
		// * bp.getRowPx());
	}

	public static void paintSunkenShips(BoardPanel bp, List<Ship> ships) {
		for (Ship s : ships)
			paintSunkenShip(bp, s);
	}

	public static void paintSunkenShip(BoardPanel bp, Ship s) {
		if (!s.isSunken())
			return;
		Graphics g = bp.getGraphics();
		g.setColor(BoardPanel.SUNKEN);
		g.setColor(BoardPanel.SUNKEN);
		g.fillRect(bp.getColPx(s.getLocation().getCol()),
				bp.getRowPx(s.getLocation().getRow()), bp.getColPx(),
				bp.getRowPx());
		g.setColor(BoardPanel.SHIP_BORDER);
		g.drawRect(bp.getColPx(s.getLocation().getCol()),
				bp.getRowPx(s.getLocation().getRow()), bp.getColPx(),
				bp.getRowPx());
	}
}
