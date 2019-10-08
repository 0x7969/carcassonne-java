package fop.model;

public enum Position {

	TOPLEFT,
	TOP,
	TOPRIGHT,
	LEFT,
	CENTER,
	RIGHT,
	BOTTOMLEFT,
	BOTTOM,
	BOTTOMRIGHT;
	
	public static Position[] getStraightPositions() {
		return new Position[]{TOP, LEFT, RIGHT, BOTTOM};
	}
	
	public static Position[] getDiagonalPositions() {
		return new Position[]{TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT};
	}

}
