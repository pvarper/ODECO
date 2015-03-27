package micrium.user.model;

import java.io.Serializable;

import javax.persistence.*;

import micrium.odeco.model.Calendario;


/**
 * The persistent class for the rol_calendario database table.
 * 
 */
@Entity
@Table(name="rol_calendario")
@NamedQuery(name="RolCalendario.findAll", query="SELECT r FROM RolCalendario r")
public class RolCalendario implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RolCalendarioPK id;

	private Boolean estado;

	//bi-directional many-to-one association to Calendario
	@ManyToOne
	@JoinColumn(name="calendario_id", nullable = false, insertable = false, updatable = false)
	private Calendario calendario;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="usuario_id", nullable = false, insertable = false, updatable = false)
	private Usuario usuario;

	public RolCalendario() {
	}

	public RolCalendarioPK getId() {
		return this.id;
	}

	public void setId(RolCalendarioPK id) {
		this.id = id;
	}

	public Boolean getEstado() {
		return this.estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public Calendario getCalendario() {
		return this.calendario;
	}

	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}