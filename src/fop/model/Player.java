package fop.model;

import java.awt.Color;

public class Player {
	private Color color;
	private String name;
	private int score;
	private int meeples;

	public Player(String name) {
		this.color = Color.RED;
		this.name = name;
		this.score = 0;
	}
	
	public Color getColor() {
		return color;
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