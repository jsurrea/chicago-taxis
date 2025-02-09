package model.comparators;
import java.util.Comparator;
import model.data_structures.*;
public class HeapsortAscendente<K> {
	private final int tamano;  // Número de elementos que debe ordenar la clase.
	private MaxPQ<K> priority;
	public HeapsortAscendente(int tamano, Comparator<K> comparador, Iterable<K> datos) {
		if(tamano<1) throw new IllegalArgumentException("Tamaño debe ser al menos 1.");
		this.tamano = tamano;
		priority = new MaxPQ<K>(this.tamano + 1, comparador);
		int inicializador = 0;
		for(K dato : datos) {
			priority.insert(dato);  // Insertar el dato que entra.
			if(inicializador++<tamano) continue;  // Inicializar los primeros <tamano> datos.
			priority.delMax();  // Borrar el que no debe quedar en el top.
		}
	}
	public Stack<K> sorted() {
		Stack<K> sorted = new Stack<K>();
		while(!priority.isEmpty()) sorted.push(priority.delMax());
		return sorted;
	}
}