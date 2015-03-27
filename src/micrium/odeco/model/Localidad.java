package micrium.odeco.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the localidad database table.
 * 
 */
@Entity
@NamedQuery(name="Localidad.findAll", query="SELECT l FROM Localidad l")
public class Localidad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LOCALIDAD_LOCALIDADID_GENERATOR", sequenceName="LOCALIDAD_LOCALIDAD_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOCALIDAD_LOCALIDADID_GENERATOR")
	@Column(name="localidad_id")
	private Integer localidadId;

	private Boolean estado;

	private String nombre;

	//bi-directional many-to-one association to Ciudad
	@ManyToOne
	@JoinColumn(name="ciudad_id")
	private Ciudad ciudad;

	public Localidad() {
	}

	public Integer getLocalidadId() {
		return this.localidadId;
	}

	public void setLocalidadId(Integer localidadId) {
		this.localidadId = localidadId;
	}

	public Boolean getEstado() {
		return this.estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Ciudad getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

}