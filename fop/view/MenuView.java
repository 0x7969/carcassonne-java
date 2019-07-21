package fop.view;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import fop.controller.GameController;

public class MenuView extends View {

	JButton b1;

	public MenuView(GameController gc) {
		super(gc);
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(400, 300));
		b1 = new JButton("Start game");
		this.add(b1);
	}

	public void addActionListener(ActionListener l) {
		b1.addActionListener(l);
	}

	@Override
	public Point getOverlayedTileGridPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasOverlay() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Object getOverlayedTile() {
		// TODO Auto-generated method stub
		return null;
	}

}
