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

import micrium.odeco.business.ObjetoReclamoBL;
import micrium.odeco.model.ObjetoReclamo;
import micrium.user.view.ControlerBitacora;
import micrium.util.DescriptorBitacora;
import micrium.util.Parameter;

import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class ObjetoReclamoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	

	@Inject
	private ObjetoReclamoBL ofertaN;

	@Inject
	private ControlerBitacora controlerBitacora;
	
	private List<SelectItem> listaTiempos;
	private String tiempoNoEstaticoSeleccionado;
	
	private ObjetoReclamo oferta;
	private String idOfertaStr;
	private String tiempoStr;
	private boolean edit;
	private List<ObjetoReclamo> listaOferta = new ArrayList<ObjetoReclamo>();
	private boolean ofertaActiva;


	public static Logger log = Logger.getLogger(ObjetoReclamoBean.class);

	@PostConstruct
	public void init() throws Exception {
	
		try {
			newObject();
		} catch (Exception e) {
			log.debug("[init] Hubo un error a la hora de crear un nuevo objeto " + e.getMessage());
		}
		setListaOferta(ofertaN.getObjetosReclamosList());//Al iniciar necesitamos llenar la tabla	
	}

	
	public void newObject() {
		this.edit = false;
		this.oferta = new ObjetoReclamo();
		this.oferta.setObjetoReclamo("");
		this.oferta.setDescripcion("");	
	}
	


	public String save() {
		
		try {
			log.debug("[save] Asignando datos a un Objeto Reclamo con la ide "+ this.getIdOfertaStr());
			if (isEdit()) {
				log.info("[save] Actualizando un Objeto Reclamo. Id: " + this.getIdOfertaStr());
				oferta.setObjetoReclamoId(Integer.valueOf(this.getIdOfertaStr()));	
			} else {
				log.info("[save] Guardando un nuevo Objeto Reclamo");
				oferta.setObjetoReclamoId(null);
			}
			
			String message = ofertaN.validate(oferta);
			
			if (!message.isEmpty()) {
				log.warn("[save] ERROR AL VALIDAR" + message);
				newObject();
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Mensaje de error", message));
				return "";
			}
			
			

				if (isEdit()) { // modificar
					
					ofertaN.updateObjetoReclamo(getOferta());
					log.info("[save] se modifico correctamente el objeto reclamo "+oferta.toString());
					controlerBitacora.update(DescriptorBitacora.MOTIVO_RECLAMO,oferta.getObjetoReclamoId() + "", oferta.getObjetoReclamo());
				} else { // insertar
					oferta.setEstado(true);
					ofertaN.saveObjetoReclamo(getOferta());
						
					log.info("[save] se registro corectamente el objeto reclamo "+oferta.toString());
					controlerBitacora.insert(DescriptorBitacora.MOTIVO_RECLAMO, oferta.getObjetoReclamoId() + "", oferta.getObjetoReclamo());
					setEdit(false);
				}
				setListaOferta(ofertaN.getObjetosReclamosList());
				
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"NOTIFICACION", Parameter.MENSAJE_GUARDADO));
		
			newObject();
			return "";
		} catch (Exception e) {
			log.error("[save] error al momento de guardar o modificar el objeto reclamo: "
					+ oferta.toString(),e );
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",Parameter.MENSAJE_GUARDADO_ERROR));
			return "";
		}
	}

	public void select() {
		String id = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("id_objeto");
		
	
			log.debug("[select] se va seleccionar el objeto reclamo con id "+id);
			ObjetoReclamo objeto=ofertaN.getObjetoReclamo((Integer.parseInt(id)));
			if(objeto==null){
				log.error("[select] error al cargar los datos del objeto Reclamo: "+id+" seleccionado");
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR","error al cargar el objeto seleccionado"));
				return;
			}
			setOferta(objeto);			
			this.idOfertaStr = String.valueOf(this.oferta.getObjetoReclamoId());
			setEdit(true);
			log.debug("[select] se selecciono correctamente el objeto reclamo con id "+id);
	
		
	}

	public void remove() {
		String idStr = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("id_objeto");
		Integer id = Integer.parseInt(idStr);
		try {
		
				log.debug("[remove]se va eliminar un registro");
				
				setOferta(ofertaN.getObjetoReclamo(id));
				ofertaN.deleteObjetoReclamo(id);
				log.debug("[remove] se elimino correctamente el objeto reclamo "+id);
				controlerBitacora.delete(DescriptorBitacora.OBJETO_RECLAMO,
						oferta.getObjetoReclamoId() + "", oferta.getObjetoReclamo());
				setListaOferta(ofertaN.getObjetosReclamosList());

				
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"NOTIFICACION", Parameter.MENSAJE_ELIMINAR));

			newObject();
		} catch (Exception e) {
			log.error(
					"[remove] error al momento de eliminar el Objeto Reclamo: "
							+ id,e);
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",Parameter.MENSAJE_ELIMINAR_ERROR));
		
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

	

	public ObjetoReclamo getOferta() {
		return oferta;
	}

	public void setOferta(ObjetoReclamo oferta) {
		this.oferta = oferta;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public List<ObjetoReclamo> getListaOferta() {
		return listaOferta;
	}

	public void setListaOferta(List<ObjetoReclamo> listaServicio) {
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


	public String getExpresionRegular() {
		return Parameter.expresionRegular;
	}


	


	public String getExpresionRegularNumero() {
		return Parameter.expresionRegularNumero;
	}


	
	
	
	
	
}
