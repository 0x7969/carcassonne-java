package fop.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import fop.view.MenuView;
import fop.view.View;

public class MenuState extends GameState {
	
	private MenuView view;

	public MenuState(GameController gc) {
		super(gc);
	}

	@Override
	View initView() {
		view = new MenuView(gc);
		initListeners();
		return view;
	}

	@Override
	void initListeners() {
		view.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				gc.setState(new GameStartState(gc));
			}
		});
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
