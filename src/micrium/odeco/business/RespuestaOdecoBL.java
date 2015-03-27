package micrium.odeco.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.odeco.dao.RespuestaOdecoDAO;
import micrium.odeco.model.RespuestaOdeco;
import micrium.util.Parameter;


@Named
public class RespuestaOdecoBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private RespuestaOdecoDAO dao;

	public void save(RespuestaOdeco ciudad)throws Exception{
		 dao.save(ciudad);
	}

	public void update(RespuestaOdeco ciudad)throws Exception{

		 dao.update(ciudad);
	}

	public void deleteMedioNotificacion(int idCiudad) throws Exception{
		RespuestaOdeco user = dao.get(idCiudad);
		user.setEstado(false);
		 dao.update(user);
	}

	public List<RespuestaOdeco> getLista()  {
		return dao.getList();
	}

	

	public RespuestaOdeco getRespuestaOdecoID(int idUser)  {
		return dao.get(idUser);
	}
	public RespuestaOdeco getRespuestaOdecoNombre(String idUser){
		return dao.getRespuetsaOdecoNombre(idUser);
	}

	public String validate(RespuestaOdeco dev) {
		if (dev.getRespuestaOdeco()==null||dev.getRespuestaOdeco().isEmpty()|| !dev.getRespuestaOdeco().matches(Parameter.expresionRegular))
			return "Debe ingresar un objeto reclamo, solo caracteres alfanumericos";
		if (!dev.getDescripcion().toString().matches(Parameter.expresionRegular))
			return "Caracteres invalidos en el campo descripcion";
		return "";
	}




}
