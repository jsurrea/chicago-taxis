package model.data_structures;
import java.util.Comparator;
public class MaxPQ<Key> {
	private Key[] pq;
	private int n;
	private Comparator<Key> comparator;  
	public MaxPQ(int initCapacity, Comparator<Key> comparator) {
		this.comparator = comparator;
		pq = (Key[]) new Object[initCapacity + 1];
		n = 0;
	}
	public boolean isEmpty() {return n == 0;}
	public int size() {return n;}
	public Key max() {
		if (isEmpty()) return null;
		return pq[1];
	}
	private void resize(int capacity) {
		assert capacity > n;
		Key[] temp = (Key[]) new Object[capacity];
		for (int i = 1; i <= n; i++) temp[i] = pq[i];
		pq = temp;
	}
	public void insert(Key x) {
		if (n == pq.length - 1) resize(2*pq.length);
		pq[++n] = x;
		swim(n);
		assert isMaxHeap();
	}
	public Key delMax() {
		if (isEmpty()) return null;
		Key max = pq[1];
		exch(1, n--);
		sink(1);
		pq[n+1] = null;
		if ((n > 0) && (n == (pq.length - 1) / 4)) resize(pq.length / 2);
		assert isMaxHeap();
		return max;
	}
	private void swim(int k) {
		while (k > 1 && less(k/2, k)) {
			exch(k, k/2);
			k = k/2;
		}
	}
	private void sink(int k) {
		while (2*k <= n) {
			int j = 2*k;
			if (j < n && less(j, j+1)) j++;
			if (!less(k, j)) break;
			exch(k, j);
			k = j;
		}
	}
	private boolean less(int i, int j) { return comparator.compare(pq[i], pq[j]) < 0;}
	private void exch(int i, int j) {
		Key swap = pq[i];
		pq[i] = pq[j];
		pq[j] = swap;
	}
	private boolean isMaxHeap() {
		for (int i = 1; i <= n; i++) if (pq[i] == null) return false;
		for (int i = n+1; i < pq.length; i++) if (pq[i] != null) return false;
		if (pq[0] != null) return false;
		return isMaxHeapOrdered(1);
	}
	private boolean isMaxHeapOrdered(int k) {
		if (k > n) return true;
		int left = 2*k;
		int right = 2*k + 1;
		if (left  <= n && less(k, left))  return false;
		if (right <= n && less(k, right)) return false;
		return isMaxHeapOrdered(left) && isMaxHeapOrdered(right);
	}
}