package micrium.odeco.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.odeco.dao.DevueltoDAO;
import micrium.odeco.model.Devuelto;

import org.apache.log4j.Logger;


@Named
public class DevueltoBL implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(DevueltoBL.class);
	@Inject
	private DevueltoDAO dao;

	public void saveDevuelto(Devuelto ciudad) throws Exception {
		dao.save(ciudad);
	}

	public void updateDevuelto(Devuelto ciudad) throws Exception {

		dao.update(ciudad);
	}

	public void deleteUser(int iddevuelto) throws Exception {	
		Devuelto d=dao.getDevuelto(iddevuelto);
		if(d==null){
			log.error("[deleteUser] no se pudo eliminar el devuelto :"+iddevuelto);
			return;
		}
		dao.delete(d);
	}

	public List<Devuelto> getList() throws Exception {
		return dao.getList();
	}

	

	public Devuelto getDevuelto(int idUser) throws Exception {
		return dao.get(idUser);
	}
	
	public Devuelto getDevueltoForm(int nombre) throws Exception {
		return dao.getDevuelto(nombre);
	}





}
