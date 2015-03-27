package micrium.odeco.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import micrium.odeco.business.FlujoBL;
import micrium.odeco.model.Flujo;
import micrium.user.business.RoleBL;
import micrium.user.model.Rol;
import micrium.user.view.ControlerBitacora;
import micrium.util.DescriptorBitacora;
import micrium.util.Parameter;

import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class FlujoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(FlujoBean.class);

	@Inject
	private FlujoBL ofertaN;


	@Inject
	private RoleBL roleBL;

	@Inject
	private ControlerBitacora controlerBitacora;

	private List<SelectItem> selectAgentes;
	private String selectAgente;

	private List<SelectItem> selectStaffs;
	private String selectStaff;

	private List<SelectItem> selectCoordinadores;
	private String selectCoordinador;

	private List<SelectItem> selectRegulaciones;
	private String selectRegulacion;

	private List<SelectItem> selectEstados;
	private String selectEstado;

	private String idOfertaStr;

	private String strIdUs = "";

	private boolean edit;
	private List<Flujo> listaOferta = new ArrayList<Flujo>();
	private boolean ofertaActiva;

	private Flujo selectOdeco;
	private Flujo oferta;

	// private RequestContext context;

	@PostConstruct
	public void init() {
		log.info("********* Iniciando Vista Gestion de Flujo **********");
		try {
			newObject();
			fillListasSeleccionables();
			log.debug("[init] se var cargar la tabla");
			setListaOferta(ofertaN.getList());// Al iniciar necesitamos llenar
												// la
												// tabla
			// System.out.println("Devolvato: " + this.oferta);
		} catch (Exception ex) {
			log.debug("[init] Hubo un error a la hora de crear un nuevo objeto "
					+ ex.getMessage());
		}

	}

	public void newObject() {
		log.debug("inicializando Nuevo Flujo...");
		selectAgentes = new ArrayList<SelectItem>();
		// selectAgentes.add(new SelectItem("Seleccionar Agente"));

		selectStaffs = new ArrayList<SelectItem>();
		// selectStaffs.add(new SelectItem("Seleccionar Staff"));

		selectCoordinadores = new ArrayList<SelectItem>();
		// selectCoordinadores.add(new SelectItem("Seleccionar Coordinador"));

		selectRegulaciones = new ArrayList<SelectItem>();
		// selectRegulaciones.add(new SelectItem("Seleccionar Regulacion"));

		selectEstados = new ArrayList<SelectItem>();
		log.debug("Flujo Inicializado [ok]");
	}

	private void fillListasSeleccionables() {
		log.debug("Inciando Carga de Datos...");

		// selectEstados.add(new SelectItem("Seleccionar Estados"));
		selectEstados.add(new SelectItem("true"));
		selectEstados.add(new SelectItem("false"));
		List<Rol> list4 = null;
		try {
			list4 = roleBL.getRoles();
			for (Rol item : list4) {
				SelectItem s = new SelectItem(item.getNombre());

				selectAgentes.add(s);
				selectStaffs.add(s);
				selectCoordinadores.add(s);
				selectRegulaciones.add(s);

			}
			log.debug("Carga de Lista de Roles  [ok]");
			Flujo flujo = ofertaN.getFlujoNombre("flujo");
			selectAgente = flujo.getAgente();
			selectStaff = flujo.getStaff();
			selectCoordinador = flujo.getCoordinador();
			// setSelectCoordinador(selectCoordinador);
			selectRegulacion = flujo.getRegulacion();
			selectEstado = flujo.getEstado().toString();
		} catch (Exception e) {
			log.error("Carga de Lista de Roles [fail]", e);
			FacesContext.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Mensaje de error",
									"No se Pudo Obtener los roles"));
		}

	}

	public void saveFlujo() {
		// System.out.println("********************************************************************");
		
		try {
			log.debug("[saveFlujo] Asignando datos al Flujo con la ide "
					+ this.getIdOfertaStr());
			log.info("[saveFlujo] se va actualizar el flujo");
			// context = RequestContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			this.strIdUs = (String) request.getSession().getAttribute(
					"TEMP$USER_NAME");
			
			log.debug("[saveFlujo] se va obtener el flujo");
			Flujo flujo  = ofertaN.getFlujoNombre("flujo");
			if (flujo == null) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"NOTIFICACION", "No se pudo Obtener el flujo"));
				return ;
			}
			log.debug("[saveFlujo] se obtuvo el flujo "+flujo.toString());

			flujo.setAgente(getSelectAgente());
			flujo.setStaff(getSelectStaff());
			flujo.setCoordinador(getSelectCoordinador());
			flujo.setRegulacion(getSelectRegulacion());
			flujo.setEstado(Boolean.valueOf(getSelectEstado()));

			// System.out.println("****************************** despues del for 1 **************************************");

			System.out
					.println("****************************** EDIT **************************************");

			System.out
					.println("**************************** iniciando Transaccion ****************************************");
			
			log.debug("[saveFlujo] se va actualizar el flujo "+flujo.toString());
			ofertaN.updateFlujo(flujo);
			log.info("[saveFlujo] se actualizo el flujo "+flujo.toString());
			controlerBitacora.update(DescriptorBitacora.FLUJO, flujo.getFlujoId()
					+ "", "");

			newObject();
			fillListasSeleccionables();

			setListaOferta(ofertaN.getList());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "NOTIFICACION",
							"Se guardo Correctamente"));


		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							Parameter.MENSAJE_GUARDADO_ERROR));
			log.error("[saveFlujo] error al acutalizar el flujo ",e);
		}
	

	}

	public void select(String id) {

		// String id =
		// FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id_objeto");

		/*
		 * HttpServletRequest request = (HttpServletRequest) FacesContext
		 * .getCurrentInstance().getExternalContext().getRequest(); this.strIdUs
		 * = (String) request.getSession().getAttribute( "TEMP$USER_NAME");
		 * Usuario usuario = usuarioBL.getUserLogin(strIdUs);
		 */
		setEdit(true);
		log.debug("flujo  Seleccionado:" + id);
		if (id == null)
			return;

		Flujo flujo = ofertaN.getFlujo(Integer.valueOf(id));
		if (flujo == null) {
			log.error("[select] error al cargar los datos del flujo con id: "+id);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							"error al obtener el Flujo"));
			return;
		}
		selectAgente = flujo.getAgente();
		selectStaff = flujo.getStaff();
		selectCoordinador = flujo.getCoordinador();
		// setSelectCoordinador(selectCoordinador);
		selectRegulacion = flujo.getRegulacion();
		selectEstado = flujo.getEstado().toString();

		log.info("[select] datos cargados correctamente del Flujo: " +flujo.toString());

	}

	public boolean booleanEstado(String charEstado) {
		log.info("[booleanEstado] ingreso al metodo boolean estado.");
		if (charEstado.equals("A")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public List<Flujo> getListaOferta() {
		return listaOferta;
	}

	public void setListaOferta(List<Flujo> listaOferta) {
		this.listaOferta = listaOferta;
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

	public Flujo getSelectOdeco() {
		return selectOdeco;
	}

	public void setSelectOdeco(Flujo selectOdeco) {
		this.selectOdeco = selectOdeco;
	}

	public String getStrIdUs() {
		return strIdUs;
	}

	public void setStrIdUs(String strIdUs) {
		this.strIdUs = strIdUs;
	}

	public void dateSelect(SelectEvent event) {
	}

	public Date getDateToday() {
		return new Date();
	}

	public List<SelectItem> getSelectAgentes() {
		return selectAgentes;
	}

	public void setSelectAgentes(List<SelectItem> selectAgentes) {
		this.selectAgentes = selectAgentes;
	}

	public String getSelectAgente() {
		return selectAgente;
	}

	public void setSelectAgente(String selectAgente) {
		this.selectAgente = selectAgente;
	}

	public List<SelectItem> getSelectStaffs() {
		return selectStaffs;
	}

	public void setSelectStaffs(List<SelectItem> selectStaffs) {
		this.selectStaffs = selectStaffs;
	}

	public String getSelectStaff() {
		return selectStaff;
	}

	public void setSelectStaff(String selectStaff) {
		this.selectStaff = selectStaff;
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

	public List<SelectItem> getSelectRegulaciones() {
		return selectRegulaciones;
	}

	public void setSelectRegulaciones(List<SelectItem> selectRegulaciones) {
		this.selectRegulaciones = selectRegulaciones;
	}

	public String getSelectRegulacion() {
		return selectRegulacion;
	}

	public void setSelectRegulacion(String selectRegulacion) {
		this.selectRegulacion = selectRegulacion;
	}

	public List<SelectItem> getSelectEstados() {
		return selectEstados;
	}

	public void setSelectEstados(List<SelectItem> selectEstados) {
		this.selectEstados = selectEstados;
	}

	public String getSelectEstado() {
		return selectEstado;
	}

	public void setSelectEstado(String selectEstado) {
		this.selectEstado = selectEstado;
	}

	public Flujo getOferta() {
		return oferta;
	}

	public void setOferta(Flujo oferta) {
		this.oferta = oferta;
	}

	/**********/

}
