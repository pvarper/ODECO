package micrium.user.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the rol database table.
 * 
 */
@Entity
@NamedQuery(name="Rol.findAll", query="SELECT r FROM Rol r")
public class Rol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ROL_ROLID_GENERATOR", sequenceName="ROL_ROL_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROL_ROLID_GENERATOR")
	@Column(name="rol_id")
	private Integer rolId;

	private String descripcion;

	private Boolean estado;

	private String nombre;

	//bi-directional many-to-one association to GrupoAd
	@OneToMany(mappedBy="rol")
	private List<GrupoAd> grupoAds;

	//bi-directional many-to-one association to RolFormulario
	@OneToMany(mappedBy="rol")
	private List<RolFormulario> rolFormularios;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="rol")
	private List<Usuario> usuarios;

	public Rol() {
	}

	public Integer getRolId() {
		return this.rolId;
	}

	public void setRolId(Integer rolId) {
		this.rolId = rolId;
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

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<GrupoAd> getGrupoAds() {
		return this.grupoAds;
	}

	public void setGrupoAds(List<GrupoAd> grupoAds) {
		this.grupoAds = grupoAds;
	}

	public GrupoAd addGrupoAd(GrupoAd grupoAd) {
		getGrupoAds().add(grupoAd);
		grupoAd.setRol(this);

		return grupoAd;
	}

	public GrupoAd removeGrupoAd(GrupoAd grupoAd) {
		getGrupoAds().remove(grupoAd);
		grupoAd.setRol(null);

		return grupoAd;
	}

	public List<RolFormulario> getRolFormularios() {
		return this.rolFormularios;
	}

	public void setRolFormularios(List<RolFormulario> rolFormularios) {
		this.rolFormularios = rolFormularios;
	}

	public RolFormulario addRolFormulario(RolFormulario rolFormulario) {
		getRolFormularios().add(rolFormulario);
		rolFormulario.setRol(this);

		return rolFormulario;
	}

	public RolFormulario removeRolFormulario(RolFormulario rolFormulario) {
		getRolFormularios().remove(rolFormulario);
		rolFormulario.setRol(null);

		return rolFormulario;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setRol(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setRol(null);

		return usuario;
	}

}