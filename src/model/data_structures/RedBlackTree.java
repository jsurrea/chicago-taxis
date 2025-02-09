package model.data_structures;
import java.util.NoSuchElementException;
public class RedBlackTree <K extends Comparable<K>, V> {
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	private Node root;
	private class Node {
		private K key;          
		private V value;         
		private Node left, right;  
		private boolean color;     
		private int size;         
		public Node(K key, V value, boolean color, int size) {
			this.key = key;
			this.value = value;
			this.color = color;
			this.size = size;
		}
	}
	private boolean isRed(Node x) {
		if (x == null) return false;
		return x.color == RED;
	}
	private int size(Node x) {
		if (x == null) return 0;
		return x.size;
	} 
	public int size() {
		return size(root);
	}
	public boolean isEmpty() {
		return root == null;
	}
	public V get(K key) {
		if (key == null) throw new IllegalArgumentException("argument to get() is null");
		return get(root, key);
	}
	public boolean contains(K key) {
		return get(key) != null;
	}
	public int getHeight(K key) {
		return getHeight(root, key, 0); 
	}
	private int getHeight(Node x, K key, int cumulativo) {
		if(x == null) return -1;
		int cmp = key.compareTo(x.key);
		if (cmp < 0) return getHeight(x.left, key, cumulativo + 1);
		else if (cmp > 0) return getHeight(x.right, key, cumulativo + 1);
		return cumulativo + 1;
	}
	public void put(K key, V value) {
		if (key == null) throw new IllegalArgumentException("first argument to put() is null");
		root = put(root, key, value);
		root.color = BLACK;
	}
	public int height() {
		return height(root);
	}
	public K min() {
		if (isEmpty()) throw new NoSuchElementException("llama min() con symbol table vacio");
		return min(root).key;
	} 
	public K max() {
		if (isEmpty()) throw new NoSuchElementException("llama max() con symbol table vacio");
		return max(root).key;
	}
	public ArregloDinamico<K> keySet() {
		if(isEmpty()) return null;
		ArregloDinamico<K> keys = new ArregloDinamico<K>(size());
		inorder(root, keys);
		return keys;
	}
	public ArregloDinamico<K> keysInRange(K init, K end) {
		if(init.compareTo(end)>0) throw new IllegalArgumentException("init is greater than end");
		ArregloDinamico<K> keys = new ArregloDinamico<K>(size());
		keys(root, keys, init, end);
		if (keys.isEmpty()) return null;
		return keys;
	}
	public ArregloDinamico<V> valuesInRange(K init, K end) {
		if(init.compareTo(end)>0) throw new IllegalArgumentException("init is greater than end");
		ArregloDinamico<V> value = new ArregloDinamico<V>(size());
		values(root, value, init, end);
		if(value.isEmpty()) return null;
		return value;
	} 
	private V get(Node x, K key) {
		while (x != null) {
			int cmp = key.compareTo(x.key);
			if (cmp < 0) x = x.left;
			else if (cmp > 0) x = x.right;
			else return x.value;
		}
		return null;
	}
	private Node put(Node h, K key, V value) { 
		if (h == null) return new Node(key, value, RED, 1);
		int cmp = key.compareTo(h.key);
		if (cmp < 0) h.left  = put(h.left,  key, value); 
		else if (cmp > 0) h.right = put(h.right, key, value); 
		else h.value = value;
		if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
		if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
		if (isRed(h.left)  &&  isRed(h.right)) flipColors(h);
		h.size = size(h.left) + size(h.right) + 1;
		return h;
	}
	private Node rotateRight(Node h) {
		Node x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = x.right.color;
		x.right.color = RED;
		x.size = h.size;
		h.size = size(h.left) + size(h.right) + 1;
		return x;
	}
	private Node rotateLeft(Node h) {
		Node x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = x.left.color;
		x.left.color = RED;
		x.size = h.size;
		h.size = size(h.left) + size(h.right) + 1;
		return x;
	}
	private void flipColors(Node h) {
		h.color = !h.color;
		h.left.color = !h.left.color;
		h.right.color = !h.right.color;
	}
	private int height(Node x) {
		if (x == null) return -1;
		return 1 + Math.max(height(x.left), height(x.right));
	}
	private Node min(Node x) { 
		if (x.left == null) return x; 
		else return min(x.left); 
	} 
	private Node max(Node x) { 
		if (x.right == null) return x; 
		else return max(x.right); 
	} 
	private void keys(Node x, ArregloDinamico<K> lista, K lo, K hi) {
		if (x == null) return;
		int cmplo = lo.compareTo(x.key);
		int cmphi = hi.compareTo(x.key);
		if (cmplo < 0) keys(x.left, lista, lo, hi);
		if (cmplo <= 0 && cmphi >= 0) lista.add(x.key);
		if (cmphi > 0) keys(x.right, lista, lo, hi);
	}
	private void values(Node x, ArregloDinamico<V> lista, K lo, K hi) {
		if (x == null) return;
		int cmplo = lo.compareTo(x.key);
		int cmphi = hi.compareTo(x.key);
		if (cmplo < 0) values(x.left, lista, lo, hi);
		if (cmplo <= 0 && cmphi >= 0) lista.add(x.value);
		if (cmphi > 0) values(x.right, lista, lo, hi);
	}
	private void inorder(Node x, ArregloDinamico<K> keys) {
		if (x == null) return;
		inorder(x.left, keys);
		keys.add(x.key);
		inorder(x.right, keys);
	}
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		toStringBuilder(root,sb);
		sb.append("}");
		return sb.toString();
	}
	private void toStringBuilder(Node x, StringBuilder sb) {
		if(x==null) return;
		toStringBuilder(x.left, sb);
		sb.append("(" + x.key + "," + x.value + ")\n");
		toStringBuilder(x.right, sb);
	}
}