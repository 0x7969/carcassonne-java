package fop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import fop.view.GameView;
import fop.view.View;

public class GameStartState extends GameState {

	public GameStartState(GameController gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	@Override
	View initView() {
		return new GameView(gc);
	}

	@Override
	void initListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	void close() {
		// TODO Auto-generated method stub

	}

	@Override
	void mouseClicked(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	void mouseMoved(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	void mouseDragged(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	void mouseWheelMoved(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub

	}

}
