package model.logic;
import model.data_structures.*;
public class Taxi {
	private final String id;
	private RedBlackTree<Fecha,Double> millas;
	private RedBlackTree<Fecha,Double> dinero;
	private RedBlackTree<Fecha,Integer> servicios;
	public Taxi(String pId) {
		id = pId;
		millas = new RedBlackTree<Fecha,Double>();
		dinero = new RedBlackTree<Fecha,Double>();
		servicios = new RedBlackTree<Fecha,Integer>();
	}
	public void nuevoServicio(Fecha fecha, double trip_miles, double trip_total) {
		if(!millas.contains(fecha)) millas.put(fecha, 0.0);
		if(!dinero.contains(fecha)) dinero.put(fecha, 0.0);
		if(!servicios.contains(fecha)) servicios.put(fecha, 0);
		millas.put(fecha, millas.get(fecha) + trip_miles);
		dinero.put(fecha, dinero.get(fecha) + trip_total);
		servicios.put(fecha, servicios.get(fecha) + 1);
	}
	public double funcionAlphaDiaria(Fecha fecha) {
		if(!existeFecha(fecha)) return 0;  // Si no hay datos para la fecha, devuelve cero.
		return millas.get(fecha) * servicios.get(fecha) / dinero.get(fecha);
	}
	public double funcionAlphaDiaria(Fecha fechaInicial, Fecha fechaFinal) {
		ArregloDinamico<Double> temp = millas.valuesInRange(fechaInicial, fechaFinal);
		if(temp == null) return 0;  // Si no hay datos para las fechas, devuelve cero.
		double totalMillas = 0;
		double totalDinero = 0;
		int totalServicios = 0;
		for(double millaActual:temp) totalMillas += millaActual;
		for(double dineroActual:dinero.valuesInRange(fechaInicial,fechaFinal)) totalDinero += dineroActual;
		for(int servicioActual:servicios.valuesInRange(fechaInicial,fechaFinal)) totalServicios += servicioActual;
		return totalMillas * totalServicios / totalDinero;
	}
	private boolean existeFecha(Fecha fecha) { return millas.contains(fecha);}  // debería servir cualquiera de las tablas hash.
	public String toString() { return id;}
}