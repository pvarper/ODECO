package micrium.odeco.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import micrium.odeco.model.ObjetoReclamo;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;


@Named
public class ObjetoReclamoDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ObjetoReclamoDAO.class);

	@PersistenceContext
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public void save(ObjetoReclamo dato)throws Exception {

			log.debug("[save] se va guardar el objeto reclamo "+dato.toString());
			transaction.begin();
			entityManager.persist(dato);
			transaction.commit();
			log.debug("[save] se guardo el objeto reclamo "+dato.toString());


	
	}

	public ObjetoReclamo get(int id) {
		
			return entityManager.find(ObjetoReclamo.class, id);
	
		
	}

	public void update(ObjetoReclamo dato)  throws Exception{

			log.debug("[update] se va actualizar el objeto reclamo "+dato.toString());
			transaction.begin();
			entityManager.merge(dato);
			transaction.commit();
			log.debug("[update] se actualizo el objeto reclamo "+dato.toString());
	
		
	}

	@SuppressWarnings("unchecked")
	public List<ObjetoReclamo> getList()  {
		return entityManager.createQuery("SELECT us FROM ObjetoReclamo us WHERE us.estado=true Order by us.objetoReclamoId desc").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getList(int idform)  {
		try {
			return entityManager.createNativeQuery("SELECT * FROM formulario_objeto_reclamo WHERE formulario_odeco_id="+StringEscapeUtils.escapeSql(String.valueOf(idform)) +" Order by formulario_odeco_id").getResultList();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public ObjetoReclamo getObjetoReclamoNombre(String objetoReclamo) {

		String consulta = "SELECT us FROM ObjetoReclamo us WHERE us.objetoReclamo =:objetoReclamo and us.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("objetoReclamo", objetoReclamo);
		List<ObjetoReclamo> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}

}
