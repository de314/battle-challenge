package battlechallenge.visual;

import java.awt.Graphics;
import java.util.List;

import battlechallenge.ActionResult;
import battlechallenge.ActionResult.ShotResult;

public class ActionPainter {

	private static final int MIN_WIDTH_PX = 8;
	private static final int MIN_HEIGHT_PX = 8;
	private static final int offset = 8;
	private static final int diameter = offset << 1;
	
	public static void paintResults(BoardPanel bp, List<ActionResult> actionResults) {
		Graphics g = bp.getGraphics();
		for (ActionResult result : actionResults) {
			if (result.getResult() == ShotResult.MISS)
				g.setColor(BoardPanel.MISS);
			else
				g.setColor(BoardPanel.HIT);
			int w = bp.getColPx() - diameter;
			w = w < MIN_WIDTH_PX ? MIN_WIDTH_PX : w;
			int h = bp.getRowPx() - diameter;
			h = h < MIN_HEIGHT_PX ? MIN_HEIGHT_PX : h;
			g.fillOval(bp.getColPx(result.getCoordinate().getCol()) + offset,
					bp.getRowPx(result.getCoordinate().getRow()) + offset,
					w, h);
			g.setColor(BoardPanel.SHIP_BORDER);
			g.drawOval(bp.getColPx(result.getCoordinate().getCol()) + offset,
					bp.getRowPx(result.getCoordinate().getRow()) + offset,
					w, h);
			// TODO: draw line from ship to hit/miss
		}
	}

}
