package model.data_structures;
public class ShortestPath<K extends Comparable<K>> {
	private final String graphName;
	private WeightedDiGraph<K>.DirectedEdge[] edgeTo;
	private double[] distTo;
	private IndexMinPQ<Double> pq;
	private final int s;
	private final int d;
	public ShortestPath(WeightedDiGraph<K> G, int source, int dest) {
		this.graphName = G.name();
		s = source;
		d = dest;
		edgeTo = new WeightedDiGraph.DirectedEdge[G.V()];
		distTo = new double[G.V()];
		pq = new IndexMinPQ<Double>(G.V());
		for (int v = 0; v < G.V(); v++) distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s] = 0.0;
		pq.insert(s, 0.0);
		while (!pq.isEmpty()) relax(G, pq.delMin());
	}
	private void relax(WeightedDiGraph<K> G, int v) {
		for(WeightedDiGraph<K>.DirectedEdge e : G.adj(v)) {
			int w = e.to();
			if (distTo[w] > distTo[v] + e.weight()) {
				distTo[w] = distTo[v] + e.weight();
				edgeTo[w] = e;
				if (pq.contains(w)) pq.changeKey(w, distTo[w]);
				else pq.insert(w, distTo[w]);
			}
		}
	}
	public double distTo() { return distTo[d]; }
	public boolean hasPathTo() { return distTo[d] != Double.POSITIVE_INFINITY; }
	public Stack<WeightedDiGraph<K>.DirectedEdge> pathTo() {
		if (!hasPathTo()) return null;
		Stack<WeightedDiGraph<K>.DirectedEdge> path = new Stack<WeightedDiGraph<K>.DirectedEdge>();
		for (WeightedDiGraph<K>.DirectedEdge e = edgeTo[d]; e != null; e = edgeTo[e.from()]) path.push(e);
		return path;
	}
	public String toString() { return graphName;}  // Indica de qué grafo se construyó.
}