package fop.view;

import javax.swing.JPanel;

import fop.controller.GameController;

public abstract class View extends JPanel {
	
	private GameController gc;

	public View(GameController gc) {
		this.setSize(800, 800);
		this.gc = gc;
	}

}
