package micrium.odeco.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import micrium.odeco.model.Adjunto;

import org.apache.log4j.Logger;


@Named
public class AdjuntoDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private transient EntityManager entityManager;
	@Resource
	private transient UserTransaction transaction;
	
	public static Logger log = Logger.getLogger(AdjuntoDAO.class);

	public synchronized void save(Adjunto dato) throws Exception {

			log.debug("[save] se va guardar el adjunto :"+dato.toString());
			transaction.begin();
			entityManager.persist(dato);
			transaction.commit();
			log.debug("[save] se guardo el adjunto: "+dato.toString());
			
		
		
	}

	public synchronized Adjunto get(int id) {
		return entityManager.find(Adjunto.class, id);
		
	}

	public synchronized void update(Adjunto dato) throws Exception{
			log.debug("[update] se va actualizar el adjunto :"+dato.toString());
			transaction.begin();
			entityManager.merge(dato);
			transaction.commit();
			log.debug("[update] se actualizo el adjunto :"+dato.toString());
	
	}

	@SuppressWarnings("unchecked")
	public synchronized List<Adjunto> getList() {
		return entityManager.createQuery("SELECT us FROM Adjunto us WHERE us.estado=true Order by us.nombre").getResultList();
	}
	

	@SuppressWarnings("unchecked")
	public synchronized List<Adjunto> getCiudadNombre(Integer ciudad) {
		String consulta = "SELECT us FROM Adjunto us WHERE us.formularioOdeco.formularioOdecoId =:ciudad and us.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("ciudad", ciudad);
		
		return qu.getResultList();

	}
	
	public synchronized Adjunto getListAdjuntoId(Integer ciudad) {
		String consulta = "SELECT us FROM Adjunto us WHERE us.adjuntoId =:ciudad and us.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("ciudad", ciudad);
		
		return (Adjunto)qu.getResultList().get(0);

	}

}
