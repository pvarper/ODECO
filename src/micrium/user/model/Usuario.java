package micrium.user.model;

import java.io.Serializable;

import javax.persistence.*;

import micrium.odeco.model.Ciudad;
import micrium.odeco.model.FormularioOdeco;

import java.util.List;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USUARIO_USUARIOID_GENERATOR", sequenceName="USUARIO_USUARIO_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USUARIO_USUARIOID_GENERATOR")
	@Column(name="usuario_id")
	private Integer usuarioId;

	private String correo;

	private Boolean estado;
	
	private Boolean especial;

	private String login;

	private String nombre;

	private String telefono;

	//bi-directional many-to-one association to FormularioOdeco
	@OneToMany(mappedBy="usuario")
	private List<FormularioOdeco> formularioOdecos;

	//bi-directional many-to-one association to RolCalendario
	@OneToMany(mappedBy="usuario")
	private List<RolCalendario> rolCalendarios;

	//bi-directional many-to-one association to Ciudad
	@ManyToOne
	@JoinColumn(name="ciudad_id")
	private Ciudad ciudad;

	//bi-directional many-to-one association to Rol
	@ManyToOne
	@JoinColumn(name="rol_id")
	private Rol rol;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="responsable_id")
	private Usuario usuario;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="usuario")
	private List<Usuario> usuarios;

	public Usuario() {
	}

	public Integer getUsuarioId() {
		return this.usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Boolean getEstado() {
		return this.estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public List<FormularioOdeco> getFormularioOdecos() {
		return this.formularioOdecos;
	}

	public void setFormularioOdecos(List<FormularioOdeco> formularioOdecos) {
		this.formularioOdecos = formularioOdecos;
	}

	public FormularioOdeco addFormularioOdeco(FormularioOdeco formularioOdeco) {
		getFormularioOdecos().add(formularioOdeco);
		formularioOdeco.setUsuario(this);

		return formularioOdeco;
	}

	public FormularioOdeco removeFormularioOdeco(FormularioOdeco formularioOdeco) {
		getFormularioOdecos().remove(formularioOdeco);
		formularioOdeco.setUsuario(null);

		return formularioOdeco;
	}

	public List<RolCalendario> getRolCalendarios() {
		return this.rolCalendarios;
	}

	public void setRolCalendarios(List<RolCalendario> rolCalendarios) {
		this.rolCalendarios = rolCalendarios;
	}

	public RolCalendario addRolCalendario(RolCalendario rolCalendario) {
		getRolCalendarios().add(rolCalendario);
		rolCalendario.setUsuario(this);

		return rolCalendario;
	}

	public RolCalendario removeRolCalendario(RolCalendario rolCalendario) {
		getRolCalendarios().remove(rolCalendario);
		rolCalendario.setUsuario(null);

		return rolCalendario;
	}

	public Ciudad getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public Rol getRol() {
		return this.rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setUsuario(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setUsuario(null);

		return usuario;
	}
	
	

	public Boolean getEspecial() {
		return especial;
	}

	public void setEspecial(Boolean especial) {
		this.especial = especial;
	}

	@Override
	public String toString() {
		return "Usuario [usuarioId=" + usuarioId + ", correo=" + correo
				+ ", estado=" + estado + ", login=" + login + ", nombre="
				+ nombre + ", telefono=" + telefono + ", ciudad=" + ciudad
				+ ", rol=" + rol + "]";
	}

}