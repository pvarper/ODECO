package micrium.odeco.model;

import java.io.Serializable;

import javax.persistence.*;

import micrium.user.model.Usuario;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the formulario_odeco database table.
 * 
 */
@Entity
@Table(name="formulario_odeco")
@NamedQuery(name="FormularioOdeco.findAll", query="SELECT f FROM FormularioOdeco f")
public class FormularioOdeco implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FORMULARIO_ODECO_FORMULARIOODECOID_GENERATOR", sequenceName="FORMULARIO_ODECO_FORMULARIO_ODECO_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FORMULARIO_ODECO_FORMULARIOODECOID_GENERATOR")
	@Column(name="formulario_odeco_id")
	private Integer formularioOdecoId;
	
	@Column(name="ciudad_localidad")
	private String ciudadLocalidad;

	@Column(name="codigo_reclamacion")
	private String codigoReclamacion;

	@Column(name="codigo_reclamacion_ciudad")
	private String codigoReclamacionCiudad;

	@Column(name="codigo_reclamacion_correlativo")
	private Integer codigoReclamacionCorrelativo;

	@Column(name="codigo_reclamacion_institucion")
	private String codigoReclamacionInstitucion;
	
	@Column(name="periodo_objeto_reclamo")
	private Integer periodoObjetoReclamo;

	private String correo;
	
	private String derivado;
	
	@Column(name="lista_objetos")
	private String listaObjetos;
	
	@Column(name="lista_motivos")
	private String listaMotivos;
	
	@Column(name="forma_registro")
	private String formaRegistro;
	
	private String fax;
	
	@Column(name="detalle_motivo_reclamacion")
	private String detalleMotivoReclamacion;

	@Column(name="detalle_respuesta")
	private String detalleRespuesta;
	
	@Column(name="detalle_anulado")
	private String detalleAnulado;

	private Boolean devuelto;

	@Column(name="direccion_domicilio")
	private String direccionDomicilio;

	@Column(name="direccion_notificacion")
	private String direccionNotificacion;

	private String enviado;
	
	@Column(name="enviado_mail")
	private Boolean enviadoMail;

	private Boolean estado;

	@Column(name="estado_respuesta")
	private String estadoRespuesta;

	@Column(name="fecha_reclamo")
	private Timestamp fechaReclamo;
	
	@Column(name="fecha_incidencia")
	private Timestamp fechaIncidencia;
	
	@Column(name="fecha_cerrado")
	private Timestamp fechaCerrado;
	
	@Column(name="fecha_resolucion")
	private Timestamp fechaResolucion;

	@Column(name="linea_reclamo")
	private String lineaReclamo;

	@Column(name="motivo_reclamacion_administrativa")
	private String motivoReclamacionAdministrativa;

	@Column(name="nombre_reclamante")
	private String nombreReclamante;

	@Column(name="nombre_titular")
	private String nombreTitular;

	private String observaciones;

	private String revisar;

	@Column(name="telefono_referencia")
	private String telefonoReferencia;

	//bi-directional many-to-one association to Adjunto
	@OneToMany(mappedBy="formularioOdeco",cascade=CascadeType.ALL)
	private List<Adjunto> adjuntos;

	//bi-directional many-to-one association to Devuelto
	@OneToMany(mappedBy="formularioOdeco")
	private List<Devuelto> devueltos;

	//bi-directional many-to-many association to MotivoReclamo
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "formulario_motivo_reclamo", joinColumns = { @JoinColumn(name = "formulario_odeco_id") }, inverseJoinColumns = { @JoinColumn(name = "motivo_reclamo_id") })
	private List<MotivoReclamo> motivoReclamos;

	//bi-directional many-to-many association to ObjetoReclamo
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="formulario_objeto_reclamo", joinColumns={@JoinColumn(name="formulario_odeco_id")}, inverseJoinColumns={@JoinColumn(name="objeto_reclamo_id")})
	private List<ObjetoReclamo> objetoReclamos;

	//bi-directional many-to-one association to Ciudad
	@ManyToOne
	@JoinColumn(name="id_ciudad_reclamacion")
	private Ciudad ciudad;

	//bi-directional many-to-one association to FormularioOdeco
	@ManyToOne
	@JoinColumn(name="id_formulario_odeco")
	private FormularioOdeco formularioOdeco;

	//bi-directional many-to-one association to FormularioOdeco
	@OneToMany(mappedBy="formularioOdeco")
	private List<FormularioOdeco> formularioOdecos;

	//bi-directional many-to-one association to MedioNotificacion
	@ManyToOne
	@JoinColumn(name="id_medio_notificacion")
	private MedioNotificacion medioNotificacion;

	//bi-directional many-to-one association to RespuestaOdeco
	@ManyToOne
	@JoinColumn(name="id_respuesta_odeco")
	private RespuestaOdeco respuestaOdeco;

	//bi-directional many-to-one association to TipoFormulario
	@ManyToOne
	@JoinColumn(name="id_tipo_formulario")
	private TipoFormulario tipoFormulario;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;
	
	transient
	private String nombreCiudad;
	transient
	private String vencidos;
	transient
	private String tiempoSolucion;
	transient
	private String fechaReclamoString;
	transient
	private String fechaResolucionString;
	transient
	private String fechaCerradoString;
	transient
	private String medioNotificacionDescripcion;
	transient
	private String respuestaOdecoDescripcion;
	

	public FormularioOdeco() {
		
	}

	public Integer getFormularioOdecoId() {
		return this.formularioOdecoId;
	}

	public void setFormularioOdecoId(Integer formularioOdecoId) {
		this.formularioOdecoId = formularioOdecoId;
	}

	public String getCiudadLocalidad() {
		return this.ciudadLocalidad;
	}

	public void setCiudadLocalidad(String ciudadLocalidad) {
		this.ciudadLocalidad = ciudadLocalidad;
	}

	public String getCodigoReclamacion() {
		return this.codigoReclamacion;
	}

	public void setCodigoReclamacion(String codigoReclamacion) {
		this.codigoReclamacion = codigoReclamacion;
	}

	public String getCodigoReclamacionCiudad() {
		return this.codigoReclamacionCiudad;
	}

	public void setCodigoReclamacionCiudad(String codigoReclamacionCiudad) {
		this.codigoReclamacionCiudad = codigoReclamacionCiudad;
	}

	public Integer getCodigoReclamacionCorrelativo() {
		return this.codigoReclamacionCorrelativo;
	}

	public void setCodigoReclamacionCorrelativo(Integer codigoReclamacionCorrelativo) {
		this.codigoReclamacionCorrelativo = codigoReclamacionCorrelativo;
	}

	public String getCodigoReclamacionInstitucion() {
		return this.codigoReclamacionInstitucion;
	}

	public void setCodigoReclamacionInstitucion(String codigoReclamacionInstitucion) {
		this.codigoReclamacionInstitucion = codigoReclamacionInstitucion;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDetalleRespuesta() {
		return this.detalleRespuesta;
	}

	public void setDetalleRespuesta(String detalleRespuesta) {
		this.detalleRespuesta = detalleRespuesta;
	}

	public Boolean getDevuelto() {
		return this.devuelto;
	}

	public void setDevuelto(Boolean devuelto) {
		this.devuelto = devuelto;
	}

	public String getDireccionDomicilio() {
		return this.direccionDomicilio;
	}

	public void setDireccionDomicilio(String direccionDomicilio) {
		this.direccionDomicilio = direccionDomicilio;
	}

	public String getDireccionNotificacion() {
		return this.direccionNotificacion;
	}

	public void setDireccionNotificacion(String direccionNotificacion) {
		this.direccionNotificacion = direccionNotificacion;
	}

	public String getEnviado() {
		return this.enviado;
	}

	public void setEnviado(String enviado) {
		this.enviado = enviado;
	}

	public Boolean getEstado() {
		return this.estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getEstadoRespuesta() {
		return this.estadoRespuesta;
	}

	public void setEstadoRespuesta(String estadoRespuesta) {
		this.estadoRespuesta = estadoRespuesta;
	}

	public Timestamp getFechaReclamo() {
		return this.fechaReclamo;
	}
	
	public String getFechaReclamoFiltro(){
		SimpleDateFormat formateador= new SimpleDateFormat("dd-MM-yyyy");
		String res="";
		if(this.getFechaReclamo()!=null){
			res=formateador.format(this.getFechaReclamo());
		}
		return res;
	}
	
	public void setFechaReclamo(Timestamp fechaReclamo) {
		this.fechaReclamo = fechaReclamo;
	}

	public String getLineaReclamo() {
		return this.lineaReclamo;
	}

	public void setLineaReclamo(String lineaReclamo) {
		this.lineaReclamo = lineaReclamo;
	}

	public String getMotivoReclamacionAdministrativa() {
		return this.motivoReclamacionAdministrativa;
	}

	public void setMotivoReclamacionAdministrativa(String motivoReclamacionAdministrativa) {
		this.motivoReclamacionAdministrativa = motivoReclamacionAdministrativa;
	}

	public String getNombreReclamante() {
		return this.nombreReclamante;
	}

	public void setNombreReclamante(String nombreReclamante) {
		this.nombreReclamante = nombreReclamante;
	}

	public String getNombreTitular() {
		return this.nombreTitular;
	}

	public void setNombreTitular(String nombreTitular) {
		this.nombreTitular = nombreTitular;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getRevisar() {
		return this.revisar;
	}

	public void setRevisar(String revisar) {
		this.revisar = revisar;
	}

	public String getTelefonoReferencia() {
		return this.telefonoReferencia;
	}

	public void setTelefonoReferencia(String telefonoReferencia) {
		this.telefonoReferencia = telefonoReferencia;
	}

	public List<Adjunto> getAdjuntos() {
		return this.adjuntos;
	}

	public void setAdjuntos(List<Adjunto> adjuntos) {
		this.adjuntos = adjuntos;
	}

	public Adjunto addAdjunto(Adjunto adjunto) {
		getAdjuntos().add(adjunto);
		adjunto.setFormularioOdeco(this);

		return adjunto;
	}

	public Adjunto removeAdjunto(Adjunto adjunto) {
		getAdjuntos().remove(adjunto);
		adjunto.setFormularioOdeco(null);

		return adjunto;
	}

	public List<Devuelto> getDevueltos() {
		return this.devueltos;
	}

	public void setDevueltos(List<Devuelto> devueltos) {
		this.devueltos = devueltos;
	}

	public Devuelto addDevuelto(Devuelto devuelto) {
		getDevueltos().add(devuelto);
		devuelto.setFormularioOdeco(this);

		return devuelto;
	}

	public Devuelto removeDevuelto(Devuelto devuelto) {
		getDevueltos().remove(devuelto);
		devuelto.setFormularioOdeco(null);

		return devuelto;
	}

	public List<MotivoReclamo> getMotivoReclamos() {
		return this.motivoReclamos;
	}

	public void setMotivoReclamos(List<MotivoReclamo> motivoReclamos) {
		this.motivoReclamos = motivoReclamos;
	}

	public List<ObjetoReclamo> getObjetoReclamos() {
		return this.objetoReclamos;
	}

	public void setObjetoReclamos(List<ObjetoReclamo> objetoReclamos) {
		this.objetoReclamos = objetoReclamos;
	}

	public Ciudad getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public FormularioOdeco getFormularioOdeco() {
		return this.formularioOdeco;
	}

	public void setFormularioOdeco(FormularioOdeco formularioOdeco) {
		this.formularioOdeco = formularioOdeco;
	}

	public List<FormularioOdeco> getFormularioOdecos() {
		return this.formularioOdecos;
	}

	public void setFormularioOdecos(List<FormularioOdeco> formularioOdecos) {
		this.formularioOdecos = formularioOdecos;
	}

	public FormularioOdeco addFormularioOdeco(FormularioOdeco formularioOdeco) {
		getFormularioOdecos().add(formularioOdeco);
		formularioOdeco.setFormularioOdeco(this);

		return formularioOdeco;
	}

	public FormularioOdeco removeFormularioOdeco(FormularioOdeco formularioOdeco) {
		getFormularioOdecos().remove(formularioOdeco);
		formularioOdeco.setFormularioOdeco(null);

		return formularioOdeco;
	}

	public MedioNotificacion getMedioNotificacion() {
		return this.medioNotificacion;
	}

	public void setMedioNotificacion(MedioNotificacion medioNotificacion) {
		this.medioNotificacion = medioNotificacion;
	}

	public RespuestaOdeco getRespuestaOdeco() {
		return this.respuestaOdeco;
	}

	public void setRespuestaOdeco(RespuestaOdeco respuestaOdeco) {
		this.respuestaOdeco = respuestaOdeco;
	}

	public TipoFormulario getTipoFormulario() {
		return this.tipoFormulario;
	}

	public void setTipoFormulario(TipoFormulario tipoFormulario) {
		this.tipoFormulario = tipoFormulario;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getDerivado() {
		return derivado;
	}

	public void setDerivado(String derivado) {
		this.derivado = derivado;
	}

	public String getDetalleMotivoReclamacion() {
		return detalleMotivoReclamacion;
	}

	public void setDetalleMotivoReclamacion(String detalleMotivoReclamacion) {
		this.detalleMotivoReclamacion = detalleMotivoReclamacion;
	}

	public Boolean getEnviadoMail() {
		return enviadoMail;
	}

	public void setEnviadoMail(Boolean enviadoMail) {
		this.enviadoMail = enviadoMail;
	}

	public Timestamp getFechaResolucion() {
		return fechaResolucion;
	}
	
	public String getFechaResolucionFiltro(){	
		SimpleDateFormat formateador= new SimpleDateFormat("dd-MM-yyyy");
		String res="";
		if(this.getFechaResolucion()!=null){
			res=formateador.format(this.getFechaResolucion());
		}
		return res;
	}
	
	public void setFechaResolucion(Timestamp fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	public Integer getPeriodoObjetoReclamo() {
		return periodoObjetoReclamo;
	}

	public void setPeriodoObjetoReclamo(Integer periodoObjetoReclamo) {
		this.periodoObjetoReclamo = periodoObjetoReclamo;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	

	public String getDetalleAnulado() {
		return detalleAnulado;
	}

	public void setDetalleAnulado(String detalleAnulado) {
		this.detalleAnulado = detalleAnulado;
	}

	public Timestamp getFechaIncidencia() {
		return fechaIncidencia;
	}

	public void setFechaIncidencia(Timestamp fechaIncidencia) {
		this.fechaIncidencia = fechaIncidencia;
	}

	public String getListaObjetos() {
		return listaObjetos;
	}

	public void setListaObjetos(String listaObjetos) {
		this.listaObjetos = listaObjetos;
	}

	public String getListaMotivos() {
		return listaMotivos;
	}

	public void setListaMotivos(String listaMotivos) {
		this.listaMotivos = listaMotivos;
	}

	public String getNombreCiudad() {
		nombreCiudad=ciudad.getNombre();
		return nombreCiudad;
	}

	public void setNombreCiudad(String nombreCiudad) {
		this.nombreCiudad = nombreCiudad;
	}

	public String getFechaReclamoString() {		
		fechaReclamoString=fechaReclamo.toString();
		return fechaReclamoString;
	}

	public void setFechaReclamoString(String fechaReclamoString) {
		this.fechaReclamoString = fechaReclamoString;
	}

	public String getFechaResolucionString() {
		fechaResolucionString=fechaResolucion.toString();
		return fechaResolucionString;
		
	}

	public void setFechaResolucionString(String fechaResolucionString) {
		this.fechaResolucionString = fechaResolucionString;
	}

	public String getMedioNotificacionDescripcion() {
		medioNotificacionDescripcion=medioNotificacion.getDescripcion();
		return medioNotificacionDescripcion;
	}

	public void setMedioNotificacionDescripcion(String medioNotificacionDescripcion) {
		this.medioNotificacionDescripcion = medioNotificacionDescripcion;
	}

	public String getRespuestaOdecoDescripcion() {
		if(respuestaOdeco!=null){
			respuestaOdecoDescripcion=respuestaOdeco.getRespuestaOdeco();
			return respuestaOdecoDescripcion;
		}
		return "";
		
	}

	public void setRespuestaOdecoDescripcion(String respuestaOdecoDescripcion) {
		this.respuestaOdecoDescripcion = respuestaOdecoDescripcion;
	}

	public Timestamp getFechaCerrado() {
		
		return fechaCerrado;
	}
	
	public String getFechaCerradoFiltro(){
		SimpleDateFormat formateador= new SimpleDateFormat("dd-MM-yyyy");
		String res="";
		if(this.getFechaCerrado()!=null){
			res=formateador.format(this.getFechaCerrado());
		}
		return res;
	}

	public void setFechaCerrado(Timestamp fechaCerrado) {
		this.fechaCerrado = fechaCerrado;
	}
	
	

	public String getFechaCerradoString() {
		if(fechaCerrado!=null){
			fechaCerradoString=fechaCerrado.toString();
		}	
		
		return fechaCerradoString;
	}

	public void setFechaCerradoString(String fechaCerradoString) {
		this.fechaCerradoString = fechaCerradoString;
	}
	
	

	public String getFormaRegistro() {
		return formaRegistro;
	}

	public void setFormaRegistro(String formaRegistro) {
		this.formaRegistro = formaRegistro;
	}
	
	

	public String getVencidos() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");	
		try {
			
			Date date = dateFormat.parse(dateFormat.format(this.getFechaResolucion()));		
			long time = date.getTime();
			Timestamp fechaResolucion=new Timestamp(time);	
			Date hoy=dateFormat.parse(dateFormat.format(new Date()));
			
			if(hoy.getTime()>fechaResolucion.getTime()){
				long d=(hoy.getTime()-fechaResolucion.getTime());
				long dias = d / (1000 * 60 * 60 * 24);
				vencidos=String.valueOf(dias);
			}else{
				return "0";
			}
			return vencidos;
		} catch (Exception e) {
			return "0";
		}
				
	}

	public void setVencidos(String vencidos) {
		this.vencidos = vencidos;
	}

	public String getTiempoSolucion() {
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			Date date = dateFormat.parse(dateFormat.format(this.getFechaCerrado()));		
			long time = date.getTime();
			Timestamp fechaCerrado=new Timestamp(time);	
			
			date = dateFormat.parse(dateFormat.format(this.getFechaReclamo()));
			time = date.getTime();
			Timestamp fechaReclamo=new Timestamp(time);
			
			if(getEstadoRespuesta().equals("CERRADO")){
				long d=(fechaCerrado.getTime()-fechaReclamo.getTime());
				long dias = d / (1000 * 60 * 60 * 24);
				tiempoSolucion=String.valueOf(dias);
			}else{
				return "0";
			}
			return tiempoSolucion;
			
		} catch (Exception e) {
			
			return "0";
		}
		
	}

	public void setTiempoSolucion(String tiempoSolucion) {
		this.tiempoSolucion = tiempoSolucion;
	}

	@Override
	public String toString() {
		return "FormularioOdeco [formularioOdecoId=" + formularioOdecoId
				+ ", ciudadLocalidad=" + ciudadLocalidad
				+ ", codigoReclamacion=" + codigoReclamacion
				+ ", codigoReclamacionCiudad=" + codigoReclamacionCiudad
				+ ", codigoReclamacionCorrelativo="
				+ codigoReclamacionCorrelativo
				+ ", codigoReclamacionInstitucion="
				+ codigoReclamacionInstitucion + ", periodoObjetoReclamo="
				+ periodoObjetoReclamo + ", correo=" + correo + ", derivado="
				+ derivado + ", fax=" + fax + ", detalleMotivoReclamacion="
				+ detalleMotivoReclamacion + ", detalleRespuesta="
				+ detalleRespuesta + ", detalleAnulado=" + detalleAnulado
				+ ", direccionDomicilio=" + direccionDomicilio
				+ ", direccionNotificacion=" + direccionNotificacion
				+ ", enviado=" + enviado + ", enviadoMail=" + enviadoMail
				+ ", fechaReclamo=" + fechaReclamo + ", fechaIncidencia="
				+ fechaIncidencia + ", fechaCerrado=" + fechaCerrado
				+ ", fechaResolucion=" + fechaResolucion + ", lineaReclamo="
				+ lineaReclamo + ", motivoReclamacionAdministrativa="
				+ motivoReclamacionAdministrativa + ", nombreReclamante="
				+ nombreReclamante + ", nombreTitular=" + nombreTitular
				+ ", observaciones=" + observaciones + ", revisar=" + revisar
				+ ", telefonoReferencia=" + telefonoReferencia
				+ ", medioNotificacion=" + medioNotificacion
				+ ", respuestaOdeco=" + respuestaOdeco + "]";
	}

	
	
	
	

}