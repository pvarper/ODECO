package micrium.odeco.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import micrium.odeco.model.ColaCoordinador;

import org.apache.log4j.Logger;

@Named
public class ColaCoordinadorDAO implements Serializable {
	public static Logger log = Logger.getLogger(ColaCoordinadorDAO.class);

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public  void save(ColaCoordinador object) throws Exception{
		log.debug("[save] se va guardar en la cola coordinador "+object.toString());
			transaction.begin();
			entityManager.persist(object);
			transaction.commit();
			log.debug("[save] se guardo en la cola coordinador "+object.toString());

		
	}

	public  void remove(ColaCoordinador object) throws Exception{
		log.debug("[remove] se va eliminar en la cola coordinador "+object.toString());
		transaction.begin();
		entityManager.remove(entityManager.merge(object));
		transaction.commit();
		log.debug("[remove] se va elimino en la cola coordinador "+object.toString());
	}

	public  ColaCoordinador find(Integer id) {
		return entityManager.find(ColaCoordinador.class, id);
	}

	
	public void update(ColaCoordinador object) throws Exception{

			log.info("[update] Actualizando objeto ODECO: " + object.toString());
			transaction.begin();
			entityManager.merge(object);
			transaction.commit();
			log.info("[update] actualizo objeto ODECO: " + object.toString());

	}

	@SuppressWarnings("unchecked")
	public  List<ColaCoordinador> getLista() {
		log.info("[getLista] Obtiendo la lista de Coordinadores ");
		return entityManager.createQuery("SELECT t FROM ColaCoordinador t where cantidad=(select min(cantidad) from ColaCoordinador)").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public  ColaCoordinador getCoordinadorNombre(String ciudad) {
		String consulta = "SELECT us FROM ColaCoordinador us WHERE us.nombreCoordinador =:ciudad ";
		Query qu = entityManager.createQuery(consulta).setParameter("ciudad", ciudad);
		List<ColaCoordinador> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}
	
	public  Long getCantidad(int id) {
		Long response = 0L;
		String sql = "select cantidad from ColaCoordinador where colaCoordinadorId="+id;
		Query query = entityManager.createQuery(sql);
		List<?> list = query.getResultList();
		if (list.size() == 0L) {
			response = 0L;
		} else {
			if (list.get(0) == null) {
				response = 0L;
			} else {
				String a= list.get(0).toString();
				response = Long.valueOf(a);
			}
		}
		return response;
	}
	
	public  void eliminar(ColaCoordinador comm) {
		try {
			transaction.begin();
			entityManager.remove(find(comm.getColaCoordinadorId()));
			transaction.commit();
		} catch (Exception i) {
			try {
				transaction.rollback();
			} catch (Exception e) {
				log.error("", e);
			}
			log.error("", i);
		}
	}

	
	

}
