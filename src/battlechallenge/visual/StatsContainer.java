package battlechallenge.visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;
import java.util.LinkedList;
import java.util.List;

import battlechallenge.server.ServerPlayer;

public class StatsContainer extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<StatPanel> panels;
	
	public StatsContainer(List<ServerPlayer> players) {
		super();
		
		panels = new LinkedList<StatPanel>();
		for (ServerPlayer p : players)
			panels.add(new StatPanel(p));
		switch(panels.size()) {
		case 1 :
			this.setLayout(new BorderLayout());
			this.add(panels.get(0), BorderLayout.CENTER);
			break;
		case 2 :
			this.setLayout(new GridLayout(2, 1));
			this.add(panels.get(0));
			this.add(panels.get(1));
			break;
		case 4 :
		default :
			this.setLayout(new GridLayout(4, 1));
			this.add(panels.get(0));
			this.add(panels.get(1));
			this.add(panels.get(2));
			this.add(panels.get(3));
		}
		this.validate();
	}
	
	@Override
	public void paint(Graphics g) {
		for(StatPanel panel : panels)
			panel.repaint();
	}
}
