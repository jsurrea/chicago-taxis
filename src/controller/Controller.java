package controller;
import java.util.Scanner;
import model.data_structures.*;
import model.logic.*;
import view.View;
public class Controller {
	private Modelo modelo;
	private View view;
	public Controller () {
		view = new View();
		modelo = new Modelo();
	}
	public void run() {
		Scanner lector = new Scanner(System.in);
		try {
			view.printMessage("¡Bienvenido!");
			cargarDatos(lector);
			boolean fin = false;
			while(!fin) {
				view.printSeparator();
				view.printRequirements();
				int option = lector.nextInt();
				switch(option){
				case 1:
					view.printSeparator();
					requerimientoA(lector);
					break;
				case 2:
					view.printSeparator();
					requerimientoB(lector);
					break;
				case 3:
					view.printSeparator();
					requerimientoC(lector);
					break;
				default: 
					view.printSeparator();
					view.printMessage("¡Opción inválida!");
					break;
				}
			}
		}
		catch(Exception e) {
			view.printSeparator();
			view.printMessage("Ocurrió un error:\n");
			e.printStackTrace();
			view.printSeparator();
		}
		finally {lector.close();}
	}
	private void cargarDatos(Scanner lector) throws Exception {
		view.printMessage("Ingrese el String correspondiente al tamaño del dataset (small/medium/large): "); 
		String datasetSize = lector.nextLine();
		switch(datasetSize) {
		case "small":
			view.printMessage("Tiempo estimado de carga: 800 milisegundos.");
			break;
		case "medium":
			view.printMessage("Tiempo estimado de carga: 2500 milisegundos.");
			break;
		case "large":
			view.printMessage("Tiempo estimado de carga: 18000 milisegundos.");
			break;
		default:
			view.printMessage("No ingresó un String válido.");
			break;
		}
		long inicio = System.currentTimeMillis();
		modelo.load(datasetSize);
		long fin = System.currentTimeMillis();
		view.printMessage("\nLa carga duró " + (fin-inicio) + " milisegundos.");
	}
	private void requerimientoA(Scanner lector) {
		view.printMessage("El número total de taxis es: " + modelo.taxis());
		view.printMessage("El número total de compañías es: " + modelo.companias());
		view.printMessage("Ingrese el valor de M para calcular el top M de compañías ordenada por la cantidad de taxis afiliados: ");
		int M = lector.nextInt();
		long inicio = System.currentTimeMillis();
		Stack<Compania> stack = modelo.topCompaniaTaxis(M);
		long fin = System.currentTimeMillis();
		view.printMessage("Top\tCompañía\tCantidad de taxis afiliados\n");
		int top = 1;
		for(Compania compania : stack) view.printMessage((top++)+".\t"+compania+"\t"+compania.taxis());
		view.printMessage("\nEl requerimiento duró " + (fin-inicio) + " milisegundos.");
		view.printMessage("Ingrese el valor de N para calcular el top N de compañías que más servicios prestaron: ");
		int N = lector.nextInt();
		inicio = System.currentTimeMillis();
		stack = modelo.topCompaniaServicios(N);
		fin = System.currentTimeMillis();
		view.printMessage("Top\tCompañía\tCantidad de servicios prestados\n");
		top = 1;
		for(Compania compania : stack) view.printMessage((top++)+".\t"+compania+"\t"+compania.servicios());
		view.printMessage("\nEl requerimiento duró " + (fin-inicio) + " milisegundos.");
	}
	private void requerimientoB(Scanner lector) {
		view.printMessage("Tenga en cuenta que las fechas deben estar en formato YYYY-MM-DD.");
		view.printMessage("Ingrese el valor de N para identificar los N taxis con más puntos en una fecha determinada: ");
		int N = lector.nextInt();
		lector.nextLine();
		view.printMessage("Ingrese la fecha que desea consultar: ");
		Fecha fechaUnica = new Fecha(lector.nextLine());
		long inicio = System.currentTimeMillis();
		Stack<Taxi> stack = modelo.topTaxiFechaUnica(N, fechaUnica);
		long fin = System.currentTimeMillis();
		view.printMessage("Top\tTaxi ID\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPuntos\n");
		int top = 1;
		for(Taxi taxi : stack) view.printMessage((top++)+".\t"+taxi+"\t"+taxi.funcionAlphaDiaria(fechaUnica));
		view.printMessage("\nEl requerimiento duró " + (fin-inicio) + " milisegundos.");
		view.printMessage("Ingrese el valor de M para identificar los M taxis con más puntos para un rango entre dos fechas determinadas: ");
		int M = lector.nextInt();
		lector.nextLine();
		view.printMessage("Ingrese el rango inferior de fechas: ");
		Fecha fechaInferior = new Fecha(lector.nextLine());
		view.printMessage("Ingrese el rango superior de fechas: ");
		Fecha fechaSuperior = new Fecha(lector.nextLine());
		inicio = System.currentTimeMillis();
		stack = modelo.topTaxiRangoFechas(M, fechaInferior, fechaSuperior);
		fin = System.currentTimeMillis();
		view.printMessage("Top\tTaxi ID\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPuntos\n");
		top = 1;
		for(Taxi taxi : stack) view.printMessage((top++)+".\t"+taxi+"\t"+taxi.funcionAlphaDiaria(fechaInferior, fechaSuperior));
		view.printMessage("\nEl requerimiento duró " + (fin-inicio) + " milisegundos.");
	}
	private void requerimientoC(Scanner lector) {
		view.printMessage("Tenga en cuenta que las horas deben estar en formato HH:MM y deben estar aproximadas a 15 minutos.");
		view.printMessage("Ingrese el identificador de la Community Area origen: ");
		int origen = lector.nextInt();
		view.printMessage("Ingrese el identificador de la Community Area destino: ");
		int destino = lector.nextInt();
		lector.nextLine();
		view.printMessage("Ingrese el rango inferior de horas: ");
		Horario horaInferior = new Horario(lector.nextLine());
		view.printMessage("Ingrese el rango superior de horas: ");
		Horario horaSuperior = new Horario(lector.nextLine());
		long inicio = System.currentTimeMillis();
		ShortestPath<Double> sp = modelo.mejorHorario(origen, destino, horaInferior, horaSuperior);
		long fin = System.currentTimeMillis();
		if(sp == null) {
			view.printMessage("En este rango de horas no se encontraron viajes desde " + origen +  " hasta " + destino + ".");
			return;
		}
		view.printMessage("El mejor horario de inicio de viaje es: " + sp);
		view.printMessage("La ruta desde " + origen +  " hasta " + destino + " es: ");
		for(WeightedDiGraph<Double>.DirectedEdge edge : sp.pathTo()) view.printMessage(edge.toString());
		view.printMessage("Esta ruta tiene una duración total de " + sp.distTo() + " segundos.");
		view.printMessage("\nEl requerimiento duró " + (fin-inicio) + " milisegundos.");
	}
}