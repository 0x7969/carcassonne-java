package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
		
		zoomInButton = new JButton("+");
		add(zoomInButton);
		zoomOutButton = new JButton("-");
		add(zoomOutButton);
		
		xValue = new JTextField("1");
		xValue.setPreferredSize(new Dimension(30, 30));
		add(new JLabel("X Position"));
		add(xValue);
		
		yValue = new JTextField("1");
		yValue.setPreferredSize(new Dimension(30, 30));
		add(new JLabel("Y Position"));
		add(yValue);
		playButton = new JButton("New tile");
		add(playButton);
		quitButton = new JButton("Quit");
		add(quitButton);
	}
	
	public void addToolbarActionListener(ActionListener l) {
		zoomInButton.addActionListener(l);
		zoomOutButton.addActionListener(l);
		playButton.addActionListener(l);
		quitButton.addActionListener(l);
	}
	
	public int getXValue() {
		return Integer.parseInt(xValue.getText());
	}
	
	public int getYValue() {
		return Integer.parseInt(yValue.getText());
	}

}
