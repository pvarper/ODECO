package micrium.odeco.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.channels.FileChannel;
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
import micrium.odeco.business.ConfiguracionCalendarioBL;
import micrium.odeco.business.FormularioOdecoBL;
import micrium.odeco.business.MailBL;
import micrium.odeco.business.MedioNotificacionBL;
import micrium.odeco.business.MotivoReclamoBL;
import micrium.odeco.business.ObjetoReclamoBL;
import micrium.odeco.business.RespuestaOdecoBL;
import micrium.odeco.business.TipoFormularioBL;
import micrium.odeco.model.Adjunto;
import micrium.odeco.model.Calendario;
import micrium.odeco.model.Ciudad;
import micrium.odeco.model.FormularioOdeco;
import micrium.odeco.model.ListaInputStream;
import micrium.odeco.model.MedioNotificacion;
import micrium.odeco.model.MotivoReclamo;
import micrium.odeco.model.ObjetoReclamo;
import micrium.odeco.model.RespuestaOdeco;
import micrium.odeco.model.TipoFormulario;
import micrium.user.business.UsuarioBL;
import micrium.user.controler.ControlParametro;
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
public class FormularioOdecoBeanS implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(FormularioOdecoBeanS.class);

	@Inject
	private FormularioOdecoBL ofertaN;
	
	@Inject
	private ControlParametro controlParametro;

	
	
	@Inject
	private CiudadBL ciudadBL;
	@Inject
	private TipoFormularioBL tipoFormularioBL;

	@Inject
	private UsuarioBL usuarioBL;

	@Inject
	private AdjuntoBL adjuntoBL;

	@Inject
	private ObjetoReclamoBL objetoReclamoBL;

	@Inject
	private MedioNotificacionBL medioNotificacionBL;
	@Inject
	private RespuestaOdecoBL respuestaOdecodBL;

	@Inject
	private MotivoReclamoBL motivoReclamoBL;
	
	@Inject
	private ConfiguracionCalendarioBL configuracionBL;

	@Inject
	private ControlerBitacora controlerBitacora;

	@Inject
	private PrintUtil printUtils;
	
	@Inject
	private MailBL mailBL;

	private String[] selectedMotivoReclamo;

	private String[] selectedObjetoReclamo;
	
	private String[] auxSelectMotivo;
	
	private String[] auxSelectObjeto;
	
	private String[] selectedAdjuntos;

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

	private List<SelectItem> selectMotivoReclamos;
	private String selectMotivoReclamo;

	private List<SelectItem> selectRespuestaOdecos;
	private String selectRespuestaOdeco;
	
	private List<String> listaFormaRegistro = new ArrayList<String>();
	private String formaRegistro;
	// private long id_acreditacion;

	private FormularioOdeco selectOdeco;

	private List<String> correos = new ArrayList<String>();

	private List<Adjunto> listaAdjuntos = new ArrayList<Adjunto>();
	private List<Adjunto> listaAjuntosVista = new ArrayList<Adjunto>();

	//private transient List<InputStream> listaInputStream = new ArrayList<InputStream>();
	
	private transient ListaInputStream listaInputStream;
	
	private List<ObjetoReclamo> objetosReclamos = new ArrayList<ObjetoReclamo>();
	private List<MotivoReclamo> motivosReclamos = new ArrayList<MotivoReclamo>();
	private File directorio;	
	private Date fechaIn;
	private boolean guardar;

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
		log.debug("[init] se var cargar la tabla");
		setListaOferta(ofertaN.listarTodo());// Al iniciar necesitamos llenar la
												// tabla
	}

	public String generarCodigoReclamacion() {
		try {
			log.debug("[generarCodigoReclamacion] generando codigo de reclamacion");
			HttpServletRequest request = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			String strIdUs = (String) request.getSession().getAttribute(
					"TEMP$USER_NAME");
			Usuario usuario = usuarioBL.getUserLogin(strIdUs);
			Ciudad c = ciudadBL.getCiudad(Integer
					.valueOf(selectCiudadReclamacion));

			long correlativo = ofertaN.getMaxId("TELECEL", c.getAbreviatura());
			correlativo++;
			codigoReclamacion = "TELECEL/" + c.getAbreviatura() + "/"
					+ correlativo;
			oferta.setCodigoReclamacion(codigoReclamacion);
			oferta.setCodigoReclamacionInstitucion("TELECEL");
			oferta.setCodigoReclamacionCiudad(c.getAbreviatura());
			oferta.setCodigoReclamacionCorrelativo((int) (long) correlativo);
			oferta.setUsuario(usuario);
			log.debug("[generarCodigoReclamacion] se genero correctamente el codigo de reclamacion "+codigoReclamacion);
			return codigoReclamacion;
		} catch (Exception e) {

			log.error("[generarCodigoReclamacion] Hubo un error a la hora de generar el codigo de Reclamacion"
					+ e.getMessage());
			return "";
		}

	}

	public void newObject() {
		log.debug("inicializando Nuevo ODECO...");
		this.oferta = new FormularioOdeco();
		this.codigoReclamacion = "";
		this.edit = false;
		this.oferta.setFechaReclamo(new Timestamp(new Date().getTime()));	
		setFechaIn(new Date());
		this.oferta.setPeriodoObjetoReclamo(0);
		this.guardar=true;
		this.oferta.setEnviadoMail(false);
		selectedMotivoReclamo = null;
		selectedObjetoReclamo = null;
		selectedAdjuntos=null;

		selectCiudad = "-1";
		selectCiudadReclamacion = "-1";
		selectObjetoReclamo = "-1";
		selectMotivoReclamo = "-1";
		selectMedioNotificacion = "-1";
		selectRespuestaOdeco = "-1";
		listaAdjuntos = new ArrayList<Adjunto>();
		listaInputStream = new ListaInputStream();
		correos = new ArrayList<String>();
		formaRegistro="";

		log.debug("ODECO Inicializado [ok]");
	}

	private void fillListasSeleccionables() {
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
							micrium.util.Parameter.MENSAJE_LISTACIUDADES_ERROR));
		}

		selectObjetoReclamos = new ArrayList<SelectItem>();

		List<ObjetoReclamo> list2 = null;
		try {
			list2 = objetoReclamoBL.getObjetosReclamosList();
			for (ObjetoReclamo item : list2) {
				SelectItem s = new SelectItem(item.getObjetoReclamoId(),
						item.getObjetoReclamo());
				;
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
									micrium.util.Parameter.MENSAJE_LISTAOBJETOS_ERROR));
		}

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
									micrium.util.Parameter.MENSAJE_LISTAMEDIOS_ERROR));
		}
		selectRespuestaOdecos = new ArrayList<SelectItem>();
		selectRespuestaOdecos.add(new SelectItem("-1", "Respuestas Odeco"));
		List<RespuestaOdeco> list5 = null;
		try {
			list5 = respuestaOdecodBL.getLista();
			for (RespuestaOdeco item : list5) {
				SelectItem s = new SelectItem(item.getRespuestaOdecoId(),
						item.getRespuestaOdeco());
				selectRespuestaOdecos.add(s);
			}
			log.debug("Carga de Lista de Respuestas de Odecos [ok]");
		} catch (Exception e) {
			log.error("Carga de Lista de Respuestas de Odecos [fail]", e);
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Mensaje de error",
									micrium.util.Parameter.MENSAJE_RESPUESTAS_ERROR));
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
									micrium.util.Parameter.MENSAJE_MOTIVOS_ERROR));
		}
		
		log.debug("se cargar las formas de registro");
		String formas=controlParametro.getFormaRegistro();
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
	
	public void reenviar() {
		try{
			log.info("[reenviar] se va reenviar la notificacion del odeco");
			RequestContext context = RequestContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			this.strIdUs = (String) request.getSession().getAttribute(
					"TEMP$USER_NAME");
			Usuario usuario = usuarioBL.getUserLogin(strIdUs);	
			listaAdjuntos= new ArrayList<Adjunto>();
			listaAdjuntos = adjuntoBL.getCiudadNombre(getOferta()
					.getFormularioOdecoId());
			if(listaAdjuntos==null){
				log.error("[save] no se pudo obtener los adjuntos del odeco "+getOferta().toString());	
				return ;
			}
			correos= new ArrayList<String>();
			correos.add(usuario.getUsuario().getCorreo());
			if(oferta.getTipoFormulario().getDescripcion().equalsIgnoreCase("SEGUNDA INSTANCIA")){
				correos= new ArrayList<String>();
				correos.add(controlParametro.getCorreoRegulacion());//correo de regulacion				
			}
			
			Result result = mailBL.sendEmail(correos, listaAdjuntos,
					getOferta());
			
			if(!result.getCode().equalsIgnoreCase(Code.OK)){
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "ERROR",
								micrium.util.Parameter.MENSAJE_REENVIO_ERRORNOTIFICACIONMAIL));
				return;
			}
			log.info("[reenviar] se reenvio la notificacion del odeco");
			oferta.setEnviadoMail(false);
			oferta.setRevisar(usuario.getUsuario().getLogin());
			if(oferta.getTipoFormulario().getDescripcion().equalsIgnoreCase("SEGUNDA INSTANCIA")){
				oferta.setRevisar("-Regulacion-");
			}
			ofertaN.update(getOferta());
			
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							micrium.util.Parameter.MENSAJE_REENVIO));			
			context.execute("dlg3.hide()");
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							micrium.util.Parameter.MENSAJE_REENVIO_ERROR));
			log.error("[save] se actualizo el formulario campos: enviadoMail y Revisar ");
		}
		
		
	}

	public String saveSegundaInstancia() {
		
		try {
			// System.out.println("********************************************************************");
			log.debug("[saveSegundaInstancia] Asignando datos al ODECO de segunda instancia con la ide "
					+ this.getIdOfertaStr());
			//log.info("[saveSegundaInstancia] se va registrar un odeco de segunda instancia");
			RequestContext context = RequestContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			this.strIdUs = (String) request.getSession().getAttribute(
					"TEMP$USER_NAME");
			Usuario usuario = usuarioBL.getUserLogin(strIdUs);
			if(usuario==null){
				log.error("[saveSegundaInstancia] no se pudo obtener el usuario");
				return "";
			}
			log.debug("[saveSegundaInstancia] usuario obtenido correctamente "+usuario.toString());
			
			TipoFormulario tipoformulario = tipoFormularioBL
					.getTipoFormularioDescripcion("SEGUNDA INSTANCIA");
			if(tipoformulario==null){
				log.error("[saveSegundaInstancia] no se pudo obtener el tipo de formulario");
				return "";
			}
			log.debug("[saveSegundaInstancia] se obtuvo el tipo de formulario correctamente "+tipoformulario.toString());
			
			oferta.setTipoFormulario(tipoformulario);
			oferta.setEnviado(this.strIdUs);
			oferta.setRevisar("-Regulacion-");
			oferta.setUsuario(usuario);
			oferta.setRespuestaOdeco(null);
			oferta.setDevuelto(false);
			oferta.setEstadoRespuesta("ABIERTO");

			objetosReclamos = new ArrayList<ObjetoReclamo>();
			motivosReclamos = new ArrayList<MotivoReclamo>();
			selectedObjetoReclamo=auxSelectObjeto;
				log.debug("[saveSegundaInstancia] se va obtener los objetos reclamos correctamente");
				for (String s : selectedObjetoReclamo) {
					ObjetoReclamo com = objetoReclamoBL.getObjetoReclamo(Integer
							.valueOf(s));
					if(com==null){
						log.error("[saveSegundaInstancia] no se cargaron los objetos de reclamos seleccionados ");
						return "";
					}
					objetosReclamos.add(com);
					log.debug("[saveSegundaInstancia] se cargo el objeto reclamo "+com.toString());		
				}
				log.debug("[saveSegundaInstancia] se obtuvieron los objetos reclamos correctamente");
			
				selectedMotivoReclamo=auxSelectMotivo;
				for (String s : selectedMotivoReclamo) {
					MotivoReclamo com = motivoReclamoBL.getMotivoReclamo(Integer
							.valueOf(s));
					if(com==null){
						log.error("[saveSegundaInstancia] no se cargaron los motivos de reclamos seleccionados ");
						return "";
					}
					motivosReclamos.add(com);
					log.debug("[saveSegundaInstancia] se cargo el motivo reclamo "+com.toString());
				}
				log.debug("[saveSegundaInstancia] se obtuvieron los motivos reclamos correctamente");
		
			

			// System.out.println("****************************** despues del for 1 **************************************");

			System.out
					.println("****************************** SAVE **************************************");

			log.info("[saveSegundaInstancia] Guardando un nuevo Odeco SEGUNDA INSTANCIA");
			oferta.setFormularioOdecoId(null);
			oferta.setMotivoReclamos(motivosReclamos);
			oferta.setObjetoReclamos(objetosReclamos);
			//oferta.setAdjuntos(listaAdjuntos);
			oferta.setFechaReclamo(new Timestamp(new Date().getTime()));
			FormularioOdeco primeraInstancia = ofertaN.getOdecoPI(oferta
					.getCodigoReclamacion());
			if(primeraInstancia==null){
				log.debug("[saveSegundaInstancia] no se pudo obtener el ODECO de primera instancia con codigo de reclamacion: "+oferta.getCodigoReclamacion());
				return "";
			}
			
			oferta.setCodigoReclamacion(codigoReclamacion);
			// oferta.setFormularioOdeco(primeraInstancia);

			oferta.setEstado(true);

			System.out
					.println("**************************** iniciando Transaccion ****************************************");
				
				List<Adjunto> aux=listaAdjuntos;
				listaAdjuntos = adjuntoBL.getCiudadNombre(primeraInstancia.getFormularioOdecoId());
				if(listaAdjuntos==null){
					log.error("[save] no se pudo obtener los adjuntos del odeco"+getOferta());				
					return "";
				}
				for (Adjunto adjunto : aux) {
					listaAdjuntos.add(adjunto);
				}
				SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
				for (Adjunto a : listaAdjuntos) {
					a.setAdjuntoId(null);
					a.setFormularioOdeco(getOferta());
					a.setRuta(controlParametro.getRutaAdjunto()+getOferta().getCodigoReclamacionInstitucion()+getOferta().getCodigoReclamacionCiudad()+"RA"+getOferta().getCodigoReclamacionCorrelativo()+"-"+formateador.format(new Date())+File.separator+a.getNombre());
				}
				getOferta().setAdjuntos(listaAdjuntos);
				listaAdjuntos = adjuntoBL.getCiudadNombre(primeraInstancia.getFormularioOdecoId());
				if(listaAdjuntos==null){
					log.error("[save] no se pudo obtener los adjuntos del odeco"+getOferta());				
					return "";
				}
				ofertaN.save(getOferta());

				log.info("[saveSegundaInstancia] se guardo correctamente odeco segunda instancia "+getOferta().toString());
				primeraInstancia.setFormularioOdeco(getOferta());
				primeraInstancia.setEstado(false);
				ofertaN.update(primeraInstancia);

				setListaOferta(ofertaN.listarTodo());

				for (Adjunto a : listaAdjuntos) {
					File b = new File(a.getRuta());
					if(!copyFileSegundaInstancia(b)){
						log.error("[saveSegundaInstancia] no se pudieron copiar de segunda instancia los archivos al servidor del odeco "+getOferta().toString());
						FacesContext.getCurrentInstance().addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Mensaje de error", "No se pudieron copiar de segunda instancia los archivos al servidor"));
						return "";
					}
				}
				if(copyFileRA(listaInputStream.get(), aux)){
					log.info("[saveSegundaInstancia]Se copiaron los adjuntos de segunda instancia");
				}else{
					log.error("[saveSegundaInstancia]No se copiaron los adjuntos de segunda instancia");
				}
				listaAdjuntos = new ArrayList<Adjunto>();

				//correos.add(controlParametro.getCorreoRegulacion());// aqui poner correo de regulacion
				agregarCorreos(controlParametro.getCorreoRegulacion());
				if (exportarSegundaInstancia(getOferta(), primeraInstancia)) {
					log.debug("se exporto satisfactoriamente el formulario con codigo: "
							+ getOferta().getCodigoReclamacion());
					oferta.setAdjuntos(listaAdjuntos);
					ofertaN.update(getOferta());

				} else {
					log.debug("No se exporto satisfactoriamente el formulario con codigo: "
							+ getOferta().getCodigoReclamacion());
					FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_WARN,
									"ERROR",
									"No se Exporto ODECO de segunda Instancia"));
					return "";
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
					oferta.setRevisar(usuario.getLogin());
					//oferta.setEnviado(enviado);
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
											micrium.util.Parameter.MENSAJE_GUARDADO_ERROR_MAIL));
					context.execute("dlg3.hide()");
					return "";*/
					oferta.setEnviadoMail(false);
					ofertaN.update(getOferta());
					listaAdjuntos = new ArrayList<Adjunto>();
					this.correos = new ArrayList<String>();
					log.debug(result.getDescription());
					controlerBitacora.insert(
							DescriptorBitacora.ODECO_SEGUNDA_INSTANCIA,
							oferta.getFormularioOdecoId() + "",
							oferta.getCodigoReclamacion());
					setEdit(false);
					setListaOferta(ofertaN.listarTodo());
					FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_WARN,
									"WARINING",
									micrium.util.Parameter.MENSAJE_GUARDADO_ERROR_MAIL));
					context.execute("dlg3.hide()");
					return "";
				}
				listaAdjuntos = new ArrayList<Adjunto>();
				this.correos = new ArrayList<String>();
				log.debug(result.getDescription());
				controlerBitacora.insert(
						DescriptorBitacora.ODECO_SEGUNDA_INSTANCIA,
						oferta.getFormularioOdecoId() + "",
						oferta.getCodigoReclamacion());
				setEdit(false);
				

				setListaOferta(ofertaN.listarTodo());
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"NOTIFICACION", micrium.util.Parameter.MENSAJE_GUARDADO));
				context.execute("dlg3.hide()");
		
			// newObject();
			return "";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							micrium.util.Parameter.MENSAJE_GUARDADO_ERROR));
			log.error("[saveSegundaInstancia] error al guardar ODECO "+getOferta().toString(),e);
			return "";
		}
		

	}

	public void agregarCorreos(String c){

	            String delims = "[;]";
	            String[] tokens = c.split(delims);
	            for (int i = 0; i < tokens.length; i++) {
	            	correos.add(tokens[i]);
	            }
	}
	
	public void calcularPeriodoObjeto(){
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
			log.error("[calcularPeriodoObjeto] error al calcular");
			
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

	
	public Boolean exportarSegundaInstancia(FormularioOdeco f,
			FormularioOdeco pi) {
		boolean result = Boolean.FALSE;
		log.debug("[exportarSegundaInstancia] se va exportar odeco segunda instancia");
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		this.strIdUs = (String) request.getSession().getAttribute(
				"TEMP$USER_NAME");
		Usuario usuario = usuarioBL.getUserLogin(strIdUs);
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		String filenameReport = "SegundaInstanciaReport";
		String pathDetalle = controlParametro.getRutaAdjunto()
				+ oferta.getCodigoReclamacionInstitucion()
				+ oferta.getCodigoReclamacionCiudad()
				+ "RA"
				+ oferta.getCodigoReclamacionCorrelativo()+"-"+formateador.format(oferta.getFechaReclamo())
				+ File.separator+"FormularioOdeco"
				+ f.getTipoFormulario().getDescripcion()
				+ ".pdf";
		String titleReport = f.getTipoFormulario().getDescripcion();
		Map<String, Object> rptParameters = new HashMap<String, Object>();
		 rptParameters.put("Logo", controlParametro.getRutaLogoOdeco());
		rptParameters.put("Titulo", titleReport);
		rptParameters.put("Ciudad_localidad", f.getCiudadLocalidad());
		rptParameters.put("nombre_reclamante", f.getNombreReclamante());
		rptParameters.put("Nombre_Titular", f.getNombreTitular());
		rptParameters.put("Telefono_Referencia", f.getTelefonoReferencia());
		if (f.getCorreo() != null) {
			rptParameters.put("correo_cliente", f.getCorreo());
		}

		rptParameters.put("fax", f.getFax());
		rptParameters.put("Linea_reclamo", f.getLineaReclamo());
		rptParameters.put("direccion_reclamo", f.getDireccionDomicilio());
		rptParameters.put("direccion_notificacion",
				f.getDireccionNotificacion());
		rptParameters.put("codigo_reclamacion", pi.getCodigoReclamacion());
		rptParameters.put("fecha_resolucion", formateador.format(f.getFechaResolucion()));
		rptParameters.put("fecha_incidente", formateador.format(pi.getFechaIncidencia()));
		rptParameters.put("fecha_reclamacion_directa",formateador.format(pi.getFechaReclamo()));
		rptParameters.put("fecha_notificacion", "");
		rptParameters.put("fecha_reclamacion_administrativa", formateador.format(f.getFechaReclamo()));
		rptParameters.put("operador_proveedor","Telecel SA");
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
		rptParameters.put("detalle_motivo_reclamacion", f.getDetalleMotivoReclamacion());
		if (f.getRespuestaOdeco() != null) {
			rptParameters.put("estado_respuesta", f.getRespuestaOdeco()
					.getRespuestaOdeco());
		}

		rptParameters.put("detalle_respuesta", f.getDetalleRespuesta());
		if (f.getMotivoReclamacionAdministrativa() != null) {
			rptParameters.put("detalle_motivo_reclamacion",
					f.getMotivoReclamacionAdministrativa());
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
			a.setLogin(usuario.getLogin());
			a.setRol(usuario.getRol().getNombre());
			a.setFecha(new Timestamp(new Date().getTime()));
			listaAdjuntos.add(a);
			log.info("Termino la exportacion del odeco "+f.toString()+" al directorio "
					+ pathDetalle);

		}else{
			log.error("[exportar] no se exporto el odeco  "
					+ pathDetalle);
		}
		
		return result;

	}

	/*public void select(String id) {

		// String id =
		// FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id_objeto");
		//try {
			setEdit(true);

			log.debug("Odeco  Seleccionado:" + id);
			if (id == null)
				return;
			setOferta(ofertaN.getEstadoTrue(Integer.parseInt(id)));
			setFechaIn(getOferta().getFechaIncidencia());
			codigoReclamacion = oferta.getCodigoReclamacionInstitucion() + "/"
					+ oferta.getCodigoReclamacionCiudad() + "/RA/"
					+ oferta.getCodigoReclamacionCorrelativo();
			listaAjuntosVista = adjuntoBL.getCiudadNombre(Integer.parseInt(id));
			setListaAjuntosVista(listaAjuntosVista);
			// this.idOfertaStr = String.valueOf(this.oferta.getId());
			this.idOfertaStr = id;
			
			
			
			selectedObjetoReclamo = new String[oferta.getObjetoReclamos().size()];
			int j = 0;
			for (ObjetoReclamo comm : oferta.getObjetoReclamos()) {

				selectedObjetoReclamo[j] = comm.getObjetoReclamoId().toString();
				j++;
			}
			
			
			List<Object[]> motrec = ofertaN.getMotivoReclamacion(idOfertaStr);
			selectedMotivoReclamo = new String[motrec.size()];
			int i = 0;
			for (Object comm : motrec) {

				selectedMotivoReclamo[i] = comm.toString();
				i++;
			}
			
			
			selectRespuestaOdeco = oferta.getRespuestaOdeco().getRespuestaOdecoId()
					.toString();
			selectCiudadReclamacion = ciudadBL
					.getCiudadNombre(oferta.getCiudad().getNombre()).getCiudadId()
					.toString();
			selectCiudad = ciudadBL.getCiudadNombre(oferta.getCiudadLocalidad())
					.getCiudadId().toString();
			selectCiudadReclamacion = oferta.getCiudad().getCiudadId().toString();
			selectMedioNotificacion = oferta.getMedioNotificacion()
					.getMedioNotificacionId().toString();
			formaRegistro=oferta.getFormaRegistro();
			log.debug("[select] odeco seleccionado correctamente con id: "+id);
		//} catch (Exception e) {
		//	log.error("[select] error al seleccionar el odeco");
		//}
		

	}
	*/
	public void selectSegundaInstancia(String id) {

		// String id =
		// FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id_objeto");
		setEdit(true);

		log.debug("Odeco  Seleccionado Segunda Instancia:" + id);
		if (id == null)
			return;
		setOferta(ofertaN.getEstadoTrue(Integer.parseInt(id)));
		setFechaIn(getOferta().getFechaIncidencia());
		codigoReclamacion = oferta.getCodigoReclamacionInstitucion() + "/"
				+ oferta.getCodigoReclamacionCiudad() + "/RA/"
				+ oferta.getCodigoReclamacionCorrelativo();
		listaAjuntosVista = adjuntoBL.getCiudadNombre(Integer.parseInt(id));
		setListaAjuntosVista(listaAjuntosVista);
		// this.idOfertaStr = String.valueOf(this.oferta.getId());
		this.idOfertaStr = id;
		
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
									micrium.util.Parameter.MENSAJE_LISTAOBJETOS_ERROR));
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
									micrium.util.Parameter.MENSAJE_MOTIVOS_ERROR));
		}

		
		
		
		selectedObjetoReclamo = new String[oferta.getObjetoReclamos().size()];
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
		
		selectRespuestaOdeco = oferta.getRespuestaOdeco().getRespuestaOdecoId()
				.toString();

		if(!oferta.getCiudadLocalidad().isEmpty() ||oferta.getCiudadLocalidad()!=null){
			Ciudad ciudad=ciudadBL.getCiudadNombre(oferta.getCiudadLocalidad());
			if(ciudad!=null){
				selectCiudad = ciudad.getCiudadId().toString();
			}else{
				log.error("[select] error al obtener la ciudad localidad");
				return ;
			}
			
		}
		formaRegistro=oferta.getFormaRegistro();
		selectCiudadReclamacion = oferta.getCiudad().getCiudadId().toString();
		selectMedioNotificacion = oferta.getMedioNotificacion()
				.getMedioNotificacionId().toString();

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
	 
	public boolean calcularFechaResolucion(){
		log.debug("se va calcular la fecha de resolucion");
	//	try {
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
				
				Date ff=new Date(calendario.getFecha().getTime());
				ff.setYear(new Date().getYear());
				cal.setTime(ff);			
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
			log.debug("[calcularFechaResolucion] se calculo la fecha de reclamacion correctamente");
			return true;
	/*	} catch (Exception e) {
			log.error("[save] [calcularFechaResolucion] error al hacer el calculo de la fecha de resolucion");
			return false;
		}*/
	
		
		
	}
	
	public void handleFileUpload(FileUploadEvent event){

		//try {
			log.debug("[handleFileUpload] se va adjuntar el archivo");
			HttpServletRequest request = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			this.strIdUs = (String) request.getSession().getAttribute(
					"TEMP$USER_NAME");
			Usuario usuario = usuarioBL.getUserLogin(strIdUs);
			SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
			/*directorio = new File(
					controlParametro.getRutaAdjunto()
							+ oferta.getCodigoReclamacionInstitucion()
							+ oferta.getCodigoReclamacionCiudad()
							+ oferta.getCodigoReclamacionCorrelativo());*/
			// directorio.mkdirs();
			Adjunto adjunto = new Adjunto();
			adjunto.setNombre(usuario.getRol().getNombre() + "_"
					+ event.getFile().getFileName());
			adjunto.setEstado(true);
			adjunto.setAdjuntoId(null);
			adjunto.setFormularioOdeco(getOferta());
			adjunto.setRuta(controlParametro.getRutaAdjunto()+ oferta.getCodigoReclamacionInstitucion()+ oferta.getCodigoReclamacionCiudad()+ oferta.getCodigoReclamacionCorrelativo()+"-"+formateador.format(oferta.getFechaReclamo()) + File.separator
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
				listaInputStream.add(event.getFile().getInputstream());
			} catch (IOException e) {
				
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Notificacion", micrium.util.Parameter.MENSAJE_ADJUNTOS_ERROR));
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
		/*} catch (Exception e2) {
			// TODO: handle exception
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Notificacion", micrium.util.Parameter.MENSAJE_ADJUNTOS_ERROR));
			log.error("[handleFileUpload] Notificacion : No Se adjunto el archivo");
		}*/

	}

	public Boolean copyFileSegundaInstancia(File sourceFile) {
		log.debug("[copyFileSegundaInstancia] se van a copiar los archivos adjuntos");
		Boolean sw= true;
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		directorio = new File(
				controlParametro.getRutaAdjunto()
						+ oferta.getCodigoReclamacionInstitucion()
						+ oferta.getCodigoReclamacionCiudad()
						+ "RA"
						+ oferta.getCodigoReclamacionCorrelativo()+"-"+formateador.format(oferta.getFechaReclamo()));
		

		FileChannel origen = null;
		FileChannel destino = null;
		try {
			if(directorio.exists()){
				origen = new FileInputStream(sourceFile).getChannel();
				destino = new FileOutputStream(directorio + File.separator
						+ sourceFile.getName()).getChannel();

				long count = 0;
				long size = origen.size();
				while ((count += destino.transferFrom(origen, count, size - count)) < size)
					;
				log.debug("[copyFileSegundaInstancia] se copiaron los archivos adjuntos");
			}else{
				if(directorio.mkdirs()){
					origen = new FileInputStream(sourceFile).getChannel();
					destino = new FileOutputStream(directorio + File.separator
							+ sourceFile.getName()).getChannel();
	
					long count = 0;
					long size = origen.size();
					while ((count += destino.transferFrom(origen, count, size - count)) < size)
						;
					log.debug("[copyFileSegundaInstancia] se copiaron los archivos adjuntos");
				}else{
				log.error("[copyFileSegundaInstancia] no se copiaron los archivos, no se pudo crear el directorio");
				return false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("[copyFileSegundaInstancia] no se copiaron los archivos adjuntos");
			return false;
		} finally {
			if (origen != null) {
				try {
					origen.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (destino != null) {
				try {
					destino.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return sw;
	}

	/*public static void main(String[] args) {
		String ruta="";
		ruta="";
		File file= new File(ruta);
		try {
			OutputStream out = new FileOutputStream(file);
			System.out.println(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}*/

	public Boolean copyFileRA(List<InputStream> listaInS, List<Adjunto> listAd) {
		Boolean sw= true;
		try {
			
	        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
			
			log.debug("[copyFile] se van a copiar los archivos adjuntos");
			File directorio = new File(
							controlParametro.getRutaAdjunto()
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
							log.error("[copyFile] no se pudo crear el outputstream",e );						
							return false;
						} finally {
							if(out!=null){
								out.close();
							}
					        
					    }
						
					}
					log.debug("[copyFile] se copiaron los archivos adjuntos");
			
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
							log.error("[copyFile] no se pudo crear el outputstream",e );						
							return false;
						} finally {
							if(out!=null){
								out.close();
							}
					        
					    }
						
					}
					log.debug("[copyFile] se copiaron los archivos adjuntos");
				}else{
					
					log.error("[copyFile] no se copiaron los archivos, no se pudo crear el directorio");
					return false;
				}
			}
			
			return sw;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("[save] Notificacion : error al copiar el archivo",e);
			return false;
		}

	}

	
	public List<String> getObjetosReclamos(int id){
		if(id!=0){
			
			List objt= ofertaN.getObjetosReclamosDescripcion(id);

			return objt;
		}
		return null;
	}
	public List<String> getMotivosReclamos(int id){
		if(id!=0){
		
		List objt= ofertaN.getMotivosReclamosDescripcion(id);

		return objt;
		}
		return null;
	}

	public String getCiudadReclamacion(int id){
		if(id!=-1){
			try {
				Ciudad ciudad = ciudadBL.getCiudad(id);
				return ciudad.getNombre();
			} catch (Exception e) {
				log.error("[getCiudadReclamacion] no se pudo obtener la ciudad con id: "+id);
				return "";
			}
			
		}
		return "";
	}
	public String getMedioNotificacion(int id){
		if(id!=-1){
		try {
			MedioNotificacion medio = medioNotificacionBL.getMedioNotificacionID(id);
			return medio.getDescripcion();
		} catch (Exception e) {
			log.error("[getMedioNotificacion] no se pudo obtener el medio de notificacion con id: "+id);
			return "";
		}
		}
		return "";
	}
	
	public String getRespuestaOdeco(int id){
		if(id!=-1){
		try {
			RespuestaOdeco medio = respuestaOdecodBL.getRespuestaOdecoID(id);
			return medio.getRespuestaOdeco();
		} catch (Exception e) {
			log.error("[getRespuestaOdeco] no se pudo obtener la respuesta odeco con id: "+id);
			return "";
		}
		}
		return "";
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

	public List<SelectItem> getSelectRespuestaOdecos() {
		return selectRespuestaOdecos;
	}

	public void setSelectRespuestaOdecos(List<SelectItem> selectRespuestaOdecos) {
		this.selectRespuestaOdecos = selectRespuestaOdecos;
	}

	public String getSelectRespuestaOdeco() {
		return selectRespuestaOdeco;
	}

	public void setSelectRespuestaOdeco(String selectRespuestaOdeco) {
		this.selectRespuestaOdeco = selectRespuestaOdeco;
	}

	public String getStrIdUs() {
		return strIdUs;
	}

	public void setStrIdUs(String strIdUs) {
		this.strIdUs = strIdUs;
	}

	public void dateSelect(SelectEvent event) {
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

	public List<Adjunto> getListaAdjuntos() {
		return listaAdjuntos;
	}

	public void setListaAdjuntos(List<Adjunto> listaAdjuntos) {
		this.listaAdjuntos = listaAdjuntos;
	}

	public String[] getSelectedAdjuntos() {
		return selectedAdjuntos;
	}

	public void setSelectedAdjuntos(String[] selectedAdjuntos) {
		this.selectedAdjuntos = selectedAdjuntos;
	}

	public Date getFechaIn() {
		return fechaIn;
	}

	public void setFechaIn(Date fechaIn) {
		this.fechaIn = fechaIn;
	}

	public File getDirectorio() {
		return directorio;
	}

	public void setDirectorio(File directorio) {
		this.directorio = directorio;
	}

	public List<String> getListaFormaRegistro() {
		return listaFormaRegistro;
	}

	public void setListaFormaRegistro(List<String> listaFormaRegistro) {
		this.listaFormaRegistro = listaFormaRegistro;
	}

	public String getFormaRegistro() {
		return formaRegistro;
	}

	public void setFormaRegistro(String formaRegistro) {
		this.formaRegistro = formaRegistro;
	}
	public String getExpresionRegular(){
		return Parameter.expresionRegular;
	}
	
	public String getExpresionRegularNumero(){
		
		return Parameter.expresionRegularNumero;
	}
	
	/**********/

}
