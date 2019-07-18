package view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ToolbarPanel extends JPanel {
	
	JButton zoomInButton;
	JButton zoomOutButton;
	JButton playButton;
	JButton quitButton;
	JTextField xValue;
	JTextField yValue;
	
	public ToolbarPanel() {
		setBorder(BorderFactory.createTitledBorder("Menu"));
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		quitButton = new JButton("Quit");
		add(quitButton);
		
		quitButton.addActionListener((event) -> {
			switch (event.getActionCommand()) {
			case "Quit":
				SwingUtilities.getWindowAncestor(this).dispose();
				break;
			}
		});
	}
	
	public void addToolbarActionListener(ActionListener l) {
		quitButton.addActionListener(l);
	}
	
	public int getXValue() {
		return Integer.parseInt(xValue.getText());
	}
	
	public int getYValue() {
		return Integer.parseInt(yValue.getText());
	}

}
