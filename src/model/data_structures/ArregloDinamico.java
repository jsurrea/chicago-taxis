package model.data_structures;
import java.util.Iterator;
public class ArregloDinamico<T> implements Iterable<T> {
	private int capacity;
	private int size;
	private T elements[];
	public ArregloDinamico() { this(4);}
	public ArregloDinamico(int max) {
		if(max<=0) throw new IllegalArgumentException("Not a valid max capacity");
		elements = (T[]) new Object[max];
		capacity = max;
		size = 0;
	}
	public void add(T element) { 
		if (size == capacity) {
			capacity *= 2;
			T[] copia = elements;
			elements =  (T[]) new Object[capacity];
			for (int i = 0; i < size; i++) elements[i] = copia[i];
		}
		elements[size++] = element;
	}
	public T getElement(int pos) {
		if(0<=pos && pos<size) return elements[pos];
		return null;
	}
	public int size() { return size;}
	public boolean isEmpty() { return size == 0;}
	public boolean contains(T element) {
		for( int i = 0; i < size; i++) if (elements[i].equals(element)) return true;
		return false;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		for(int i=0; i<size; i++) sb.append(elements[i] + ", ");
		sb.delete(sb.length()-2, sb.length());
		sb.append("]");
		return sb.toString();
	}
	private class ArregloDinamicoIterator implements Iterator<T> {
		private int pos = 0;
		public boolean hasNext() { return pos < size;}
		public T next() { return getElement(pos++);}
	}
	public Iterator<T> iterator() { return new ArregloDinamicoIterator();}
}