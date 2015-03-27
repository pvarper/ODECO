package micrium.user.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the formulario database table.
 * 
 */
@Entity
@NamedQuery(name="Formulario.findAll", query="SELECT f FROM Formulario f")
public class Formulario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FORMULARIO_FORMULARIOID_GENERATOR", sequenceName="FORMULARIO_FORMULARIO_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FORMULARIO_FORMULARIOID_GENERATOR")
	@Column(name="formulario_id")
	private Integer formularioId;

	private Integer depende;

	private Boolean estado;

	private String nivel;

	private String nombre;

	private String url;

	//bi-directional many-to-one association to RolFormulario
	@OneToMany(mappedBy="formulario")
	private List<RolFormulario> rolFormularios;

	public Formulario() {
	}

	public Integer getFormularioId() {
		return this.formularioId;
	}

	public void setFormularioId(Integer formularioId) {
		this.formularioId = formularioId;
	}

	public Integer getDepende() {
		return this.depende;
	}

	public void setDepende(Integer depende) {
		this.depende = depende;
	}

	public Boolean getEstado() {
		return this.estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getNivel() {
		return this.nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<RolFormulario> getRolFormularios() {
		return this.rolFormularios;
	}

	public void setRolFormularios(List<RolFormulario> rolFormularios) {
		this.rolFormularios = rolFormularios;
	}

	public RolFormulario addRolFormulario(RolFormulario rolFormulario) {
		getRolFormularios().add(rolFormulario);
		rolFormulario.setFormulario(this);

		return rolFormulario;
	}

	public RolFormulario removeRolFormulario(RolFormulario rolFormulario) {
		getRolFormularios().remove(rolFormulario);
		rolFormulario.setFormulario(null);

		return rolFormulario;
	}

}