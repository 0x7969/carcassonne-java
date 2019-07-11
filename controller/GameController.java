package controller;

import javax.swing.JFrame;

import model.Gameboard;

public class GameController {
//	startingTile Tile;
	private Gameboard board;
	private JFrame view;

	public GameController() {
		board = new Gameboard();
		// hier werden dann tatsächlich die actionlistener erstellt
		// man holt sich die components von view (getToolbar oä) und
		// setzt von hier den listener, den man als lamda erstellt
		// oder die lambda funktion auf methoden hier im code verweisen lässt.

		// wobei man dann wahrscheinlich statt im konstruktor alle listener zu
		// implementierren eher ein setupGameview, setupMenuview und closeGameview und
		// closeMenuview bereitstellt. und dann ein changeGameState(STATE), das view
		// schließt und entsprechend eine neue view öffnet
	}

	public static void main(String[] args) {
		GameController game = new GameController();
	}
}