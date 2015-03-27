package micrium.odeco.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the devuelto database table.
 * 
 */
@Entity
@NamedQuery(name="Devuelto.findAll", query="SELECT d FROM Devuelto d")
public class Devuelto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DEVUELTO_DEVUELVTOID_GENERATOR", sequenceName="DEVUELTO_DEVUELVTO_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEVUELTO_DEVUELVTOID_GENERATOR")
	@Column(name="devuelvto_id")
	private Integer devuelvtoId;

	@Column(name="ciudad_localidad")
	private Boolean ciudadLocalidad;

	@Column(name="ciudad_reclamacion")
	private Boolean ciudadReclamacion;

	private Boolean correo;

	@Column(name="direccion_domicilio")
	private Boolean direccionDomicilio;

	@Column(name="direccion_notificacion")
	private Boolean direccionNotificacion;

	@Column(name="linea_reclamo")
	private Boolean lineaReclamo;

	@Column(name="medio_notificacion")
	private Boolean medioNotificacion;

	@Column(name="motivo_reclamo")
	private Boolean motivoReclamo;

	@Column(name="nombre_reclamante")
	private Boolean nombreReclamante;

	@Column(name="nombre_titular")
	private Boolean nombreTitular;

	@Column(name="objeto_reclamo")
	private Boolean objetoReclamo;

	@Column(name="telefono_referencia")
	private Boolean telefonoReferencia;
	
	@Column(name="forma_registro")
	private Boolean formaRegistro;
	
	@Column(name="fecha_resolucion")
	private Boolean fechaResolucion;
	@Column(name="fecha_incidencia")
	private Boolean fechaIncidencia;
	
	private Boolean fax;
	
	@Column(name="detalle_motivo")
	private Boolean detalleMotivo;

	//bi-directional many-to-one association to FormularioOdeco
	@ManyToOne
	@JoinColumn(name="id_formulario_odeco")
	private FormularioOdeco formularioOdeco;

	public Devuelto() {
	}

	public Integer getDevuelvtoId() {
		return this.devuelvtoId;
	}

	public void setDevuelvtoId(Integer devuelvtoId) {
		this.devuelvtoId = devuelvtoId;
	}

	public Boolean getCiudadLocalidad() {
		return this.ciudadLocalidad;
	}

	public void setCiudadLocalidad(Boolean ciudadLocalidad) {
		this.ciudadLocalidad = ciudadLocalidad;
	}

	public Boolean getCiudadReclamacion() {
		return this.ciudadReclamacion;
	}

	public void setCiudadReclamacion(Boolean ciudadReclamacion) {
		this.ciudadReclamacion = ciudadReclamacion;
	}

	public Boolean getCorreo() {
		return this.correo;
	}

	public void setCorreo(Boolean correo) {
		this.correo = correo;
	}

	public Boolean getDireccionDomicilio() {
		return this.direccionDomicilio;
	}

	public void setDireccionDomicilio(Boolean direccionDomicilio) {
		this.direccionDomicilio = direccionDomicilio;
	}

	public Boolean getDireccionNotificacion() {
		return this.direccionNotificacion;
	}

	public void setDireccionNotificacion(Boolean direccionNotificacion) {
		this.direccionNotificacion = direccionNotificacion;
	}

	public Boolean getLineaReclamo() {
		return this.lineaReclamo;
	}

	public void setLineaReclamo(Boolean lineaReclamo) {
		this.lineaReclamo = lineaReclamo;
	}

	public Boolean getMedioNotificacion() {
		return this.medioNotificacion;
	}

	public void setMedioNotificacion(Boolean medioNotificacion) {
		this.medioNotificacion = medioNotificacion;
	}

	public Boolean getMotivoReclamo() {
		return this.motivoReclamo;
	}

	public void setMotivoReclamo(Boolean motivoReclamo) {
		this.motivoReclamo = motivoReclamo;
	}

	public Boolean getNombreReclamante() {
		return this.nombreReclamante;
	}

	public void setNombreReclamante(Boolean nombreReclamante) {
		this.nombreReclamante = nombreReclamante;
	}

	public Boolean getNombreTitular() {
		return this.nombreTitular;
	}

	public void setNombreTitular(Boolean nombreTitular) {
		this.nombreTitular = nombreTitular;
	}

	public Boolean getObjetoReclamo() {
		return this.objetoReclamo;
	}

	public void setObjetoReclamo(Boolean objetoReclamo) {
		this.objetoReclamo = objetoReclamo;
	}

	public Boolean getTelefonoReferencia() {
		return this.telefonoReferencia;
	}

	public void setTelefonoReferencia(Boolean telefonoReferencia) {
		this.telefonoReferencia = telefonoReferencia;
	}

	public FormularioOdeco getFormularioOdeco() {
		return this.formularioOdeco;
	}

	public void setFormularioOdeco(FormularioOdeco formularioOdeco) {
		this.formularioOdeco = formularioOdeco;
	}

	public Boolean getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(Boolean fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	public Boolean getFechaIncidencia() {
		return fechaIncidencia;
	}

	public void setFechaIncidencia(Boolean fechaIncidencia) {
		this.fechaIncidencia = fechaIncidencia;
	}

	public Boolean getFax() {
		return fax;
	}

	public void setFax(Boolean fax) {
		this.fax = fax;
	}

	public Boolean getDetalleMotivo() {
		return detalleMotivo;
	}

	public void setDetalleMotivo(Boolean detalleMotivo) {
		this.detalleMotivo = detalleMotivo;
	}

	public Boolean getFormaRegistro() {
		return formaRegistro;
	}

	public void setFormaRegistro(Boolean formaRegistro) {
		this.formaRegistro = formaRegistro;
	}

	@Override
	public String toString() {
		return "Devuelto [ciudadLocalidad=" + ciudadLocalidad
				+ ", ciudadReclamacion=" + ciudadReclamacion + ", correo="
				+ correo + ", direccionDomicilio=" + direccionDomicilio
				+ ", direccionNotificacion=" + direccionNotificacion
				+ ", lineaReclamo=" + lineaReclamo + ", medioNotificacion="
				+ medioNotificacion + ", motivoReclamo=" + motivoReclamo
				+ ", nombreReclamante=" + nombreReclamante + ", nombreTitular="
				+ nombreTitular + ", objetoReclamo=" + objetoReclamo
				+ ", telefonoReferencia=" + telefonoReferencia
				+ ", formaRegistro=" + formaRegistro + ", fechaResolucion="
				+ fechaResolucion + ", fechaIncidencia=" + fechaIncidencia
				+ ", fax=" + fax + ", detalleMotivo=" + detalleMotivo
				+ ", formularioOdeco=" + formularioOdeco + "]";
	}
	
	

}