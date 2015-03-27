package micrium.odeco.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import micrium.odeco.business.AdjuntoBL;
import micrium.odeco.business.CiudadBL;
import micrium.odeco.business.ColaCoordinadorBL;
import micrium.odeco.business.ConfiguracionCalendarioBL;
import micrium.odeco.business.DevueltoBL;
import micrium.odeco.business.FlujoBL;
import micrium.odeco.business.FormularioOdecoBL;
import micrium.odeco.business.MailBL;
import micrium.odeco.business.MedioNotificacionBL;
import micrium.odeco.business.MotivoReclamoBL;
import micrium.odeco.business.ObjetoReclamoBL;
import micrium.odeco.business.RespuestaOdecoBL;
import micrium.odeco.model.Adjunto;
import micrium.odeco.model.Calendario;
import micrium.odeco.model.Ciudad;
import micrium.odeco.model.ColaCoordinador;
import micrium.odeco.model.Devuelto;
import micrium.odeco.model.Flujo;
import micrium.odeco.model.FormularioOdeco;
import micrium.odeco.model.ListaInputStream;
import micrium.odeco.model.MedioNotificacion;
import micrium.odeco.model.MotivoReclamo;
import micrium.odeco.model.ObjetoReclamo;
import micrium.odeco.model.RespuestaOdeco;
import micrium.user.business.RoleBL;
import micrium.user.business.UsuarioBL;
import micrium.user.controler.ControlParametro;
import micrium.user.model.Rol;
import micrium.user.model.Usuario;
import micrium.user.view.ControlerBitacora;
import micrium.util.Code;
import micrium.util.DescriptorBitacora;
import micrium.util.Parameter;
import micrium.util.PrintUtil;
import micrium.util.Result;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class VisualizarOdecoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(VisualizarOdecoBean.class);

	@Inject
	private FormularioOdecoBL ofertaN;
	@Inject
	private FlujoBL flujoBL;

	@Inject
	private CiudadBL ciudadBL;

	@Inject
	private MailBL mailBL;

	@Inject
	private DevueltoBL devueltoBL;

	@Inject
	private UsuarioBL usuarioBL;

	@Inject
	private ConfiguracionCalendarioBL configuracionBL;
	
	@Inject
	private AdjuntoBL adjuntoBL;

	@Inject
	private ControlParametro parametroBL;

	@Inject
	private ObjetoReclamoBL objetoReclamoBL;

	@Inject
	private MedioNotificacionBL medioNotificacionBL;

	@Inject
	private RespuestaOdecoBL respuestaOdecoBL;
	
	@Inject
	private PrintUtil printUtils;
	
	@Inject
	private RoleBL rolBL;
	

	@Inject
	private MotivoReclamoBL motivoReclamoBL;

	@Inject
	private ControlerBitacora controlerBitacora;
	@Inject
	private ColaCoordinadorBL colaCoordinadorBL;

	private String[] selectedMotivoReclamo;
	
	private String[] auxSelectMotivo;
	
	private String[] auxSelectObjeto;

	private String[] selectedObjetoReclamo;
	
	private String[] selectedAdjuntos;
	
	private List<SelectItem> selectCoordinadores;
	private String selectCoordinador;

	private FormularioOdeco oferta;

	private String idOfertaStr;

	private String codigoReclamacion;

	private String strIdUs = "";

	private boolean edit;
	private List<FormularioOdeco> listaOferta = new ArrayList<FormularioOdeco>();
	private boolean ofertaActiva;

	private List<SelectItem> selectCiudades;
	private String selectCiudad;
	private List<SelectItem> selectCiudadesReclamacion;
	private String selectCiudadReclamacion;

	private List<SelectItem> selectObjetoReclamos;
	private String selectObjetoReclamo;

	private List<SelectItem> selectMedioNotificaciones;
	private String selectMedioNotificacion;

	private List<SelectItem> selectEstadoRespuestas;
	private String selectEstadoRespuesta;

	private List<SelectItem> selectMotivoReclamos;
	private String selectMotivoReclamo;

	private List<String> listaFormaRegistro = new ArrayList<String>();
	private String formRegistro;
	
	private FormularioOdeco selectOdeco;

	

	private List<String> correos = new ArrayList<String>();
	private List<Adjunto> listaAdjuntos = new ArrayList<Adjunto>();
	private List<Adjunto> listaAjuntosVista = new ArrayList<Adjunto>();
	
	//private transient List<InputStream> listaInputStream = new ArrayList<InputStream>();
	
	private transient ListaInputStream listaInputStream;
	
	List<ObjetoReclamo> objetosReclamos = new ArrayList<ObjetoReclamo>();
	List<MotivoReclamo> motivosReclamos = new ArrayList<MotivoReclamo>();
	private File directorio;
	private boolean guardar;
	private Date fechaIn;
	private Integer opcionDerivar;

	private boolean codRecla, ciudadLocalidad, nombreReclamante, nombreTitutar,
			telefonoReferencia, periodoObjeto, lineaReclamo, correo, anulado, anuladoT,
			direccionDomicilio, direccionNotificacion, objetoReclamo,
			ciudadReclamacion, medioNotificacion, motivoReclamo,fax,formaRegistro,fechaReclamo, fechaResolucion, fechaIncidencia, detalleMotivo;
	
	
	
	@PostConstruct
	public void init() {
		log.info("********* Iniciando Vista Gestion de ODECO **********");
		try {
			newObject();
			fillListasSeleccionables();
			
		} catch (Exception ex) {
			log.debug("[init] Hubo un error a la hora de crear un nuevo objeto "
					+ ex.getMessage());
		}
		log.debug("[init] se va cargar la tabla");
		setListaOferta(ofertaN.listarTodo());// Al iniciar necesitamos llenar la
												// tabla
	}

	

	public String generarCodigoReclamacion() {
		try {
			log.debug("generarCodigoReclamacion] se va generar el codigo de reclamacion");
			HttpServletRequest request = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			String strIdUs = (String) request.getSession().getAttribute(
					"TEMP$USER_NAME");
			Usuario usuario = usuarioBL.getUserLogin(strIdUs);
			Ciudad c = ciudadBL.getCiudad(Integer
					.valueOf(selectCiudadReclamacion));
			long correlativo = ofertaN.getMaxId("TELECEL", c.getAbreviatura());
			correlativo++;
			codigoReclamacion = "TELECEL/"
					+ c.getAbreviatura()  + "/" + correlativo;

			oferta.setCodigoReclamacion(codigoReclamacion);
			oferta.setCodigoReclamacionInstitucion("TELECEL");
			oferta.setCodigoReclamacionCiudad(c.getAbreviatura());
			oferta.setCodigoReclamacionCorrelativo((int) (long) correlativo);
			oferta.setUsuario(usuario);
			log.debug("generarCodigoReclamacion] se va genero el codigo de reclamacion "+codigoReclamacion);
			return codigoReclamacion;
		} catch (Exception e) {

			log.debug("[generarCodigoReclamacion] Hubo un error a la hora de generar el codigo de Reclamacion "
					+ e.getMessage());
			return "";
		}
		
	}

	public void newObject() throws Exception {
		log.debug("inicializando Nuevo ODECO...");
		this.oferta = new FormularioOdeco();
		//this.codigoReclamacion = generarCodigoReclamacion();
		this.edit = false;
		this.oferta.setFechaReclamo(new Timestamp(new Date().getTime()));
		this.oferta.setEnviadoMail(true);
		listaAdjuntos = new ArrayList<Adjunto>();
		listaInputStream = new ListaInputStream();
		correos = new ArrayList<String>();
		selectedMotivoReclamo = null;
		selectedObjetoReclamo = null;
		selectedAdjuntos=null;
		codRecla = false;
		ciudadLocalidad = false;
		nombreReclamante = false;
		formaRegistro=false;
		nombreTitutar = false;
		telefonoReferencia = false;
		lineaReclamo = false;
		correo = false;
		direccionDomicilio = false;
		direccionNotificacion = false;
		objetoReclamo = false;
		ciudadReclamacion = false;
		motivoReclamo = false;
		medioNotificacion = false;
		periodoObjeto = false;
		fechaResolucion=false;
		fechaIncidencia=false;
		detalleMotivo=false;
		fechaReclamo=false;
		fax=false;
		anulado=false;
		selectCiudad = "-1";
		selectCiudadReclamacion = "-1";
		selectObjetoReclamo = "-1";
		selectMotivoReclamo = "-1";
		selectMedioNotificacion = "-1";
		selectEstadoRespuesta = "-1";
		formRegistro="";
		this.guardar = true;
		log.debug("ODECO Inicializado [ok]");
	}
	
	private void fillListasSeleccionables() throws Exception {
		log.debug("Inciando Carga de Datos...");
		selectCiudades = new ArrayList<SelectItem>();
		selectCiudades.add(new SelectItem("-1", "Ciudades"));
		selectCiudadesReclamacion = new ArrayList<SelectItem>();
		selectCiudadesReclamacion.add(new SelectItem("-1", "Ciudades"));
		List<Ciudad> list = null;
		try {
			list = ciudadBL.getList();

			for (Ciudad item : list) {
				SelectItem s = new SelectItem(item.getCiudadId(),
						item.getNombre());
				selectCiudades.add(s);
				selectCiudadesReclamacion.add(s);
			}
			log.debug("Carga de Ciudades [ok]");
		} catch (Exception e) {
			log.error("Carga de Lista de Ciudades [fail]", e);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error",
							Parameter.MENSAJE_LISTACIUDADES_ERROR));
		}
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		this.strIdUs = (String) request.getSession().getAttribute(
				"TEMP$USER_NAME");
		
		
		selectMedioNotificaciones = new ArrayList<SelectItem>();
		selectMedioNotificaciones.add(new SelectItem("-1",
				"Medio de Notificacion"));
		List<MedioNotificacion> list3 = null;
		try {
			list3 = medioNotificacionBL.getLista();
			for (MedioNotificacion item : list3) {
				SelectItem s = new SelectItem(item.getMedioNotificacionId(),
						item.getDescripcion());
				selectMedioNotificaciones.add(s);
			}
			log.debug("Carga de Lista de Medios de Notificacion [ok]");
		} catch (Exception e) {
			log.error("Carga de Lista de Medios de Notificacion [fail]", e);
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Mensaje de error",
									Parameter.MENSAJE_LISTAMEDIOS_ERROR));
		}

		

		selectEstadoRespuestas = new ArrayList<SelectItem>();
		selectEstadoRespuestas.add(new SelectItem("-1", "Respuesta Odeco"));
		List<RespuestaOdeco> list5 = null;
		try {
			list5 = respuestaOdecoBL.getLista();
			for (RespuestaOdeco item : list5) {
				SelectItem s = new SelectItem(item.getRespuestaOdecoId(),
						item.getRespuestaOdeco());
				selectEstadoRespuestas.add(s);
			}
			log.debug("Carga de Lista de Respuesta de Odeco [ok]");
		} catch (Exception e) {
			log.error("Carga de Lista de Respuesta de Odeco[fail]", e);
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Mensaje de error",
									Parameter.MENSAJE_RESPUESTAS_ERROR));
		}
		
		selectCoordinadores = new ArrayList<SelectItem>();
		selectCoordinadores.add(new SelectItem("-1", "Coordinadores"));
		List<Usuario> listU = null;
		try {
			Flujo flujo=flujoBL.getFlujoNombre("flujo");
			Rol rol=rolBL.getname(flujo.getCoordinador());
			listU = usuarioBL.getUsersRol(rol.getRolId().toString());

			for (Usuario item : listU) {
				SelectItem s = new SelectItem(item.getUsuarioId(),
						item.getLogin());
				selectCoordinadores.add(s);				
			}
			log.debug("Carga de Coordinadores [ok]");
		} catch (Exception e) {
			log.error("Carga de Lista de Coordinadores [fail]", e);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error",
							"Error al Obtener la lista de Coordinadores Responsables"));
		}
		
		
		log.debug("se cargar las formas de registro");
		String formas=parametroBL.getFormaRegistro();
		if(formas.isEmpty()){
			log.error("Carga de Formas de Registro [fail]");
		}
		listaFormaRegistro.add("Forma de Registro");
		agregarFormasRegitro(formas);
		log.debug("Carga de Formas de Registro [ok]");

	}

	public void agregarFormasRegitro(String c){

        String delims = "[;]";
        String[] tokens = c.split(delims);
        for (int i = 0; i < tokens.length; i++) {
        	listaFormaRegistro.add(tokens[i]);
        }
}
	
	/*public String save() throws Exception {
		// System.out.println("********************************************************************");
		log.debug("[save] Asignando datos al ODECO con la ide "
				+ this.getIdOfertaStr());

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		this.strIdUs = (String) request.getSession().getAttribute(
				"TEMP$USER_NAME");
		Usuario usuario = usuarioBL.getUserLogin(strIdUs);
		long correlativo = ofertaN.getMaxId("TELECEL", usuario.getCiudad()
				.getAbreviatura());
		correlativo++;
		codigoReclamacion = "TELECEL/" + usuario.getCiudad().getAbreviatura()
				+ "/" + correlativo;
		oferta.setCodigoReclamacionInstitucion("TELECEL");
		oferta.setCodigoReclamacionCiudad(usuario.getCiudad().getAbreviatura());
		oferta.setCodigoReclamacionCorrelativo((int) (long) correlativo);
		oferta.setUsuario(usuario);

		objetosReclamos = new ArrayList<ObjetoReclamo>();
		motivosReclamos = new ArrayList<MotivoReclamo>();

		// System.out.println("********************************************************************");
		for (String s : selectedObjetoReclamo) {
			ObjetoReclamo com = objetoReclamoBL.getObjetoReclamo(Integer
					.valueOf(s));
			objetosReclamos.add(com);
		}
		for (String s : selectedMotivoReclamo) {
			MotivoReclamo com = motivoReclamoBL.getMotivoReclamo(Integer
					.valueOf(s));
			motivosReclamos.add(com);
		}

		// System.out.println("****************************** despues del for 1 **************************************");

		System.out
				.println("****************************** EDIT **************************************");
		if (isEdit()) {
			log.info("[save] Actualizando un ODECO. Id: "
					+ this.getIdOfertaStr());
			oferta.setFormularioOdecoId(Integer.valueOf(this.getIdOfertaStr()));
		} else {
			log.info("[save] Guardando un nuevo Odeco");
			oferta.setFormularioOdecoId(null);

		}
		oferta.setCiudadLocalidad(ciudadBL.getCiudad(
				Integer.valueOf(selectCiudadReclamacion)).getNombre());
		oferta.setMotivoReclamos(motivosReclamos);
		oferta.setObjetoReclamos(objetosReclamos);
		oferta.setCiudad(ciudadBL.getCiudad(Integer
				.valueOf(selectCiudadReclamacion)));
		oferta.setMedioNotificacion(medioNotificacionBL
				.getMedioNotificacionID(Integer
						.valueOf(selectMedioNotificacion)));
		oferta.setCodigoReclamacion(codigoReclamacion);
		String message = ofertaN.validate(oferta);

		if (!message.isEmpty()) {
			log.info("[save] Error al Validar ODECO:" + message);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error", message));
			System.out
					.println("****************************** VALIDATE **************************************");
			return "";
		}
		System.out
				.println("**************************** iniciando Transaccion ****************************************");
		try {

			if (isEdit()) { // modificar

				ofertaN.update(getOferta());

				controlerBitacora.update(
						DescriptorBitacora.ODECO_PRIMERA_INSTANCIA,
						oferta.getFormularioOdecoId() + "",
						oferta.getCodigoReclamacionInstitucion() + "/"
								+ oferta.getCodigoReclamacionCiudad() + "/"
								+ oferta.getCodigoReclamacionCorrelativo());
			} else { // insertar

				// oferta.setObjetoReclamos(objetosReclamos);
				// oferta.setMotivoReclamos(motivosReclamos);
				ofertaN.save(getOferta());
				setListaOferta(ofertaN.listarTodo());
				controlerBitacora.insert(
						DescriptorBitacora.ODECO_PRIMERA_INSTANCIA,
						oferta.getFormularioOdecoId() + "",
						oferta.getCodigoReclamacionInstitucion() + "/"
								+ oferta.getCodigoReclamacionCiudad() + "/"
								+ oferta.getCodigoReclamacionCorrelativo());
				setEdit(false);

			}
			setListaOferta(ofertaN.listarTodo());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"NOTIFICACION", "Se guardo correctamente"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							"error al guardar en la BD"));
			log.error("[save] Exception : " + e.getMessage(), e);
		}
		// newObject();
		return "";

	}*/

	public synchronized String saveAgente() {
		// System.out.println("********************************************************************");
		log.debug("[saveAgente] Asignando datos al ODECO con la ide "
				+ this.getIdOfertaStr());
		RequestContext context = RequestContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		this.strIdUs = (String) request.getSession().getAttribute(
				"TEMP$USER_NAME");
		Usuario usuario = usuarioBL.getUserLogin(strIdUs);
	//	String enviado=oferta.getEnviado();
		oferta.setEnviado(this.strIdUs);
		
		Flujo flujo=null;	
		flujo = flujoBL.getFlujoNombre("flujo");
		if(flujo==null){
			log.error("[save] No se pudo obtener el flujo");
			return "";			
		}
		log.debug("[save] se obtuvo el flujo "+flujo.toString());
		if(flujo.getEstado()){
			oferta.setRevisar(usuario.getUsuario().getLogin());
		}
		if(!flujo.getEstado()){
			oferta.setRevisar(this.strIdUs);
		}
		//oferta.setRevisar(usuario.getUsuario().getLogin());
		oferta.setDevuelto(null);
		oferta.setUsuario(usuario);
		oferta.setAdjuntos(listaAdjuntos);
		oferta.setFormaRegistro(formRegistro);
		objetosReclamos = new ArrayList<ObjetoReclamo>();
		motivosReclamos = new ArrayList<MotivoReclamo>();
		correos= new ArrayList<String>();
		
		// System.out.println("********************************************************************");
		
			log.debug("[saveAgente] se van a obtener los objetos reclamos");
			//selectedObjetoReclamo=auxSelectObjeto;
			for (String s : selectedObjetoReclamo) {
				ObjetoReclamo com = objetoReclamoBL.getObjetoReclamo(Integer
						.valueOf(s));
				if(com==null){
					log.error("[saveAgente] no se cargaron los objetos de reclamos seleccionados ");
					return "";
				}
				log.debug("[saveAgente]  se cargo el objeto de reclamos seleccionado "+com.toString());
				objetosReclamos.add(com);
			}
			log.debug("[saveAgente] se obtuvieron los objetos reclamos");
			
			log.debug("[saveAgente] se van a obtener los motivos reclamos");
			//selectedMotivoReclamo=auxSelectMotivo;
			for (String s : selectedMotivoReclamo) {
				MotivoReclamo com = motivoReclamoBL.getMotivoReclamo(Integer
						.valueOf(s));
				if(com==null){
					log.error("[save] no se cargaron los motivos de reclamos seleccionados ");
					return "";
				}
				motivosReclamos.add(com);
				log.debug("[saveAgente]  se cargo el objeto de motivo seleccionado "+com.toString());
				
			}
			log.debug("[saveAgente] se obtuvieron los motivos reclamos");
		

		// System.out.println("****************************** despues del for 1 **************************************");

		System.out
				.println("****************************** EDIT **************************************");
		if (isEdit()) {
			log.info("[saveAgente] Actualizando un ODECO revisador por Agente. Id: "
					+ this.getIdOfertaStr());
			oferta.setFormularioOdecoId(Integer.valueOf(this.getIdOfertaStr()));
		} else {
			log.info("[saveAgente] Guardando un nuevo Odeco");
			oferta.setFormularioOdecoId(null);

		}try {
			log.debug("[saveAgente] se va obtener la ciudad localidad con id: "+selectCiudad);
			oferta.setCiudadLocalidad(ciudadBL.getCiudad(
					Integer.valueOf(selectCiudad)).getNombre());
			log.debug("[saveAgente] se obtuvo la ciudad de localidad "+oferta.getCiudadLocalidad());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[saveAgente] error al obtener la ciudad de localidad con id "+selectCiudad,e);
			return "";
		}
		
		oferta.setMotivoReclamos(motivosReclamos);
		oferta.setObjetoReclamos(objetosReclamos);
		try {
			log.debug("[saveAgente] se va obtener la ciudad reclamacion con id: "+selectCiudadReclamacion);
			oferta.setCiudad(ciudadBL.getCiudad(Integer
					.valueOf(selectCiudadReclamacion)));
			log.debug("[saveAgente] se obtuvo la ciudad reclamacion "+oferta.getCiudad().toString());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[saveAgente] error al obtener la ciudad reclamacion con id "+selectCiudadReclamacion,e);
			return "";
			
		}
		try {
			log.debug("[saveAgente] se va obtener el medio de notififacion con id: "+selectMedioNotificacion);
			oferta.setMedioNotificacion(medioNotificacionBL
					.getMedioNotificacionID(Integer
							.valueOf(selectMedioNotificacion)));
			log.debug("[saveAgente] se obtuvo el medio de notificacion "+oferta.getMedioNotificacion().toString());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[saveAgente] error al obtener el medio de notificacion con id "+selectMedioNotificacion,e);
			return "";
			
		}
		
		oferta.setCodigoReclamacion(codigoReclamacion);
		
		if(fechaIn==null){
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error", Parameter.MENSAJE_VALIDATE_FECHAINCIDENCIA));
			return "";
		}
		oferta.setFechaIncidencia( new Timestamp(fechaIn.getTime()));
		String message = ofertaN.validate(oferta);
		
		if (!message.isEmpty()) {
			log.info("[saveAgente] Error al Validar ODECO:" + message);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error", message));
			System.out
					.println("****************************** VALIDATE **************************************");
			return "";
		}
		
		System.out
				.println("**************************** iniciando Transaccion ****************************************");
		try {
			calcularFechaResolucion();
			
			if (isEdit()) { // modificar

				ofertaN.update(getOferta());
				setGuardar(false);
				devueltoBL.deleteUser(oferta.getFormularioOdecoId());
				if(flujo.getEstado()){
					correos.add(getOferta().getUsuario().getUsuario().getCorreo());	
				}else{
					correos.add(getOferta().getUsuario().getCorreo());	
				}
				//copyFile(listaInputStream, listaAdjuntos);
				
				if(!copyFile(listaInputStream.get(), listaAdjuntos)){					
					log.error("[saveAgente] no se pudieron copiar los archivos al servidor");
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Mensaje de error", "No se pudieron Copiar los arhivos al servidor"));
					return "";
					
				}
				if (exportar(getOferta())) {
					log.debug("[saveAgente] se exporto satisfactoriamente el formulario con codigo: "
							+ getOferta().getCodigoReclamacion());

				} else {
					log.debug("[saveAgente] No se exporto satisfactoriamente el formulario con codigo: "
							+ getOferta().getCodigoReclamacion());
				}
				listaAdjuntos = adjuntoBL.getCiudadNombre(getOferta()
						.getFormularioOdecoId());
				Result result = mailBL.sendEmail(correos, listaAdjuntos,
						getOferta());
				listaAdjuntos = new ArrayList<Adjunto>();
				oferta.setAdjuntos(listaAdjuntos);
				if (result.getCode().equalsIgnoreCase(Code.OK)) {
					oferta.setEnviadoMail(false);
					ofertaN.update(getOferta());
				} else {
					/*oferta.setEnviadoMail(true);
					oferta.setEnviado(enviado);
					oferta.setRevisar(usuario.getLogin());
					ofertaN.update(getOferta());
					setListaOferta(ofertaN.listarTodo());
					setEdit(false);
					listaAdjuntos = new ArrayList<Adjunto>();
					//listaInputStream = new ArrayList<InputStream>();
					listaInputStream = new ListaInputStream();
					correos = new ArrayList<String>();
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_WARN,
											"ERROR",
											Parameter.MENSAJE_GUARDADO_ERROR_MAIL));
					context.execute("dlg7.hide()");
					return "";*/
					oferta.setEnviadoMail(false);
					ofertaN.update(getOferta());
					log.debug(result.getDescription());
					listaAdjuntos = new ArrayList<Adjunto>();
					//listaInputStream = new ArrayList<InputStream>();
					listaInputStream = new ListaInputStream();
					setListaOferta(ofertaN.listarTodo());
					
					controlerBitacora.update(
							DescriptorBitacora.ODECO_REVISION_AGENTE,
							oferta.getFormularioOdecoId() + "",
							oferta.getCodigoReclamacionInstitucion() + "/"
									+ oferta.getCodigoReclamacionCiudad() + "/"
									+ oferta.getCodigoReclamacionCorrelativo());

					context.execute("dlg7.hide()");
					setListaOferta(ofertaN.listarTodo());
					FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_WARN,
									"WARNING",
									Parameter.MENSAJE_GUARDADO_ERROR_MAIL));
					return "";
				}
				log.debug(result.getDescription());
				listaAdjuntos = new ArrayList<Adjunto>();
				//listaInputStream = new ArrayList<InputStream>();
				listaInputStream = new ListaInputStream();
				correos = new ArrayList<String>();
				setListaOferta(ofertaN.listarTodo());
				
				controlerBitacora.update(
						DescriptorBitacora.ODECO_REVISION_AGENTE,
						oferta.getFormularioOdecoId() + "",
						oferta.getCodigoReclamacionInstitucion() + "/"
								+ oferta.getCodigoReclamacionCiudad() + "/"
								+ oferta.getCodigoReclamacionCorrelativo());

				context.execute("dlg7.hide()");
			} else { // insertar

				/*// oferta.setObjetoReclamos(objetosReclamos);
				// oferta.setMotivoReclamos(motivosReclamos);
				ofertaN.save(getOferta());
				setListaOferta(ofertaN.listarTodo());
				controlerBitacora.insert(
						DescriptorBitacora.ODECO_PRIMERA_INSTANCIA,
						oferta.getFormularioOdecoId() + "",
						oferta.getCodigoReclamacionInstitucion() + "/"
								+ oferta.getCodigoReclamacionCiudad() + "/"
								+ oferta.getCodigoReclamacionCorrelativo());
				setEdit(false);
				 		*/
			}
			setListaOferta(ofertaN.listarTodo());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"NOTIFICACION", Parameter.MENSAJE_GUARDADO));
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							Parameter.MENSAJE_GUARDADO_ERROR));
			log.error("[save] Exception : " + e.getMessage(), e);
		}
		// newObject();
		return "";

	}

	public String saveStaff() {
		// System.out.println("********************************************************************");
		log.debug("[saveStaff] Asignando datos al ODECO con la ide "
				+ this.getIdOfertaStr());
		RequestContext context = RequestContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		this.strIdUs = (String) request.getSession().getAttribute(
				"TEMP$USER_NAME");
		Usuario usuario = usuarioBL.getUserLogin(strIdUs);
	//	String enviado=oferta.getEnviado();
		correos= new ArrayList<String>();
		oferta.setRevisar(null);
		oferta.setEnviado(this.strIdUs);
		oferta.setUsuario(usuario);
		oferta.setDevuelto(false);

		objetosReclamos = new ArrayList<ObjetoReclamo>();
		motivosReclamos = new ArrayList<MotivoReclamo>();

		selectedObjetoReclamo=auxSelectObjeto;
		try {
			log.debug("[saveStaff] se van a obtener los objetos de reclamos");
			for (String s : selectedObjetoReclamo) {
				ObjetoReclamo com = objetoReclamoBL.getObjetoReclamo(Integer
						.valueOf(s));
				objetosReclamos.add(com);
				log.debug("[saveStaff] se obtuvo el objeto de reclamo "+com.toString());
			}
			log.debug("[saveStaff] se obtuvieron los objetos de reclamos");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[saveStaff] error al obtener los objetos de reclamos",e);
			return "";
		}
		
		selectedMotivoReclamo=auxSelectMotivo;
		try {
			log.debug("[saveStaff] se van a obtener los motivos de reclamos");
			for (String s : selectedMotivoReclamo) {
				MotivoReclamo com = motivoReclamoBL.getMotivoReclamo(Integer
						.valueOf(s));
				motivosReclamos.add(com);
				if(com.getCoordinador()!=null){
					if(!com.getCoordinador().isEmpty()){
						oferta.setRevisar(com.getCoordinador());
					}
				}
				log.debug("[saveStaff] se obtuvo el motivo de reclamo "+com.toString());
			}
			log.debug("[saveStaff] se obtuvieron los motivos de reclamos");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[saveStaff] error al obtener los motivos de reclamos",e);
			return "";
		}
		ColaCoordinador coordinador=null;
		if(oferta.getRevisar()==null){
			coordinador=colaCoordinadorBL.getCoordinador();
			if(coordinador==null){
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Mensaje de error", Parameter.MENSAJE_COLA_COORDINADOR));
				return "";
			}
			oferta.setRevisar(coordinador.getNombreCoordinador());
			correos.add(usuarioBL.getUserLogin(coordinador.getNombreCoordinador()).getCorreo());// correo de
			// uno de
			// los
			// coordinadores
		}else{
			correos.add(usuarioBL.getUserLogin(oferta.getRevisar()).getCorreo());// correo de
			// uno de
			// los
			// coordinadores
		}
	
		
		// System.out.println("****************************** despues del for 1 **************************************");

		System.out
				.println("****************************** EDIT **************************************");
		if (isEdit()) {
			log.info("[saveStaff] Actualizando un ODECO revisador por Staff Id: "
					+ this.getIdOfertaStr());
			oferta.setFormularioOdecoId(Integer.valueOf(this.getIdOfertaStr()));
		} else {
			log.info("[saveStaff] Guardando un nuevo Odeco");
			oferta.setFormularioOdecoId(null);

		}
		try {
			log.debug("[saveStaff] se va obtener la ciudad localidad "+selectCiudad);
			oferta.setCiudadLocalidad(ciudadBL.getCiudad(
					Integer.valueOf(selectCiudadReclamacion)).getNombre());
			log.debug("[saveStaff] se obtuvo la ciudad de localidad "+selectCiudad);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[saveStaff] error al obtener la ciudad de localidad "+selectCiudad,e);
			return "";
		}
		
		
		oferta.setMotivoReclamos(motivosReclamos);
		oferta.setObjetoReclamos(objetosReclamos);
		try {
			log.debug("[saveStaff] se va obtener la ciudad de reclamacion "+selectCiudadReclamacion);
			oferta.setCiudad(ciudadBL.getCiudad(Integer
					.valueOf(selectCiudadReclamacion)));
			log.debug("[saveStaff] se obtuvo la ciudad reclamacion "+selectCiudadReclamacion);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[saveStaff] error al obtener la ciudad reclamacion "+selectCiudadReclamacion,e);
			return "";
		}
		
		try {
			log.debug("[saveStaff] se va obtener el medio de notififacion "+selectMedioNotificacion);
			oferta.setMedioNotificacion(medioNotificacionBL
					.getMedioNotificacionID(Integer
							.valueOf(selectMedioNotificacion)));
			log.debug("[saveStaff] se obtuvo el medio de notififacion "+selectMedioNotificacion);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[saveStaff] error al obtener el medio de notififacion "+selectMedioNotificacion,e);
			return "";
			
		}
	
		oferta.setCodigoReclamacion(codigoReclamacion);
		String message = ofertaN.validate(oferta);

		if (!message.isEmpty()) {
			log.info("[save] Error al Validar ODECO:" + message);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error", message));
			System.out
					.println("****************************** VALIDATE **************************************");
			return "";
		}
		System.out
				.println("**************************** iniciando Transaccion ****************************************");
		try {

			if (isEdit()) { // modificar

				ofertaN.update(getOferta());
					
				if(coordinador!=null){
					colaCoordinadorBL.incrementarCantidad(coordinador);
				}
				
				setGuardar(false);
				setListaOferta(ofertaN.listarTodo());

				listaAdjuntos = adjuntoBL.getCiudadNombre(getOferta()
						.getFormularioOdecoId());
				Result result = mailBL.sendEmail(correos, listaAdjuntos,
						getOferta());
				listaAdjuntos = new ArrayList<Adjunto>();
				oferta.setAdjuntos(listaAdjuntos);
				if (result.getCode().equalsIgnoreCase(Code.OK)) {
					oferta.setEnviadoMail(false);
					ofertaN.update(getOferta());
				} else {
					/*oferta.setEnviadoMail(true);
					oferta.setEnviado(enviado);
					oferta.setRevisar(usuario.getLogin());
					ofertaN.update(getOferta());
					setListaOferta(ofertaN.listarTodo());
					setEdit(false);
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_WARN,
											"ERROR",
											Parameter.MENSAJE_GUARDADO_ERROR_MAIL));
					context.execute("dlg5.hide()");
					listaAdjuntos = new ArrayList<Adjunto>();
					//listaInputStream = new ArrayList<InputStream>();
					listaInputStream= new ListaInputStream();
					correos = new ArrayList<String>();
					return "";*/
					oferta.setEnviadoMail(false);
					ofertaN.update(getOferta());
					listaAdjuntos = new ArrayList<Adjunto>();
					//listaInputStream = new ArrayList<InputStream>();
					listaInputStream= new ListaInputStream();
					correos = new ArrayList<String>();
					log.debug(result.getDescription());
					controlerBitacora.update(
							DescriptorBitacora.ODECO_REVISION_STAFF,
							oferta.getFormularioOdecoId() + "",
							oferta.getCodigoReclamacionInstitucion() + "/"
									+ oferta.getCodigoReclamacionCiudad() + "/"
									+ oferta.getCodigoReclamacionCorrelativo());
					
					context.execute("dlg5.hide()");
					setListaOferta(ofertaN.listarTodo());
					FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_WARN,
									"ERROR",
									Parameter.MENSAJE_GUARDADO_ERROR_MAIL));
					return "";
				}
				listaAdjuntos = new ArrayList<Adjunto>();
				//listaInputStream = new ArrayList<InputStream>();
				listaInputStream= new ListaInputStream();
				correos = new ArrayList<String>();
				log.debug(result.getDescription());
				setListaOferta(ofertaN.listarTodo());
				controlerBitacora.update(
						DescriptorBitacora.ODECO_REVISION_STAFF,
						oferta.getFormularioOdecoId() + "",
						oferta.getCodigoReclamacionInstitucion() + "/"
								+ oferta.getCodigoReclamacionCiudad() + "/"
								+ oferta.getCodigoReclamacionCorrelativo());
				context.execute("dlg5.hide()");
			} else { // insertar

			/*	// oferta.setObjetoReclamos(objetosReclamos);
				// oferta.setMotivoReclamos(motivosReclamos);
				ofertaN.save(getOferta());
				setListaOferta(ofertaN.listarTodo());
				controlerBitacora.insert(
						DescriptorBitacora.ODECO_PRIMERA_INSTANCIA,
						oferta.getFormularioOdecoId() + "",
						oferta.getCodigoReclamacionInstitucion() + "/"
								+ oferta.getCodigoReclamacionCiudad() + "/"
								+ oferta.getCodigoReclamacionCorrelativo());
				setEdit(false);*/

			}
			setListaOferta(ofertaN.listarTodo());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"NOTIFICACION", Parameter.MENSAJE_GUARDADO));
			return "";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							Parameter.MENSAJE_GUARDADO_ERROR));
			log.error("[saveStaff] Exception : " + e.getMessage(), e);
			return "";
		}

		

	}

	public String saveCoordinador(){
		// System.out.println("********************************************************************");
		log.debug("[saveCoordinador] Asignando datos al ODECO con la ide "
				+ this.getIdOfertaStr());
		RequestContext context = RequestContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		this.strIdUs = (String) request.getSession().getAttribute(
				"TEMP$USER_NAME");
		Usuario usuario = usuarioBL.getUserLogin(strIdUs);
		//String enviado= oferta.getEnviado();
		//oferta.setEnviado(this.strIdUs);
		oferta.setRevisar(this.strIdUs);
		oferta.setUsuario(usuario);
		oferta.setDevuelto(null);
		oferta.setAdjuntos(listaAdjuntos);
	//	String enviado=oferta.getEnviado();
		

		objetosReclamos = new ArrayList<ObjetoReclamo>();
		motivosReclamos = new ArrayList<MotivoReclamo>();

		// System.out.println("********************************************************************");
		selectedObjetoReclamo=auxSelectObjeto;
		for (String s : selectedObjetoReclamo) {
			ObjetoReclamo com = objetoReclamoBL.getObjetoReclamo(Integer
					.valueOf(s));
			objetosReclamos.add(com);
		}
		selectedMotivoReclamo=auxSelectMotivo;
		for (String s : selectedMotivoReclamo) {
			MotivoReclamo com = motivoReclamoBL.getMotivoReclamo(Integer
					.valueOf(s));
			motivosReclamos.add(com);
		}
		oferta.setEstadoRespuesta("ANULADO");
		if(!anulado){
			
			if (selectEstadoRespuesta.equalsIgnoreCase("-1")) {
				log.info("[saveCoordinador] estado respuesta vacio");
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Mensaje de error",
								Parameter.MENSAJE_VALIDATE_ESTADORESPUESTA));

				return "";

			}
			RespuestaOdeco respuestaOdeco = respuestaOdecoBL
					.getRespuestaOdecoNombre(respuestaOdecoBL.getRespuestaOdecoID(
							Integer.valueOf(selectEstadoRespuesta))
							.getRespuestaOdeco());
			if (oferta.getDetalleRespuesta().isEmpty()
					|| oferta.getDetalleRespuesta() == null) {
				log.info("[saveCoordinador] detalle respuesta vacio");
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Mensaje de error", Parameter.MENSAJE_VALIDATE_DETALLERESPUESTA));

				return "";

			}
			oferta.setRespuestaOdeco(respuestaOdeco);			
			oferta.setEstadoRespuesta("CERRADO");
		
		}else{
			if(oferta.getDetalleAnulado().isEmpty()){
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Mensaje de error", Parameter.MENSAJE_VALIDATE_DETALLEANULADO));

				return "";
				
			}
		}
		
		// System.out.println("****************************** despues del for 1 **************************************");

		System.out
				.println("****************************** EDIT **************************************");
		if (isEdit()) {
			log.info("[saveCoordinador] Actualizando un ODECO revisador por Coordinadior Id: "
					+ this.getIdOfertaStr());
			oferta.setFormularioOdecoId(Integer.valueOf(this.getIdOfertaStr()));
		} else {
			log.info("[saveCoordinador] Guardando un nuevo Odeco");
			oferta.setFormularioOdecoId(null);

		}
		oferta.setCiudadLocalidad(ciudadBL.getCiudad(
				Integer.valueOf(selectCiudadReclamacion)).getNombre());
		oferta.setMotivoReclamos(motivosReclamos);
		oferta.setObjetoReclamos(objetosReclamos);
		oferta.setCiudad(ciudadBL.getCiudad(Integer
				.valueOf(selectCiudadReclamacion)));
		oferta.setMedioNotificacion(medioNotificacionBL
				.getMedioNotificacionID(Integer
						.valueOf(selectMedioNotificacion)));
		oferta.setCodigoReclamacion(codigoReclamacion);
		oferta.setFechaCerrado(new Timestamp(new Date().getTime()));
		oferta.setEstado(false);
	
		
		String message = ofertaN.validate(oferta);

		if (!message.isEmpty()) {
			log.info("[saveCoordinador] Error al Validar ODECO:" + message);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error", message));
			System.out
					.println("****************************** VALIDATE **************************************");
			return "";
		}
		System.out
				.println("**************************** iniciando Transaccion ****************************************");
		try {

			if (isEdit()) { // modificar

				ofertaN.update(getOferta());
					
				
				//copyFile(listaInputStream, listaAdjuntos);
				//copyFile(listaInputStream.get(), listaAdjuntos);
				if(!copyFile(listaInputStream.get(), listaAdjuntos)){					
					log.error("[saveCoordinador] no se pudieron copiar los archivos al servidor");
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Mensaje de error", "No se pudieron Copiar los arhivos al servidor"));
					return "";
					
				}
				correos.add(usuario.getCorreo());// correo de el mismo
				if (exportar(getOferta())) {
					log.debug("se exporto satisfactoriamente el formulario con codigo: "
							+ getOferta().getCodigoReclamacion());

				} else {
					log.debug("No se exporto satisfactoriamente el formulario con codigo: "
							+ getOferta().getCodigoReclamacion());
				}
				listaAdjuntos = adjuntoBL.getCiudadNombre(getOferta()
						.getFormularioOdecoId());
				Result result = mailBL.sendEmail(correos, listaAdjuntos,
						getOferta());
				listaAdjuntos = new ArrayList<Adjunto>();
				oferta.setAdjuntos(listaAdjuntos);
				if (result.getCode().equalsIgnoreCase(Code.OK)) {
					oferta.setEnviadoMail(false);
					ofertaN.update(getOferta());
				} else {
					/*oferta.setEnviadoMail(true);
					oferta.setEnviado(enviado);
					oferta.setRevisar(usuario.getLogin());
					ofertaN.update(getOferta());
					setListaOferta(ofertaN.listarTodo());
					setEdit(false);
					listaAdjuntos = new ArrayList<Adjunto>();
					//listaInputStream = new ArrayList<InputStream>();
					listaInputStream = new ListaInputStream();
					correos = new ArrayList<String>();
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_WARN,
											"ERROR",
											Parameter.MENSAJE_GUARDADO_ERROR_MAIL));
					context.execute("dlg3.hide()");
					return "";*/
					oferta.setEnviadoMail(false);
					ofertaN.update(getOferta());
					listaAdjuntos = new ArrayList<Adjunto>();
					//listaInputStream = new ArrayList<InputStream>();
					listaInputStream= new ListaInputStream();
					
					correos = new ArrayList<String>();
					setListaOferta(ofertaN.listarTodo());
					controlerBitacora.update(
							DescriptorBitacora.ODECO_REVISION_COORDINADOR,
							oferta.getFormularioOdecoId() + "",
							oferta.getCodigoReclamacionInstitucion() + "/"
									+ oferta.getCodigoReclamacionCiudad() + "/"
									+ oferta.getCodigoReclamacionCorrelativo());
					context.execute("dlg3.hide()");
					setListaOferta(ofertaN.listarTodo());
					FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_WARN,
									"ERROR",
									Parameter.MENSAJE_GUARDADO_ERROR_MAIL));
					return "";
				}
				listaAdjuntos = new ArrayList<Adjunto>();
				//listaInputStream = new ArrayList<InputStream>();
				listaInputStream= new ListaInputStream();
				
				correos = new ArrayList<String>();
				setListaOferta(ofertaN.listarTodo());
				controlerBitacora.update(
						DescriptorBitacora.ODECO_REVISION_COORDINADOR,
						oferta.getFormularioOdecoId() + "",
						oferta.getCodigoReclamacionInstitucion() + "/"
								+ oferta.getCodigoReclamacionCiudad() + "/"
								+ oferta.getCodigoReclamacionCorrelativo());
				context.execute("dlg3.hide()");
				
			} else { // insertar

				// oferta.setObjetoReclamos(objetosReclamos);
				// oferta.setMotivoReclamos(motivosReclamos);
				ofertaN.save(getOferta());
				setListaOferta(ofertaN.listarTodo());
				controlerBitacora.insert(
						DescriptorBitacora.ODECO_PRIMERA_INSTANCIA,
						oferta.getFormularioOdecoId() + "",
						oferta.getCodigoReclamacionInstitucion() + "/"
								+ oferta.getCodigoReclamacionCiudad() + "/"
								+ oferta.getCodigoReclamacionCorrelativo());
				setEdit(false);

			}
			setListaOferta(ofertaN.listarTodo());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"NOTIFICACION", Parameter.MENSAJE_GUARDADO));
			return "";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							Parameter.MENSAJE_GUARDADO_ERROR));
			log.error("[saveCoordinador] Exception : " + e.getMessage(), e);
			return "";
		}
		// newObject();
		

	}

	public String saveRegulacion() throws Exception {
		// System.out.println("********************************************************************");
		log.debug("[saveRegulacion] Asignando datos al ODECO con la ide "
				+ this.getIdOfertaStr());
		RequestContext context = RequestContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		this.strIdUs = (String) request.getSession().getAttribute(
				"TEMP$USER_NAME");
		Usuario usuario = usuarioBL.getUserLogin(strIdUs);
		oferta = ofertaN.getEstadoTrue(Integer.valueOf(this.getIdOfertaStr()));
		//String enviado= oferta.getEnviado();
		oferta.setEnviado(this.strIdUs);
		oferta.setRevisar("ENVIADO ATT");
		oferta.setEstadoRespuesta("ABIERTO");
		oferta.setUsuario(usuario);
		oferta.setDevuelto(null);
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		for (Adjunto a : listaAdjuntos) {
			a.setAdjuntoId(null);
			a.setFormularioOdeco(getOferta());
			a.setRuta(parametroBL.getRutaAdjunto()+getOferta().getCodigoReclamacionInstitucion()+getOferta().getCodigoReclamacionCiudad()+"RA"+getOferta().getCodigoReclamacionCorrelativo()+"-"+formateador.format(new Date())+File.separator+a.getNombre());
		}
		oferta.setAdjuntos(listaAdjuntos);		
		objetosReclamos = new ArrayList<ObjetoReclamo>();
		motivosReclamos = new ArrayList<MotivoReclamo>();

		// System.out.println("********************************************************************");
		for (String s : selectedObjetoReclamo) {
			ObjetoReclamo com = objetoReclamoBL.getObjetoReclamo(Integer
					.valueOf(s));
			objetosReclamos.add(com);
		}
		for (String s : selectedMotivoReclamo) {
			MotivoReclamo com = motivoReclamoBL.getMotivoReclamo(Integer
					.valueOf(s));
			motivosReclamos.add(com);
		}

		try {

			ofertaN.update(getOferta());
			setGuardar(false);
			
			if(!listaInputStream.get().isEmpty()){
				//copyFileSegundaInstancia(listaInputStream.get(), listaAdjuntos);
				if(!copyFileSegundaInstancia(listaInputStream.get(), listaAdjuntos)){					
					log.error("[saveRegulacion] no se pudieron copiar los archivos al servidor");
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Mensaje de error", "No se pudieron Copiar los arhivos al servidor"));
					return "";
					
				}
			}
			//correos.add(parametroBL.getCorreoATT());// mail de ATT
			agregarCorreos(parametroBL.getCorreoATT());
			listaAdjuntos = adjuntoBL.getCiudadNombre(getOferta()
					.getFormularioOdecoId());
			Result result = mailBL.sendEmail(correos, listaAdjuntos,
					getOferta());
			listaAdjuntos = new ArrayList<Adjunto>();
			oferta.setAdjuntos(listaAdjuntos);
			if (result.getCode().equalsIgnoreCase(Code.OK)) {
				oferta.setEnviadoMail(false);
				ofertaN.update(getOferta());
			} else {
				/*oferta.setEnviadoMail(true);
				oferta.setRevisar(usuario.getLogin());
				oferta.setEnviado(enviado);
				ofertaN.update(getOferta());
				setListaOferta(ofertaN.listarTodo());
				setEdit(false);
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_WARN,
										"ERROR",
										Parameter.MENSAJE_GUARDADO_ERROR_MAIL));
				context.execute("dlg6.hide()");
				return "";*/
				oferta.setEnviadoMail(false);
				ofertaN.update(getOferta());
				log.debug(result.getDescription());
				listaAdjuntos = new ArrayList<Adjunto>();
				//listaInputStream = new ArrayList<InputStream>();
				listaInputStream=new ListaInputStream();
				correos = new ArrayList<String>();
				setListaOferta(ofertaN.listarTodo());
				controlerBitacora.update(
						DescriptorBitacora.ODECO_REVISION_REGULACION,
						oferta.getFormularioOdecoId() + "",
						oferta.getCodigoReclamacionInstitucion() + "/"
								+ oferta.getCodigoReclamacionCiudad() + "/"
								+ oferta.getCodigoReclamacionCorrelativo());

				setListaOferta(ofertaN.listarTodo());
				FacesContext
				.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"ERROR",
								Parameter.MENSAJE_GUARDADO_ERROR_MAIL));
				context.execute("dlg6.hide()");
				return "";
			}
			
			log.debug(result.getDescription());
			listaAdjuntos = new ArrayList<Adjunto>();
			//listaInputStream = new ArrayList<InputStream>();
			listaInputStream=new ListaInputStream();
			correos = new ArrayList<String>();
			setListaOferta(ofertaN.listarTodo());
			controlerBitacora.update(
					DescriptorBitacora.ODECO_REVISION_REGULACION,
					oferta.getFormularioOdecoId() + "",
					oferta.getCodigoReclamacionInstitucion() + "/"
							+ oferta.getCodigoReclamacionCiudad() + "/"
							+ oferta.getCodigoReclamacionCorrelativo());

			setListaOferta(ofertaN.listarTodo());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"NOTIFICACION", Parameter.MENSAJE_GUARDADO));
			context.execute("dlg6.hide()");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							Parameter.MENSAJE_GUARDADO_ERROR));
			log.error("[saveRegulacion] Exception : " + e.getMessage(), e);
		}
		// newObject();
		return "";

	}
	public void agregarCorreos(String c){

        String delims = "[;]";
        String[] tokens = c.split(delims);
        for (int i = 0; i < tokens.length; i++) {
        	correos.add(tokens[i]);
        }
}
	public void select(String id){

		try {
			setEdit(true);
			log.debug("Odeco  Seleccionado:" + id);
			if (id == null)
				return;
			listaAjuntosVista = adjuntoBL.getCiudadNombre(Integer.parseInt(id));
			setListaAjuntosVista(listaAjuntosVista);
			setOferta(ofertaN.getEstadoTrue(Integer.parseInt(id)));
			codigoReclamacion = oferta.getCodigoReclamacion();
			// this.idOfertaStr = String.valueOf(this.oferta.getId());
			this.idOfertaStr = id;
			setFechaIn(getOferta().getFechaIncidencia());
			
			if(isGuardar()){
				selectObjetoReclamos = new ArrayList<SelectItem>();		
				List<ObjetoReclamo> list2 = null;
				try {
					list2 = objetoReclamoBL.getObjetosReclamosList();
					for (ObjetoReclamo item : list2) {
						SelectItem s = new SelectItem(item.getObjetoReclamoId(),
								item.getObjetoReclamo());
						
						selectObjetoReclamos.add(s);
						
					}
					
					log.debug("Carga de Lista de Objetos de Reclamos [ok]");
				} catch (Exception e) {
					log.error("Carga de Lista de Objetos de Reclamos [fail]", e);
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"Mensaje de error",
											Parameter.MENSAJE_LISTAOBJETOS_ERROR));
				}
				
				selectMotivoReclamos = new ArrayList<SelectItem>();
				
				List<MotivoReclamo> list4 = null;
				try {
					list4 = motivoReclamoBL.getMotivoReclamosList();
					for (MotivoReclamo item : list4) {
							SelectItem s = new SelectItem(item.getMotivoReclamoId(),
									item.getMotivoReclamo());					
							
							selectMotivoReclamos.add(s);
						
					}
					log.debug("Carga de Lista de Motivos de Reclamo [ok]");
				} catch (Exception e) {
					log.error("Carga de Lista de Motivos de Reclamo [fail]", e);
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"Mensaje de error",
											Parameter.MENSAJE_MOTIVOS_ERROR));
				}
			}else{
				selectObjetoReclamos = new ArrayList<SelectItem>();		
				List<ObjetoReclamo> list2 = null;
				try {
					list2 = objetoReclamoBL.getObjetosReclamosList();
					for (ObjetoReclamo item : list2) {
						SelectItem s = new SelectItem(item.getObjetoReclamoId(),
								item.getObjetoReclamo());
						s.setDisabled(true);
						selectObjetoReclamos.add(s);
						
					}
					
					log.debug("Carga de Lista de Objetos de Reclamos [ok]");
				} catch (Exception e) {
					log.error("Carga de Lista de Objetos de Reclamos [fail]", e);
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"Mensaje de error",
											Parameter.MENSAJE_LISTAOBJETOS_ERROR));
				}
				
				selectMotivoReclamos = new ArrayList<SelectItem>();
				
				List<MotivoReclamo> list4 = null;
				try {
					list4 = motivoReclamoBL.getMotivoReclamosList();
					for (MotivoReclamo item : list4) {
							SelectItem s = new SelectItem(item.getMotivoReclamoId(),
									item.getMotivoReclamo());					
							s.setDisabled(true);
							selectMotivoReclamos.add(s);
						
					}
					log.debug("Carga de Lista de Motivos de Reclamo [ok]");
				} catch (Exception e) {
					log.error("Carga de Lista de Motivos de Reclamo [fail]", e);
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"Mensaje de error",
											Parameter.MENSAJE_MOTIVOS_ERROR));
				}
			}
			selectedObjetoReclamo = new String[oferta.getObjetoReclamos().size()];
			System.out.println("obj:"+oferta.getObjetoReclamos().size());
			int j = 0;
			for (ObjetoReclamo comm : oferta.getObjetoReclamos()) {

				selectedObjetoReclamo[j] = comm.getObjetoReclamoId().toString();			
				j++;			

			}
			auxSelectObjeto=selectedObjetoReclamo;
			
			List<Object[]> motrec = ofertaN.getMotivoReclamacion(idOfertaStr);
			selectedMotivoReclamo = new String[motrec.size()];
			int i = 0;
			for (Object comm : motrec) {
				selectedMotivoReclamo[i] = comm.toString();
				i++;						
			}
			
		
			auxSelectMotivo=selectedMotivoReclamo;
		
			if(!oferta.getCiudadLocalidad().isEmpty() ||oferta.getCiudadLocalidad()!=null){
				Ciudad ciudad=ciudadBL.getCiudadNombre(oferta.getCiudadLocalidad());
				if(ciudad!=null){
					selectCiudad = ciudad.getCiudadId().toString();
				}else{
					log.error("[select] error al obtener la ciudad localidad");
					return ;
				}
				
			}
			
			formRegistro=oferta.getFormaRegistro();			 
			selectCiudadReclamacion = oferta.getCiudad().getCiudadId().toString();
			selectMedioNotificacion = oferta.getMedioNotificacion()
					.getMedioNotificacionId().toString();
			
			log.info("[select] datos cargados correctamente del odeco: "+id);
		} catch (Exception e) {
			log.error("[select] error al cargar los datos del odeco: "+id+" seleccionado,"+e.getMessage(),e);
			return ;
		}
		
		
		

	}
	

	public void enviarCorregir() {
		try {
			log.debug("[enviarCorregir] se enviara a corregir el odeco");
			if (oferta.getObservaciones().isEmpty()
					|| oferta.getObservaciones() == null) {
				log.info("[enviarCorregir] Debe llenar el campo Observacion");
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Mensaje de error",
								Parameter.MENSAJE_VALIDATE_OBSERVACION));

				return;

			}
			RequestContext context = RequestContext.getCurrentInstance();
			correos= new ArrayList<String>();
			Devuelto d = new Devuelto();
			d.setDevuelvtoId(null);
			d.setFormularioOdeco(oferta);
			d.setCiudadLocalidad(ciudadLocalidad);
			d.setNombreReclamante(nombreReclamante);
			d.setNombreTitular(nombreTitutar);
			d.setTelefonoReferencia(telefonoReferencia);
			d.setLineaReclamo(lineaReclamo);
			d.setCorreo(correo);
			d.setDireccionDomicilio(direccionDomicilio);
			d.setDireccionNotificacion(direccionNotificacion);
			d.setObjetoReclamo(objetoReclamo);
			d.setCiudadReclamacion(ciudadReclamacion);
			d.setMedioNotificacion(medioNotificacion);
			d.setMotivoReclamo(motivoReclamo);
			d.setFechaResolucion(fechaResolucion);
			d.setFechaIncidencia(fechaIncidencia);
			d.setFax(fax);
			d.setDetalleMotivo(detalleMotivo);
			d.setFormaRegistro(formaRegistro);
			log.debug("[enviarCorregir] se va guardar un devuelto");
			devueltoBL.saveDevuelto(d);
			HttpServletRequest request = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			this.strIdUs = (String) request.getSession().getAttribute(
					"TEMP$USER_NAME");
			//Usuario usuario = usuarioBL.getUserLogin(strIdUs);
			ofertaN.getEstadoTrue(oferta.getFormularioOdecoId());
			String enviado=oferta.getEnviado();
			oferta.setRevisar(oferta.getEnviado());
			oferta.setEnviado(this.strIdUs);
			oferta.setDevuelto(true);
			ofertaN.update(getOferta());
			setGuardar(false);
			Usuario usuario2=usuarioBL.getUserLogin(enviado);
			correos.add(usuario2.getCorreo());// correo del
																// agente para
																// corregir
			listaAdjuntos = adjuntoBL.getCiudadNombre(getOferta()
					.getFormularioOdecoId());
			Result result = mailBL.sendEmail(correos, listaAdjuntos,
					getOferta());
			listaAdjuntos = new ArrayList<Adjunto>();
			correos= new ArrayList<String>();
			oferta.setAdjuntos(listaAdjuntos);
			if (result.getCode().equalsIgnoreCase(Code.OK)) {
				oferta.setEnviadoMail(false);				
				ofertaN.update(getOferta());
			} else {
				/*oferta.setEnviadoMail(true);
				oferta.setRevisar(usuario.getLogin());
				oferta.setEnviado(enviado);
				ofertaN.update(getOferta());
				setListaOferta(ofertaN.listarTodo());
				setEdit(false);
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_WARN,
										"ERROR",
										Parameter.MENSAJE_GUARDADO_ERROR_MAIL));
				context.execute("dlg5.hide()");
				return;*/
				oferta.setEnviadoMail(false);				
				ofertaN.update(getOferta());
				listaAdjuntos = new ArrayList<Adjunto>();
				log.debug(result.getDescription());
				log.info("[enviarCorregir] se registro un devuelto. Id: ");
				controlerBitacora.insert(
						DescriptorBitacora.DEVUELTO,
						oferta.getFormularioOdecoId() + "",
						oferta.getCodigoReclamacionInstitucion() + "/"
								+ oferta.getCodigoReclamacionCiudad() + "/"
								+ oferta.getCodigoReclamacionCorrelativo());
				FacesContext
				.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"ERROR",
								Parameter.MENSAJE_GUARDADO_ERROR_MAIL));
				setEdit(false);
				setListaOferta(ofertaN.listarTodo());
				context.execute("dlg5.hide()");
				return ;
			}
			listaAdjuntos = new ArrayList<Adjunto>();
			log.debug(result.getDescription());
			log.info("[enviarCorregir] se registro un devuelto. Id: ");
			controlerBitacora.insert(
					DescriptorBitacora.DEVUELTO,
					oferta.getFormularioOdecoId() + "",
					oferta.getCodigoReclamacionInstitucion() + "/"
							+ oferta.getCodigoReclamacionCiudad() + "/"
							+ oferta.getCodigoReclamacionCorrelativo());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"NOTIFICACION", Parameter.MENSAJE_GUARDADO));
			setListaOferta(ofertaN.listarTodo());
			context.execute("dlg5.hide()");
		} catch (Exception e) {
			// TODO: handle exception
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							Parameter.MENSAJE_GUARDADO_ERROR));
			log.error("[save] Exception : " + e.getMessage(), e);
		}

	}
	
	public Boolean exportar(FormularioOdeco f) {
		boolean result = Boolean.FALSE;
		log.debug("se va exportar el odeco "+f.toString());
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		String titleReport="";
		String pathDetalle="";
		String filenameReport="";
		Map<String, Object> rptParameters = new HashMap<String, Object>();
		if(f.getTipoFormulario().getTipoFormularioId()==1){
			 filenameReport = "PrimeraInstanciaReport";
			 pathDetalle = parametroBL.getRutaAdjunto()
					+ oferta.getCodigoReclamacionInstitucion()
					+ oferta.getCodigoReclamacionCiudad()
					+ oferta.getCodigoReclamacionCorrelativo()+"-"+formateador.format(oferta.getFechaReclamo())
					+  File.separator+"FormularioOdeco"
					+ f.getTipoFormulario().getDescripcion()
					+ ".pdf";
			 titleReport = f.getTipoFormulario().getDescripcion();
			 rptParameters.put("fecha_reclamo",  formateador.format(f.getFechaReclamo()));
				rptParameters.put("fecha_incidencia",formateador.format(f.getFechaIncidencia()));
		}else{
			 filenameReport = "SegundaInstanciaReport";
			 pathDetalle = parametroBL.getRutaAdjunto()
					+ oferta.getCodigoReclamacionInstitucion()
					+ oferta.getCodigoReclamacionCiudad()+"RA"
					+ oferta.getCodigoReclamacionCorrelativo()+"-"+formateador.format(oferta.getFechaReclamo())
					+  File.separator+"FormularioOdeco"
					+ f.getTipoFormulario().getDescripcion()
					+ ".pdf";
			 titleReport = f.getTipoFormulario().getDescripcion();
			 FormularioOdeco pi=ofertaN.getFormularioPIidSegunda(f.getFormularioOdecoId());
			 if(pi==null){
				 log.error("[Exportar] no se pudo obtener el formulario de primera instancia con id: "+f.getFormularioOdecoId());
				 return false;
			 }
				rptParameters.put("fecha_reclamacion_directa",formateador.format(pi.getFechaReclamo()));
				rptParameters.put("fecha_notificacion", "");
				rptParameters.put("fecha_reclamacion_administrativa", formateador.format(f.getFechaReclamo()));
				rptParameters.put("fecha_incidente", formateador.format(f.getFechaIncidencia()));
				rptParameters.put("operador_proveedor","Telecel SA");
		}
	
		//String pathLogo = FileUtil
		//		.getRealPath("C:/TigoTrabajo/ODECO DIGITAL/PROYECTO/COD/ODECO_DIGITAL/WebContent/resources/report/logo.png");
		
		rptParameters.put("Logo", parametroBL.getRutaLogoOdeco());
		rptParameters.put("Titulo", titleReport);
		rptParameters.put("Ciudad_localidad", f.getCiudadLocalidad());
		rptParameters.put("nombre_reclamante", f.getNombreReclamante());
		rptParameters.put("Nombre_Titular", f.getNombreTitular());
		rptParameters.put("Telefono_Referencia", f.getTelefonoReferencia());
		rptParameters.put("correo_cliente", f.getCorreo());
		rptParameters.put("fax", f.getFax());
		rptParameters.put("Linea_reclamo", f.getLineaReclamo());
		rptParameters.put("direccion_reclamo", f.getDireccionDomicilio());
		rptParameters.put("direccion_notificacion",
				f.getDireccionNotificacion());
		rptParameters.put("codigo_reclamacion", f.getCodigoReclamacion());
		rptParameters.put("fecha_resolucion",formateador.format(f.getFechaResolucion()));				
		rptParameters.put("detalle_motivo_reclamacion", f.getDetalleMotivoReclamacion());
		rptParameters.put("periodo_objeto_reclamo",
				String.valueOf(f.getPeriodoObjetoReclamo()));
		rptParameters.put("ciudad_reclamacion", f.getCiudad().getNombre());
		rptParameters.put("medio_notificacion", f.getMedioNotificacion()
				.getDescripcion());
		String lista = "";
		StringBuffer buf = new StringBuffer();		
		for (ObjetoReclamo a : objetosReclamos) {
			lista += a.getObjetoReclamo() + ",";
			 buf.append(a.getObjetoReclamo());
			 buf.append(",");
		}
		lista = buf.toString();
		rptParameters.put("objeto_reclamo", lista);
		lista = "";
		 buf = new StringBuffer();
		for (MotivoReclamo a : motivosReclamos) {
			buf.append(a.getMotivoReclamo());
			buf.append(",");
		}
		lista = buf.toString();
		rptParameters.put("motivo_reclamacion", lista);
		//rptParameters.put("detalle_motivo_reclamacion", "");
		if (f.getRespuestaOdeco() != null) {
			rptParameters.put("estado_respuesta","  "+ f.getRespuestaOdeco()
					.getRespuestaOdeco());
		}
		if(f.getDetalleRespuesta() != null){
			rptParameters.put("detalle_respuesta","  "+ f.getDetalleRespuesta());
		}
		

		result = printUtils.exportReportToXlsFile(filenameReport, pathDetalle,
				rptParameters);
		if (result) {
			log.info("El detalle de llamadas se exporto al directorio "
					+ pathDetalle);
			Adjunto a = new Adjunto();
			a.setNombre("FormularioOdeco"
					+ f.getTipoFormulario().getDescripcion() + ".pdf");
			a.setRuta(pathDetalle);
			a.setEstado(true);
			a.setFormularioOdeco(f);
			listaAdjuntos.add(a);
			log.info("Termino la exportacion del detalle al directorio "
					+ pathDetalle);

		}else{
			log.error("[exportar] no se exporto el odeco  "+f.toString()+" a la ruta "
					+ pathDetalle);
		}
		
		return result;

	}

	public void handleFileUpload(FileUploadEvent event)  {
	
		//try {
			log.debug("[handleFileUpload] se va adjuntar los archivos");
			HttpServletRequest request = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			this.strIdUs = (String) request.getSession().getAttribute(
					"TEMP$USER_NAME");
			Usuario usuario = usuarioBL.getUserLogin(strIdUs);
			SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
			directorio = new File(
					parametroBL.getRutaAdjunto()
							+ oferta.getCodigoReclamacionInstitucion()
							+ oferta.getCodigoReclamacionCiudad()
							+ oferta.getCodigoReclamacionCorrelativo()+"-"+formateador.format(oferta.getFechaReclamo()));
			// directorio.mkdirs();
			Adjunto adjunto = new Adjunto();
			adjunto.setNombre(usuario.getRol().getNombre() + "_"
					+ event.getFile().getFileName());
			adjunto.setEstado(true);
			adjunto.setAdjuntoId(null);
			adjunto.setFormularioOdeco(getOferta());
			adjunto.setRuta(parametroBL.getRutaAdjunto()+ oferta.getCodigoReclamacionInstitucion()+ oferta.getCodigoReclamacionCiudad()+ oferta.getCodigoReclamacionCorrelativo()+"-"+formateador.format(oferta.getFechaReclamo()) +  File.separator
					+ usuario.getRol().getNombre() + "_"
					+ event.getFile().getFileName());
			adjunto.setLogin(usuario.getLogin());
			adjunto.setRol(usuario.getRol().getNombre());
			adjunto.setFecha(new Timestamp(new Date().getTime()));
			listaAdjuntos.add(adjunto);
			String[] a= new String[listaAdjuntos.size()];			
			for (int i = 0; i < listaAdjuntos.size(); i++) {
				a[i]=listaAdjuntos.get(i).getNombre();
			}
			setSelectedAdjuntos(a);
			try {
				//listaInputStream.add(event.getFile().getInputstream());
				listaInputStream.add(event.getFile().getInputstream());
			} catch (IOException e) {
				
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Notificacion",Parameter.MENSAJE_ADJUNTOS_ERROR));
				log.error("[handleFileUpload] Notificacion : No Se adjunto el archivo",e);
				return ;
			}
			// copyFile(event.getFile().getFileName(),
			// event.getFile().getInputstream());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Notificacion", micrium.util.Parameter.MENSAJE_ADJUNTOS));
			log.debug("[handleFileUpload] Notificacion : Se adjunto Correctamente");
	
		

	}

	public Boolean copyFile(List<InputStream> listaInS, List<Adjunto> listAd) {
	//	try {
			Boolean sw = true;
			log.debug("[copyFile] se van a copiar los adjuntos");
			SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
			File directorio = new File(
					parametroBL.getRutaAdjunto()
							+ oferta.getCodigoReclamacionInstitucion()
							+ oferta.getCodigoReclamacionCiudad()
							+ oferta.getCodigoReclamacionCorrelativo()+"-"+formateador.format(oferta.getFechaReclamo()));
			if(directorio.exists()){				
					for (int i = 0; i < listAd.size(); i++) {
						String ruta="";
						ruta=listAd.get(i).getRuta();
						File file= new File(ruta);
						OutputStream out=null;
						try {
							 out= new FileOutputStream(file);
							 int read = 0;
								byte[] bytes = new byte[1024];
				
								while ((read = listaInS.get(i).read(bytes)) != -1) {
									out.write(bytes, 0, read);
								}
				
								listaInS.get(i).close();
								out.flush();
								out.close();
				
								log.debug("[copyFile] se copio el adjunto "+listAd.get(i).toString());
						} catch (Exception e) {
							e.printStackTrace();
							log.error("[copyFile] no se pudo crear el outputstream",e );
							return false;
						}finally{
							if(out!=null){
								try {
									out.close();
								} catch (IOException e) {							
									e.printStackTrace();
									log.error("[copyFile] error al cerra el outputStream",e);
								}
							}
						}
						
					}
			}else{
				if(directorio.mkdirs()){
					for (int i = 0; i < listAd.size(); i++) {
						String ruta="";
						ruta=listAd.get(i).getRuta();
						File file= new File(ruta);
						OutputStream out=null;
						try {
							 out= new FileOutputStream(file);
							 int read = 0;
								byte[] bytes = new byte[1024];
				
								while ((read = listaInS.get(i).read(bytes)) != -1) {
									out.write(bytes, 0, read);
								}
				
								listaInS.get(i).close();
								out.flush();
								out.close();
				
								log.debug("[copyFile] se copio el adjunto "+listAd.get(i).toString());
						} catch (Exception e) {
							e.printStackTrace();
							log.error("[copyFile] no se pudo crear el outputstream",e );
							return false;
						}finally{
							if(out!=null){
								try {
									out.close();
								} catch (IOException e) {							
									e.printStackTrace();
									log.error("[copyFile] error al cerra el outputStream",e);
								}
							}
						}
						
					}
				}else{
				log.error("[copyFile] no se copiaron los archivos, no se pudo crear el directorio");
				return false;
				}
			}
			
			return sw;

	//	} catch (Exception e) {

	//		log.error("[copyFile] Notificacion : error al copiar el archivo");
	//	}

	}

	public Boolean copyFileSegundaInstancia(List<InputStream> listaInS,
			List<Adjunto> listAd) {
		Boolean sw = true;
		try {
			SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
			log.debug("[copyFileSegundaInstancia] se van a copiar los archivos de segunda instancia");
			File directorio = new File(
					parametroBL.getRutaAdjunto()
							+ oferta.getCodigoReclamacionInstitucion()
							+ oferta.getCodigoReclamacionCiudad()
							+ "RA"
							+ oferta.getCodigoReclamacionCorrelativo()+"-"+formateador.format(oferta.getFechaReclamo()));
			if(directorio.exists()){				
					for (int i = 0; i < listAd.size(); i++) {
						String ruta="";
						ruta=listAd.get(i).getRuta();
						File file= new File(ruta);
						OutputStream out=null;
						try {
							 out= new FileOutputStream(file);
							 int read = 0;
								byte[] bytes = new byte[1024];
				
								while ((read = listaInS.get(i).read(bytes)) != -1) {
									out.write(bytes, 0, read);
								}
				
								listaInS.get(i).close();
								out.flush();
								out.close();
				
								System.out.println("New file created!");
						} catch (Exception e) {
							e.printStackTrace();
							log.error("[copyFileSegundaInstancia] no se pudo crear el outputstream",e );
							return false;
						}finally{
							if(out!=null){
								out.close();
							}
						}
						
					}
				

			}else{
				if(directorio.mkdirs()){
					for (int i = 0; i < listAd.size(); i++) {
						String ruta="";
						ruta=listAd.get(i).getRuta();
						File file= new File(ruta);
						OutputStream out=null;
						try {
							 out= new FileOutputStream(file);
							 int read = 0;
								byte[] bytes = new byte[1024];
				
								while ((read = listaInS.get(i).read(bytes)) != -1) {
									out.write(bytes, 0, read);
								}
				
								listaInS.get(i).close();
								out.flush();
								out.close();
				
								System.out.println("New file created!");
						} catch (Exception e) {
							e.printStackTrace();
							log.error("[copyFileSegundaInstancia] no se pudo crear el outputstream",e );
							return false;
						}finally{
							if(out!=null){
								out.close();
							}
						}
						
					}
				}else{
				log.error("[copyFileSegundaInstancia] no se copiaron los archivos, no se pudo crear el directorio");
				return false;
				}

			}
			return sw;
		} catch (IOException e) {

			log.error("[copyFileSegundaInstancia] Notificacion : error al copiar el archivo",e);
			return false;
		}
	
	}
	public void calcularFechaResolucion(){
		try {
			log.debug("[calcularFechaResolucion] se va calcular la fecha de resolucion");
			Calendar fechaReclamo= Calendar.getInstance();
			fechaReclamo.setTime(new Date(getOferta().getFechaReclamo().getTime()));		
			Calendar fechaRes1= Calendar.getInstance();
			fechaRes1.setTime(fechaReclamo.getTime());
			//calculando fecha de resolucion contando dias no habiles(sabado y domingo)
			for (int i = 1; i <= getOferta().getPeriodoObjetoReclamo(); i++) {
				fechaRes1.add(Calendar.DAY_OF_YEAR, 1);
				while(fechaRes1.getTime().getDay()==6 ||fechaRes1.getTime().getDay()==0){
					fechaRes1.add(Calendar.DAY_OF_YEAR, 1);				
				}			
			}
			
			//calculando fecha de resolucion contando feriados	
			Ciudad ciudad = ciudadBL.getCiudad(Integer.valueOf(selectCiudadReclamacion));
			List<Calendario> listaFeriados=configuracionBL.getCalendarioListCiudad(ciudad.getNombre());
			for (Calendario calendario : listaFeriados) {
				Calendar cal= Calendar.getInstance();
				cal.setTime(new Date(calendario.getFecha().getTime()));
				if(cal.before(fechaRes1)&& cal.after(fechaReclamo)){
					fechaRes1.add(Calendar.DAY_OF_YEAR, 1);
					while(fechaRes1.getTime().getDay()==6 ||fechaRes1.getTime().getDay()==0){
						fechaRes1.add(Calendar.DAY_OF_YEAR, 1);
						//System.out.println(fechaRes1.getTime());
						int a=fechaRes1.getTime().getDay();
						//System.out.println(a);
					}
				}
			}
			//System.out.println(fechaRes1.getTime());
			oferta.setFechaResolucion(new Timestamp(fechaRes1.getTime().getTime()));
			log.debug("[calcularFechaResolucion] se va calculo la fecha de resolucion");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[calcularFechaResolucion] error al calcular la fecha de resolucion",e);
		}
		
	
		
	}

	public void calcularPeriodoObjeto() {
		try {
			log.debug("se va calcular el periodo del objeto");
			oferta.setPeriodoObjetoReclamo(0);
			int valores[]= new int[selectedMotivoReclamo.length];
			int c=0;
			for (String s : selectedMotivoReclamo) {
				MotivoReclamo com = motivoReclamoBL.getMotivoReclamo(Integer
						.valueOf(s));
				valores[c]=com.getTiempoResolucion();
				c++;
			}
			oferta.setPeriodoObjetoReclamo(valores[0]);
			for (int i : valores) {
				if(i<=oferta.getPeriodoObjetoReclamo()){
					oferta.setPeriodoObjetoReclamo(i);
				}
			}
			log.debug("[calcularPeriodoObjeto] se calculo correctamente");
		} catch (Exception e) {
			log.error("[calcularPeriodoObjeto] error al calcular",e);		
		}

	}
	
	public void handleChange(ToggleSelectEvent toggle){
		  if(toggle.isSelected()){
		   try {
		    
		    List<MotivoReclamo> lista=motivoReclamoBL.getMotivoReclamosList();
		    selectedMotivoReclamo=new String[lista.size()];
		    int valores[]= new int[selectedMotivoReclamo.length];
		    int i=0;
		    for(MotivoReclamo mt : lista ){
		     selectedMotivoReclamo[i]=mt.getMotivoReclamoId()+"";
		     valores[i]=mt.getTiempoResolucion();
		     i++;
		    }
		    oferta.setPeriodoObjetoReclamo(valores[0]);
			for (int o : valores) {
				if(o<=oferta.getPeriodoObjetoReclamo()){
					oferta.setPeriodoObjetoReclamo(o);
				}
			}
		    		   
		   } catch (Exception e) {
		    e.printStackTrace();
		   }
		  }else{
		   oferta.setPeriodoObjetoReclamo(0);
		   selectedMotivoReclamo=null;
		  }
		 }
	
	//revisa que actor puede revisar el odeco
	public Boolean revisar(int idformulario) throws Exception {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		this.strIdUs = (String) request.getSession().getAttribute(
				"TEMP$USER_NAME");
		boolean sw = false;
		FormularioOdeco form = ofertaN.getEstadoTrue(idformulario);
		Flujo flujo = flujoBL.getFlujoNombre("flujo");
				
			if(form.getRevisar().equalsIgnoreCase("-Regulacion-") && form.getEstado()){
				Usuario u = usuarioBL.getUserLogin(strIdUs);
				if(u.getRol().getNombre().equalsIgnoreCase(flujo.getRegulacion())){
					return true;
				}
				
			}
			if(form.getRevisar().equalsIgnoreCase("ENVIADO ATT") ){
				Usuario u = usuarioBL.getUserLogin(strIdUs);
				if(u.getRol().getNombre().equalsIgnoreCase(flujo.getCoordinador())){
					return true;
				}
				
			}
			if(form.getDerivado()!=null){
				if(!form.getDerivado().isEmpty()){
					Usuario u = usuarioBL.getUserLogin(strIdUs);
					if(u.getRol().getNombre().equalsIgnoreCase(flujo.getCoordinador())){
						return true;
					}
				}
				
				
			}
			/*if( form.getEstadoRespuesta().equalsIgnoreCase("CERRADO") && form.getFormularioOdeco()==null){
				Usuario u = usuarioBL.getUserLogin(strIdUs);
				if(u.getRol().getNombre().equalsIgnoreCase(flujo.getCoordinador())){
					return true;
				}
				
			}*/
			if (form.getRevisar() != null) {
				if (form.getRevisar().equalsIgnoreCase(this.strIdUs)
						&& form.getEnviadoMail() == false && form.getEstado()) {
							sw = true;
				}
			}
			
			
			
			return sw;
		
	

	}

	//revisa que actor puede reenviar el odeco si este fue fallido
	public Boolean revisarReenvio(int idformulario) throws Exception {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		this.strIdUs = (String) request.getSession().getAttribute(
				"TEMP$USER_NAME");
		// Usuario u = usuarioBL.getUserLogin(strIdUs);
		FormularioOdeco form = ofertaN.getEstadoTrue(idformulario);
		boolean sw = false;
		if (form.getRevisar() != null) {
			if (form.getRevisar().equalsIgnoreCase(this.strIdUs)) {
				if (form.getEnviadoMail()) {
					sw = true;
				}

			}
		}
		return sw;

	}

	 public StreamedContent getFile(String id) throws FileNotFoundException {
		 StreamedContent file;
		 String ruta="";String nombre="";
		 	FormularioOdeco f = ofertaN.getEstadoTrue(Integer.valueOf(id));
		 	List<Adjunto> a= adjuntoBL.getCiudadNombre(Integer.valueOf(id));
		 	for (Adjunto adjunto : a) {
		 		if(f.getTipoFormulario().getTipoFormularioId()==1){
		 			if(adjunto.getNombre().equalsIgnoreCase("FormularioOdecoPRIMERA INSTANCIA.pdf")){
						ruta=adjunto.getRuta();
						nombre=adjunto.getNombre();
					}
		 		}else{
		 			if(adjunto.getNombre().equalsIgnoreCase("FormularioOdecoSEGUNDA INSTANCIA.pdf")){
						ruta=adjunto.getRuta();
						nombre=adjunto.getNombre();
					}
		 		}
			}
	    	InputStream stream = new FileInputStream(ruta);
	        file = new DefaultStreamedContent(stream, "aplication/pdf", nombre);
	        return file;
	    }
	 
	 public StreamedContent getFileAdjunto(String id) throws FileNotFoundException {
		 StreamedContent file;
		
		 	Adjunto a= adjuntoBL.getListAdjuntoId(Integer.valueOf(id));
		 	 String ruta=a.getRuta();
		 	 String nombre=a.getNombre(); 	
	    	InputStream stream = new FileInputStream(ruta);
	        file = new DefaultStreamedContent(stream, "aplication/pdf", nombre);
	        return file;
	    }
	/*public void reenviar() {
		try {
			correos= new ArrayList<String>();
			String idStr = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap()
					.get("id_objeto");
			String coordinadorEspecial="";
			HttpServletRequest request = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			this.strIdUs = (String) request.getSession().getAttribute(
					"TEMP$USER_NAME");
			Usuario usuario = usuarioBL.getUserLogin(strIdUs);
			if(usuario==null){
				log.error("[reenviar] error al obtener el usuario: "+strIdUs);
				return;
			}
			listaAdjuntos = new ArrayList<Adjunto>();
			listaAdjuntos = adjuntoBL.getCiudadNombre(Integer.valueOf(idStr));
			FormularioOdeco form = ofertaN
					.getEstadoTrue(Integer.valueOf(idStr));
			if(form==null){
				log.error("[reenviar] error al obtener el ODECO: "+idStr);
				return;
			}
			
			ColaCoordinador coordinador=colaCoordinadorBL.getCoordinador();
			if(coordinador==null){
				log.error("[reenviar] error al obtener el coordinador correspondiente pa la notificacion ");
				return;
			}
			Flujo flujo = flujoBL.getFlujoNombre("flujo");
			if(flujo==null){
				log.error("[reenviar] error al obtener el flujo ");
				return;
			}
			if(flujo.getEstado()){
				correos.add(usuario.getUsuario().getCorreo());
				String s=flujo.getStaff();
				Rol r=rolBL.getname(s);
				if(r==null){
					log.error("[reenviar] error al obtener el rol: "+s);
					return;
				}
				
				if(usuario.getRol().getRolId().equals(r.getRolId())){				
					if(form.getDevuelto()){
						Usuario usuario2=usuarioBL.getUserLogin(form.getEnviado());
						correos= new ArrayList<String>();
						correos.add(usuario2.getCorreo());
					}else{
						correos= new ArrayList<String>();
						form.setRevisar(null);
						try {
							log.debug("[saveStaff] se van a obtener los motivos de reclamos");
							List<Object[]> motrec = ofertaN.getMotivoReclamacion(idStr);
							selectedMotivoReclamo = new String[motrec.size()];
							int i = 0;
							for (Object comm : motrec) {
								selectedMotivoReclamo[i] = comm.toString();
								i++;						
							}
							
							for (String ss : selectedMotivoReclamo) {
								MotivoReclamo com = motivoReclamoBL.getMotivoReclamo(Integer
										.valueOf(ss));
								motivosReclamos.add(com);
								if(com.getCoordinador()!=null){
									if(!com.getCoordinador().isEmpty()){
										form.setRevisar(com.getCoordinador());
										coordinadorEspecial=com.getCoordinador();
									}
								}
								log.debug("[saveStaff] se obtuvo el motivo de reclamo "+com.toString());
							}
							log.debug("[saveStaff] se obtuvieron los motivos de reclamos");
						} catch (Exception e) {
							e.printStackTrace();
							log.error("[saveStaff] error al obtener los motivos de reclamos",e);
							return ;
						}
						if(form.getRevisar()==null){
							correos.add(usuarioBL.getUserLogin(coordinador.getNombreCoordinador()).getCorreo());//correo de un coordinador
						}else{
							correos.add(usuarioBL.getUserLogin(form.getRevisar()).getCorreo());
						}
						
					}
					
					
				}
				//if(usuario.getRol().getRolId()==4){
				//	correos= new ArrayList<String>();
				//	correos.add("p.varper@gmail.com");//correo de regulacion
				//}
				if(usuario.getRol().getRolId().equals(rolBL.getname(flujo.getRegulacion()).getRolId())){
					correos= new ArrayList<String>();
					agregarCorreos(parametroBL.getCorreoATT());
					//correos.add(parametroBL.getCorreoATT());//correo de ATT
					
				}
				if(usuario.getRol().getRolId().equals(rolBL.getname(flujo.getAgente()).getRolId())){
					if(form.getTipoFormulario().getDescripcion().equalsIgnoreCase("SEGUNDA INSTANCIA")){
						correos= new ArrayList<String>();
						agregarCorreos(parametroBL.getCorreoRegulacion());
						//correos.add(parametroBL.getCorreoRegulacion());//correo de regulacion				
					}
				}
				
				Result result = mailBL.sendEmail(correos, listaAdjuntos, form);

				if (!result.getCode().equalsIgnoreCase(Code.OK)) {
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_WARN, "ERROR",
									"no se pudo reenviar la notificacion"));
					setListaOferta(ofertaN.listarTodo());
					return;
				}
				form.setEnviadoMail(false);
				form.setRevisar(usuario.getLogin());
				form.setEnviado(usuario.getLogin());//si es es staff debe revisar un coordinador si es agente debe ir su responsable
				if(usuario.getRol().getRolId().equals(rolBL.getname(flujo.getStaff()).getRolId())){
					if(!form.getDevuelto()){
						if(coordinadorEspecial.isEmpty()){
							form.setRevisar(coordinador.getNombreCoordinador());	
						}else{
							form.setRevisar(coordinadorEspecial);
						}
					}
					
					
				}
				if(usuario.getRol().getRolId().equals(rolBL.getname(flujo.getAgente()).getRolId())){				
					form.setRevisar(usuario.getUsuario().getLogin());//si es agente va revisar su responsable
					if(form.getTipoFormulario().getDescripcion().equalsIgnoreCase("SEGUNDA INSTANCIA")){
						form.setRevisar("-Regulacion-");//si es agente pero es de 2da instancia revisa regulacion
					}
				}
				//if(usuario.getRol().getRolId()==4){
					
				//	form.setRevisar("-Regulacion-");//
				//}
				if(usuario.getRol().getRolId().equals(rolBL.getname(flujo.getRegulacion()).getRolId())){
					
					form.setRevisar("-ATT-");//si es regulacion va revisar la att
					
				}
				ofertaN.update(form);
				setListaOferta(ofertaN.listarTodo());	
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								Parameter.MENSAJE_REENVIO));
			}else{
				//correos.add(usuario.getUsuario().getCorreo());
				String s=flujo.getStaff();
				Rol r=rolBL.getname(s);
				if(r==null){
					log.error("[reenviar] error al obtener el rol: "+s);
					return;
				}
				
				if(usuario.getRol().getRolId().equals(r.getRolId())&&form.getTipoFormulario().getDescripcion().equalsIgnoreCase("PRIMERA INSTANCIA")){
					if(form.getDevuelto()==null){
						correos= new ArrayList<String>();
						correos.add(usuario.getCorreo());
					}else{
						if(form.getDevuelto()){						
							correos= new ArrayList<String>();
							correos.add(usuario.getCorreo());
						}else{
							correos= new ArrayList<String>();
							form.setRevisar(null);
							try {
								log.debug("[reenviar] se van a obtener los motivos de reclamos");
								List<Object[]> motrec = ofertaN.getMotivoReclamacion(idStr);
								selectedMotivoReclamo = new String[motrec.size()];
								int i = 0;
								for (Object comm : motrec) {
									selectedMotivoReclamo[i] = comm.toString();
									i++;						
								}
								
								for (String ss : selectedMotivoReclamo) {
									MotivoReclamo com = motivoReclamoBL.getMotivoReclamo(Integer
											.valueOf(ss));
									motivosReclamos.add(com);
									if(com.getCoordinador()!=null){
										if(!com.getCoordinador().isEmpty()){
											form.setRevisar(com.getCoordinador());
											coordinadorEspecial=com.getCoordinador();
										}
									}
									log.debug("[reenviar] se obtuvo el motivo de reclamo "+com.toString());
								}
								log.debug("[reenviar] se obtuvieron los motivos de reclamos");
							} catch (Exception e) {
								e.printStackTrace();
								log.error("[reenviar] error al obtener los motivos de reclamos",e);
								return ;
							}
							if(form.getRevisar()==null){
								correos.add(usuarioBL.getUserLogin(coordinador.getNombreCoordinador()).getCorreo());//correo de un coordinador
							}else{
								correos.add(usuarioBL.getUserLogin(form.getRevisar()).getCorreo());
							}
							
						}
					}
					
					
					
				}
				//if(usuario.getRol().getRolId()==4){
				//	correos= new ArrayList<String>();
				//	correos.add("p.varper@gmail.com");//correo de regulacion
				//}
				if(usuario.getRol().getRolId().equals(rolBL.getname(flujo.getRegulacion()).getRolId())){
					correos= new ArrayList<String>();
					agregarCorreos(parametroBL.getCorreoATT());
					//correos.add();//correo de ATT
					
				}
				if(usuario.getRol().getRolId().equals(rolBL.getname(flujo.getCoordinador()).getRolId())){
					correos= new ArrayList<String>();
					//agregarCorreos(parametroBL.getCorreoATT());
					correos.add(usuario.getCorreo());//correo de ATT
					
				}
				if(usuario.getRol().getRolId().equals(rolBL.getname(flujo.getStaff()).getRolId())){
					if(form.getTipoFormulario().getDescripcion().equalsIgnoreCase("SEGUNDA INSTANCIA")){
						correos= new ArrayList<String>();
						agregarCorreos(parametroBL.getCorreoRegulacion());
						//correos.add(parametroBL.getCorreoRegulacion());//correo de regulacion				
					}
				}
				
				//Result result = mailBL.sendEmail(correos, listaAdjuntos, form);

				//if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				//	FacesContext.getCurrentInstance().addMessage(
				//			null,
				//			new FacesMessage(FacesMessage.SEVERITY_WARN, "ERROR",
				//					"no se pudo reenviar la notificacion"));
				//	setListaOferta(ofertaN.listarTodo());
			//		return;
			//	}
				form.setEnviadoMail(false);
				form.setRevisar(usuario.getLogin());
				form.setEnviado(usuario.getLogin());//si es es staff debe revisar un coordinador si es agente debe ir su responsable
				if(usuario.getRol().getRolId().equals(rolBL.getname(flujo.getStaff()).getRolId())){
					if(form.getDevuelto()!=null){
						if(!form.getDevuelto()){
							if(coordinadorEspecial.isEmpty()){
								form.setRevisar(coordinador.getNombreCoordinador());	
							}else{
								form.setRevisar(coordinadorEspecial);
							}
						}
					}
				
					
					
				}
				if(usuario.getRol().getRolId().equals(rolBL.getname(flujo.getStaff()).getRolId())){				
					form.setRevisar(usuario.getUsuario().getLogin());//si es agente va revisar su responsable
					if(form.getTipoFormulario().getDescripcion().equalsIgnoreCase("SEGUNDA INSTANCIA")){
						form.setRevisar("-Regulacion-");//si es agente pero es de 2da instancia revisa regulacion
					}
				}
				//if(usuario.getRol().getRolId()==4){
					
				//	form.setRevisar("-Regulacion-");//
				//}
				if(usuario.getRol().getRolId().equals(rolBL.getname(flujo.getRegulacion()).getRolId())){
					
					form.setRevisar("-ATT-");//si es regulacion va revisar la att
					
				}
				ofertaN.update(form);
				listaAdjuntos=new ArrayList<Adjunto>();
				listaInputStream = new ListaInputStream();
				setListaOferta(ofertaN.listarTodo());	
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								Parameter.MENSAJE_REENVIO));
			}
			
			
			

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							"error al reenviar"));
		}
		

	}*/

	public Boolean revisarSegundaInstancia(int idformulario) throws Exception {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		this.strIdUs = (String) request.getSession().getAttribute(
				"TEMP$USER_NAME");
		Usuario u = usuarioBL.getUserLogin(strIdUs);
		FormularioOdeco form = ofertaN.getEstadoTrue(idformulario);
		Flujo flujo = flujoBL.getFlujoNombre("flujo");
		
		boolean sw = false;
		
		if(flujo.getEstado()){
			if (form.getRespuestaOdeco() != null
					&& u.getRol().getNombre().equalsIgnoreCase(flujo.getAgente())
					&& form.getTipoFormulario().getDescripcion()
							.equalsIgnoreCase("PRIMERA INSTANCIA")
					&& form.getEstadoRespuesta().equalsIgnoreCase("CERRADO")
					&& form.getFormularioOdeco() == null && !form.getEnviadoMail()) {
				sw = true;

			}
		}
		if(!flujo.getEstado()){
			if (form.getRespuestaOdeco() != null
					&& u.getRol().getNombre().equalsIgnoreCase(flujo.getStaff())
					&& form.getTipoFormulario().getDescripcion()
							.equalsIgnoreCase("PRIMERA INSTANCIA")
					&& form.getEstadoRespuesta().equalsIgnoreCase("CERRADO")
					&& form.getFormularioOdeco() == null && !form.getEnviadoMail()) {
				sw = true;

			}
		}
		
		return sw;

	}

	public Boolean mostrarCodReclamacion() throws Exception {
		return true;

	}

	public Boolean mostrarCiudadLocalidad() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
		Devuelto d = devueltoBL.getDevueltoForm(oferta
				.getFormularioOdecoId());
			if (d == null) {
				return true;
		}
			if (d.getCiudadLocalidad()) {
				return false;
			} else {
				return true;
			}

		}
		return true;

	}

	public String mostrarCiudadLocalidadB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getCiudadLocalidad()) {
				return "color: red;font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";

	}

	public Boolean mostrarNombreReclamante() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return true;
			}
			if (d.getNombreReclamante()) {
				return false;
			} else {
				return true;
			}

		}
		return true;

	}

	public String mostrarNombreReclamanteB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getNombreReclamante()) {
				return "color: red;font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";

	}

	public Boolean mostrarNombreTitular() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return true;
			}
			if (d.getNombreTitular()) {
				return false;
			} else {
				return true;
			}

		}
		return true;

	}

	public String mostrarNombreTitularB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getNombreTitular()) {
				return "color: red;font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";

	}

	public Boolean mostrarTelefonoReferencia() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return true;
			}
			if (d.getTelefonoReferencia()) {
				return false;
			} else {
				return true;
			}

		}
		return true;

	}

	public String mostrarTelefonoReferenciaB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getTelefonoReferencia()) {
				return "color: red;font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";

	}

	public Boolean mostrarLineaReclamo() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return true;
			}
			if (d.getLineaReclamo()) {
				return false;
			} else {
				return true;
			}

		}
		return true;

	}

	public String mostrarLineaReclamoB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getLineaReclamo()) {
				return "color: red;font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";

	}

	public Boolean mostrarCorreo() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return true;
			}
			if (d.getCorreo()) {
				return false;
			} else {
				return true;
			}

		}
		return true;

	}

	public String mostrarCorreoB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getCorreo()) {
				return "color: red;font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";

	}

	public Boolean mostrarDireccionDomicilio() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return true;
			}
			if (d.getDireccionDomicilio()) {
				return false;
			} else {
				return true;
			}

		}
		return true;
	}

	public String mostrarDireccionDomicilioB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getDireccionDomicilio()) {
				return "color: red;font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";
	}

	public Boolean mostrarFax() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return true;
			}
			if (d.getFax()) {
				return false;
			} else {
				return true;
			}

		}
		return true;

	}

	public String mostrarFaxB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getFax()) {
				return "color: red;font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";
	}
	
	public Boolean mostrarFechaIncidencia() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return true;
			}
			if (d.getFechaIncidencia()) {
				return false;
			} else {
				return true;
			}

		}
		return true;

	}

	public String mostrarFechaIncidenciaB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getFechaIncidencia()) {
				return "color: red;font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";
	}
	
	public Boolean mostrarDireccionNotificacion() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return true;
			}
			if (d.getDireccionNotificacion()) {
				return false;
			} else {
				return true;
			}

		}
		return true;

	}

	public String mostrarDireccionNotificacionB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getDireccionNotificacion()) {
				return "color: red;font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";
	}

	public Boolean mostrarObjetoReclamo() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return true;
			}
			if (d.getObjetoReclamo()) {
				return false;
			} else {
				return true;
			}

		}
		return true;

	}

	public String mostrarObjetoReclamoB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getObjetoReclamo()) {
				return "color: red;font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";

	}

	public Boolean mostrarCiudadReclamacion() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return true;
			}
			if (d.getCiudadReclamacion()) {
				return false;
			} else {
				return true;
			}

		}
		return true;

	}

	public String mostrarCiudadReclamacionB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getCiudadReclamacion()) {
				return "font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";

	}

	public Boolean mostrarMedioNotificacion() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return true;
			}
			if (d.getMedioNotificacion()) {
				return false;
			} else {
				return true;
			}

		}
		return true;

	}
	
	public Boolean mostrarFormaRegistro() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return true;
			}
			if (d.getFormaRegistro()) {
				return false;
			} else {
				return true;
			}

		}
		return true;

	}

	public String mostrarMedioNotificacionB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getMedioNotificacion()) {
				return "color: red;font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";

	}
	
	public String mostrarFormaRegistroB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getFormaRegistro()) {
				return "color: red;font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";

	}

	public Boolean mostrarMotivoReclamo() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return true;
			}
			if (d.getMotivoReclamo()) {
				return false;
			} else {
				return true;
			}

		}
		return true;

	}

	public String mostrarMotivoReclamoB() throws Exception {

		if (oferta.getFormularioOdecoId() != null) {
			Devuelto d = devueltoBL.getDevueltoForm(oferta
					.getFormularioOdecoId());
			if (d == null) {
				return "background:white";
			}
			if (d.getMotivoReclamo()) {
				return "color: red;font-weight:bold;font-size:15px;";
			} else {
				return "background:white";
			}

		}
		return "background:white";

	}

	public boolean renderAnulado(){
		if(anulado){
			return false;
		}
		return true;
	}
	
	public String abrirDialogo(int idformulario) throws Exception {

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		this.strIdUs = (String) request.getSession().getAttribute(
				"TEMP$USER_NAME");
		Usuario usuario = usuarioBL.getUserLogin(strIdUs);
		Flujo flujo = flujoBL.getFlujoNombre("flujo");
		FormularioOdeco form = ofertaN.getEstadoTrue(idformulario);
		
		if(flujo.getEstado()){
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getAgente())){
				return "dlg7.show();";
			}
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getStaff())){
				return "dlg5.show();";
			}
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getCoordinador())){
				return "dlg3.show();";
			}
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getRegulacion())){
				return "dlg6.show();";
			}
		}
		if(!flujo.getEstado()){
			
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getStaff())){
				if(form.getDevuelto()!=null){
					if(form.getDevuelto()){
						setGuardar(true);
						return "dlg7.show();";
					}
				}
				
			}
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getStaff())){
				if(form.getDevuelto()==null){
					setGuardar(false);
					return "dlg5.show();";
				}
				if(!form.getDevuelto()){
					setGuardar(false);
					return "dlg5.show();";
				}
				
			}
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getCoordinador())){
				return "dlg3.show();";
			}
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getRegulacion())){
				return "dlg6.show();";
			}
		}
		
		
		
		return "dlg7.show();";
		
		
		
	}

	public String abrirDialogo2(int idformulario) throws Exception {

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		this.strIdUs = (String) request.getSession().getAttribute(
				"TEMP$USER_NAME");
		Usuario usuario = usuarioBL.getUserLogin(strIdUs);
		
		Flujo flujo = flujoBL.getFlujoNombre("flujo");
		FormularioOdeco form = ofertaN.getEstadoTrue(idformulario);
		if(flujo.getEstado()){
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getAgente())){
				setGuardar(true);
				return ":formPanel4";
			}
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getStaff())){
				setGuardar(false);
				return ":formPanel2";
			}
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getCoordinador())){
				setGuardar(false);
				return ":formPanel";
			}
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getRegulacion())){
				setGuardar(false);
				return ":formPanel3";
			}
		}
		if(!flujo.getEstado()){
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getStaff())){
				if(form.getDevuelto()!=null){
					if(form.getDevuelto()){
						setGuardar(true);
						return ":formPanel4";
					}
				}
				
			}
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getStaff())){
				if(form.getDevuelto()==null){
					setGuardar(false);
					return ":formPanel2";
				}
				if(!form.getDevuelto()){
					setGuardar(false);
					return ":formPanel2";
				}
				
			}
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getCoordinador())){
				setGuardar(false);
				return ":formPanel";
			}
			if(usuario.getRol().getNombre().equalsIgnoreCase(flujo.getRegulacion())){
				setGuardar(false);
				return ":formPanel3";
			}
		}
	
		
		return ":formPanel4";
	}

	public void derivar()  {
		//oferta.setEstadoRespuesta("CERRADO");
		
		/*if(opcionDerivar.equals(0)){
			try {
				RequestContext context = RequestContext.getCurrentInstance();
				if(getOferta().getDerivado().isEmpty()||getOferta().getDerivado()==null){
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_WARN, "ERROR",
									"Debe ingresar un derivado"));
					return;
				}
				//getOferta().setEstado(false);
				//getOferta().setEstadoRespuesta("DERIVADO");
				
				getOferta().setRevisar("TODOS LOS COORDINADORES");
				ofertaN.update(getOferta());
				controlerBitacora.update(
						DescriptorBitacora.DERIVACION_TODOS_COORDINADOR,
						oferta.getFormularioOdecoId() + "",
						oferta.getCodigoReclamacionInstitucion() + "/"
								+ oferta.getCodigoReclamacionCiudad() + "/"
								+ oferta.getCodigoReclamacionCorrelativo());
				context.execute("dlg3.hide()");
				
				context.execute("dlg10.hide()");
				context = RequestContext.getCurrentInstance();
				context.execute("dlg3.hide()");
			} catch (Exception e) {
				
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
								"ERROR AL GUARDAR"));
			}
		}else{*/
			try {
				RequestContext context = RequestContext.getCurrentInstance();
				if(selectCoordinador.equals("-1")){
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_WARN, "ERROR",
									"Debe Seleccionar un coordinador"));
					return;
				}
				//getOferta().setEstado(false);
				//getOferta().setEstadoRespuesta("DERIVADO");
				String enviado=oferta.getEnviado();
				String revisar=oferta.getRevisar();
				getOferta().setDerivado(null);
				Usuario us= usuarioBL.getUser(Integer.valueOf(selectCoordinador));
				getOferta().setRevisar(us.getLogin());
				ofertaN.update(getOferta());
				correos.add(us.getCorreo());
				listaAdjuntos = adjuntoBL.getCiudadNombre(getOferta()
						.getFormularioOdecoId());
				Result result = mailBL.sendEmail(correos, listaAdjuntos,
						getOferta());
				listaAdjuntos = new ArrayList<Adjunto>();
				oferta.setAdjuntos(listaAdjuntos);
				if (result.getCode().equalsIgnoreCase(Code.OK)) {
					oferta.setEnviadoMail(false);
					ofertaN.update(getOferta());
				} else {
					
					oferta.setEnviado(enviado);
					oferta.setRevisar(revisar);
					ofertaN.update(getOferta());
					setListaOferta(ofertaN.listarTodo());
					setEdit(false);
					FacesContext
							.getCurrentInstance()
							.addMessage(
									null,
									new FacesMessage(
											FacesMessage.SEVERITY_WARN,
											"ERROR",
											Parameter.MENSAJE_GUARDADO_ERROR_MAIL));
					context.execute("dlg10.hide()");
					return ;
				}
				listaAdjuntos = new ArrayList<Adjunto>();
				//listaInputStream = new ArrayList<InputStream>();
				listaInputStream= new ListaInputStream();
				
				correos = new ArrayList<String>();
				setListaOferta(ofertaN.listarTodo());
				controlerBitacora.update(
						DescriptorBitacora.DERIVACION_ESPECIFICO_COORDINADOR,
						oferta.getFormularioOdecoId() + "",
						oferta.getCodigoReclamacionInstitucion() + "/"
								+ oferta.getCodigoReclamacionCiudad() + "/"
								+ oferta.getCodigoReclamacionCorrelativo());
				context.execute("dlg3.hide()");
				
				context.execute("dlg10.hide()");
				context = RequestContext.getCurrentInstance();
				context.execute("dlg3.hide()");
			} catch (Exception e) {
				
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
								"ERROR AL GUARDAR"));
			}
		//}
	
	
	}
	
	public List<String> getObjetosReclamos(int id){
		
		List objt= ofertaN.getObjetosReclamosDescripcion(id);
		
		
		return objt;
	}
	public List<String> getMotivosReclamos(int id){
		
		List objt= ofertaN.getMotivosReclamosDescripcion(id);
		
		return objt;
	}
	
	public String getCiudadReclamacion(int id){
		try {
			if(id!=-1){
				Ciudad ciudad = ciudadBL.getCiudad(id);
				return ciudad.getNombre();
			}		
			return "";
		} catch (Exception e) {
			log.error("[getCiudadReclamacion] no se pudo obtener la ciudad con id: "+id,e);
			return "";
		}
		
	}
	public String getMedioNotificacion(int id){
		try {
			if(id!=-1){
			MedioNotificacion medio = medioNotificacionBL.getMedioNotificacionID(id);
			return medio.getDescripcion();
			}
			return "";
		} catch (Exception e) {
			log.error("[getMedioNotificacion] no se pudo obtener el medio de notificacion con id: "+id,e);
			return "";
		}
		
	}
	public String getRespuestaOdeco(int id){
		try {
			if(id!=-1){
			RespuestaOdeco medio = respuestaOdecoBL.getRespuestaOdecoID(id);
			return medio.getRespuestaOdeco();
			}
			return "";
		} catch (Exception e) {
			log.error("[getRespuestaOdeco] no se pudo obtener la respuesta odeco con id: "+id,e);
			return "";
		}
		
	}
	public boolean booleanEstado(String charEstado) {
		log.info("[booleanEstado] ingreso al metodo boolean estado.");
		if (charEstado.equals("A")) {
			return true;
		} else {
			return false;
		}
	}

	public String headerLabel() {
		System.out.println("ID ODECO:" + this.oferta.getFormularioOdecoId());
		if (this.oferta.getFormularioOdecoId() > 0)
			return "Modificando Datos...";
		else
			return "Creando Nuevo Registro...";
	}

	public FormularioOdeco getOferta() {
		return oferta;
	}

	public void setOferta(FormularioOdeco oferta) {
		this.oferta = oferta;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public List<FormularioOdeco> getListaOferta() {
		return listaOferta;
	}

	public void setListaOferta(List<FormularioOdeco> listaServicio) {
		this.listaOferta = listaServicio;
	}

	public String getIdOfertaStr() {
		return idOfertaStr;
	}

	public void setIdOfertaStr(String idOfertaStr) {
		this.idOfertaStr = idOfertaStr;
	}

	public boolean isOfertaActiva() {
		return ofertaActiva;
	}

	public void setOfertaActiva(boolean ofertaActiva) {
		this.ofertaActiva = ofertaActiva;
	}

	public FormularioOdeco getSelectOdeco() {
		return selectOdeco;
	}

	public void setSelectOdeco(FormularioOdeco selectOdeco) {
		this.selectOdeco = selectOdeco;
	}

	public List<SelectItem> getSelectCiudades() {
		return selectCiudades;
	}

	public void setSelectCiudades(List<SelectItem> selectCiudades) {
		this.selectCiudades = selectCiudades;
	}

	public String getSelectCiudad() {
		return selectCiudad;
	}

	public void setSelectCiudad(String selectCiudad) {
		this.selectCiudad = selectCiudad;
	}

	public List<SelectItem> getSelectObjetoReclamos() {
		return selectObjetoReclamos;
	}

	public void setSelectObjetoReclamos(List<SelectItem> selectObjetoReclamos) {
		this.selectObjetoReclamos = selectObjetoReclamos;
	}

	public String getSelectObjetoReclamo() {
		return selectObjetoReclamo;
	}

	public void setSelectObjetoReclamo(String selectObjetoReclamo) {
		this.selectObjetoReclamo = selectObjetoReclamo;
	}

	public List<SelectItem> getSelectMedioNotificaciones() {
		return selectMedioNotificaciones;
	}

	public void setSelectMedioNotificaciones(
			List<SelectItem> selectMedioNotificaciones) {
		this.selectMedioNotificaciones = selectMedioNotificaciones;
	}

	public String getSelectMedioNotificacion() {
		return selectMedioNotificacion;
	}

	public void setSelectMedioNotificacion(String selectMedioNotificacion) {
		this.selectMedioNotificacion = selectMedioNotificacion;
	}

	public List<SelectItem> getSelectMotivoReclamos() {
		return selectMotivoReclamos;
	}

	public void setSelectMotivoReclamos(List<SelectItem> selectMotivoReclamos) {
		this.selectMotivoReclamos = selectMotivoReclamos;
	}

	public String getSelectMotivoReclamo() {
		return selectMotivoReclamo;
	}

	public void setSelectMotivoReclamo(String selectMotivoReclamo) {
		this.selectMotivoReclamo = selectMotivoReclamo;
	}

	public String[] getSelectedMotivoReclamo() {
		return selectedMotivoReclamo;
	}

	public void setSelectedMotivoReclamo(String[] selectedMotivoReclamo) {
		this.selectedMotivoReclamo = selectedMotivoReclamo;
	}

	public String[] getSelectedObjetoReclamo() {
		return selectedObjetoReclamo;
	}

	public void setSelectedObjetoReclamo(String[] selectedObjetoReclamo) {
		this.selectedObjetoReclamo = selectedObjetoReclamo;
	}

	public String getCodigoReclamacion() {
		return codigoReclamacion;
	}

	public void setCodigoReclamacion(String codigoReclamacion) {
		this.codigoReclamacion = codigoReclamacion;
	}

	public List<SelectItem> getSelectCiudadesReclamacion() {
		return selectCiudadesReclamacion;
	}

	public void setSelectCiudadesReclamacion(
			List<SelectItem> selectCiudadesReclamacion) {
		this.selectCiudadesReclamacion = selectCiudadesReclamacion;
	}

	public String getSelectCiudadReclamacion() {
		return selectCiudadReclamacion;
	}

	public void setSelectCiudadReclamacion(String selectCiudadReclamacion) {
		this.selectCiudadReclamacion = selectCiudadReclamacion;
	}

	public List<SelectItem> getSelectEstadoRespuestas() {
		return selectEstadoRespuestas;
	}

	public void setSelectEstadoRespuestas(
			List<SelectItem> selectEstadoRespuestas) {
		this.selectEstadoRespuestas = selectEstadoRespuestas;
	}

	public String getSelectEstadoRespuesta() {
		return selectEstadoRespuesta;
	}

	public void setSelectEstadoRespuesta(String selectEstadoRespuesta) {
		this.selectEstadoRespuesta = selectEstadoRespuesta;
	}

	public String getStrIdUs() {
		return strIdUs;
	}

	public void setStrIdUs(String strIdUs) {
		this.strIdUs = strIdUs;
	}

	public boolean isCiudadLocalidad() {
		return ciudadLocalidad;
	}

	public void setCiudadLocalidad(boolean ciudadLocalidad) {
		this.ciudadLocalidad = ciudadLocalidad;
	}

	public boolean isNombreReclamante() {
		return nombreReclamante;
	}

	public void setNombreReclamante(boolean nombreReclamante) {
		this.nombreReclamante = nombreReclamante;
	}

	public boolean isNombreTitutar() {
		return nombreTitutar;
	}

	public void setNombreTitutar(boolean nombreTitutar) {
		this.nombreTitutar = nombreTitutar;
	}

	public boolean isTelefonoReferencia() {
		return telefonoReferencia;
	}

	public void setTelefonoReferencia(boolean telefonoReferencia) {
		this.telefonoReferencia = telefonoReferencia;
	}

	public boolean isLineaReclamo() {
		return lineaReclamo;
	}

	public void setLineaReclamo(boolean lineaReclamo) {
		this.lineaReclamo = lineaReclamo;
	}

	public boolean isCorreo() {
		return correo;
	}

	public void setCorreo(boolean correo) {
		this.correo = correo;
	}

	public boolean isDireccionDomicilio() {
		return direccionDomicilio;
	}

	public void setDireccionDomicilio(boolean direccionDomicilio) {
		this.direccionDomicilio = direccionDomicilio;
	}

	public boolean isDireccionNotificacion() {
		return direccionNotificacion;
	}

	public void setDireccionNotificacion(boolean direccionNotificacion) {
		this.direccionNotificacion = direccionNotificacion;
	}

	public boolean isObjetoReclamo() {
		return objetoReclamo;
	}

	public void setObjetoReclamo(boolean objetoReclamo) {
		this.objetoReclamo = objetoReclamo;
	}

	public boolean isCiudadReclamacion() {
		return ciudadReclamacion;
	}

	public void setCiudadReclamacion(boolean ciudadReclamacion) {
		this.ciudadReclamacion = ciudadReclamacion;
	}

	public boolean isMotivoReclamo() {
		return motivoReclamo;
	}

	public void setMotivoReclamo(boolean motivoReclamo) {
		this.motivoReclamo = motivoReclamo;
	}

	public boolean isCodRecla() {
		return codRecla;
	}

	public void setCodRecla(boolean codRecla) {
		this.codRecla = codRecla;
	}

	public boolean isPeriodoObjeto() {
		return periodoObjeto;
	}

	public void setPeriodoObjeto(boolean periodoObjeto) {
		this.periodoObjeto = periodoObjeto;
	}

	public void dateSelect(SelectEvent event) {
	}

	public boolean isMedioNotificacion() {
		return medioNotificacion;
	}

	public void setMedioNotificacion(boolean medioNotificacion) {
		this.medioNotificacion = medioNotificacion;
	}

	public List<Adjunto> getListaAdjuntos() {
		return listaAdjuntos;
	}

	public void setListaAdjuntos(List<Adjunto> listaAdjuntos) {
		this.listaAdjuntos = listaAdjuntos;
	}

	public List<Adjunto> getListaAjuntosVista() {
		return listaAjuntosVista;
	}

	public void setListaAjuntosVista(List<Adjunto> listaAjuntosVista) {
		this.listaAjuntosVista = listaAjuntosVista;
	}

	public Date getDateToday() {
		return new Date();
	}

	public boolean isGuardar() {
		return guardar;
	}

	public void setGuardar(boolean guardar) {
		this.guardar = guardar;
	}



	public String[] getSelectedAdjuntos() {
		return selectedAdjuntos;
	}



	public void setSelectedAdjuntos(String[] selectedAdjuntos) {
		this.selectedAdjuntos = selectedAdjuntos;
	}



	public boolean isFechaResolucion() {
		return fechaResolucion;
	}



	public void setFechaResolucion(boolean fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}



	public boolean isFechaIncidencia() {
		return fechaIncidencia;
	}



	public void setFechaIncidencia(boolean fechaIncidencia) {
		this.fechaIncidencia = fechaIncidencia;
	}



	public boolean isDetalleMotivo() {
		return detalleMotivo;
	}



	public void setDetalleMotivo(boolean detalleMotivo) {
		this.detalleMotivo = detalleMotivo;
	}



	public boolean isFechaReclamo() {
		return fechaReclamo;
	}



	public void setFechaReclamo(boolean fechaReclamo) {
		this.fechaReclamo = fechaReclamo;
	}



	public boolean isFax() {
		return fax;
	}



	public void setFax(boolean fax) {
		this.fax = fax;
	}



	public Date getFechaIn() {
		return fechaIn;
	}



	public void setFechaIn(Date fechaIn) {
		this.fechaIn = fechaIn;
	}



	public boolean isAnulado() {
		return anulado;
	}



	public void setAnulado(boolean anulado) {
		this.anulado = anulado;
	}



	public boolean isAnuladoT() {
		return anuladoT;
	}



	public void setAnuladoT(boolean anuladoT) {
		this.anuladoT = anuladoT;
	}



	public File getDirectorio() {
		return directorio;
	}



	public void setDirectorio(File directorio) {
		this.directorio = directorio;
	}



	public boolean isFormaRegistro() {
		return formaRegistro;
	}



	public void setFormaRegistro(boolean formaRegistro) {
		this.formaRegistro = formaRegistro;
	}



	public String getFormRegistro() {
		return formRegistro;
	}

	


	public Integer getOpcionDerivar() {
		return opcionDerivar;
	}



	public void setOpcionDerivar(Integer opcionDerivar) {
		this.opcionDerivar = opcionDerivar;
	}



	public void setFormRegistro(String formRegistro) {
		this.formRegistro = formRegistro;
	}



	public List<String> getListaFormaRegistro() {
		return listaFormaRegistro;
	}



	public void setListaFormaRegistro(List<String> listaFormaRegistro) {
		this.listaFormaRegistro = listaFormaRegistro;
	}



	public List<SelectItem> getSelectCoordinadores() {
		return selectCoordinadores;
	}



	public void setSelectCoordinadores(List<SelectItem> selectCoordinadores) {
		this.selectCoordinadores = selectCoordinadores;
	}



	public String getSelectCoordinador() {
		return selectCoordinador;
	}



	public void setSelectCoordinador(String selectCoordinador) {
		this.selectCoordinador = selectCoordinador;
	}

	public String getExpresionRegular(){
		return Parameter.expresionRegular;
	}
	
	public String getExpresionRegularNumero(){
		
		return Parameter.expresionRegularNumero;
	}
	
	
	/**********/

}
