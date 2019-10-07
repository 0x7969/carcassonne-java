package fop.model;

import java.awt.Color;

public class Player {
	private MeepleColour colour;
	private String name;
	private int score;
	private int meeples;

	public Player(String name, MeepleColour colour) {
		this.colour = colour;
		this.name = name;
		this.score = 0;
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

}