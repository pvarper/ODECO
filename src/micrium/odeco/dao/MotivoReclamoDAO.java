package micrium.odeco.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import micrium.odeco.model.MotivoReclamo;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;


@Named
public class MotivoReclamoDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private transient EntityManager entityManager;
	
	public static Logger log = Logger.getLogger(MotivoReclamoDAO.class);

	@Resource
	private transient UserTransaction transaction;

	public void save(MotivoReclamo dato) throws Exception{

			log.debug("[save] se va guardar el motivo reclamo "+dato.toString());
			transaction.begin();
			entityManager.persist(dato);
			transaction.commit();
			log.debug("[save] se guardo el motivo reclamo "+dato.toString());
	
		
	}

	public MotivoReclamo get(int id)  {
		return entityManager.find(MotivoReclamo.class, id);
	}

	public void update(MotivoReclamo dato)throws Exception {

			log.debug("[update] se va actualizar el motivo reclamo "+dato.toString());
			transaction.begin();
			entityManager.merge(dato);
			transaction.commit();
			log.debug("[update] se actualizo el motivo reclamo"+dato.toString());
	
		
	}

	@SuppressWarnings("unchecked")
	public List<MotivoReclamo> getList()  {
		return entityManager.createQuery("SELECT us FROM MotivoReclamo us WHERE us.estado=true Order by us.motivoReclamoId desc").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<MotivoReclamo> getListCoordinador(String coordinador)  {
		return entityManager.createQuery("SELECT us FROM MotivoReclamo us WHERE us.estado=true and us.coordinador='"+StringEscapeUtils.escapeSql(coordinador) +"' Order by us.motivoReclamoId desc").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getList(int idform)  {
		try {
			return entityManager.createNativeQuery("SELECT * FROM formulario_motivo_reclamo WHERE formulario_odeco_id="+StringEscapeUtils.escapeSql(String.valueOf(idform)) +"Order by formulario_odeco_id").getResultList();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public MotivoReclamo getMotivoReclamoNombre(String objetoReclamo) {

		String consulta = "SELECT us FROM MotivoReclamo us WHERE us.motivoReclamo =:objetoReclamo and us.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("objetoReclamo", objetoReclamo);
		List<MotivoReclamo> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}

}
