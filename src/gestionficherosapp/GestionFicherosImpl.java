package gestionficherosapp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

public class GestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;

	public GestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {

		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el número de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila más
		}

		// dimensionar la matriz contenido según los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {

		System.out.println("holaaa");
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}

	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		// que se pueda escribir -> lanzará una excepción
		if (!file.canWrite())
			throw new GestionFicherosException("Error. No tiene permisos de escritura");
		// que no exista -> lanzará una excepción
		if (file.exists()) {
			throw new GestionFicherosException("Error. Ya existe una carpeta con este nombre");
		}
		// crear la carpeta -> lanzará una excepción
		try {

			file.mkdir();

		} catch (SecurityException e) {
			throw new GestionFicherosException("El nombre del archivo no es válido");
		}
		actualiza();
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		// que se pueda escribir -> lanzará una excepción
		if (!carpetaDeTrabajo.canWrite())
			throw new GestionFicherosException("Error. No tiene permisos de escritura");
		// que no exista -> lanzará una excepción
		if (file.exists()) {
			throw new GestionFicherosException("Error. Ya existe una fichero con este nombre");
		}
		// crear la carpeta -> lanzará una excepción
		try {
			file.createNewFile();

		} catch (IOException e) {
			throw new GestionFicherosException("El nombre del archivo no es válido");
		}
		actualiza();

	}

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		if (!file.canWrite())
			throw new GestionFicherosException("No tiene permisos para realizar esta acción");

		if (!file.exists())
			throw new GestionFicherosException("El archivo que intenta eliminar no existe o ya ha sido eliminado");

	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		// se controla que el nombre corresponda a una carpeta existente
		if (!file.isDirectory()) {
			throw new GestionFicherosException(
					"Error. Se ha encontrado " + file.getAbsolutePath() + " pero se esperaba un directorio");
		}
		// se controla que se tengan permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a " + file.getAbsolutePath() + ". No hay permiso");
		}
		// nueva asignación de la carpeta de trabajo
		carpetaDeTrabajo = file;
		// se requiere actualizar contenido
		actualiza();

	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {

		StringBuilder strBuilder = new StringBuilder();
		File file = new File(carpetaDeTrabajo, arg0);

		// Controlar que existe. Si no, se lanzará una excepción
		if (!file.exists()) {
			throw new GestionFicherosException("Error. El fichero" + file.getAbsolutePath()
					+ "al que intenta acceder no existe o ha sido borrado");
		} else {
			// Controlar que haya permisos de lectura. Si no, se lanzará una excepción
			if (!file.canRead()) {
				throw new GestionFicherosException("Error. No tiene permisos de Lectura sobre este fichero");
			} else {
				// Título
				strBuilder.append("INFORMACIÓN DEL SISTEMA");
				strBuilder.append("\n\n");

				// Nombre
				strBuilder.append("Nombre: ");
				strBuilder.append(arg0);
				strBuilder.append("\n");

				// Tipo: fichero o directorio
				if (file.isDirectory()) {
					strBuilder.append("Tipo archivo: Directorio");
					strBuilder.append("\n");
					// Si es directorio: Número de elementos que contiene,
					strBuilder.append("Este directorio tiene: " + file.list().length + " elementos");
					// Si es directorio: Espacio libre, espacio disponible, espacio total (bytes)
					strBuilder.append(" El espacio libre es : " + file.getFreeSpace());
					strBuilder.append("\n");
					strBuilder.append(" El espacio disponible es : " + file.getUsableSpace());

				} else {
					if (file.isFile()) {
						strBuilder.append("Tipo de archivo: Fichero");
						strBuilder.append("\n");
						// Si es un fichero: Tamaño en bytes
						strBuilder.append("El tamaño de este fichero en es : " + file.length() + "bytes");
					}
				}

				strBuilder.append("\n");
				// Ubicación
				strBuilder.append(" Ruta : " + file.getAbsolutePath());
				strBuilder.append("\n");
				// Fecha de última modificación
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				strBuilder.append("Última modificación: " + sdf.format(file.lastModified()));
				strBuilder.append("\n");
				// Si es un fichero oculto o no
				if (file.isHidden()) {
					strBuilder.append("Este archivo esta oculto");
				} else {
					strBuilder.append("Este archivo no esta oculto");
				}

				return strBuilder.toString();
			}
		}
	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		File file1 = new File(carpetaDeTrabajo, arg1);
		
		if(!file.exists()) {
			
			throw new GestionFicherosException("El fichero " + arg0 + " no exixte.");	
			
		} 
		if(!carpetaDeTrabajo.canWrite()){
			
			throw new GestionFicherosException("No tiene permiso para renombrar " + arg0);
			
		} 
		
		if(file1.exists()){
			throw new GestionFicherosException(arg0 + " ya exsiste.");
			}else {
				
				try{
					file.renameTo(file1);
					}catch(Exception GestionFicherosException) {
						throw new GestionFicherosException("Error al modificar el ficheto" + arg0 + ".txt");
					}
				}
		actualiza();
	}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;

	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);

		// se controla que la dirección exista y sea directorio
		if (!file.isDirectory()) {
			throw new GestionFicherosException(
					"Error. Se esperaba " + "un directorio, pero " + file.getAbsolutePath() + " no es un directorio.");
		}

		// se controla que haya permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a  " + file.getAbsolutePath() + ". No hay permisos");
		}

		// actualizar la carpeta de trabajo
		carpetaDeTrabajo = file;

		// actualizar el contenido
		actualiza();

	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

}
