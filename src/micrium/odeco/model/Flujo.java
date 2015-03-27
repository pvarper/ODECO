package micrium.odeco.model;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the flujo database table.
 * 
 */
@Entity
@NamedQuery(name="Flujo.findAll", query="SELECT f FROM Flujo f")
public class Flujo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="flujo_id")
	private Integer flujoId;

	private String agente;

	private String coordinador;

	private String nombre;

	private String regulacion;

	private String staff;
	
	private Boolean estado;

	public Flujo() {
	}

	public Integer getFlujoId() {
		return this.flujoId;
	}

	public void setFlujoId(Integer flujoId) {
		this.flujoId = flujoId;
	}

	public String getAgente() {
		return this.agente;
	}

	public void setAgente(String agente) {
		this.agente = agente;
	}

	public String getCoordinador() {
		return this.coordinador;
	}

	public void setCoordinador(String coordinador) {
		this.coordinador = coordinador;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRegulacion() {
		return this.regulacion;
	}

	public void setRegulacion(String regulacion) {
		this.regulacion = regulacion;
	}

	public String getStaff() {
		return this.staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "Flujo [flujoId=" + flujoId + ", agente=" + agente
				+ ", coordinador=" + coordinador + ", nombre=" + nombre
				+ ", regulacion=" + regulacion + ", staff=" + staff
				+ ", estado=" + estado + "]";
	}

	
}