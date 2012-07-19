package battlechallenge.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Panel;

import battlechallenge.server.ServerPlayer;

public class StatPanel extends Panel {

	public static final int PADDING_VERT = 15;
	public static final int PADDING_HORZ = 2;
	public static final int DEFAULT_WIDTH = 120;
	public static final int DEFAULT_HEIGHT = 60;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ServerPlayer p;
	private String compressedName;
	private boolean dead;

	public StatPanel(ServerPlayer player) {
		super();
		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.p = player;
		this.setBackground(Color.DARK_GRAY);
		this.dead = false;
	}

	@Override
	public void paint(Graphics g) {

		g.setColor(Color.green);
		int ts = g.getFont().getSize();
		int i = 1;
		g.drawString(compressName(), PADDING_HORZ, PADDING_VERT);
		g.drawString("Score: " + p.getScore(), PADDING_HORZ, PADDING_VERT
				+ (ts * i++));
		g.drawString("Ships: " + p.getNumLiveShips(), PADDING_HORZ,
				PADDING_VERT + (ts * i++));
		g.drawString("Income: " + p.getLastIncome(), PADDING_HORZ,
				PADDING_VERT + (ts * i++));
		g.drawString("Minerals: " + p.getMinerals(), PADDING_HORZ,
				PADDING_VERT + (ts * i++));
//		g.drawString("Next Ship: " + (p.getMinsPerShip() - p.getMinerals()),
//				PADDING_HORZ, PADDING_VERT + (ts * i++));
//		g.drawString("Total Mins: " + p.getTotalMinerals(), PADDING_HORZ,
//				PADDING_VERT + (ts * i++));
//		g.drawString("Total Ships: " + (p.getTotalShips()), PADDING_HORZ,
//				PADDING_VERT + (ts * i++));
//		g.drawString("Hits/Shots/Acc.: ", PADDING_HORZ, PADDING_VERT
//				+ (ts * i++));
//		g.drawString(p.getHitCount() + "/" + p.getTotalShotCount() + "/"
//				+ ((int) (p.getShotAccuracy() * 1000) / 10), PADDING_HORZ,
//				PADDING_VERT + (ts * i++));
	}

	private String compressName() {
		if (compressedName == null || (!p.isAlive() && !dead)) {
			if (p.getName() == null)
				compressedName = "Unknown";
			else
				compressedName = p.getName();
			if (!p.isAlive()) {
				compressedName += "*DEAD*";
				dead = true;
			}
			if (compressedName.length() > 15)
				compressedName = compressedName.substring(0, 13) + "...";
		}
		return compressedName;
	}
}
