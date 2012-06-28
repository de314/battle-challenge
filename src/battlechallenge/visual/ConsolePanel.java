package battlechallenge.visual;

import java.awt.BorderLayout;
import java.awt.Panel;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConsolePanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConsolePanel() {
		// Create Scrolling Text Area in Swing
		JTextArea ta = new JTextArea("", 5, 50);
		ta.setLineWrap(true);
		JScrollPane sbrText = new JScrollPane(ta);
		sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(sbrText, BorderLayout.CENTER);
	}
}
