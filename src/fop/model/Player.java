package fop.model;

public class Player {
	private MeepleColor color;
	private String name;
	private int score;
	private int meeples; // the amount of meeples

	public Player(String name, MeepleColor color) {
		this.color = color;
		this.name = name;
		this.score = 0;
		this.meeples = 7;
	}

	/**
	 * Returns the meeple colour of this player.
	 * 
	 * @return the meeple colour of this player.
	 */
	public MeepleColor getMeepleColor() {
		return color;
	}

	/**
	 * Returns the name of this player.
	 * 
	 * @return the name of this player.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Adds points to this players score.
	 * 
	 * @param score The score to be added.
	 */
	void addScore(int score) {
		this.score += score;
	}

	/**
	 * Return this players score.
	 * 
	 * @return the players score.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Returns the amount of meeple this player has left.
	 * 
	 * @return the amount of meeple this player has left.
	 */
	public int getMeepleAmount() {
		return meeples;
	}

	/**
	 * Removes one meeple from this players amount of meeple.
	 */
	void removeMeeple() {
		meeples--;
	}

	/**
	 * Adds one meeple to this players amount of meeple.
	 */
	void returnMeeple() {
		meeples++;
	}

}