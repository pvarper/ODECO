package micrium.user.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.odeco.model.Ciudad;
import micrium.odeco.model.Flujo;
import micrium.user.dao.GrupoAdDAO;
import micrium.user.dao.RoleDAO;
import micrium.user.dao.UsuarioDAO;
import micrium.user.model.GrupoAd;
import micrium.user.model.Rol;
import micrium.user.model.RolFormulario;
import micrium.user.model.Usuario;

@Named
public class UsuarioBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioDAO dao;

	@Inject
	private RoleDAO rolDao;
	
	@Inject
	private RoleBL rolBL;

	@Inject
	private GrupoAdDAO groupDao;

	public String validate(Usuario user, String idStr) throws Exception {

		if (user.getLogin().isEmpty()) {
			return "El campo Login esta Vacio";
		}

		Usuario usAux = dao.getUsuarioLogin(user.getLogin());
		if (usAux == null)
			return "";

		if (idStr != null && !idStr.isEmpty()) {
			int id = Integer.parseInt(idStr);
			if (id == usAux.getUsuarioId())
				if (user.getLogin().equals(usAux.getLogin()))
					return "";
		}
		return "este login existe";
	}

	public void saveUserRole(Usuario user, int idRole,int idCiudad,int idResponsable) throws Exception {
		Rol rol = new Rol();
		rol.setRolId(idRole);
		user.setRol(rol);
		Ciudad ciudad= new Ciudad();
		ciudad.setCiudadId(idCiudad);
		user.setCiudad(ciudad);
		if(idResponsable!=-2){
			Usuario responsable= new Usuario();
			responsable.setUsuarioId(idResponsable);
			user.setUsuario(responsable);
		}else{
			user.setUsuario(user);
		}
		user.setEstado(true);
		dao.save(user);
	}

	public void updateUser(Usuario user, int idRole,int idCiudad,int idResponsable,Flujo flujo) throws Exception {

		Usuario userAux = dao.get(user.getUsuarioId());
		userAux.setLogin(user.getLogin());
		userAux.setNombre(user.getNombre());
		userAux.setEspecial(user.getEspecial());
		userAux.setTelefono(user.getTelefono());
		Rol rol = new Rol();
		rol.setRolId(idRole);
		userAux.setRol(rol);
		
		Ciudad ciudad= new Ciudad();
		ciudad.setCiudadId(idCiudad);
		userAux.setCiudad(ciudad);
		
		if(idResponsable!=-2 ){
			Usuario responsable= new Usuario();
			responsable.setUsuarioId(idResponsable);
			userAux.setUsuario(responsable);
		}
		if((idResponsable==-2&&idRole==rolBL.getname(flujo.getCoordinador()).getRolId())){
			Usuario responsable= new Usuario();
			responsable.setUsuarioId(user.getUsuarioId());
			userAux.setUsuario(responsable);
		}
		if((idResponsable==-2&&idRole==rolBL.getname(flujo.getRegulacion()).getRolId())){
			Usuario responsable= new Usuario();
			responsable.setUsuarioId(user.getUsuarioId());
			userAux.setUsuario(responsable);
		}
		if((idResponsable==-2&&idRole==1)){
			Usuario responsable= new Usuario();
			responsable.setUsuarioId(user.getUsuarioId());
			userAux.setUsuario(responsable);
		}
	

		dao.update(userAux);
	}

	public void deleteUser(int idUser) throws Exception {
		Usuario user = dao.get(idUser);
		user.setEstado(false);
		dao.update(user);
	}

	public List<Usuario> getUsers() throws Exception {
		return dao.getList();
	}
	public List<Usuario> getUsersNoadmin() throws Exception {
		return dao.getListNoAdmin();
	}
	public List<Usuario> getUsersRol(String s) throws Exception {
		return dao.getListRol(s);
	}
	
	public List<Usuario> getListRolCoorEspecial(String s) throws Exception {
		return dao.getListRolCoorEspecial(s);
	}

	public Usuario getUser(int idUser) throws Exception {
		return dao.get(idUser);
	}
	
	public Usuario getUserLogin(String nombre) {
		return dao.getUsuarioLogin(nombre);
	}

	public List<String> getListFormUser(String login) throws Exception {

		int k = 1;
		Usuario user = dao.getUsuarioLogin(login);
		if (user != null)
			k = user.getRol().getRolId();

		List<RolFormulario> lista = rolDao.getRolFormularioUser(k);
		List<String> listaUrl = new ArrayList<String>();
		for (RolFormulario rolFormulario : lista) {
			listaUrl.add(rolFormulario.getFormulario().getUrl());
		}
		return listaUrl;
	}

	public List<RolFormulario> getRolFormularios(String login) throws Exception {
		int k = 1;
		Usuario user = dao.getUsuarioLogin(login);
		if (user != null)
			k = user.getRol().getRolId();

		return rolDao.getRolFormulario(k);

	}

	public int getIdRole(String login, List<Object> userGroups) {

		Usuario user = dao.getUsuarioLogin(login);
		System.out.println("user: " + user);

		if (user != null)
			return user.getRol().getRolId();

		for (Object object : userGroups) {
			GrupoAd gr = groupDao.getGroupName(object.toString());
			if (gr != null)
				return gr.getRol().getRolId();
		}

		return -1;
	}
	
	public List<Usuario> getListResponsables(String id) throws Exception {
		return dao.getListResponsables(id);
	}
}
