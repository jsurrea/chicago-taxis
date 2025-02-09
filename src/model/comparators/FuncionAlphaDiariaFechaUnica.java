package model.comparators;
import java.util.Comparator;
import model.logic.*;
public class FuncionAlphaDiariaFechaUnica implements Comparator<Taxi> {
	private final Fecha fecha;
	public FuncionAlphaDiariaFechaUnica(Fecha fecha) { this.fecha = fecha;}
	public int compare(Taxi arg0, Taxi arg1) {
		return Double.compare(arg0.funcionAlphaDiaria(fecha), arg1.funcionAlphaDiaria(fecha));
	}
}