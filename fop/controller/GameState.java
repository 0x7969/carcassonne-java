package fop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import fop.view.View;

public abstract class GameState {
	
	GameController gc;
	
	public GameState(GameController gc) {
		this.gc = gc;
	}
	
	abstract View initView();
	abstract void initListeners();
	abstract void close();
	abstract void mouseClicked(MouseEvent event);
	abstract void mouseMoved(MouseEvent event);
	abstract void mouseDragged(MouseEvent event);
	abstract void mouseWheelMoved(MouseEvent event);
	abstract void actionPerformed(ActionEvent event);
}
