package fop.controller;

public enum State {

	GAME_MENU, GAME_START, PLACING_TILE, PLACING_MEEPLE, GAME_OVER, GAME_SCORE;
	
	private int round;
	
	public void nextRound() {
		round++;
	}
	
	public int getRound() {
		return round;
	}

}
