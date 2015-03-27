package micrium.user.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.dao.FormDAO;
import micrium.user.dao.GrupoAdDAO;
import micrium.user.dao.RoleDAO;
import micrium.user.dao.UsuarioDAO;
import micrium.user.model.Formulario;
import micrium.user.model.GrupoAd;
import micrium.user.model.Rol;
import micrium.user.model.RolFormulario;
import micrium.user.model.RolFormularioPK;
import micrium.user.model.Usuario;

import org.apache.log4j.Logger;

@Named
public class RoleBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private RoleDAO dao;

	@Inject
	private FormDAO formDao;

	@Inject
	private GrupoAdDAO grupoDao;

	@Inject
	private UsuarioDAO usuarioDao;

	private static Logger log = Logger.getLogger(RoleBL.class);

	public String validate(Rol role, String idStr) {
		log.debug("[validate]: Ingresando..");

		if (role.getNombre().isEmpty()) {
			return "El campo Nombre esta Vacio";
		}

		Rol rolAux = dao.getName(role.getNombre());
		if (rolAux == null)
			return "";

		if (idStr != null && !idStr.isEmpty()) {
			int id = Integer.parseInt(idStr);
			if (id == rolAux.getRolId()) {
				if (role.getNombre().equals(rolAux.getNombre()))
					return "";
			}

		}
		return "este nombre existe";
	}
	
	public String validate(String nombre){
		if(!nombre.matches("^[A-Za-z0-9_ ]*$")){
			return "no se permite el caracter especiales";
		}
		return "";
	}
	
	public void saveRole(Rol role) throws Exception {

		role.setEstado(true);
		dao.save(role);
		List<Formulario> lista = formDao.getList();

		for (Formulario formulario : lista) {

			RolFormularioPK rfk = new RolFormularioPK();
			rfk.setRolId(role.getRolId());
			rfk.setFormularioId(formulario.getFormularioId());
			RolFormulario rolFor = new RolFormulario();
			rolFor.setEstado(true);
			rolFor.setId(rfk);
			dao.saveRolForulario(rolFor);
		}

	}

	public void updateRole(Rol role) throws Exception {

		Rol roleAux = dao.get(role.getRolId());
		roleAux.setNombre(role.getNombre());
		roleAux.setDescripcion(role.getDescripcion());
		dao.update(roleAux);
	}

	public void updateRoleFormulario(RolFormulario roleForm) throws Exception {
		dao.updateRolForulario(roleForm);
	}

	public void deleteRole(int idRole) throws Exception {

		List<GrupoAd> listGrupo = grupoDao.getList(idRole);
		for (GrupoAd g : listGrupo) {
			g.setEstado(false);
			grupoDao.update(g);
		}

		List<Usuario> listUser = usuarioDao.getList(idRole);
		for (Usuario u : listUser) {
			u.setEstado(false);
			usuarioDao.update(u);
		}

		List<RolFormulario> listRolForm = dao.getRolFormularioDelete(idRole);
		for (RolFormulario rf : listRolForm) {
			rf.setEstado(false);
			dao.updateRolFormulario(rf);
		}

		Rol rol = dao.get(idRole);
		rol.setEstado(false);
		dao.update(rol);
	}
	
	public Rol getname(String name){
		return dao.getName(name);
	}
	
	public List<Rol> getRoles() {
		return dao.getList();
	}

	public Rol getRole(int idRole) {
		return dao.get(idRole);
	}

	public List<RolFormulario> getRolFormulario(int id) {
		return dao.getRolFormulario(id);
	}

	public void updateRoleFormularioList(List<String> listaAvil, int idRol) {
		try {

			List<RolFormulario> listRolFor = getRolFormulario(idRol);

			if (listRolFor != null) {
				Map<Integer, RolFormulario> mapRolForIdForm = new HashMap<Integer, RolFormulario>();
				Map<String, RolFormulario> mapRolForName = new HashMap<String, RolFormulario>();
				Map<String, RolFormulario> mapRolForNivel = new HashMap<String, RolFormulario>();

				List<RolFormulario> listRFAux = new ArrayList<RolFormulario>();
				List<RolFormulario> listRFDepende = new ArrayList<RolFormulario>();

				for (RolFormulario rolFor : listRolFor) {
					String str = rolFor.getFormulario().getNivel();

					if (str != null) {
						mapRolForName.put(rolFor.getFormulario().getNombre(), rolFor);
						mapRolForNivel.put(rolFor.getFormulario().getNivel(), rolFor);
						listRFAux.add(0, rolFor);
					} else
						listRFDepende.add(rolFor);

					mapRolForIdForm.put(rolFor.getFormulario().getFormularioId(), rolFor);
				}

				for (RolFormulario rolFor : listRFAux) {

					String name = rolFor.getFormulario().getNombre();
					if (listaAvil.indexOf(name) != -1) {

						RolFormulario rolForAux = mapRolForName.get(name);
						String str = rolForAux.getFormulario().getNivel();
						int k = str.lastIndexOf(".");
						if (k != -1) {
							String path = str.substring(0, k);
							rolForAux = mapRolForNivel.get(path);
							name = rolForAux.getFormulario().getNombre();
							if (listaAvil.indexOf(name) == -1) {
								listaAvil.add(name);
							}
						}
					}
				}

				try {

					for (RolFormulario rolFor : listRFAux) {
						String name = rolFor.getFormulario().getNombre();
						if (listaAvil.indexOf(name) != -1)
							rolFor.setEstado(Boolean.TRUE);
						else
							rolFor.setEstado(Boolean.FALSE);

						updateRoleFormulario(rolFor);
					}

					for (RolFormulario rolFor : listRFDepende) {

						RolFormulario rolForAux;
						do {
							rolForAux = mapRolForIdForm.get(rolFor.getFormulario().getDepende());
						} while (rolForAux.getFormulario().getDepende() != 0);

						rolFor.setEstado(rolForAux.getEstado());
						updateRoleFormulario(rolFor);
					}

				} catch (Exception e) {
					log.error("[updateRoleFormularioList] error al guardar los roles", e);
				}
			} else {
				log.error("[updateRoleFormularioList] Error al obtener la lista de Roles.");
			}

		} catch (Exception e) {
			log.error("[updateRoleFormularioList] error al guardar los roles", e);
		}

	}

}
