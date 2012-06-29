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

	public StatPanel(ServerPlayer player) {
		super();
		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.p = player;
		this.setBackground(Color.DARK_GRAY);
	}

	@Override
	public void paint(Graphics g) {
		
		g.setColor(Color.green);
		int ts = g.getFont().getSize();
		int i = 1;
		if (!p.isAlive())
			g.drawString("**DEAD**", PADDING_HORZ, PADDING_VERT);
		else
			g.drawString(p.getName(), PADDING_HORZ, PADDING_VERT);
		g.drawString("Score: " + p.getScore(), PADDING_HORZ, PADDING_VERT + (ts * i++));
		g.drawString("Ships: " + p.getNumLiveShips(), PADDING_HORZ,
				PADDING_VERT + (ts * i++));
		g.drawString("Hits: " + p.getHitCount(), PADDING_HORZ, PADDING_VERT
				+ (ts * i++));
		g.drawString("Shots: " + p.getTotalShotCount(), PADDING_HORZ, PADDING_VERT
				+ (ts * i++));
		// g.drawString(String.format("Acc: %.1g%n",
		// p.getShotAccuracy()*100)+"%", PADDING_HORZ, PADDING_VERT + (ts * 4));
		g.drawString(
				new StringBuilder("Acc: ")
						.append((int) (p.getShotAccuracy() * 1000)/10).append("%")
						.toString(), PADDING_HORZ, PADDING_VERT + (ts * i++));
	}
}
