package micrium.odeco.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.odeco.dao.ObjetoReclamoDAO;
import micrium.odeco.model.ObjetoReclamo;
import micrium.util.Parameter;


@Named
public class ObjetoReclamoBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ObjetoReclamoDAO dao;

	public void saveObjetoReclamo(ObjetoReclamo ciudad) throws Exception {
		 dao.save(ciudad);
	}

	public void updateObjetoReclamo(ObjetoReclamo ciudad) throws Exception {

		 dao.update(ciudad);
	}

	public void deleteObjetoReclamo(int idCiudad)throws Exception  {
		ObjetoReclamo user = dao.get(idCiudad);
		user.setEstado(false);
		 dao.update(user);
	}

	public List<ObjetoReclamo> getObjetosReclamosList() {
		return dao.getList();
	}
	public List<Object[]> getObjetosReclamosList(int idForm)  {
		return dao.getList(idForm);
	}

	

	public ObjetoReclamo getObjetoReclamo(int idUser)  {
		
		return dao.get(idUser);
	}
	
	public ObjetoReclamo getObjetoReclamoNombre(String nombre) {
		return dao.getObjetoReclamoNombre(nombre);
	}

	public String validate(ObjetoReclamo dev) {
		if (dev.getObjetoReclamo()==null||dev.getObjetoReclamo().isEmpty()|| !dev.getObjetoReclamo().matches(Parameter.expresionRegular))
			return "Debe ingresar un objeto reclamo, solo caracteres alfanumericos";
		if (!dev.getDescripcion().toString().matches(Parameter.expresionRegular))
			return "Caracteres invalidos en el campo descripcion";
		return "";
	}




}
