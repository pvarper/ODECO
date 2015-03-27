package micrium.user.controler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import micrium.util.ActiveDirectory;
import micrium.util.Parameter;

import org.apache.log4j.Logger;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

@ManagedBean
@SessionScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	ControlLoginImpl controler;
	
	/*@Inject
	ControlParametro controlerParametro;*/
	
	private String userSalud;
	private String userId;
	private String password;

	private String messageError;

	private MenuModel model;

	private static final Logger log = Logger.getLogger(Login.class);

	public Login() {
		
		if (Parameter.swActive.equals("false"))
		{
			this.userId = "admin";
			this.password = "a";
		}
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String verifyLogin() {

		String str = controler.validateIngreso(userId, password);
		if (!str.isEmpty()) {
			messageError = str;
			return "";

		}

		int valid = (Parameter.swActive.equals("true")) ? ActiveDirectory.existUser(userId, password) : controler.existUser(userId, password);
		if (valid == ActiveDirectory.EXIT_USER) {

			List groups = (Parameter.swActive.equals("true")) ? ActiveDirectory.getGroups(userId) : controler.getListaGrupo();

			if (groups.size() > 0) {

				int idRol = controler.getIdRole(userId, groups);
				log.info("[verifyLogin] el rol Id:" + idRol);
				if (idRol > 0) {

					HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
					request.getSession().setAttribute("TEMP$USER_NAME", userId);
					String remoteAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
					controler.addBitacoraLogin(userId, remoteAddr, idRol);
					userSalud = "Bienvenido: " + userId;
					cargar(idRol);
					return "/view/Menu.xhtml";
				} else {
					messageError = "Usted no esta registrado para ningun Rol";
					return "";
				}

			} else {
				messageError = "Usted no tiene grupo de trabajo";
				return "";
			}
		} else {
			str = controler.controlError(userId);
			messageError = str;
			return "";
		}
	}
	/*private void cargar(long idRole) {
		//Usuario user = userN.getUsuarioActual();
		model = new DefaultMenuModel();
		List<NodoMenu> listaNM = controler.getListaMenu(idRole);
		Map<String, Submenu> mapTreeNode = new HashMap<String, Submenu>();
		for (NodoMenu nodoMenu : listaNM) {
			int tipo = nodoMenu.getTipo();
			if (tipo == ControlLoginImpl.MENU_MENU) {
				DefaultSubMenu submenu = new DefaultSubMenu();
				submenu.setLabel(nodoMenu.getName());
				submenu.setRendered(nodoMenu.isSwRendered());				
				mapTreeNode.put(nodoMenu.getName(), submenu);
				model.addElement(submenu);
			} else {
				String path = nodoMenu.getPather();
				DefaultSubMenu submenuF = (DefaultSubMenu) mapTreeNode
						.get(path);
				if (tipo == ControlLoginImpl.MENU_RAIZ) {
					DefaultSubMenu submenu = new DefaultSubMenu();
					//submenu.setStyle("height: auto; z-index: 1015; left: 0px; top: -1px; display: block;"); No es necesario
					submenu.setLabel(nodoMenu.getName());
					submenu.setRendered(nodoMenu.isSwRendered());
					mapTreeNode.put(nodoMenu.getName(), submenu);
					submenuF.getElements().add(submenu);
				} else {
					DefaultMenuItem item = new DefaultMenuItem();
					item.setValue(nodoMenu.getName());
					if (redirectToUnknow(nodoMenu.getUrl(), user.getDistrital())) {
						item.setUrl("/view/NoTienePermiso.xhtml");
					} else {
						item.setUrl(nodoMenu.getUrl());
					}
					item.setRendered(nodoMenu.isSwRendered());
					submenuF.getElements().add(item);
				}
			}
		}
		DefaultSubMenu submenu = new DefaultSubMenu();
		
		submenu.setLabel("Opciones");
		DefaultMenuItem item = new DefaultMenuItem();
		item.setValue("Manual de Usuario");
		String pathDoc = "/RecaudacionesSenasag/resources/MU_SISTEMA_RECAUDACIONES.pdf";
		String strUrlDoc = "window.open('" + pathDoc + "'); return false;";
		item.setOnclick(strUrlDoc);
		item.setIcon("ui-icon ui-icon-document");
		submenu.getElements().add(item);

		item = new DefaultMenuItem();
		item.setValue("Cerrar Sesion");
		item.setIcon("ui-icon ui-icon-close");
		item.setUrl("/Logout");
		submenu.getElements().add(item);
		model.addElement(submenu);

		item = new DefaultMenuItem();
		item.setValue(userSalud);
		model.addElement(item);
	}*/
	private void cargar(int idRole) {
		// *

		model = new DefaultMenuModel();
		List<NodoMenu> listaNM = controler.getListaMenu(idRole);
		Map<String, Submenu> mapTreeNode = new HashMap<String, Submenu>();
		for (NodoMenu nodoMenu : listaNM) {

			int tipo = nodoMenu.getTipo();
			if (tipo == ControlLoginImpl.MENU_MENU) {
				Submenu submenu = new Submenu();
				submenu.setLabel(nodoMenu.getName());
				submenu.setRendered(nodoMenu.isSwRendered());
				mapTreeNode.put(nodoMenu.getName(), submenu);
				model.addSubmenu(submenu);
			} else {

				String path = nodoMenu.getPather();
				Submenu submenuF = mapTreeNode.get(path);
				if (tipo == ControlLoginImpl.MENU_RAIZ) {
					Submenu submenu = new Submenu();
					submenu.setLabel(nodoMenu.getName());
					submenu.setRendered(nodoMenu.isSwRendered());
					mapTreeNode.put(nodoMenu.getName(), submenu);
					submenuF.getChildren().add(submenu);
				} else {
					MenuItem item = new MenuItem();
					item.setValue(nodoMenu.getName());
					item.setUrl(nodoMenu.getUrl());
					item.setRendered(nodoMenu.isSwRendered());
					submenuF.getChildren().add(item);
				}
			}
		}
		Submenu submenu = new Submenu();
		submenu.setLabel("Opciones");
		MenuItem item = new MenuItem();
		item.setValue("Manual de Usuario");
		String pathDoc = "/ODECO_DIGITAL/resources/MU_ODECO.pdf";
		String strUrlDoc = "window.open('" + pathDoc + "'); return false;";
		item.setOnclick(strUrlDoc);
		item.setIcon("ui-icon ui-icon-document");
		submenu.getChildren().add(item);

		item = new MenuItem();
		item.setValue("Cerrar Sesion");
		item.setIcon("ui-icon ui-icon-close");
		item.setUrl("/Logout");
		submenu.getChildren().add(item);
		model.addSubmenu(submenu);

		item = new MenuItem();
		item.setValue(userSalud);
		model.addMenuItem(item);
	}

	public MenuModel getModel() {
		return model;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserSalud() {
		return userSalud;
	}

	public void setUserSalud(String userSalud) {
		this.userSalud = userSalud;
	}

	public String getMessageError() {
		return messageError;
	}

}
