package model.logic;
public class Horario implements Comparable<Horario>{
	private final int hora;
	private final int cuartil; 
	public Horario(String time) {  //HH:MM
		hora = Integer.parseInt(time.substring(0, 2));
		int minutes = Integer.parseInt(time.substring(3,5));
		if (minutes % 15 != 0) throw new IllegalArgumentException("Minutes is not a multiple of 15: " + minutes);
		cuartil = minutes / 15;
	}
	public int hora() { return hora;}
	public int cuartil() { return cuartil;}
	public String toString() { return String.format("%02d:%02d", hora(), cuartil()*15);}
	public boolean equals(Object x) {
		if (this == x) return true;
		if (x == null) return false;
		if (this.getClass() != x.getClass()) return false;
		Horario that = (Horario) x;
		if (this.hora != that.hora) return false;
		if (this.cuartil != that.cuartil) return false;
		return true;
	}
	public int compareTo(Horario that) {
		if (this.hora > that.hora ) return +1;
		if (this.hora < that.hora ) return -1;
		if (this.cuartil > that.cuartil) return +1;
		if (this.cuartil < that.cuartil) return -1;
		return 0;
	}
	public int hashCode() { return cuartil + hora*4;}
}