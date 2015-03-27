package micrium.odeco.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;


/**
 * The persistent class for the adjunto database table.
 * 
 */
@Entity
@NamedQuery(name="Adjunto.findAll", query="SELECT a FROM Adjunto a")
public class Adjunto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ADJUNTO_ADJUNTOID_GENERATOR", sequenceName="ADJUNTO_ADJUNTO_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ADJUNTO_ADJUNTOID_GENERATOR")
	@Column(name="adjunto_id")
	private Integer adjuntoId;

	private Boolean estado;

	private String nombre;
	
	private String login;
	
	private Timestamp fecha;
	
	private String rol;

	private String ruta;

	//bi-directional many-to-one association to FormularioOdeco
	@ManyToOne
	@JoinColumn(name="id_formulario_odeco")
	private FormularioOdeco formularioOdeco;

	public Adjunto() {
	}

	public Integer getAdjuntoId() {
		return this.adjuntoId;
	}

	public void setAdjuntoId(Integer adjuntoId) {
		this.adjuntoId = adjuntoId;
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

	public String getRuta() {
		return this.ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public FormularioOdeco getFormularioOdeco() {
		return this.formularioOdeco;
	}

	public void setFormularioOdeco(FormularioOdeco formularioOdeco) {
		this.formularioOdeco = formularioOdeco;
	}


	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	@Override
	public String toString() {
		return "Adjunto [adjuntoId=" + adjuntoId + ", estado=" + estado
				+ ", nombre=" + nombre + ", login=" + login + ", fecha="
				+ fecha + ", rol=" + rol + ", ruta=" + ruta + "]";
	}
	
	
	
}