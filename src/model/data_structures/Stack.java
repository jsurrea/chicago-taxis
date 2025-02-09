package model.data_structures;
import java.util.*;
public class Stack<K> implements Iterable<K> {
	private Node<K> first;
	private int n;
	private static class Node<K> {
		private K item;
		private Node<K> next;
	}
	public Stack() {
		first = null;
		n = 0;
	}
	public boolean isEmpty() {
		return first == null;
	}
	public int size() {
		return n;
	}
	public void push(K item) {
		Node<K> oldfirst = first;
		first = new Node<K>();
		first.item = item;
		first.next = oldfirst;
		n++;
	}
	public K pop() {
		if (isEmpty()) throw new NoSuchElementException("Stack underflow");
		K item = first.item;
		first = first.next;
		n--;
		return item;
	}
	public K peek() {
		if (isEmpty()) throw new NoSuchElementException("Stack underflow");
		return first.item;
	}
	public String toString() {
		StringBuilder s = new StringBuilder("[");
		for (K item : this) s.append(item + "\n");
		s.append("]");
		return s.toString();
	}
	public Iterator<K> iterator() {
		return new LinkedIterator(first);
	}
	private class LinkedIterator implements Iterator<K> {
		private Node<K> current;
		public LinkedIterator(Node<K> first) { current = first;}
		public boolean hasNext() { return current != null;}
		public K next() {
			K item = current.item;
			current = current.next; 
			return item;
		}
	}
}