package micrium.odeco.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipo_formulario database table.
 * 
 */
@Entity
@Table(name="tipo_formulario")
@NamedQuery(name="TipoFormulario.findAll", query="SELECT t FROM TipoFormulario t")
public class TipoFormulario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_FORMULARIO_TIPOFORMULARIOID_GENERATOR", sequenceName="TIPO_FORMULARIO_TIPO_FORMULARIO_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_FORMULARIO_TIPOFORMULARIOID_GENERATOR")
	@Column(name="tipo_formulario_id")
	private Integer tipoFormularioId;

	private String descripcion;

	private Boolean estado;

	//bi-directional many-to-one association to FormularioOdeco
	@OneToMany(mappedBy="tipoFormulario")
	private List<FormularioOdeco> formularioOdecos;

	public TipoFormulario() {
	}

	public Integer getTipoFormularioId() {
		return this.tipoFormularioId;
	}

	public void setTipoFormularioId(Integer tipoFormularioId) {
		this.tipoFormularioId = tipoFormularioId;
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

	public List<FormularioOdeco> getFormularioOdecos() {
		return this.formularioOdecos;
	}

	public void setFormularioOdecos(List<FormularioOdeco> formularioOdecos) {
		this.formularioOdecos = formularioOdecos;
	}

	public FormularioOdeco addFormularioOdeco(FormularioOdeco formularioOdeco) {
		getFormularioOdecos().add(formularioOdeco);
		formularioOdeco.setTipoFormulario(this);

		return formularioOdeco;
	}

	public FormularioOdeco removeFormularioOdeco(FormularioOdeco formularioOdeco) {
		getFormularioOdecos().remove(formularioOdeco);
		formularioOdeco.setTipoFormulario(null);

		return formularioOdeco;
	}

	@Override
	public String toString() {
		return "TipoFormulario [tipoFormularioId=" + tipoFormularioId
				+ ", descripcion=" + descripcion + ", estado=" + estado + "]";
	}
	
	
}