package model.data_structures;
public class TablaHashLinearProbing<K,V> {
	private int n;
	private int m;
	private K[] keys;
	private V[] vals;
	public TablaHashLinearProbing() { this(4);}
	public TablaHashLinearProbing(int capacity) {
		m = capacity;
		n = 0;
		keys = (K[]) new Comparable[m];
		vals = (V[]) new Object[m];
	}
	public int size() { return n;}
	public boolean isEmpty() { return size() == 0;}
	public boolean contains(K key) {
		if (key == null) throw new IllegalArgumentException("argument to contains() is null");
		return get(key) != null;
	}
	private int hash(K key) {
		int h = key.hashCode();
		h ^= (h >>> 20) ^ (h >>> 12) ^ (h >>> 7) ^ (h >>> 4);
		return h & (m-1);
	}
	private void resize(int capacity) {
		TablaHashLinearProbing<K, V> temp = new TablaHashLinearProbing<K, V>(capacity);
		for (int i = 0; i < m; i++) if (keys[i] != null) temp.put(keys[i], vals[i]);
		keys = temp.keys;
		vals = temp.vals;
		m = temp.m;
	}
	public void put(K key, V val) {
		if (key == null) throw new IllegalArgumentException("first argument to put() is null");
		if (val == null) {
			remove(key);
			return;
		}
		if (n >= m/2) resize(2*m);  // double table size if 50% full
		int i;
		for (i = hash(key); keys[i] != null; i = (i + 1) % m) {
			if (keys[i].equals(key)) {
				vals[i] = val;
				return;
			}
		}
		keys[i] = key;
		vals[i] = val;
		n++;
	}
	public V get(K key) {
		if (key == null) throw new IllegalArgumentException("argument to get() is null");
		for (int i = hash(key); keys[i] != null; i = (i + 1) % m) if (keys[i].equals(key)) return vals[i];
		return null;
	}
	public void remove(K key) {
		if (key == null) throw new IllegalArgumentException("argument to delete() is null");
		if (!contains(key)) return;
		int i = hash(key);
		while (!key.equals(keys[i])) {
			i = (i + 1) % m;
		}
		keys[i] = null;
		vals[i] = null;
		i = (i + 1) % m;
		while (keys[i] != null) {
			K   keyToRehash = keys[i];
			V valToRehash = vals[i];
			keys[i] = null;
			vals[i] = null;
			n--;
			put(keyToRehash, valToRehash);
			i = (i + 1) % m;
		}
		n--;
		if (n > 0 && n <= m/8) resize(m/2);  // halves size of array if it's 12.5% full or less
		assert check();
	}
	public ArregloDinamico<K> keySet() {
		ArregloDinamico<K> array = new ArregloDinamico<K>(size());
		for (int i = 0; i < m; i++) if (keys[i] != null) array.add(keys[i]);
		return array;
	}
	public ArregloDinamico<V> valueSet() {
		ArregloDinamico<V> array = new ArregloDinamico<V>(size());
		for (int i = 0; i < m; i++) if (vals[i] != null) array.add(vals[i]);
		return array;
	}
	private boolean check() {
		if (m < 2*n) {  // check that hash table is at most 50% full
			System.err.println("Hash table size m = " + m + "; array size n = " + n);
			return false;
		}
		for (int i = 0; i < m; i++) {  // check that each key in table can be found by get()
			if (keys[i] == null) continue;
			else if (get(keys[i]) != vals[i]) {
				System.err.println("get[" + keys[i] + "] = " + get(keys[i]) + "; vals[i] = " + vals[i]);
				return false;
			}
		}
		return true;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		for (int i = 0; i < m; i++) if(keys[i] != null) sb.append("(" + keys[i] + "," + vals[i] + ")\n");
		sb.append("}");
		return sb.toString();
	}
}