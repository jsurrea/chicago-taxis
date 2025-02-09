package model.logic;
public class Compania {
	private final String nombre;
	private int taxis;
	private int servicios;
	public Compania(String pNombre) {
		nombre = pNombre;
		taxis = 0;
		servicios = 0;
	}
	public String toString() { return nombre;}
	public void nuevoServicio() { servicios++;}
	public void nuevoTaxi() { taxis++;}
	public int servicios() { return servicios;}
	public int taxis() { return taxis;}
}