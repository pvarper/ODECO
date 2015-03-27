package micrium.odeco.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.odeco.dao.CiudadDAO;
import micrium.odeco.model.Ciudad;


@Named
public class CiudadBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CiudadDAO dao;

	public void saveCiudad(Ciudad ciudad)throws Exception {
		 dao.save(ciudad);
	}

	public void updateCiudad(Ciudad ciudad)throws Exception {

		 dao.update(ciudad);
	}

	public void deleteUser(int idCiudad)  throws Exception{
		Ciudad user = dao.get(idCiudad);
		user.setEstado(false);
		 dao.update(user);
	}

	public List<Ciudad> getList()  {
		return dao.getList();
	}

	

	public Ciudad getCiudad(int idUser)  {
		return dao.get(idUser);
	}
	
	public Ciudad getCiudadNombre(String nombre) {
		return dao.getCiudadNombre(nombre);
	}





}
