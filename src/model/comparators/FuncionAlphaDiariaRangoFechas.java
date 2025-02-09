package model.comparators;
import java.util.Comparator;
import model.logic.*;
public class FuncionAlphaDiariaRangoFechas implements Comparator<Taxi> {
	private final Fecha fechaInicial;
	private final Fecha fechaFinal;
	public FuncionAlphaDiariaRangoFechas(Fecha fechaInicial, Fecha fechaFinal) {
		this.fechaInicial = fechaInicial;
		this.fechaFinal = fechaFinal;
	}
	public int compare(Taxi arg0, Taxi arg1) {
		return Double.compare(arg0.funcionAlphaDiaria(fechaInicial,fechaFinal), arg1.funcionAlphaDiaria(fechaInicial,fechaFinal));
	}
}