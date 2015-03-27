package micrium.user.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.business.GrupoAdBL;
import micrium.user.business.RoleBL;
import micrium.user.model.GrupoAd;
import micrium.user.model.Rol;
import micrium.util.ActiveDirectory;
import micrium.util.DescriptorBitacora;
import micrium.util.Utils;

import org.apache.log4j.Logger;

@Named
@javax.enterprise.context.RequestScoped
public class RoleGroupForm {

	@Inject
	private GrupoAdBL groupBL;

	@Inject
	private RoleBL rolBL;

	@Inject
	private ControlerBitacora controlerBitacora;

	private List<GrupoAd> listGroup;

	private GrupoAd group = new GrupoAd();
	private String groupId;
	private Boolean edit;

	private List<SelectItem> selectItems;
	private String select;

	public static Logger log = Logger.getLogger(RoleGroupForm.class);

	@PostConstruct
	public void init() {

		try {
			group = new GrupoAd();
			listGroup = groupBL.getGroups();
			fillSelectItems();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void fillSelectItems() {

		selectItems = new ArrayList<SelectItem>();
		selectItems.add(new SelectItem("-1", "Grupos_Rol"));
		List<Rol> listaRol = rolBL.getRoles();
		for (Rol role : listaRol) {
			SelectItem sel = new SelectItem(role.getRolId(), role.getNombre());
			selectItems.add(sel);
		}
	}

	public String saveGroup() {
		int idRole = Integer.parseInt(select);
		if (idRole == -1) {
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error",
			// "seleccione un role"));
			Utils.showError("Seleccione un Rol.");
			return "";
		}

		String str = groupBL.validate(group, groupId);
		str = validarExisteEnAD(group.getNombre());
		if (!str.isEmpty()) {
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error",
			// str));
			Utils.showError(str);
			return "";
		}

		try {
			if (!edit) {
				groupBL.saveGroupRole(group, idRole);
				controlerBitacora.insert(DescriptorBitacora.GRUPO, group.getGrupoId() + "", group.getNombre());
			} else {
				int id = Integer.parseInt(groupId);
				group.setGrupoId(id);
				groupBL.updateGroup(group, idRole);
				controlerBitacora.update(DescriptorBitacora.GRUPO, group.getGrupoId() + "", group.getNombre());
			}
			listGroup = groupBL.getGroups();
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_INFO, "Información",
			// "Se guardo correctamente"));
			Utils.showInfo("Se guardo correctamente.");
			newGroup();
		} catch (Exception e) {
			log.error("[saveGroup] error al momento de modificar o guardar: " + group.getNombre() + " " + e);
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
			// "error al guardar en la BD"));
			Utils.showError("Error al guardar en la Base de Datos.");

		}
		return "";
	}

	public void editRoleGroup() {
		String Idstr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("groupId");
		int id = Integer.parseInt(Idstr);
		group = groupBL.getGroup(id);
		select = group.getRol().getRolId() + "";
		groupId = Idstr;
		edit = true;
	}

	public void deleteRoleGroup() {
		String Idstr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("groupId");
		int id = Integer.parseInt(Idstr);
		try {
			group = groupBL.getGroup(id);
			groupBL.deleteGroup(id);
			controlerBitacora.delete(DescriptorBitacora.GRUPO, group.getGrupoId() + "", group.getNombre());
			listGroup = groupBL.getGroups();
			newGroup();
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_INFO, "Información",
			// "Se elimino correctamente"));
			Utils.showInfo("Se elimino correctamente.");
		} catch (Exception e) {
			log.error("[deleteRoleGroup]  error al eliminar el menu id:" + Idstr + "  " + e);
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Notificación",
			// "Error al eliminar"));
			Utils.showError("Fallo al eliminar.");
		}
	}

	public void newGroup() {
		edit = false;
		group = new GrupoAd();
		select = "-1";
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String GroupId) {
		this.groupId = GroupId;
	}

	public Boolean getEdit() {
		return edit;
	}

	public void setEdit(Boolean edit) {
		this.edit = edit;
	}

	public GrupoAd getGroup() {
		return group;
	}

	public void setGroup(GrupoAd Group) {
		this.group = Group;
	}

	public List<SelectItem> getSelectItems() {
		return selectItems;
	}

	public void setSelectItems(List<SelectItem> selectItems) {
		this.selectItems = selectItems;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public List<GrupoAd> getListGroup() {
		return listGroup;
	}

	public void setListGroup(List<GrupoAd> listGroup) {
		this.listGroup = listGroup;
	}

	private String validarExisteEnAD(String nameNewGrupo) {
		try {
			if (!ActiveDirectory.validarGrupo(nameNewGrupo)) {
				return "No existe un grupo  '" + nameNewGrupo + "' en Active Directory";
			}
		} catch (Exception e) {
			log.error("[validarExisteEnAD] Error al intentar verificar si grupo existe en AD  nombre=" + nameNewGrupo);
			return "No se puede comprobar si este grupo existe en Active Directory";
		}
		return "";
	}

}
