package model;

import base.Edge;
import base.Graph;

public class FeatureGraph extends Graph<FeatureType> {

	public Edge<FeatureType> addEdge(FeatureNode nodeA, FeatureNode nodeB, int weight) {
		nodeA.setConnectsTiles();
		nodeB.setConnectsTiles();
		return super.addEdge(nodeA, nodeB, weight);
	}

}
