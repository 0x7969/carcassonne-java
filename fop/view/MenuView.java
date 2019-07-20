package fop.view;

import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import fop.controller.GameController;

public class MenuView extends View {
	
	JButton b1;
	
	public MenuView(GameController gc) {
		super(gc);
	    this.setLayout(new GridBagLayout());
		b1 = new JButton("Start game");
		this.add(b1);
	}

	@Override
	public void addActionListener(ActionListener l) {
		b1.addActionListener(l);
	}

}
