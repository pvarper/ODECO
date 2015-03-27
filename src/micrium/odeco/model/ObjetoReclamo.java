package micrium.odeco.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the objeto_reclamo database table.
 * 
 */
@Entity
@Table(name="objeto_reclamo")
@NamedQuery(name="ObjetoReclamo.findAll", query="SELECT o FROM ObjetoReclamo o")
public class ObjetoReclamo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OBJETO_RECLAMO_OBJETORECLAMOID_GENERATOR", sequenceName="OBJETO_RECLAMO_OBJETO_RECLAMO_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OBJETO_RECLAMO_OBJETORECLAMOID_GENERATOR")
	@Column(name="objeto_reclamo_id")
	private Integer objetoReclamoId;

	private String descripcion;

	private Boolean estado;

	@Column(name="objeto_reclamo")
	private String objetoReclamo;

	//bi-directional many-to-many association to FormularioOdeco
	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(
		name="formulario_objeto_reclamo"
		, joinColumns={
			@JoinColumn(name="objeto_reclamo_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="formulario_odeco_id")
			}
		)
	private List<FormularioOdeco> formularioOdecos;

	public ObjetoReclamo() {
	}

	public Integer getObjetoReclamoId() {
		return this.objetoReclamoId;
	}

	public void setObjetoReclamoId(Integer objetoReclamoId) {
		this.objetoReclamoId = objetoReclamoId;
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

	public String getObjetoReclamo() {
		return this.objetoReclamo;
	}

	public void setObjetoReclamo(String objetoReclamo) {
		this.objetoReclamo = objetoReclamo;
	}

	public List<FormularioOdeco> getFormularioOdecos() {
		return this.formularioOdecos;
	}

	public void setFormularioOdecos(List<FormularioOdeco> formularioOdecos) {
		this.formularioOdecos = formularioOdecos;
	}

	@Override
	public String toString() {
		return "ObjetoReclamo [objetoReclamoId=" + objetoReclamoId
				+ ", descripcion=" + descripcion + ", estado=" + estado
				+ ", objetoReclamo=" + objetoReclamo + "]";
	}



}