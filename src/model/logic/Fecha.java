package model.logic;
public class Fecha implements Comparable<Fecha> {
	private final int month;
	private final int day;
	private final int year;
	public Fecha(String date) {  //YYYY-MM-DD
		month = Integer.parseInt(date.substring(5, 7));
		day = Integer.parseInt(date.substring(8,10));
		year = Integer.parseInt(date.substring(0,4));
	}
	public int month() { return month; }
	public int day() { return day; }
	public int year() { return year; }
	public String toString() { return String.format("%04d-%02d-%02d", year(), month(), day());}
	public boolean equals(Object x) {
		if (this == x) return true;
		if (x == null) return false;
		if (this.getClass() != x.getClass()) return false;
		Fecha that = (Fecha) x;
		if (this.day != that.day) return false;
		if (this.month != that.month) return false;
		if (this.year != that.year) return false;
		return true;
	}
	public int compareTo(Fecha that) {
		if (this.year > that.year ) return +1;
		if (this.year < that.year ) return -1;
		if (this.month > that.month) return +1;
		if (this.month < that.month) return -1;
		if (this.day > that.day ) return +1;
		if (this.day < that.day ) return -1;
		return 0;
	}
	public int hashCode() { return day + month*31 + year*31*12;}
} 