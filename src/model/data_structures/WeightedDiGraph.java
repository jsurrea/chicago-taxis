package model.data_structures;
public class WeightedDiGraph<K extends Comparable<K>> {
	private final String name;  // Nombre del grafo
	private int V;
	private int E;
	private ArregloDinamico<Bag<DirectedEdge>> adj;
	private TablaHashLinearProbing<K,Integer> map;  	// Para convertir de K a Integer
	private ArregloDinamico<K> index;  					// Para convertir de Integer a K
	public WeightedDiGraph(String name) {
		this.name = name;
		this.V = 0;
		this.E = 0;
		adj = new ArregloDinamico<Bag<DirectedEdge>>();
		map = new TablaHashLinearProbing<K,Integer>();
		index = new ArregloDinamico<K>();
	}
	public int V() { return V;}
	public int E() { return E;}
	private void addVertex(K id) {
		adj.add(new Bag<DirectedEdge>());
		index.add(id);
		map.put(id,V++);
	}
	private DirectedEdge edgeBetween(int v, int w) {
		for(DirectedEdge e : adj.getElement(v)) if(e.to() == w) return e;
		return null;
	}
	public void addEdge(K from, K to, double weight) {
		Integer v = map.get(from);
		Integer w = map.get(to);
		if(v==null) {
			addVertex(from);
			v = map.get(from);
		}
		if(w==null) {
			addVertex(to);
			w = map.get(to);
		}
		DirectedEdge currentEdge = edgeBetween(v,w);
		if(currentEdge != null) {
			currentEdge.update(weight); 
			return;
		}
		DirectedEdge e = new DirectedEdge(v, from, w, to, weight);
		adj.getElement(v).add(e);
		E++;
	}
	public Bag<DirectedEdge> adj(int v) { return adj.getElement(v);}
	public ArregloDinamico<DirectedEdge> edges() {
		ArregloDinamico<DirectedEdge> list = new ArregloDinamico<DirectedEdge>(E);
		for (int v = 0; v < V; v++) for (DirectedEdge e : adj(v)) list.add(e);
		return list;
	}
	public ShortestPath<K> shortestPathFrom(K source, K dest) {
		Integer v =  map.get(source);
		if(v==null) return null;
		Integer w =  map.get(dest);
		if(w==null) return null;
		return new ShortestPath<K>(this,v,w);
	}
	public Integer map(K key) { return map.get(key);}
	public String name() { return name;}
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("V=" + V + "\tE=" + E + "\n");
		for (int v = 0; v < V; v++) {
			s.append(index.getElement(v) + ": ");
			for (DirectedEdge e : adj.getElement(v)) s.append(e + "  ");
			s.append("\n");
		}
		return s.toString();
	}
	public class DirectedEdge {
		private final int v;
		private final K vLabel;
		private final int w;
		private final K wLabel;
		private double weight;
		private int n; // Número de viajes v->w para el promedio.
		public DirectedEdge(int v, K vLabel, int w, K wLabel, double weight) {
			this.v = v;
			this.vLabel = vLabel;
			this.w = w;
			this.wLabel = wLabel;
			this.weight = weight;
			this.n = 1;
		}
		public void update(double newWeight) {
			this.weight = (this.weight * this.n + newWeight) / (n+1);
			n++;
		}
		public int from() { return v;}
		public int to() { return w;}
		public double weight() { return weight;}
		public String toString() { return vLabel + "->" + wLabel + " " + String.format("%5.2f", weight);}
	}
}