package fop.view;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

import fop.controller.GameController;

public abstract class View extends JPanel {

	private GameController gc;

	public View(GameController gc) {
		this.gc = gc;
	}
	
	public abstract void addActionListener(ActionListener event);

}
