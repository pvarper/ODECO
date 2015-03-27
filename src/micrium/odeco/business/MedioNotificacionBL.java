package micrium.odeco.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;







import micrium.odeco.dao.MedioNotificacionDAO;
import micrium.odeco.model.MedioNotificacion;


@Named
public class MedioNotificacionBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MedioNotificacionDAO dao;

	public void save(MedioNotificacion ciudad)throws Exception  {
		 dao.save(ciudad);
	}

	public void update(MedioNotificacion ciudad) throws Exception{

		 dao.update(ciudad);
	}

	public void deleteMedioNotificacion(int idCiudad)throws Exception {
		MedioNotificacion user = dao.get(idCiudad);
		user.setEstado(false);
		 dao.update(user);
	}

	public List<MedioNotificacion> getLista()  {
		return dao.getList();
	}

	

	public MedioNotificacion getMedioNotificacionID(int idUser) {
		return dao.get(idUser);
	}
	public MedioNotificacion getMedioNotificacionNombre(String idUser)  {
		return dao.getMedioNotificacionNombre(idUser);
	}






}
