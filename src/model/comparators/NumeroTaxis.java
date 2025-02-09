package model.comparators;
import java.util.Comparator;
import model.logic.Compania;
public class NumeroTaxis implements Comparator<Compania> {
	public int compare(Compania arg0, Compania arg1) {
		return Integer.compare(arg0.taxis(), arg1.taxis());
	}
}