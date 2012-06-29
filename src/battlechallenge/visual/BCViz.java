package battlechallenge.visual;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import battlechallenge.server.ServerPlayer;

public class BCViz extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_HEIGHT_PX = 400;
	public static final int DEFAULT_WIDTH_PX = 800;
	public static final int DEFAULT_ROW_OFFSET_PX = 30;
	public static final int DEFAULT_COL_OFFSET_PX = 0;
	public final int TOTAL_HEIGHT_PX;
	public final int TOTAL_WIDTH_PX;

	private List<ServerPlayer> players;
	private final int totalWidth;
	private final int totalHeight;
	
	private BoardPanel bp;
	private ConsolePanel cp;
	private StatsContainer scL;
	private StatsContainer scR;

	public BCViz(List<ServerPlayer> players, int boardWidth,
			int boardHeight) {
		super("Battle Challenge Viz 0.1");
		this.totalWidth = boardWidth;
		this.totalHeight = boardHeight;
		this.TOTAL_WIDTH_PX = DEFAULT_WIDTH_PX;
		this.TOTAL_HEIGHT_PX = DEFAULT_HEIGHT_PX;
		switch (players.size()) {
		case 2 :
			scL = new StatsContainer(players);
			this.add(scL, BorderLayout.WEST);
			break;
		case 4 :
			scL = new StatsContainer(players);
			this.add(scL, BorderLayout.WEST);
			break;
		case 8 :;
			List<ServerPlayer> temp = new LinkedList<ServerPlayer>();
			for (int i=0;i<4;i++)
				temp.add(players.get(i));
			scL = new StatsContainer(temp);
			this.add(scL, BorderLayout.WEST);
			temp = new LinkedList<ServerPlayer>();
			for (int i=4;i<8;i++)
				temp.add(players.get(i));
			scR = new StatsContainer(temp);
			this.add(scR, BorderLayout.EAST);
			break;
		default :
			// error, invalid players list size
			return;
		}
		
		// add header panel
		this.add(new HeaderPanel(players), BorderLayout.NORTH);

		// add board panel
		bp = new BoardPanel(totalWidth, totalHeight, players);
		this.add(bp, BorderLayout.CENTER);
		
		// add console panel
		cp = new ConsolePanel();
		this.add(cp, BorderLayout.SOUTH);
		
		this.setSize(DEFAULT_WIDTH_PX, DEFAULT_HEIGHT_PX);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(true);
		
		this.validate();
	}
	
	public void kill() {
		this.dispose();
	}

	public void updateGraphics() {
		this.repaint();
	}

	@Override
	public void paint(Graphics g) {
		bp.repaint();
		cp.repaint();
		scL.repaint();
		if (scR != null)
			scR.repaint();
	}
}