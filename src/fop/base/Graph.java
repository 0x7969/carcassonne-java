package fop.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a generic graph consisting of edges and nodes.
 *
 * @param <T> The datatype to be used.
 */
public class Graph<T> {

	private List<Edge<T>> edges;
	private List<Node<T>> nodes;

	public Graph() {
		this.nodes = new ArrayList<>();
		this.edges = new LinkedList<>();
	}

	public boolean addAllNodes(Collection<? extends Node<T>> nodes) {
		return this.nodes.addAll(nodes);
	}

	/**
	 * Eine neue Kante zwischen zwei Knoten hinzufügen. Sollte die Kante schon
	 * existieren, wird die vorhandene Kante zurückgegeben.
	 * 
	 * @param nodeA Der erste Knoten
	 * @param nodeB Der zweite Knoten
	 * @return Die erstellte oder bereits vorhandene Kante zwischen beiden gegebenen
	 *         Knoten
	 */
	public Edge<T> addEdge(Node<T> nodeA, Node<T> nodeB) {
		Edge<T> edge = getEdge(nodeA, nodeB);
		if (edge != null) {
			return edge;
		}

		edge = new Edge<>(nodeA, nodeB);
		this.edges.add(edge);
		return edge;
	}

	public boolean addAllEdges(Collection<Edge<T>> edges) {
		return this.edges.addAll(edges); // TODO kann das probleme machen? die methode addEdge prüft vorher noch ob sie
											// bereits vorhanden ist.
	}

	/**
	 * Gibt die Liste aller Knoten zurück
	 * 
	 * @return die Liste aller Knoten
	 */
	public List<Node<T>> getNodes() {
		return this.nodes;
	}

	/**
	 * Returns all edges of a given node as a list.
	 * 
	 * @return List of all edges of given node.
	 */
	public List<Edge<T>> getEdges(Node<T> node) {
		return this.edges.stream().filter(edge -> edge.contains(node)).collect(Collectors.toList());
	}

	/**
	 * Diese Methode sucht eine Kante zwischen beiden angegebenen Knoten und gibt
	 * diese zurück oder null, falls diese Kante nicht existiert
	 * 
	 * @param nodeA Der erste Knoten
	 * @param nodeB Der zweite Knoten
	 * @return Die Kante zwischen beiden Knoten oder null
	 */
	private Edge<T> getEdge(Node<T> nodeA, Node<T> nodeB) {
		return this.edges.stream().filter(edge -> (edge.getNodeA() == nodeA && edge.getNodeB() == nodeB)
				|| (edge.getNodeA() == nodeB && edge.getNodeB() == nodeA)).findFirst().orElse(null);
	}

	/**
	 * Returns all nodes with given value
	 * 
	 * @param value
	 * @return List of nodes containing all nodes with given value
	 */
	public List<Node<T>> getNodes(T value) {
		return this.nodes.stream().filter(n -> n.getValue() == value).collect(Collectors.toList());
	}
}
