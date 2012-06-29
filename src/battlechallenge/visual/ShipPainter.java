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
		int minRow = Integer.MAX_VALUE;
		int minCol = Integer.MAX_VALUE;
		int maxRow = -1;
		int maxCol = -1;
		for (String c : s.getCoordinateStrings()) {
			String[] arr = c.split(",");
			int row = Integer.parseInt(arr[0]);
			int col = Integer.parseInt(arr[1]);
			if (maxCol < col)
				maxCol = col;
			if (minCol > col)
				minCol = col;
			if (maxRow < row)
				maxRow = row;
			if (minRow > row)
				minRow = row;
		}
		// fill in ship color
		g.setColor(teamColor);
		g.fillRect(bp.getColPx(minCol), bp.getRowPx(minRow),
				(maxCol - minCol + 1) * bp.getColPx(), (maxRow - minRow + 1)
						* bp.getRowPx());
		// draw health box
		g.setColor(Color.white);
		int col = s.getCenter().getCol();
		int row = s.getCenter().getRow();
		g.fillRect(bp.getColPx(col - 1), bp.getRowPx(row), bp.getColPx(),
				bp.getRowPx());
		// write health
		g.setColor(Color.black);
		g.drawString("" + s.getHealth(), bp.getColPx(col) - bp.getColPx() / 2 + FONT_OFFSET_X,
				bp.getRowPx(row) + bp.getRowPx() / 2 + FONT_OFFSET_Y);
		// draw boarder last
		g.setColor(BoardPanel.SHIP_BORDER);
		g.drawRect(bp.getColPx(minCol), bp.getRowPx(minRow),
				(maxCol - minCol + 1) * bp.getColPx(), (maxRow - minRow + 1)
						* bp.getRowPx());
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
		int minRow = Integer.MAX_VALUE;
		int minCol = Integer.MAX_VALUE;
		int maxRow = -1;
		int maxCol = -1;
		for (String c : s.getCoordinateStrings()) {
			String[] arr = c.split(",");
			int row = Integer.parseInt(arr[0]);
			int col = Integer.parseInt(arr[1]);
			if (maxCol < col)
				maxCol = col;
			if (minCol > col)
				minCol = col;
			if (maxRow < row)
				maxRow = row;
			if (minRow > row)
				minRow = row;
		}
		g.setColor(BoardPanel.SUNKEN);
		g.fillRect(bp.getColPx(minCol), bp.getRowPx(minRow),
				(maxCol - minCol + 1) * bp.getColPx(), (maxRow - minRow + 1)
						* bp.getRowPx());
		g.setColor(BoardPanel.SHIP_BORDER);
		g.drawRect(bp.getColPx(minCol), bp.getRowPx(minRow),
				(maxCol - minCol + 1) * bp.getColPx(), (maxRow - minRow + 1)
						* bp.getRowPx());
	}

}
