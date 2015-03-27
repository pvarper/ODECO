package micrium.odeco.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the motivo_reclamo database table.
 * 
 */
@Entity
@Table(name="motivo_reclamo")
@NamedQuery(name="MotivoReclamo.findAll", query="SELECT m FROM MotivoReclamo m")
public class MotivoReclamo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MOTIVO_RECLAMO_MOTIVORECLAMOID_GENERATOR", sequenceName="MOTIVO_RECLAMO_MOTIVO_RECLAMO_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MOTIVO_RECLAMO_MOTIVORECLAMOID_GENERATOR")
	@Column(name="motivo_reclamo_id")
	private Integer motivoReclamoId;

	private String descripcion;
	
	private String coordinador;

	private Boolean estado;

	@Column(name="motivo_reclamo")
	private String motivoReclamo;

	@Column(name="tiempo_resolucion")
	private Integer tiempoResolucion;

	//bi-directional many-to-many association to FormularioOdeco
	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(
		name="formulario_motivo_reclamo"
		, joinColumns={
			@JoinColumn(name="motivo_reclamo_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="formulario_odeco_id")
			}
		)
	private List<FormularioOdeco> formularioOdecos;

	public MotivoReclamo() {
	}

	public Integer getMotivoReclamoId() {
		return this.motivoReclamoId;
	}

	public void setMotivoReclamoId(Integer motivoReclamoId) {
		this.motivoReclamoId = motivoReclamoId;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Boolean getEstado() {
		return this.estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getMotivoReclamo() {
		return this.motivoReclamo;
	}

	public void setMotivoReclamo(String motivoReclamo) {
		this.motivoReclamo = motivoReclamo;
	}

	public Integer getTiempoResolucion() {
		return this.tiempoResolucion;
	}

	public void setTiempoResolucion(Integer tiempoResolucion) {
		this.tiempoResolucion = tiempoResolucion;
	}

	public List<FormularioOdeco> getFormularioOdecos() {
		return this.formularioOdecos;
	}

	public void setFormularioOdecos(List<FormularioOdeco> formularioOdecos) {
		this.formularioOdecos = formularioOdecos;
	}
	
	public String getCoordinador() {
		return coordinador;
	}

	public void setCoordinador(String coordinador) {
		this.coordinador = coordinador;
	}

	@Override
	public String toString() {
		return "MotivoReclamo [motivoReclamoId=" + motivoReclamoId
				+ ", descripcion=" + descripcion + ", estado=" + estado
				+ ", motivoReclamo=" + motivoReclamo + ", tiempoResolucion="
				+ tiempoResolucion + "]";
	}

	

}