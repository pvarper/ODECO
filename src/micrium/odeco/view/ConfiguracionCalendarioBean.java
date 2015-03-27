package micrium.odeco.view;

import java.io.Serializable;
import java.sql.Timestamp;
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

import micrium.odeco.business.CiudadBL;
import micrium.odeco.business.ConfiguracionCalendarioBL;
import micrium.odeco.model.Calendario;
import micrium.odeco.model.Ciudad;
import micrium.user.view.ControlerBitacora;
import micrium.util.DescriptorBitacora;
import micrium.util.Parameter;

import org.apache.log4j.Logger;



@ManagedBean
@ViewScoped
public class ConfiguracionCalendarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private ConfiguracionCalendarioBL ofertaN;
	@Inject
	private CiudadBL ciudadBL;
	@Inject
	private ControlerBitacora controlerBitacora;
	private List<SelectItem> listaTiempos;
	private String tiempoNoEstaticoSeleccionado;
	private Calendario oferta;
	private String idOfertaStr;
	private String tiempoStr;
	private boolean edit;
	private List<Calendario> listaOferta = new ArrayList<Calendario>();
	private boolean ofertaActiva;
	private List<SelectItem> selectCiudades;
	private String selectCiudad;
	private Date fecha;
	public static Logger log = Logger.getLogger(ConfiguracionCalendarioBean.class);
	

	@PostConstruct
	public void init(){
	
		try {
			newObject();
			fillListasSeleccionables();
		} catch (Exception e) {
			log.debug("[init] Hubo un error a la hora de crear un nuevo objeto " + e.getMessage());
			return;
		}
		try {
			setListaOferta(ofertaN.getCalendarioList());//Al iniciar necesitamos llenar la tabla
		} catch (Exception e) {
			
			log.error("[init] error al llenar la tabla",e);
			return;
		}
			
	}

	private void fillListasSeleccionables() {
		selectCiudades = new ArrayList<SelectItem>();
		selectCiudades.add(new SelectItem("-1", "Ciudades"));
		List<Ciudad> list = null;
		try {
			list = ciudadBL.getList();

			for (Ciudad item : list) {
				SelectItem s = new SelectItem(item.getCiudadId(), item.getNombre());
				selectCiudades.add(s);			
			}
			log.debug("Carga de Ciudades [ok]");
		} catch (Exception e) {
			log.error("Carga de Lista de Ciudades [fail]", e);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error", micrium.util.Parameter.MENSAJE_LISTACIUDADES_ERROR));
		}
		
	}
	public void newObject() {
		
		this.edit = false;
		this.oferta = new Calendario();
		setFecha(new Date());
		this.oferta.setMotivo("");
		selectCiudad = "-1";
	
	}
	


	public void save()  {
		
		try {
			log.debug("[save] Asignando datos a un Configuracion de Calendario "+ oferta.toString());
			if (isEdit()) {
				log.info("[save] Actualizando una Configuracion de Calendario. Id: " + this.getIdOfertaStr());
				oferta.setCalendarioId(Integer.valueOf(this.getIdOfertaStr()));	
			} else {
				log.info("[save] Guardando una nueva Configuracion de Calendario");
				oferta.setCalendarioId(null);
			}
			if(selectCiudad.equalsIgnoreCase("-1")){
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Mensaje de error", micrium.util.Parameter.MENSAJE_VALIDATE_CIUDAD_LOCALIDAD));
				return;
			}
			
			try {
				Ciudad ciudad= ciudadBL.getCiudad(Integer.valueOf(selectCiudad));
				
				if(ciudad==null){
					log.debug("[save] no se obtuvo ninguna ciudad con el valor: "+selectCiudad);
					return;
				}
				oferta.setCiudad(ciudad.getNombre());
				log.debug("[save] se obtuvo la ciudad "+ciudad.toString());
			} catch (Exception e) {
				e.printStackTrace();
				log.error("[save] error al obtener la ciudad con id: "+selectCiudad,e);
				return;
			}
			
			
			if(fecha==null){
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Mensaje de error",micrium.util.Parameter.MENSAJE_VALIDATE_FECHAFERIADO));
				return ;
			}
			oferta.setFecha( new Timestamp(fecha.getTime()));
			
			String message = ofertaN.validate(oferta);
			
			if (!message.isEmpty()) {
				log.warn("[save] Error al Validar :" + message);
				newObject();
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Mensaje de error", message));
				return ;
			}
			
				if (isEdit()) { // modificar
					log.debug("[save] se va modificar la configuracion de calendario "+oferta.toString());
					ofertaN.update(getOferta());				
					controlerBitacora.update(DescriptorBitacora.CONFIGURACION_CALENDARIO,oferta.getCalendarioId() + "", oferta.getMotivo());
				} else { // insertar
					log.debug("[save] se va guardar la configuracion de calendario "+oferta.toString());
					ofertaN.save(getOferta());
						
					log.info("[save] se guardo la configuracion de calendario "+oferta.toString());
					controlerBitacora.insert(DescriptorBitacora.CONFIGURACION_CALENDARIO, oferta.getCalendarioId() + "", oferta.getMotivo());
					setEdit(false);
				}
				setListaOferta(ofertaN.getCalendarioList());
				
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"NOTIFICACION", "Se guardo Correctamente"));

			newObject();
		} catch (Exception e) {
			log.error("[save] error al momento de guadar o modificar la configuracion de calendario "
					+ oferta.toString(),e);
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",micrium.util.Parameter.MENSAJE_GUARDADO_ERROR));					
		
		}
		
		
		
	}

	public void select() {
		String id = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("id_objeto");
		Ciudad ciudad=null;
		log.debug("[select] se va seleccionar la configuracion con id: "+id);

			setOferta(ofertaN.getCalendario((Integer.parseInt(id))));
			ciudad= ciudadBL.getCiudadNombre(oferta.getCiudad());
			setFecha(new java.sql.Date(oferta.getFecha().getTime()));
			if(ciudad!=null){
				this.selectCiudad=ciudad.getCiudadId().toString();
			}else{
				log.error("[select] no se pudo obtener ciudad con nombre: "+oferta.getCiudad());
				return;
			}
			
			this.idOfertaStr = String.valueOf(this.oferta.getCalendarioId());
			setEdit(true);
			log.debug("[select] se selecciono la configuracion correctamente con id: "+id);

		
		
	}

	public void remove() {
		try {
			String idStr = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestParameterMap().get("id_objeto");
			
				log.info("[remove] se va eliminar la configuracion id: "+idStr);
				Integer id = Integer.parseInt(idStr);
				setOferta(ofertaN.getCalendario(id));
				ofertaN.delete(id);		
				log.info("[remove] se elminio la confifuracion con id: "+id);
				controlerBitacora.delete(DescriptorBitacora.CONFIGURACION_CALENDARIO,
						oferta.getCalendarioId() + "", oferta.getMotivo());
				setListaOferta(ofertaN.getCalendarioList());			
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"NOTIFICACION",micrium.util.Parameter.MENSAJE_ELIMINAR ));
				newObject();
		} catch (Exception e) {
			log.error(
					"[remove] error al momento de eliminar"
							,e);
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",micrium.util.Parameter.MENSAJE_ELIMINAR_ERROR));
		
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

	

	public Calendario getOferta() {
		return oferta;
	}

	public void setOferta(Calendario oferta) {
		this.oferta = oferta;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public List<Calendario> getListaOferta() {
		return listaOferta;
	}

	public void setListaOferta(List<Calendario> listaServicio) {
		this.listaOferta = listaServicio;
	}

	public String getIdOfertaStr() {
		return idOfertaStr;
	}

	public void setIdOfertaStr(String idOfertaStr) {
		this.idOfertaStr = idOfertaStr;
	}
	public String getTiempoStr() {
		return tiempoStr;
	}

	public void setTiempoStr(String tiempoStr) {
		this.tiempoStr = tiempoStr;
	}


	public List<SelectItem> getListaTiempos() {
		return listaTiempos;
	}



	public void setListaTiempos(List<SelectItem> listaTiempos) {
		this.listaTiempos = listaTiempos;
	}

	public String getTiempoNoEstaticoSeleccionado() {
		return tiempoNoEstaticoSeleccionado;
	}

	public void setTiempoNoEstaticoSeleccionado(
			String tiempoNoEstaticoSeleccionado) {
		this.tiempoNoEstaticoSeleccionado = tiempoNoEstaticoSeleccionado;
	}

	public boolean isOfertaActiva() {
		return ofertaActiva;
	}

	public void setOfertaActiva(boolean ofertaActiva) {
		this.ofertaActiva = ofertaActiva;
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getExpresionRegular() {
		return Parameter.expresionRegular;
	}

	public String getExpresionRegularNumero() {
		return Parameter.expresionRegularNumero;
	}
	
	
	
	
}
