package micrium.odeco.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the respuesta_odeco database table.
 * 
 */
@Entity
@Table(name="respuesta_odeco")
@NamedQuery(name="RespuestaOdeco.findAll", query="SELECT r FROM RespuestaOdeco r")
public class RespuestaOdeco implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="RESPUESTA_ODECO_RESPUESTAODECOID_GENERATOR", sequenceName="RESPUESTA_ODECO_RESPUESTA_ODECO_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RESPUESTA_ODECO_RESPUESTAODECOID_GENERATOR")
	@Column(name="respuesta_odeco_id")
	private Integer respuestaOdecoId;

	private String descripcion;

	private Boolean estado;

	@Column(name="respuesta_odeco")
	private String respuestaOdeco;

	//bi-directional many-to-one association to FormularioOdeco
	@OneToMany(mappedBy="respuestaOdeco")
	private List<FormularioOdeco> formularioOdecos;

	public RespuestaOdeco() {
	}

	public Integer getRespuestaOdecoId() {
		return this.respuestaOdecoId;
	}

	public void setRespuestaOdecoId(Integer respuestaOdecoId) {
		this.respuestaOdecoId = respuestaOdecoId;
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

	public String getRespuestaOdeco() {
		return this.respuestaOdeco;
	}

	public void setRespuestaOdeco(String respuestaOdeco) {
		this.respuestaOdeco = respuestaOdeco;
	}

	public List<FormularioOdeco> getFormularioOdecos() {
		return this.formularioOdecos;
	}

	public void setFormularioOdecos(List<FormularioOdeco> formularioOdecos) {
		this.formularioOdecos = formularioOdecos;
	}

	public FormularioOdeco addFormularioOdeco(FormularioOdeco formularioOdeco) {
		getFormularioOdecos().add(formularioOdeco);
		formularioOdeco.setRespuestaOdeco(this);

		return formularioOdeco;
	}

	public FormularioOdeco removeFormularioOdeco(FormularioOdeco formularioOdeco) {
		getFormularioOdecos().remove(formularioOdeco);
		formularioOdeco.setRespuestaOdeco(null);

		return formularioOdeco;
	}

	@Override
	public String toString() {
		return respuestaOdeco;
	}

}