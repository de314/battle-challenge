package battlechallenge.visual;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;
import java.util.List;

import battlechallenge.server.ServerPlayer;

public class HeaderPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ServerPlayer> players;
	
	public HeaderPanel(List<ServerPlayer> players) {
		super();
		this.setPreferredSize(new Dimension(BCViz.DEFAULT_WIDTH_PX, 40));
		this.players = players;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
		int spacing = BCViz.DEFAULT_WIDTH_PX / players.size();
		for (int i =0;i<players.size();i++) {
			g.setColor(BoardPanel.PLAYER_COLORS[i]);
			g.drawString(players.get(i).getName() == null ? "**DEAD**" : players.get(i).getName(), (i * spacing) + (spacing/4), 20);
		}
	}
	
}
