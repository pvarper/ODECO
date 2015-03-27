package micrium.odeco.model;

import java.io.Serializable;

import javax.persistence.*;

import micrium.user.model.Usuario;

import java.util.List;


/**
 * The persistent class for the ciudad database table.
 * 
 */
@Entity
@NamedQuery(name="Ciudad.findAll", query="SELECT c FROM Ciudad c")
public class Ciudad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CIUDAD_CIUDADID_GENERATOR", sequenceName="CIUDAD_CIUDAD_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CIUDAD_CIUDADID_GENERATOR")
	@Column(name="ciudad_id")
	private Integer ciudadId;

	private String abreviatura;

	private Boolean estado;

	private String nombre;

	//bi-directional many-to-one association to FormularioOdeco
	@OneToMany(mappedBy="ciudad")
	private List<FormularioOdeco> formularioOdecos;

	//bi-directional many-to-one association to Localidad
	@OneToMany(mappedBy="ciudad")
	private List<Localidad> localidads;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="ciudad")
	private List<Usuario> usuarios;

	public Ciudad() {
	}

	public Integer getCiudadId() {
		return this.ciudadId;
	}

	public void setCiudadId(Integer ciudadId) {
		this.ciudadId = ciudadId;
	}

	public String getAbreviatura() {
		return this.abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
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

	public List<FormularioOdeco> getFormularioOdecos() {
		return this.formularioOdecos;
	}

	public void setFormularioOdecos(List<FormularioOdeco> formularioOdecos) {
		this.formularioOdecos = formularioOdecos;
	}

	public FormularioOdeco addFormularioOdeco(FormularioOdeco formularioOdeco) {
		getFormularioOdecos().add(formularioOdeco);
		formularioOdeco.setCiudad(this);

		return formularioOdeco;
	}

	public FormularioOdeco removeFormularioOdeco(FormularioOdeco formularioOdeco) {
		getFormularioOdecos().remove(formularioOdeco);
		formularioOdeco.setCiudad(null);

		return formularioOdeco;
	}

	public List<Localidad> getLocalidads() {
		return this.localidads;
	}

	public void setLocalidads(List<Localidad> localidads) {
		this.localidads = localidads;
	}

	public Localidad addLocalidad(Localidad localidad) {
		getLocalidads().add(localidad);
		localidad.setCiudad(this);

		return localidad;
	}

	public Localidad removeLocalidad(Localidad localidad) {
		getLocalidads().remove(localidad);
		localidad.setCiudad(null);

		return localidad;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setCiudad(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setCiudad(null);

		return usuario;
	}

	@Override
	public String toString() {
		return  nombre ;
	}

	
}