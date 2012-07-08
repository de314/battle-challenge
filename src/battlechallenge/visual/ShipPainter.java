package battlechallenge.visual;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import battlechallenge.Coordinate;
import battlechallenge.ship.Ship;

// TODO: Auto-generated Javadoc
/**
 * The Class ShipPainter.
 */
public class ShipPainter {
	
	/**
	 * Paint ships.
	 * 
	 * @param bp
	 *            the bp
	 * @param ships
	 *            the ships
	 * @param teamColor
	 *            the team color
	 */
	public static List<Coordinate> paintShips(BoardPanel bp, List<Ship> ships,
			Color teamColor) {
		List<Coordinate> coords = new LinkedList<Coordinate>();
		Graphics g = bp.getGraphics();
		g.setColor(teamColor);
		for (Ship s : ships)
			coords.add(paintShip(bp, s, teamColor));
		return coords;
	}

	/**
	 * Paint ship.
	 * 
	 * @param bp
	 *            the bp
	 * @param s
	 *            the s
	 * @param teamColor
	 *            the team color
	 */
	public static Coordinate paintShip(BoardPanel bp, Ship s, Color teamColor) {
		Graphics g = bp.getGraphics();
		// fill in ship color
		g.setColor(teamColor);
		g.fillOval(bp.getColPx(s.getLocation().getCol()) + 2,
				bp.getRowPx(s.getLocation().getRow()) + (bp.getRowPx() >> 2),
				bp.getColPx() - 4, bp.getRowPx() >> 1);
		g.setColor(BoardPanel.SHIP_BORDER);
		g.drawOval(bp.getColPx(s.getLocation().getCol()) + 2,
				bp.getRowPx(s.getLocation().getRow()) + (bp.getRowPx() >> 2),
				bp.getColPx() - 4, bp.getRowPx() >> 1);
		// // draw health box
		// g.setColor(Color.white);
		// int col = s.getLocation().getCol();
		// int row = s.getLocation().getRow();
		// g.fillRect(bp.getColPx(col + 2), bp.getRowPx(row + 2),
		// bp.getColPx() - 4, bp.getRowPx() - 4);
		// // write health
		// g.setColor(Color.black);
		// g.drawString("" + s.getHealth(), bp.getColPx(col) - bp.getColPx() / 2
		// + FONT_OFFSET_X, bp.getRowPx(row) + bp.getRowPx() / 2
		// + FONT_OFFSET_Y);
		return s.getLocation();
	}
}
