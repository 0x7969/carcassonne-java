package fop.model;

public class Terrain {
	
	private FeatureType terrain;
	private Direction direction;
	private int position;
	
	Terrain(FeatureType terrain, int position, Direction direction) {
		this.terrain = terrain;
		this.position = position;
		this.direction = direction;
	}

}
