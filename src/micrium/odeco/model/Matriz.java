package micrium.odeco.model;

import java.io.Serializable;
import java.util.List;

public class Matriz implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<ObjetoColumna> columnas;
	private List<ObjetoFila> filas;
	
	
	public List<ObjetoColumna> getColumnas() {
		//columnas.add(0,new)
		return columnas;
	}
	public void setColumnas(List<ObjetoColumna> columnas) {
		this.columnas = columnas;
	}
	public List<ObjetoFila> getFilas() {
		return filas;
	}
	public void setFilas(List<ObjetoFila> filas) {
		this.filas = filas;
	}
	
	

	

	
	

	

}
