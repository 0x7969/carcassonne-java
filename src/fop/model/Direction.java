package fop.model;

public enum Direction {
	
	X, Y, BOTH;
	
	public Direction getOpposite() {
		if (this == BOTH)
			throw new IllegalArgumentException("Direction BOTH does not have an opposite direction.");
		else return this == X ? Y : X;
	}
	
}
