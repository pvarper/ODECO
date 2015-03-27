package micrium.odeco.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.odeco.dao.AdjuntoDAO;
import micrium.odeco.model.Adjunto;


@Named
public class AdjuntoBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AdjuntoDAO dao;

	public void saveAdjunto(Adjunto ciudad)throws Exception {
		dao.save(ciudad);
		//aqui trabajo 12344
	}

	public void updateAdjunto(Adjunto ciudad)  throws Exception{
		//aqui casa1
		 dao.update(ciudad);
	}

	public void deleteUser(int idCiudad)throws Exception {
		Adjunto user = dao.get(idCiudad);
		user.setEstado(false);
		 dao.update(user);
	}

	public List<Adjunto> getList()  {
		return dao.getList();
	}

	

	public Adjunto getCiudad(int idUser) {
		return dao.get(idUser);
	}
	
	public List<Adjunto> getCiudadNombre(Integer nombre)  {
		return dao.getCiudadNombre(nombre);
	}


	public Adjunto getListAdjuntoId(Integer nombre)  {
		return dao.getListAdjuntoId(nombre);
	}


}
