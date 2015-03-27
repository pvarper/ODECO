package micrium.odeco.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import micrium.odeco.model.Calendario;

import org.apache.log4j.Logger;


@Named
public class ConfiguracionCalendarioDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static Logger log = Logger.getLogger(ConfiguracionCalendarioDAO.class);

	@PersistenceContext
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public synchronized void save(Calendario dato)throws Exception {

			log.debug("[save] se va guardar la configuracion de calendario :" +dato.toString());
			transaction.begin();
			entityManager.persist(dato);
			transaction.commit();
			log.debug("[save] se guardo la configuracion de calendario:" +dato.toString());
	
		
	}

	public synchronized Calendario get(int id) {
		return entityManager.find(Calendario.class, id);
	}

	public synchronized void update(Calendario dato) throws Exception{

			log.debug("[update] se va actualizar la configuracion de calendario :" +dato.toString());
			transaction.begin();
			entityManager.merge(dato);
			transaction.commit();
			log.debug("[update] se actualizo la configuracion de calendario :" +dato.toString());
	
		
	}
	public synchronized void remove(Calendario dato) throws Exception{

			log.debug("[remove] se va eliminar la configuracion de calendario :" +dato.toString());
			transaction.begin();
			entityManager.remove(entityManager.merge(dato));
			transaction.commit();
			log.debug("[remove] se elimino la configuracion de calendario :" +dato.toString());
	
	}

	@SuppressWarnings("unchecked")
	public synchronized List<Calendario> getList()  {
		return entityManager.createQuery("SELECT us FROM Calendario us  Order by us.calendarioId desc").getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public synchronized List<Calendario> getCalendarioCiudad(String objetoReclamo) {

		String consulta = "SELECT us FROM Calendario us WHERE us.ciudad =:objetoReclamo";
		Query qu = entityManager.createQuery(consulta).setParameter("objetoReclamo", objetoReclamo);		
		return qu.getResultList();

	}

	@SuppressWarnings("unchecked")
	public synchronized Calendario getCalendarioFecha(String objetoReclamo) {

		String consulta = "SELECT us FROM Calendario us WHERE us.fecha =:objetoReclamo";
		Query qu = entityManager.createQuery(consulta).setParameter("objetoReclamo", objetoReclamo);
		List<Calendario> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}

}
