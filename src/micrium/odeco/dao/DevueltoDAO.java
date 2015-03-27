package micrium.odeco.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

import micrium.odeco.model.Devuelto;

@Named
public class DevueltoDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private transient EntityManager entityManager;
	
	public static Logger log = Logger.getLogger(DevueltoDAO.class);

	@Resource
	private transient UserTransaction transaction;

	public void save(Devuelto dato) throws Exception {
		log.debug("[save] se va guardar el devuelto :" +dato.toString());
		transaction.begin();
		entityManager.persist(dato);
		transaction.commit();
		log.debug("[save] se va guardo el devuelto :" +dato.toString());
	}

	public Devuelto get(int id) throws Exception {
		return entityManager.find(Devuelto.class, id);
	}

	public void update(Devuelto dato) throws Exception {
		log.debug("[update] se va actualizar el devuelto :" +dato.toString());
		transaction.begin();
		entityManager.merge(dato);
		transaction.commit();
		log.debug("[update] se actualizo el devuelto :" +dato.toString());
	}
	
	public void delete(Devuelto dato) throws Exception {
		log.debug("[delete] se va eleminar el devuelto :" +dato.toString());
		transaction.begin();
		entityManager.remove(entityManager.merge(dato));
		transaction.commit();
		log.debug("[delete] se elimino el devuelto :" +dato.toString());
	}

	@SuppressWarnings("unchecked")
	public List<Devuelto> getList() throws Exception {
		return entityManager.createQuery("SELECT us FROM Devuelto us Order by us.devuelvtoId").getResultList();
	}
	
	

	@SuppressWarnings("unchecked")
	public Devuelto getDevuelto(int idform) {
		String consulta = "SELECT us FROM Devuelto us WHERE us.formularioOdeco.formularioOdecoId =:ciudad ";
		Query qu = entityManager.createQuery(consulta).setParameter("ciudad", idform);
		List<Devuelto> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}

}
