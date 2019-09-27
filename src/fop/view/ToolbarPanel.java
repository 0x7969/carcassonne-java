package fop.view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ToolbarPanel extends JPanel {
	
	JButton quitButton;
	JButton skipButton;
	
	public ToolbarPanel() {
		setBorder(BorderFactory.createTitledBorder("Menu"));
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		quitButton = new JButton("Quit");
		add(quitButton);
		
		skipButton = new JButton("Skip");
		add(skipButton);
	}
	
	public void addToolbarActionListeners(ActionListener l) {
		quitButton.addActionListener(l);
		skipButton.addActionListener(l);
	}
	
	public void toggleSkipButton() {
		if(skipButton.isVisible())
			skipButton.setVisible(false);
		else
			skipButton.setVisible(true);
	}


}
