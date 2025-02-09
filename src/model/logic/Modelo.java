package model.logic;
import java.io.FileReader;
import com.opencsv.*;
import model.comparators.*;
import model.data_structures.*;
public class Modelo {
	private final static int[] small = {1,2,4,5,12,14,19,7};  // Índices para cada dataset: small, medium o large.
	private final static int[] medium = {1,2,4,5,11,13,6,7};  // El orden en que se guardan en los arreglos es:
	private final static int[] large = {1,2,4,5,10,12,15,16};  // taxi_id, trip_start_timestamp, trip_seconds, trip_miles, trip_total, company, pickup_community_area, dropoff_community_area
	private TablaHashLinearProbing<String,Compania> companias;
	private TablaHashLinearProbing<String,Taxi> taxis;
	private RedBlackTree<Horario,WeightedDiGraph<Double>> recorridos;
	public int taxis() { return taxis.size();}
	public int companias() { return companias.size();}
	public Modelo() {
		companias = new TablaHashLinearProbing<String,Compania>();
		taxis = new TablaHashLinearProbing<String,Taxi>();
		recorridos = new RedBlackTree<Horario,WeightedDiGraph<Double>>();
	}
	public void load(String datasetSize) throws Exception {
		String ruta = "data/taxi-trips-wrvz-psew-subset-" + datasetSize + ".csv";
		CSVParser csvParser = new CSVParserBuilder().withSeparator(',').build();
		CSVReader reader = new CSVReaderBuilder(new FileReader(ruta)).withCSVParser(csvParser).build();
		String [] nextLine = reader.readNext();
		int[] indices = new int[8];
		switch(datasetSize) {
		case "small":
			indices = small;
			break;
		case "medium":
			indices = medium;
			break;
		case "large":
			indices = large;
			break;
		}
		while ((nextLine = reader.readNext()) != null) {
			String taxi_id = nextLine[indices[0]];
			String trip_start_timestamp = nextLine[indices[1]];
			Fecha fecha = new Fecha(trip_start_timestamp.substring(0,10));  // Extraer la fecha
			Horario horario = new Horario(trip_start_timestamp.substring(11,16));  // Extraer el horario
			// se ignoran los viajes con info incompleta (trip_seconds,trip_miles,trip_total nulos o cero; pickup_community_area, dropoff_community_area nulos).
			if(nextLine[indices[2]].equals("") || nextLine[indices[3]].equals("") || nextLine[indices[4]].equals("") || nextLine[indices[6]].equals("") || nextLine[indices[7]].equals("")) continue;
			double trip_seconds = Double.parseDouble(nextLine[indices[2]]);
			double trip_miles = Double.parseDouble(nextLine[indices[3]]);
			double trip_total = Double.parseDouble(nextLine[indices[4]]);
			if(trip_miles <= 0 || trip_total <= 0) continue;  // Ignorar viajes que tienen millas = 0.0 ó dinero = 0.0
			String company = nextLine[indices[5]];
			if(company.equals("")) company = "Independent Owner";  // Al parecer no hay Independent Owners en el dataset Large.
			double pickup_community_area = Double.parseDouble(nextLine[indices[6]]);
			double dropoff_community_area = Double.parseDouble(nextLine[indices[7]]);
			if(pickup_community_area == dropoff_community_area) continue;  // ignorar viajes desde y hacia la misma community_area
			// compañías
			if(!companias.contains(company)) companias.put(company, new Compania(company));
			companias.get(company).nuevoServicio();
			// taxis
			if(!taxis.contains(taxi_id)) {  // Para simplificar, cada taxi se añade a la primera compañía con la que aparece afiliado en el dataset.
				taxis.put(taxi_id, new Taxi(taxi_id));
				companias.get(company).nuevoTaxi();
			}
			taxis.get(taxi_id).nuevoServicio(fecha, trip_miles, trip_total);
			// recorridos
			if(!recorridos.contains(horario)) recorridos.put(horario, new WeightedDiGraph<Double>(horario.toString()));
			recorridos.get(horario).addEdge(pickup_community_area, dropoff_community_area, trip_seconds);
		}
		reader.close();
		for(Compania compania : companias.valueSet()) if(compania.taxis() == 0) companias.remove(compania.toString());  // Quitar las compañías sin taxis
	}
	public Stack<Compania> topCompaniaTaxis(int top) {
		HeapsortDescendente<Compania> sorter = new HeapsortDescendente<Compania>(top, new NumeroTaxis(), companias.valueSet());
		return sorter.sorted();
	}
	public Stack<Compania> topCompaniaServicios(int top) {
		HeapsortDescendente<Compania> sorter = new HeapsortDescendente<Compania>(top, new NumeroServicios(), companias.valueSet());
		return sorter.sorted();
	}
	public Stack<Taxi> topTaxiFechaUnica(int top, Fecha fecha) {
		HeapsortDescendente<Taxi> sorter = new HeapsortDescendente<Taxi>(top, new FuncionAlphaDiariaFechaUnica(fecha), taxis.valueSet());
		return sorter.sorted();
	}
	public Stack<Taxi> topTaxiRangoFechas(int top, Fecha lower, Fecha upper) {
		HeapsortDescendente<Taxi> sorter = new HeapsortDescendente<Taxi>(top, new FuncionAlphaDiariaRangoFechas(lower,upper), taxis.valueSet());
		return sorter.sorted();
	}
	public ShortestPath<Double> mejorHorario(double origen, double destino, Horario lower, Horario upper) {
		ArregloDinamico<WeightedDiGraph<Double>> grafos = recorridos.valuesInRange(lower, upper);
		if(grafos == null) return null;  // No existen grafos entre los horarios dados.
		ShortestPath<Double> minimum = null;
		for(WeightedDiGraph<Double> grafo : grafos) {
			ShortestPath<Double> dijkstra = grafo.shortestPathFrom(origen,destino);
			if(dijkstra == null) continue;  // No existen o el origen o el destino en el grafo actual
			if(!dijkstra.hasPathTo()) continue;  // No hay path de origen a destino
			if(minimum == null || dijkstra.distTo() < minimum.distTo()) minimum = dijkstra;
		}
		return minimum;
	}
}