package micrium.user.business;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.dao.GrupoAdDAO;
import micrium.user.model.GrupoAd;
import micrium.user.model.Rol;

@Named
public class GrupoAdBL {
	
	@Inject
	private  GrupoAdDAO dao;
		
	
	
	public String validate(GrupoAd user,String idStr){
		
		if(user.getNombre().isEmpty()){
			return "El campo Nombre esta Vacio";
		  }
		
		GrupoAd  usAux=dao.getGroupName(user.getNombre());
		if(usAux==null)
			return "";
		
		if(idStr!=null && !idStr.isEmpty()){
			int id=Integer.parseInt(idStr);
			if(id==usAux.getGrupoId())
				if(user.getNombre().equals(usAux.getNombre()))
					return "";
		}
		
		
		return "este nombre de grupo ya existe";
	}
	
	public  void saveGroupRole(GrupoAd group,int idRole)throws Exception{
		Rol rol=new Rol();
		rol.setRolId(idRole);
		group.setRol(rol);
		group.setEstado(true);
		dao.save(group);
	}
	public void updateGroup(GrupoAd group,int idRole)throws Exception{
		Rol rol=new Rol();
		rol.setRolId(idRole);
		
		GrupoAd  groupAux=dao.get(group.getGrupoId());
		groupAux.setNombre(group.getNombre());
		groupAux.setDetalle(group.getDetalle());
		groupAux.setRol(rol);
		dao.update(groupAux);
	}

	public void deleteGroup(int idGroup)throws Exception{
		GrupoAd  user=dao.get(idGroup);
		user.setEstado(false);
		dao.update(user);
	}
	public List<GrupoAd> getGroups(){
		 return dao.getList();
	}
	public GrupoAd getGroup(int idGroup){
		 return dao.get(idGroup);
	}
}