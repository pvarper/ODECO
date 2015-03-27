package micrium.odeco.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the medio_notificacion database table.
 * 
 */
@Entity
@Table(name="medio_notificacion")
@NamedQuery(name="MedioNotificacion.findAll", query="SELECT m FROM MedioNotificacion m")
public class MedioNotificacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MEDIO_NOTIFICACION_MEDIONOTIFICACIONID_GENERATOR", sequenceName="MEDIO_NOTIFICACION_MEDIO_NOTIFICACION_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MEDIO_NOTIFICACION_MEDIONOTIFICACIONID_GENERATOR")
	@Column(name="medio_notificacion_id")
	private Integer medioNotificacionId;

	private String descripcion;

	private Boolean estado;

	//bi-directional many-to-one association to FormularioOdeco
	@OneToMany(mappedBy="medioNotificacion")
	private List<FormularioOdeco> formularioOdecos;

	public MedioNotificacion() {
	}

	public Integer getMedioNotificacionId() {
		return this.medioNotificacionId;
	}

	public void setMedioNotificacionId(Integer medioNotificacionId) {
		this.medioNotificacionId = medioNotificacionId;
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
		formularioOdeco.setMedioNotificacion(this);

		return formularioOdeco;
	}

	public FormularioOdeco removeFormularioOdeco(FormularioOdeco formularioOdeco) {
		getFormularioOdecos().remove(formularioOdeco);
		formularioOdeco.setMedioNotificacion(null);

		return formularioOdeco;
	}

	@Override
	public String toString() {
		return descripcion;
	}

}