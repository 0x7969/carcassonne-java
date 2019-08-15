package fop.view;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import fop.controller.GameController;
import fop.controller.State;

public class MenuView extends JPanel {

	JButton b1;

	public MenuView(GameController gc) {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(400, 300));
		b1 = new JButton("Start game");
		this.add(b1);
		
		this.addActionListener(event -> {
			// hier kann dann noch genauer der button abgefragt werden usw.
			gc.setState(State.GAME_START);
		});
		
	}

	public void addActionListener(ActionListener l) {
		b1.addActionListener(l);
	}

}
