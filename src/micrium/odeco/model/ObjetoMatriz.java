package micrium.odeco.model;

import java.io.Serializable;
import java.util.List;

public class ObjetoMatriz implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<ObjetoColumna> columnas;
	private List<Matriz> filas;
	
	public List<Matriz> getFilas() {
		return filas;
	}
	public void setFilas(List<Matriz> filas) {
		this.filas = filas;
	}
	public List<ObjetoColumna> getColumnas() {
		return columnas;
	}
	public void setColumnas(List<ObjetoColumna> columnas) {
		this.columnas = columnas;
	}

}
