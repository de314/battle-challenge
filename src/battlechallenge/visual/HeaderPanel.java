package battlechallenge.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;
import java.util.Collection;

import battlechallenge.server.Game;
import battlechallenge.server.ServerPlayer;

public class HeaderPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Collection<ServerPlayer> players;
	
	private int numTurns = 0;

	public void setNumTurns(int numTurns) {
		this.numTurns = numTurns + 1;
	}
	
	public HeaderPanel(Collection<ServerPlayer> players) {
		super();
		this.setPreferredSize(new Dimension(BCViz.DEFAULT_WIDTH_PX, 40));
		this.players = players;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
		int spacing = BCViz.DEFAULT_WIDTH_PX / (players.size() + 1);
		int i = 0;
		for (ServerPlayer p : players) {
			g.setColor(BoardPanel.PLAYER_COLORS[i]);
			g.drawString(p.getName() == null ? "**DEAD**" : p.getName(), ((i++) * spacing) + (spacing/4), 20);
		}
		g.setColor(Color.black);
		g.drawString(numTurns + "/" + Game.MAX_NUM_TURNS, ((i++) * spacing) + (spacing/4), 20);
	}
	
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);       // paint background
//		Graphics2D g2 = (Graphics2D)g; 
//		BufferedImage grid = (BufferedImage)(this.createImage(BCViz.DEFAULT_WIDTH_PX , 40));
//		Graphics2D gc = grid.createGraphics();
//		gc.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
//		int spacing = BCViz.DEFAULT_WIDTH_PX / (players.size() + 1);
//		int i = 0;
//		for (ServerPlayer p : players) {
//			gc.setColor(BoardPanel.PLAYER_COLORS[i]);
//			gc.drawString(p.getName() == null ? "**DEAD**" : p.getName(), ((i++) * spacing) + (spacing/4), 20);
//		}
//		gc.setColor(Color.black);
//		gc.drawString(numTurns + "/" + Game.MAX_NUM_TURNS, ((i++) * spacing) + (spacing/4), 20);
//		g2.drawImage(grid, null, 0, 0);
//	}
	
}
