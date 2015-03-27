package micrium.odeco.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.odeco.dao.ConfiguracionCalendarioDAO;
import micrium.odeco.model.Calendario;
import micrium.util.Parameter;



@Named
public class ConfiguracionCalendarioBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ConfiguracionCalendarioDAO dao;

	public void save(Calendario ciudad)throws Exception {
		 dao.save(ciudad);
	}

	public void update(Calendario ciudad) throws Exception{

		 dao.update(ciudad);
	}

	public void delete(int idCiudad)throws Exception {
		Calendario user = dao.get(idCiudad);
		 dao.remove(user);
	}

	public List<Calendario> getCalendarioList() {
		return dao.getList();
	}

	public List<Calendario> getCalendarioListCiudad(String ciudad)  {
		return dao.getCalendarioCiudad(ciudad);
	}

	public Calendario getCalendario(int idUser)  {
		return dao.get(idUser);
	}
	public Calendario getCalendarioFecha(String nombre) {
		return dao.getCalendarioFecha(nombre);
	}

	public String validate(Calendario dev) {
		if (dev.getCiudad()==null||dev.getCiudad().equalsIgnoreCase("-1"))
			return "Debe ingresar una ciudad";
		if (dev.getFecha()==null)
			return "Debe ingresar una fecha";
		if (dev.getMotivo()==null||dev.getMotivo().isEmpty()||!dev.getMotivo().matches(Parameter.expresionRegular))
			return "Caracteres invalidos en el campo motivo";
		return "";
	}



}
