package micrium.odeco.business;

import java.io.Serializable;





import javax.inject.Inject;
import javax.inject.Named;





import micrium.odeco.dao.ColaCoordinadorDAO;
import micrium.odeco.model.ColaCoordinador;


@Named
public class ColaCoordinadorBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ColaCoordinadorDAO dao;

	public void save(ColaCoordinador ciudad) throws Exception {
		dao.save(ciudad);
	}

	public void update(ColaCoordinador ciudad) throws Exception {

		dao.update(ciudad);
	}

	public void deleteUser(int idCiudad) throws Exception {
		ColaCoordinador user = dao.find(idCiudad);
		dao.eliminar(user);
	}
	
	public void remove(ColaCoordinador idCiudad) throws Exception {
		dao.remove(idCiudad);
	}
	public ColaCoordinador find(String nombre) throws Exception {
		return dao.getCoordinadorNombre(nombre);
	}

	public ColaCoordinador getCoordinador() {
		try {
			return dao.getLista().get(0);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	
	public void incrementarCantidad(ColaCoordinador a) throws Exception {
		Long cantidad=dao.getCantidad(a.getColaCoordinadorId());
		cantidad=cantidad+1;
		a.setCantidad((int) (long) cantidad);
		dao.update(a);
	}

	



}
