package micrium.odeco.model;

import java.io.Serializable;

public class ObjetoColumna implements Serializable {
	private static final long serialVersionUID = 1L;

	private int indice;
	private String motivoReclamo;
	private int id;
	
	public int getIndice() {
		return indice;
	}
	public void setIndice(int indice) {
		this.indice = indice;
	}
	public String getMotivoReclamo() {
		return motivoReclamo;
	}
	public void setMotivoReclamo(String motivoReclamo) {
		this.motivoReclamo = motivoReclamo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return  motivoReclamo;
	}
	
	
}