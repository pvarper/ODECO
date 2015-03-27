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

import micrium.odeco.business.FlujoBL;
import micrium.odeco.business.MotivoReclamoBL;
import micrium.odeco.model.Flujo;
import micrium.odeco.model.MotivoReclamo;
import micrium.user.business.RoleBL;
import micrium.user.business.UsuarioBL;
import micrium.user.model.Rol;
import micrium.user.model.Usuario;
import micrium.user.view.ControlerBitacora;
import micrium.util.DescriptorBitacora;
import micrium.util.Parameter;

import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class MotivoReclamoBean implements Serializable {

	private static final long serialVersionUID = 1L;


	@Inject
	private MotivoReclamoBL ofertaN;

	@Inject
	private ControlerBitacora controlerBitacora;
	@Inject
	private FlujoBL flujoBL;
	@Inject
	private RoleBL rolBL;
	@Inject
	private UsuarioBL usuarioBL;
	private List<SelectItem> listaTiempos;
	private String tiempoNoEstaticoSeleccionado;
	
	private MotivoReclamo oferta;
	private String idOfertaStr;
	private String tiempoStr;
	private boolean edit;
	private List<MotivoReclamo> listaOferta = new ArrayList<MotivoReclamo>();
	private boolean ofertaActiva;
	
	private List<SelectItem> selectCoordinadores;
	private String selectCoordinador;
	
	

	public static Logger log = Logger.getLogger(MotivoReclamoBean.class);

	@PostConstruct
	public void init() throws Exception {
	
		try {
			newObject();
			fillListasSeleccionables();
		} catch (Exception e) {
			log.debug("[init] Hubo un error a la hora de crear un nuevo objeto " + e.getMessage());
		}
		setListaOferta(ofertaN.getMotivoReclamosList());//Al iniciar necesitamos llenar la tabla	
	}

	
	public void newObject() {
		this.edit = false;
		this.oferta = new MotivoReclamo();
		this.oferta.setMotivoReclamo("");
		this.oferta.setDescripcion("");
		this.oferta.setTiempoResolucion(null);
		this.selectCoordinador="-1";
	}
	
	private void fillListasSeleccionables() {
		log.debug("[fillListasSelecionables] cargando datos");
		
		
		selectCoordinadores = new ArrayList<SelectItem>();
		selectCoordinadores.add(new SelectItem("-1","Coordinadores"));
		List<Usuario> listU = null;
		try {
			Flujo flujo=flujoBL.getFlujoNombre("flujo");
			Rol rol=rolBL.getname(flujo.getCoordinador());
			listU = usuarioBL.getListRolCoorEspecial(rol.getRolId().toString());
			for (Usuario item : listU) {
				SelectItem s = new SelectItem(item.getUsuarioId(),item.getLogin());
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
	}

	public String save() {
		try {
			log.debug("[save] Asignando datos a un Motivo Reclamo con la ide "+ this.getIdOfertaStr());
			if (isEdit()) {
				log.info("[save] Actualizando un Motivo Reclamo. Id: " + this.getIdOfertaStr());
				oferta.setMotivoReclamoId(Integer.valueOf(this.getIdOfertaStr()));	
			} else {
				log.info("[save] Guardando un nuevo Motivo Reclamo");
				oferta.setMotivoReclamoId(null);
			}
			
			if(!selectCoordinador.equals("-1")){
				Usuario u=usuarioBL.getUser(Integer.valueOf(selectCoordinador));
				oferta.setCoordinador(u.getLogin());			
			}else{
				oferta.setCoordinador(null);
			}
			
			String message = ofertaN.validate(oferta);
			
			if (!message.isEmpty()) {
				log.warn("[save]ERROR AL VALIDAR " + message);

				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Mensaje de error", message));
				return "";
			}
			

				if (isEdit()) { // modificar
					
					ofertaN.updateMotivoReclamo(getOferta());
					log.info("[save] se actualizo el motivo reclamo "+oferta.toString());
					controlerBitacora.update(DescriptorBitacora.MOTIVO_RECLAMO,oferta.getMotivoReclamoId() + "", oferta.getMotivoReclamo());
				} else { // insertar
					oferta.setEstado(true);
					ofertaN.saveMotivoReclamo(getOferta());
					log.info("[save] se registro el motivo de reclamo "+oferta.toString());
					controlerBitacora.insert(DescriptorBitacora.MOTIVO_RECLAMO, oferta.getMotivoReclamoId() + "", oferta.getMotivoReclamo());
					setEdit(false);
				}
				setListaOferta(ofertaN.getMotivoReclamosList());
				
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"NOTIFICACION", "Se guardo correctamente"));

			newObject();
			return "";
		} catch (Exception e) {
			log.error("[save] error al momento de guardar o modificar "
					+ oferta.toString() ,e);
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",Parameter.MENSAJE_GUARDADO_ERROR));
			return "";
		}
	
	}

	public void select() {
		String id = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("id_objeto");
		try {
			log.debug("[select] se va seleccionar un motivo reclamo con id "+id);
			MotivoReclamo motivo=ofertaN.getMotivoReclamo((Integer.parseInt(id)));			
			if(motivo==null){
				log.error("[select] error al obtener el motivo reclamo "+id);
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
								"error al obtener el Motivo Reclamo"));
				return;
			}
			if(motivo.getCoordinador()!=null){
				Usuario u = usuarioBL.getUserLogin(motivo.getCoordinador());
				selectCoordinador=u.getUsuarioId().toString();
			}else{
				selectCoordinador="-1";
			}
			setOferta(motivo);
			
			this.idOfertaStr = String.valueOf(this.oferta.getMotivoReclamoId());
			setEdit(true);
			log.debug("[select] se selecciono el motivo de reclamo correctamente con id "+id);
		} catch (Exception e) {
			log.error("[select] error al obtener el motivo reclamo",e);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
							"error al obtener el Motivo Reclamo"));
		}
			
	
		
	}

	public void remove() {
		try {
			String idStr = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestParameterMap().get("id_objeto");
			
				log.info("[remove] se va eliminar un motivo reclamo con id "+idStr);
				Integer id = Integer.parseInt(idStr);
				setOferta(ofertaN.getMotivoReclamo(id));
				ofertaN.deleteMotivoReclamo(id);
				log.info("[remove] se elimino correctamente el motivo reclamo con id "+idStr);
				controlerBitacora.delete(DescriptorBitacora.MOTIVO_RECLAMO,
						oferta.getMotivoReclamoId() + "", oferta.getMotivoReclamo());
				setListaOferta(ofertaN.getMotivoReclamosList());

				
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"NOTIFICACION", Parameter.MENSAJE_ELIMINAR));

		
			newObject();
		} catch (Exception e) {
			log.error(
					"[remove] error al momento de eliminar el Motivo Reclamo: "
							+ oferta.getDescripcion(),e);
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",Parameter.MENSAJE_ELIMINAR_ERROR));
			return ;
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

	

	public MotivoReclamo getOferta() {
		return oferta;
	}

	public void setOferta(MotivoReclamo oferta) {
		this.oferta = oferta;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public List<MotivoReclamo> getListaOferta() {
		return listaOferta;
	}

	public void setListaOferta(List<MotivoReclamo> listaServicio) {
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


	public String getExpresionRegular() {
		return Parameter.expresionRegular;
	}


	


	public String getExpresionRegularNumero() {
		return Parameter.expresionRegularNumero;
	}


	


	

	
	
	
	
	
}
