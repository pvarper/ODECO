package micrium.odeco.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;







import micrium.odeco.dao.TipoFormularioDAO;
import micrium.odeco.model.TipoFormulario;


@Named
public class TipoFormularioBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TipoFormularioDAO dao;

	public void saveTipoFormulario(TipoFormulario ciudad) throws Exception{
		 dao.save(ciudad);
	}

	public void updateTipoFormulario(TipoFormulario ciudad) throws Exception {

		 dao.update(ciudad);
	}

	public void deleteTipoFormulario(int idCiudad) throws Exception{
		TipoFormulario user = dao.get(idCiudad);
		user.setEstado(false);
		dao.update(user);
	}

	public List<TipoFormulario> getList()  {
		return dao.getList();
	}

	

	public TipoFormulario getTipoFormulario(int idUser) {
		return dao.get(idUser);
	}
	
	public TipoFormulario getTipoFormularioDescripcion(String nombre) {
		return dao.getCiudadNombre(nombre);
	}





}
