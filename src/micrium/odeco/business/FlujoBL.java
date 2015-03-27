package micrium.odeco.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.odeco.dao.FlujoDAO;
import micrium.odeco.model.Flujo;


@Named
public class FlujoBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FlujoDAO dao;

	public void saveFlujo(Flujo ciudad)throws Exception {
		 dao.save(ciudad);
	}

	public void updateFlujo(Flujo ciudad)throws Exception  {

		 dao.update(ciudad);
	}


	public List<Flujo> getList() {
		return dao.getList();
	}

	

	public Flujo getFlujo(int idUser)  {
		return dao.get(idUser);
	}
	
	public Flujo getFlujoNombre(String nombre) {
		return dao.getFlujoNombre(nombre);
	}





}
