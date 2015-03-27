package micrium.odeco.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the cola_coordinador database table.
 * 
 */
@Entity
@Table(name="cola_coordinador")
@NamedQuery(name="ColaCoordinador.findAll", query="SELECT c FROM ColaCoordinador c")
public class ColaCoordinador implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="COLA_COORDINADOR_COLACOORDINADORID_GENERATOR", sequenceName="COLA_COORDINADOR_COLA_COORDINADOR_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COLA_COORDINADOR_COLACOORDINADORID_GENERATOR")
	@Column(name="cola_coordinador_id")
	private Integer colaCoordinadorId;

	private Integer cantidad;

	@Column(name="nombre_coordinador")
	private String nombreCoordinador;

	public ColaCoordinador() {
	}

	public Integer getColaCoordinadorId() {
		return this.colaCoordinadorId;
	}

	public void setColaCoordinadorId(Integer colaCoordinadorId) {
		this.colaCoordinadorId = colaCoordinadorId;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String getNombreCoordinador() {
		return this.nombreCoordinador;
	}

	public void setNombreCoordinador(String nombreCoordinador) {
		this.nombreCoordinador = nombreCoordinador;
	}

	@Override
	public String toString() {
		return "ColaCoordinador [colaCoordinadorId=" + colaCoordinadorId
				+ ", cantidad=" + cantidad + ", nombreCoordinador="
				+ nombreCoordinador + "]";
	}
	
	

}