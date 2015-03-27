package micrium.odeco.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import micrium.odeco.model.Flujo;

import org.apache.log4j.Logger;

@Named
public class FlujoDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Logger log = Logger.getLogger(FlujoDAO.class);
	
	@PersistenceContext
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public void save(Flujo dato) throws Exception{

			log.debug("[save] se va guardar el flujo :"+dato.toString());
			transaction.begin();
			entityManager.persist(dato);
			transaction.commit();
			log.debug("[save] se guardo el flujo :"+dato.toString());
		
		
	}

	public Flujo get(int id) {
		return entityManager.find(Flujo.class, id);
	}

	public void update(Flujo dato) throws Exception {

			log.debug("[update] se va actualizar el flujo " +dato.toString());
			transaction.begin();
			entityManager.merge(dato);
			transaction.commit();
			log.debug("[update] se actualizo el flujo "+dato.toString());
	
		
	}

	@SuppressWarnings("unchecked")
	public List<Flujo> getList()  {
		return entityManager.createQuery("SELECT us FROM Flujo us ").getResultList();
	}
	

	@SuppressWarnings("unchecked")
	public Flujo getFlujoNombre(String nombre) {
		String consulta = "SELECT us FROM Flujo us WHERE us.nombre =:nombre ";
		Query qu = entityManager.createQuery(consulta).setParameter("nombre", nombre);
		List<Flujo> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}

}
