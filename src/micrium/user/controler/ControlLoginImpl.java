/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package micrium.user.controler;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.business.BitacoraBL;
import micrium.user.business.RoleBL;
import micrium.user.business.UsuarioBL;
import micrium.user.model.Bitacora;
import micrium.user.model.RolFormulario;

import org.apache.log4j.Logger;

@Named
public class ControlLoginImpl implements Serializable {

	private static final long serialVersionUID = 1L;

	public static int MENU_MENU = 1;
	public static int MENU_RAIZ = 2;
	public static int MENU_ITEM = 3;
	private static int nroOption = ParametersCM.NRO_INTENTOS;
	private static int timeOut = ParametersCM.MINUTOS_FUERA;

	@Inject
	private BitacoraBL logBL;

	@Inject
	private UsuarioBL userBL;

	@Inject
	private RoleBL roleBL;

	@Inject
	private ControlTimeOutImpl controlTA;

	private static Logger log = Logger.getLogger(ControlLoginImpl.class);

	public String validateIngreso(String login, String passWord) {

		if (login == null)
			return "EL USUARIO O CONTRASENA INGRESADOS SON INCORRECTOS";

		if (login.isEmpty())
			return "EL USUARIO O CONTRASENA INGRESADOS SON INCORRECTOS";

		if (passWord == null)
			return "EL USUARIO O CONTRASENA INGRESADOS SON INCORRECTOS";

		if (passWord.isEmpty())
			return "EL USUARIO O CONTRASENA INGRESADOS SON INCORRECTOS";

		try {
			if (controlTA != null) {
				if (controlTA.exisUserIngreso(login)) {
					log.info("existe:" + login);
					int cont = controlTA.countIntentoUserIngreso(login);
					log.info(nroOption + " contador:" + cont);
					if (nroOption == cont) {
						Date dateAct = new Date();
						Calendar cal = Calendar.getInstance();
						NodoIngresoUser user = controlTA.getNodoIngresoUser(login);
						cal.setTime(user.getDate());
						cal.add(Calendar.MINUTE, timeOut);
						Date dateUser = cal.getTime();
						if (dateAct.before(dateUser)) {
							String str = "EL USUARIO " + '"' + login + '"' + " HA SIDO BLOQUEADO.VUELVA A INTENTAR EN " + timeOut + " MINUTOS.";
							return str;
						} else {
							user.setCount(0);
							user.setDate(new Date());
						}
					}
				}
			} else {
				throw new Exception("controlTA is null");
			}
			return "";
		} catch (Exception e) {
			return "Error de obtener Login. " + e.getMessage();
		}

	}

	public String controlError(String login) {

		String str = "";
		if (controlTA.exisUserIngreso(login)) {
			controlTA.ingrementIntentoUser(login);
			NodoIngresoUser user = controlTA.getNodoIngresoUser(login);
			int countOp = nroOption;
			int cont = user.getCount();
			countOp = countOp - cont;

			if (countOp > 0)
				str = "EL USUARIO O CONTRASENA INGRESADOS SON INCORRECTOS";
			else {
				int nroTime = timeOut;
				str = "EL USUARIO " + '"' + login + '"' + " HA SIDO BLOQUEADO.VUELVA A INTENTAR EN " + nroTime + " MINUTOS.";
			}
		} else {
			controlTA.insertUserIngreso(login);
			str = "EL USUARIO O CONTRASENA INGRESADOS SON INCORRECTOS";
		}
		return str;

	}

	public int getIdRole(String userID, List<Object> userGroups) {
		return userBL.getIdRole(userID, userGroups);
	}

	public int existUser(String userId, String password) {

		String strLogin = ParametersCM.LOGIN;

		// System.out.println("login prop: " + strLogin);
		// System.out.println("pass prop: " + ParametersCM.PASSWORD_USER);

		boolean swLogin = userId.indexOf(strLogin) != -1 ? true : false;

		if (swLogin && password.equals(ParametersCM.PASSWORD_USER))
			return 1;
		else
			return 2;
	}

	public void addBitacoraLogin(String strIdUs, String address, int idRol) {

		Bitacora bitacora = new Bitacora();
		bitacora.setFormulario("INICIO");
		bitacora.setAccion("Ingreso al Sistema");
		bitacora.setUsuario(strIdUs);
		bitacora.setDireccionIp(address);
		Date t = new Date();
		Timestamp ts = new Timestamp(t.getTime());
		bitacora.setFecha(ts);
		logBL.save(bitacora);

		List<RolFormulario> lista = roleBL.getRolFormulario(idRol);

		NodoClient nd = new NodoClient();
		nd.setUser(strIdUs);
		nd.setAddressIp(address);
		nd.setTime(new Date().getTime());

		for (RolFormulario rolFormulario : lista) {
			log.info("[addBitacoraLogin] URL:" + rolFormulario.getFormulario().getUrl() + " estado:" + rolFormulario.getEstado());
			nd.addUrl(rolFormulario.getFormulario().getUrl(), rolFormulario.getEstado());
		}
		nd.addUrl("Menu.xhtml", true);
		controlTA.addNodoClient(nd);

	}

	public void salirBitacora(String strIdUs, String address) {

		Bitacora logg = new Bitacora();
		logg.setFormulario("");
		logg.setAccion("Salio del Sistema");
		logg.setUsuario(strIdUs);
		String addr = controlTA.getAddressIP(strIdUs);
		logg.setDireccionIp(address);
		logBL.save(logg);
		if (addr.equals(address))
			controlTA.remove(strIdUs);

	}

	public List<NodoMenu> getListaMenu(int idRole) {

		List<NodoMenu> listaNM = new ArrayList<NodoMenu>();

		Map<String, NodoMenu> mapTreeNode = new HashMap<String, NodoMenu>();
		List<RolFormulario> lista = roleBL.getRolFormulario(idRole);

		for (RolFormulario rolFor : lista) {

			String str = rolFor.getFormulario().getNivel();
			if (str != null)
				if (!str.isEmpty()) {
					int k = str.lastIndexOf(".");
					if (k == -1) {
						NodoMenu ndMenu = new NodoMenu();
						ndMenu.setName(rolFor.getFormulario().getNombre());
						ndMenu.setSwRendered(rolFor.getEstado());
						ndMenu.setTipo(MENU_MENU);
						mapTreeNode.put(str, ndMenu);
						listaNM.add(ndMenu);
					} else {

						String path = str.substring(0, k);
						NodoMenu ndMenuF = mapTreeNode.get(path);
						if (rolFor.getFormulario().getUrl().isEmpty()) {
							NodoMenu ndMenu = new NodoMenu();
							ndMenu.setName(rolFor.getFormulario().getNombre());
							ndMenu.setSwRendered(rolFor.getEstado());
							ndMenu.setPather(ndMenuF.getName());
							ndMenu.setTipo(MENU_RAIZ);
							mapTreeNode.put(str, ndMenu);
							listaNM.add(ndMenu);
						} else {
							NodoMenu item = new NodoMenu();
							item.setName(rolFor.getFormulario().getNombre());
							String url = "/view/" + rolFor.getFormulario().getUrl();
							item.setUrl(url);
							item.setSwRendered(rolFor.getEstado());
							item.setPather(ndMenuF.getName());
							item.setTipo(MENU_ITEM);
							listaNM.add(item);
						}
					}
				}
		}
		return listaNM;
	}

	public List<String> getListaGrupo() {

		List<String> groups = new ArrayList<String>();
		groups.add("Administradores");
		return groups;

	}

}
