package fop.model;

import fop.base.WeightedEdge;
import fop.base.Graph;

public class FeatureGraph extends Graph<FeatureType> {

	public WeightedEdge<FeatureType> addEdge(FeatureNode nodeA, FeatureNode nodeB, int weight) {
		nodeA.setConnectsTiles();
		nodeB.setConnectsTiles();
		return super.addEdge(nodeA, nodeB, weight);
	}

}
