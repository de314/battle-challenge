package battlechallenge.visual;

import java.awt.Graphics;
import java.util.List;

import battlechallenge.ActionResult;
import battlechallenge.ActionResult.ShotResult;

public class ActionPainter {

	private static final int MIN_WIDTH_PX = 8;
	private static final int MIN_HEIGHT_PX = 8;
	private static final int OFFSET_X = 8;
	private static final int OFFSET_Y = 5;
	private static final int diameter = 8 << 1;
	
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
			g.fillOval(bp.getColPx(result.getCoordinate().getCol()) + OFFSET_X,
					bp.getRowPx(result.getCoordinate().getRow()) + OFFSET_Y,
					w, h);
			g.setColor(BoardPanel.SHIP_BORDER);
			g.drawOval(bp.getColPx(result.getCoordinate().getCol()) + OFFSET_X,
					bp.getRowPx(result.getCoordinate().getRow()) + OFFSET_Y,
					w, h);
			// TODO: draw line from ship to hit/miss
		}
	}

}
