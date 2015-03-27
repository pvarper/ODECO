package micrium.odeco.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import micrium.odeco.business.RespuestaOdecoBL;
import micrium.odeco.model.RespuestaOdeco;
import micrium.user.view.ControlerBitacora;
import micrium.util.DescriptorBitacora;
import micrium.util.Parameter;

import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class RespuestaOdecoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Inject
	private RespuestaOdecoBL ofertaN;

	@Inject
	private ControlerBitacora controlerBitacora;
	private List<SelectItem> listaTiempos;
	private String tiempoNoEstaticoSeleccionado;
	
	private RespuestaOdeco oferta;
	private String idOfertaStr;
	private String tiempoStr;
	private boolean edit;
	private List<RespuestaOdeco> listaOferta = new ArrayList<RespuestaOdeco>();
	private boolean ofertaActiva;

	public static Logger log = Logger.getLogger(RespuestaOdecoBean.class);

	@PostConstruct
	public void init()  {
	
		try {
			newObject();
		} catch (Exception e) {
			log.debug("[init] Hubo un error a la hora de crear un nuevo objeto " + e.getMessage());
		}
		setListaOferta(ofertaN.getLista());//Al iniciar necesitamos llenar la tabla	
	}

	
	public void newObject() {
		this.edit = false;
		this.oferta = new RespuestaOdeco();
		this.oferta.setRespuestaOdeco("");;
		this.oferta.setDescripcion("");	
	}
	


	public String save() {
		try {
			log.debug("[save] Asignando datos a una Respuesta Odeco con la ide "+ this.getIdOfertaStr());
			if (isEdit()) {
				log.info("[save] Actualizando una Respuesta Odeco. Id: " + this.getIdOfertaStr());
				oferta.setRespuestaOdecoId(Integer.valueOf(this.getIdOfertaStr()));	
			} else {
				log.info("[save] Guardando un nuevo Objeto Reclamo");
				oferta.setRespuestaOdecoId(null);
			}
			
			String message = ofertaN.validate(oferta);
			
			if (!message.isEmpty()) {
				log.warn("[save] ERROR AL VALIDAR" + message);
				newObject();
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Mensaje de error", message));
				return "";
			}
			
		

				if (isEdit()) { // modificar
					
					ofertaN.update((getOferta()));
					log.info("[save] se modifico correctamente la respuesta odeco "+oferta.toString());
					controlerBitacora.update(DescriptorBitacora.MOTIVO_RECLAMO,oferta.getRespuestaOdecoId() + "", oferta.getRespuestaOdeco());
				} else { // insertar
					oferta.setEstado(true);
					ofertaN.save(getOferta());
					log.info("[save] se guardo correctamente la respuesta odeco "+oferta.toString());
					controlerBitacora.insert(DescriptorBitacora.MOTIVO_RECLAMO, oferta.getRespuestaOdecoId() + "", oferta.getRespuestaOdeco());
					setEdit(false);
				}
				setListaOferta(ofertaN.getLista());
				
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"NOTIFICACION", Parameter.MENSAJE_GUARDADO));

			newObject();
			return "";
		} catch (Exception e) {
			log.error("[save] error al momento de guardar o modificar la respuesta odeco"
					+ oferta.toString() );
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",Parameter.MENSAJE_GUARDADO_ERROR));
			return "";
		}
		
	}

	public void select() {
		String id = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("id_objeto");
		log.debug("[select] se va seleecionar una respuesta odeco con id "+id);

			RespuestaOdeco respuesta=ofertaN.getRespuestaOdecoID((Integer.parseInt(id)));
			if(respuesta==null){
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR","Error al momento de seleccionar la respuesta odeco"));
				log.error("[select] error al seleecionar la respuesta odeco");
				return ;
			}
			setOferta(respuesta);		
			this.idOfertaStr = String.valueOf(this.oferta.getRespuestaOdecoId());
			setEdit(true);
			log.debug("[select] se selecciono correctamente una respuesta odeco con id "+id);
		
		
	}

	public void remove() {
		String idStr = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("id_objeto");
		Integer id = Integer.parseInt(idStr);
			try {
				log.info("se va eliminar una respuesta odeco con id "+idStr);
				
				setOferta(ofertaN.getRespuestaOdecoID(id));
				ofertaN.deleteMedioNotificacion(id);
				log.debug("Se elimino correctamente la respuesta odeco "+id);
				controlerBitacora.delete(DescriptorBitacora.OBJETO_RECLAMO,
						oferta.getRespuestaOdecoId() + "", oferta.getRespuestaOdeco());
				setListaOferta(ofertaN.getLista());
		
				
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"NOTIFICACION", Parameter.MENSAJE_ELIMINAR));
		
		
			newObject();
			} catch (Exception e) {
				log.error(
						"[remove] error al momento de eliminar la respuesta odeco: "
								+ id);
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",Parameter.MENSAJE_ELIMINAR_ERROR));
				return;
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

	

	public RespuestaOdeco getOferta() {
		return oferta;
	}

	public void setOferta(RespuestaOdeco oferta) {
		this.oferta = oferta;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public List<RespuestaOdeco> getListaOferta() {
		return listaOferta;
	}

	public void setListaOferta(List<RespuestaOdeco> listaServicio) {
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
	
	public String getExpresionRegular(){
		return Parameter.expresionRegular;
	}
	
	public String getExpresionRegularNumero(){
		
		return Parameter.expresionRegularNumero;
	}
	
	
}
