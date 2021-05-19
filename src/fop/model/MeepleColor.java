package fop.model;

import java.awt.Color;

public enum MeepleColor {

	YELLOW, GREEN, GREY, RED, BLUE, BLACK;

	public Color getColor() {
		switch (this) {
		case YELLOW:
			return Color.YELLOW;
		case BLACK:
			return Color.BLACK;
		case BLUE:
			return Color.BLUE;
		case GREEN:
			return Color.GREEN;
		case GREY:
			return Color.GRAY;
		case RED:
			return Color.RED;
		default:
			return null;
		}
	}
}
