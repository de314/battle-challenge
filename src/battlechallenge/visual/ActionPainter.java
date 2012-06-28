package battlechallenge.visual;

import java.awt.Graphics;
import java.util.List;

import battlechallenge.ActionResult;
import battlechallenge.ActionResult.ShotResult;

public class ActionPainter {

	private static final int offset = 8;
	private static final int diameter = offset << 1;
	
	public static void paintResults(BoardPanel bp, List<ActionResult> actionResults) {
		Graphics g = bp.getGraphics();
		for (ActionResult result : actionResults) {
			if (result.getResult() == ShotResult.MISS)
				g.setColor(BoardPanel.MISS);
			else
				g.setColor(BoardPanel.HIT);
			g.fillOval(bp.getColPx(result.getCoordinate().getCol()) + offset,
					bp.getRowPx(result.getCoordinate().getRow()) + offset,
					bp.getColPx() - diameter, bp.getRowPx() - diameter);
			g.setColor(BoardPanel.SHIP_BORDER);
			g.drawOval(bp.getColPx(result.getCoordinate().getCol()) + offset,
					bp.getRowPx(result.getCoordinate().getRow()) + offset,
					bp.getColPx() - diameter, bp.getRowPx() - diameter);
			// TODO: draw line from ship to hit/miss
		}
	}

}
