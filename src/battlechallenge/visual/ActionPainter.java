package battlechallenge.visual;

import java.awt.Graphics;
import java.util.List;

import battlechallenge.ActionResult;
import battlechallenge.ActionResult.ShotResult;

public class ActionPainter {

	public static void paintResults(BoardPanel bp,
			List<ActionResult> actionResults) {
		Graphics g = bp.getGraphics();
		g.setColor(BoardPanel.SHOT);
		// line offset
		int offsetCol = bp.getColPx() >> 1;
		int offsetRow = bp.getRowPx() >> 1;
		// hit (X) offset
		int eigthCol = bp.getColPx() >> 3;
		int eigthRow = bp.getRowPx() >> 3;
		for (ActionResult result : actionResults) {
			int endCol = result.getCoordinate().getCol();
			int endRow = result.getCoordinate().getRow();
			int originCol = result.getOrigin().getCol();
			int originRow = result.getOrigin().getRow();
			g.drawLine(bp.getColPx(originCol) + offsetCol,
					bp.getRowPx(originRow) + offsetRow, 
					bp.getColPx(endCol) + offsetCol, 
					bp.getRowPx(endRow) + offsetRow);
			if (result.getResult() == ShotResult.HIT) {
				g.drawLine(bp.getColPx(endCol) + eigthCol, bp.getRowPx(endRow)
						+ eigthRow, bp.getColPx(endCol+1) - eigthCol,
						bp.getRowPx(endRow+1) - eigthRow);
				g.drawLine(bp.getColPx(endCol+1) - eigthCol,
						bp.getRowPx(endRow) + eigthRow, bp.getColPx(endCol)
								+ eigthCol, bp.getRowPx(endRow+1) - eigthRow);
			}

		}
	}

}
