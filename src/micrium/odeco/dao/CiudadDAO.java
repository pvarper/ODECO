package micrium.odeco.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import micrium.odeco.model.Ciudad;

import org.apache.log4j.Logger;

@Named
public class CiudadDAO implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	public static Logger log = Logger.getLogger(CiudadDAO.class);
	
	@PersistenceContext
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public  void save(Ciudad dato) throws Exception  {

			log.debug("[save] se va guardar la ciudad :"+dato.toString());
			transaction.begin();
			entityManager.persist(dato);
			transaction.commit();
			log.debug("[save] se guardo la ciudad :"+dato.toString());
		
	}

	public  Ciudad get(int id)  {
		return entityManager.find(Ciudad.class, id);
	}

	public  void update(Ciudad dato) throws Exception{
			log.debug("[update] se va actualizar la ciudad :"+dato.toString());
			transaction.begin();
			entityManager.merge(dato);
			transaction.commit();
			log.debug("[update] se actualizo la ciudad :"+dato.toString());
		
		
	}

	@SuppressWarnings("unchecked")
	public  List<Ciudad> getList()  {
		return entityManager.createQuery("SELECT us FROM Ciudad us WHERE us.estado=true Order by us.nombre").getResultList();
	}
	

	@SuppressWarnings("unchecked")
	public Ciudad getCiudadNombre(String ciudad) {
		String consulta = "SELECT us FROM Ciudad us WHERE us.nombre =:ciudad and us.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("ciudad", ciudad);
		List<Ciudad> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}

}
