package view;
public class View { 
	public void printMessage(String mensaje) { System.out.println(mensaje);}
	public void printSeparator() { printMessage("---------");}
	public void printRequirements() {
		printMessage("Seleccione el requerimiento funcional:");
		printMessage("1. Generar un reporte de las compañías con más servicios y más taxis afiliados.");
		printMessage("2. Identificar los taxis con mayor cantidad de puntos alpha diarios.");
		printMessage("3. Consultar el mejor horario para desplazarse entre dos Community Area.");
	}
}