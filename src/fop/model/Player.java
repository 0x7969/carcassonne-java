package fop.model;

public class Player {
	private MeepleColour colour;
	private String name;
	private int score;
	private int meeples; // the amount of meeples

	public Player(String name, MeepleColour colour) {
		this.colour = colour;
		this.name = name;
		this.score = 0;
		this.meeples = 7;
	}
	
	public MeepleColour getColour() {
		return colour;
	}
	
	public String getName() {
		return name;
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getMeeples() {
		return meeples;
	}
	
	public void removeMeeple() {
		meeples--;
	}
	
	public void returnMeeple() {
		meeples++;
	}

}