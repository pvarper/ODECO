package micrium.user.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.business.RoleBL;
import micrium.user.business.UsuarioBL;
import micrium.user.model.Rol;
import micrium.user.model.Usuario;
import micrium.util.DescriptorBitacora;
import micrium.util.Utils;

import org.apache.log4j.Logger;

//import micrium.tvtigo.util.MessageBitacora;

@Named
@javax.enterprise.context.RequestScoped
public class RoleForm {

	@Inject
	private RoleBL roleBL;

	@Inject
	private ControlerBitacora controlerBitacora;
	
	@Inject
	private UsuarioBL usuarioBL;

	private List<Rol> listRole;

	private Rol role = new Rol();
	private String roleId;
	private Boolean edit;

	public static Logger log = Logger.getLogger(RoleForm.class);

	@PostConstruct
	public void init() {

		try {
			role = new Rol();
			listRole = roleBL.getRoles();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public String saveRole() {
		log.debug("[saveRole]: Ingresando..");

		String str1 =roleBL.validate(role.getNombre());
		if(!str1.isEmpty()){
			Utils.showError(str1);
			return "";
		}
		String str = roleBL.validate(role, roleId);
		if (!str.isEmpty()) {
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error",
			// str));
			Utils.showError(str);
			return "";
		}
		try {
			if (!edit) {
				roleBL.saveRole(role);
				controlerBitacora.insert(DescriptorBitacora.ROL, role.getRolId() + "", role.getNombre());
			} else {
				int id = Integer.parseInt(roleId);
				role.setRolId(id);
				roleBL.updateRole(role);
				controlerBitacora.update(DescriptorBitacora.ROL, role.getRolId() + "", role.getNombre());
			}
			listRole = roleBL.getRoles();
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_INFO, "Información",
			// "Se guardo correctamente"));
			Utils.showInfo("Se guardo correctamente.");
			newRole();
		} catch (Exception e) {
			log.error("[saveRole] error al momento de modificar o guardar: " + role.getRolId() + " " + e);
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
			// "error al guardar en la BD"));
			Utils.showError("Fallo al guardar en la Base de Datos.");

		}
		return "";
	}

	public void editRole() {
		String Idstr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("roleId");
		int id = Integer.parseInt(Idstr);
		role = roleBL.getRole(id);
		roleId = Idstr;
		edit = true;
	}

	public String deleteRole() {
		String Idstr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("roleId");
		int id = Integer.parseInt(Idstr);
		if (id == 1) {
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error",
			// "Este Rol no puede eliminarse es el rol de Adminstracion"));
			Utils.showError("Este Rol no puede eliminarse es el rol de Adminstracion.");
			return "";
		}
		
		
		try {
			List<Usuario> listaUsuarios=usuarioBL.getUsersRol(Idstr);
			if(!listaUsuarios.isEmpty()){
				Utils.showError("Este Rol no puede eliminarse por que tiene usuarios asociados");
				return "";	
			}
			
			role = roleBL.getRole(id);
			roleBL.deleteRole(id);
			controlerBitacora.delete(DescriptorBitacora.ROL, role.getRolId() + "", role.getNombre());
			listRole = roleBL.getRoles();
			newRole();
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_INFO, "Información",
			// "Se eliminó correctamente"));
			Utils.showInfo("Se elimino correctamente.");
		} catch (Exception e) {
			log.error("[deleteRole]  error al eliminar el menu id:" + Idstr + "  " + e);
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, "Notificación",
			// "Error al eliminar"));
			Utils.showError("Fallo al eliminar.");

		}
		return "";
	}

	public void newRole() {
		edit = false;
		role = new Rol();
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String RoleId) {
		this.roleId = RoleId;
	}

	public Boolean getEdit() {
		return edit;
	}

	public void setEdit(Boolean edit) {
		this.edit = edit;
	}

	public Rol getRole() {
		return role;
	}

	public void setRole(Rol Role) {
		this.role = Role;
	}

	public List<Rol> getListRole() {
		return listRole;
	}

	public void setListRole(List<Rol> listRole) {
		this.listRole = listRole;
	}
}
