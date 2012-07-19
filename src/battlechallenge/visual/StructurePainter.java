package battlechallenge.visual;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import battlechallenge.structures.Barrier;
import battlechallenge.structures.Base;
import battlechallenge.structures.City;
import battlechallenge.structures.Structure;

public class StructurePainter {
	
	
	
	public static void paintStructures(BoardPanel bp, List<Structure> structures) {
		for (Structure s : structures) {
			if (s instanceof Base) { /* DO NOTHING */ }
			else if (s instanceof City)
				paintCity(bp, (City)s);
			else if (s instanceof Barrier) {
				paintBarrier(bp, (Barrier)s);
			}
			// TODO: paint destructable barrier
			// TODO: paint barrier
		}
	}
	
	public static void paintBase(BoardPanel bp, Base b, Color teamColor) {
		Graphics g = bp.getGraphics();
		int eigthCol = bp.getColPx() >> 3;
		int eigthRow = bp.getRowPx() >> 3;
		int fourthCol = bp.getColPx() >> 2;
		int fourthRow = bp.getRowPx() >> 2;
		int colPx = bp.getColPx(b.getLocation().getCol());
		int rowPx = bp.getRowPx(b.getLocation().getRow());
		g.setColor(teamColor);
		g.fillRect(colPx+eigthCol, rowPx+eigthRow, bp.getColPx()-fourthCol, bp.getRowPx()-fourthRow);
	}
	
	public static void paintBarrier(BoardPanel bp, Barrier b) {
		Graphics g = bp.getGraphics();
		int eigthCol = bp.getColPx() >> 3;
		int eigthRow = bp.getRowPx() >> 3;
		int fourthCol = bp.getColPx() >> 2;
		int fourthRow = bp.getRowPx() >> 2;
		int colPx = bp.getColPx(b.getLocation().getCol());
		int rowPx = bp.getRowPx(b.getLocation().getRow());
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(colPx+eigthCol, rowPx+eigthRow, 3*fourthCol, 3*fourthRow);
	}
	
	public static void paintCity(BoardPanel bp, City b) {
		Graphics g = bp.getGraphics();
		int eigthCol = bp.getColPx() >> 3;
		int eigthRow = bp.getRowPx() >> 3;
		int fourthCol = bp.getColPx() >> 2;
		int fourthRow = bp.getRowPx() >> 2;
		int colPx = bp.getColPx(b.getLocation().getCol());
		int rowPx = bp.getRowPx(b.getLocation().getRow());
		g.setColor(Color.DARK_GRAY);
		g.fillRect(colPx+eigthCol, rowPx+eigthRow, 3*fourthCol, 3*fourthRow);
		g.setColor(BoardPanel.OCEAN);
		g.fillRect(colPx+eigthCol+fourthCol, rowPx+eigthRow+fourthRow, fourthCol, fourthRow);
	}

}
