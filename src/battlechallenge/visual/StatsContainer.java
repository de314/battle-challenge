package battlechallenge.visual;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import battlechallenge.server.ServerPlayer;

public class StatsContainer extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<StatPanel> panels;
	
	public StatsContainer(Collection<ServerPlayer> players) {
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
	
	public void update(Graphics g) {
		for(StatPanel panel : panels)
			panel.paint(g);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);       // paint background
		for(StatPanel panel : panels)
			panel.repaint();
	}

	public void paint2 (Graphics g) {
		//super.paintComponent(g);       // paint background
		Graphics2D g2 = (Graphics2D)g; 
		//BufferedImage grid = new BufferedImage(120, 760, BufferedImage.TYPE_INT_RGB);
		BufferedImage grid = (BufferedImage)(this.createImage(120, 760));
		Graphics2D gc = grid.createGraphics();
		for(StatPanel panel : panels) {
			gc.clearRect(0, 40, 120, 760);
			panel.paint(gc);
			g2.drawImage(grid, null, panel.getX(), panel.getY() + 40);
			
		}
	
	}
} 

