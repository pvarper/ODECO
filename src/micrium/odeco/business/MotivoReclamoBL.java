package micrium.odeco.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.odeco.dao.MotivoReclamoDAO;
import micrium.odeco.model.MotivoReclamo;
import micrium.util.Parameter;



@Named
public class MotivoReclamoBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MotivoReclamoDAO dao;

	public void saveMotivoReclamo(MotivoReclamo ciudad) throws Exception {
		 dao.save(ciudad);
	}

	public void updateMotivoReclamo(MotivoReclamo ciudad)  throws Exception{

		 dao.update(ciudad);
	}

	public void deleteMotivoReclamo(int idCiudad)throws Exception {
		MotivoReclamo user = dao.get(idCiudad);
		user.setEstado(false);
		 dao.update(user);
	}

	public List<MotivoReclamo> getMotivoReclamosList()  {
		return dao.getList();
	}
	
	public List<Object[]> getMotivoReclamosList(int idform) {
		return dao.getList(idform);
	}

	

	public MotivoReclamo getMotivoReclamo(int idUser)  {
		return dao.get(idUser);
	}
	public MotivoReclamo getMotivoReclamoNombre(String nombre) {
		return dao.getMotivoReclamoNombre(nombre);
	}
	public List<MotivoReclamo> getListCoordinador(String coordinador)  {
		return dao.getListCoordinador(coordinador);
	}
	
	public String validate(MotivoReclamo dev) {
		if (dev.getMotivoReclamo()==null||dev.getMotivoReclamo().isEmpty()|| !dev.getMotivoReclamo().matches(Parameter.expresionRegular))
			return "Debe ingresar un motivo reclamo, solo caracteres alfanumericos";
		if (dev.getTiempoResolucion()==0  || !dev.getTiempoResolucion().toString().matches(Parameter.expresionRegularNumero))
			return "Debe ingresar un tiempo de resolucion, solo caracteres numericos";
		if(!dev.getDescripcion().isEmpty()){
			if (!dev.getDescripcion().toString().matches(Parameter.expresionRegular))
				return "Caracteres invalidos en el campo descripcion";
		}
		
		return "";
	}



}
