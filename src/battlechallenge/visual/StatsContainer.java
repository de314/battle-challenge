package battlechallenge.visual;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

		Graphics2D g2 = (Graphics2D)g; 
		BufferedImage image = new BufferedImage(120, 720/panels.size(), BufferedImage.TYPE_INT_RGB);
		BufferedImage image2 = new BufferedImage(120, 720/panels.size(), BufferedImage.TYPE_INT_RGB);
		BufferedImage image3 = new BufferedImage(120, 720/panels.size(), BufferedImage.TYPE_INT_RGB);
		BufferedImage image4 = new BufferedImage(120, 720/panels.size(), BufferedImage.TYPE_INT_RGB);
		Map<Integer, BufferedImage> imageMap = new HashMap<Integer, BufferedImage>();
		imageMap.put(0, image);
		imageMap.put(1, image2);
		imageMap.put(2, image3);
		imageMap.put(3, image4);

		int yCoord = 0;
		int i = 0;
		for(StatPanel panel : panels) {
			if (i == 0)
				yCoord = 40;
			panel.paint(imageMap.get(i).getGraphics());
			g2.drawImage(imageMap.get(i), null, 0, i * 80 + yCoord); 
			i++;
			
		}
	}
} 

