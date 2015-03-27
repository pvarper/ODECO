package micrium.user.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the rol_formulario database table.
 * 
 */
@Entity
@Table(name="rol_formulario")
@NamedQuery(name="RolFormulario.findAll", query="SELECT r FROM RolFormulario r")
public class RolFormulario implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RolFormularioPK id;

	private Boolean estado;

	//bi-directional many-to-one association to Formulario
	@ManyToOne
	@JoinColumn(name="formulario_id", nullable = false, insertable = false, updatable = false)
	private Formulario formulario;

	//bi-directional many-to-one association to Rol
	@ManyToOne
	@JoinColumn(name="rol_id", nullable = false, insertable = false, updatable = false)
	private Rol rol;

	public RolFormulario() {
	}

	public RolFormularioPK getId() {
		return this.id;
	}

	public void setId(RolFormularioPK id) {
		this.id = id;
	}

	public Boolean getEstado() {
		return this.estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public Formulario getFormulario() {
		return this.formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}

	public Rol getRol() {
		return this.rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

}