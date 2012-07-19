package battlechallenge.server;

import java.awt.Checkbox;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import battlechallenge.maps.MapDescription;
import battlechallenge.maps.MapIO;

public class GameManagerWindow extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant DEFAULT_HEIGHT_PX. */
	public static final int DEFAULT_HEIGHT_PX = 650;

	/** The Constant DEFAULT_WIDTH_PX. */
	public static final int DEFAULT_WIDTH_PX = 500;

	/** The Constant DEFAULT_ROW_OFFSET_PX. */
	public static final int DEFAULT_ROW_OFFSET_PX = 30;

	/** The Constant DEFAULT_COL_OFFSET_PX. */
	public static final int DEFAULT_COL_OFFSET_PX = 0;

	// gross hack... I am so sorry...
	private static GameManagerWindow me;
	
	private Map<Integer, Collection<MapDescription>> maps;
	private int minNumPlayers;
	private int currNumPlayers;

	private JCheckBox randomMapCheckBox;
	private JCheckBox randomPlayerCheckBox;
	private JComboBox mapDropDown;
	private JComboBox playerDropDown;
	private JButton mapUpdateButton;
	private JTextArea infoTextArea;
	private JTextArea mapTextArea;
	
	public static boolean randomPlayers() {
		return me.randomPlayerCheckBox.isSelected();
	}
	
	public static boolean randomMap() {
		return me.randomMapCheckBox.isSelected();
	}
	
	public static int numberOfPlayers() {
		// TODO: handle random
		if (me.playerDropDown.getItemCount() > 0)
			return (Integer)me.playerDropDown.getSelectedItem();
		return 0;
	}
	
	public static MapDescription getSelectedMap() {
		// TODO: handle random
		if (me.mapDropDown.getItemCount() > 0)
			return (MapDescription)me.mapDropDown.getSelectedItem();
		return null;
	}

	public GameManagerWindow() {
		super("Battle Challenge Viz 0.1");
		
		me = this;

		randomPlayerCheckBox = new JCheckBox("Random Number of Players");
		randomMapCheckBox = new JCheckBox("Random Maps");
		mapDropDown = new JComboBox();
		playerDropDown = new JComboBox();
		mapUpdateButton = new JButton("Update Ship Files");
		infoTextArea = new JTextArea();
		setupTextArea(infoTextArea);
		mapTextArea = new JTextArea();
		setupTextArea(mapTextArea);
		
		randomPlayerCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (randomPlayerCheckBox.isSelected()) {
					randomPlayerCheckBox.setSelected(true);
					randomMapCheckBox.setEnabled(false);
					playerDropDown.setEnabled(false);
					mapDropDown.setEnabled(false);
					infoTextArea.setText("  Number of Players: Random  \n  Maps: Random  ");
					System.out.println("Number of Players: Random\tMaps: Random");
					mapTextArea.setText("");
				} else {
					randomMapCheckBox.setEnabled(true);
					playerDropDown.setEnabled(true);
					mapDropDown.setEnabled(true);
					updateMapDropDown();
				}
			}
		});
		randomMapCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (randomMapCheckBox.isSelected()) {
					mapDropDown.setEnabled(false);
					infoTextArea.setText("  Number of Players: " + playerDropDown.getSelectedItem() + "  \n  Maps: Random\n  ");
					System.out.println("Number of Players: " + playerDropDown.getSelectedItem() + "\tMaps: Random");
					mapTextArea.setText("");
				} else {
					mapDropDown.setEnabled(true);
					updateMapDropDown();
				}
			}
		});
		mapUpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateMapsFiles();
				updateMapDropDown();
			}
		});
		playerDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (playerDropDown.getItemCount() > 0) {
					currNumPlayers = (Integer)playerDropDown.getSelectedItem();
					System.out.println("Setting Number of players: " + currNumPlayers);
					updateMapDropDown();
				}
			}
		});
		mapDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mapDropDown.getItemCount() > 0) {
					MapDescription map = (MapDescription)mapDropDown.getSelectedItem();
					System.out.println("Setting current map: " + map.getName());
					infoTextArea.setText(map.infoString());
					mapTextArea.setText(map.getMap());		
				}
			}
		});
		
		updateMapsFiles();
		updateMapDropDown();

		Container cp = getContentPane();
	    cp.setLayout(new GridBagLayout());

	    GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.anchor = GridBagConstraints.NORTH;
	    c.weighty = 1;
	    c.gridy = 0;
	    c.gridx = 0;
		cp.add(mapUpdateButton, c);
		c.gridy++;
		c.gridx = 1;
		cp.add(randomMapCheckBox, c);
		c.gridx = 2;
		cp.add(randomPlayerCheckBox, c);
		c.gridy++; 
		c.gridx = 1;
		cp.add(playerDropDown, c);
		c.gridx = 2;
		cp.add(mapDropDown, c);
		c.gridwidth = 3;
		c.gridy++;
		c.gridx = 0;
		cp.add(infoTextArea, c);
		c.gridy++;
		cp.add(mapTextArea, c);

		this.setSize(DEFAULT_WIDTH_PX, DEFAULT_HEIGHT_PX);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(true);

		this.validate();
	}
	
	private void setupTextArea(JTextArea ta) {
		ta.setEditable(false);
		ta.setText("");
		ta.setFont(new Font(Font.MONOSPACED, 0, 10));
	}

	private void updateMapsFiles() {
		maps = MapIO.getAvailableMaps();
		minNumPlayers = -1;
		for (Integer i : maps.keySet())
			if (minNumPlayers < 0 || i < minNumPlayers)
				minNumPlayers = i;
		currNumPlayers = minNumPlayers;
		playerDropDown.removeAllItems();
		for (Integer numPlayers : maps.keySet())
			playerDropDown.addItem(numPlayers);
//		mapDropDown.setSelectedIndex(0);
	}

	private void updateMapDropDown() {
		mapDropDown.removeAllItems();
		for (MapDescription desc : maps.get(currNumPlayers))
			mapDropDown.addItem(desc);
		mapDropDown.setSelectedIndex(0);
	}
}
