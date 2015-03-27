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

import micrium.odeco.model.MedioNotificacion;

@Named
public class MedioNotificacionDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private transient EntityManager entityManager;
	
	public static Logger log = Logger.getLogger(MedioNotificacionDAO.class);

	@Resource
	private transient UserTransaction transaction;

	public void save(MedioNotificacion dato)throws Exception{

			log.debug("[save] se va guardar el medio de notifificacion "+dato.toString());
			transaction.begin();
			entityManager.persist(dato);
			transaction.commit();
			log.debug("[save] se guardo el medio de notificacion "+dato.toString());
	
		
	}

	public MedioNotificacion get(int id) {
		return entityManager.find(MedioNotificacion.class, id);
	}

	public void update(MedioNotificacion dato)throws Exception{

			log.debug("[update] se va actualizar el medio de notifificacion "+dato.toString());
			transaction.begin();
			entityManager.merge(dato);
			transaction.commit();
			log.debug("[update] se actualizo el medio de notificacion "+dato.toString());

		
	}

	@SuppressWarnings("unchecked")
	public List<MedioNotificacion> getList(){
		return entityManager.createQuery("SELECT us FROM MedioNotificacion us WHERE us.estado=true Order by us.medioNotificacionId").getResultList();
	}
	

	@SuppressWarnings("unchecked")
	public MedioNotificacion getMedioNotificacionNombre(String objetoReclamo) {

		String consulta = "SELECT us FROM MedioNotificacion us WHERE us.descripcion =:objetoReclamo and us.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("objetoReclamo", objetoReclamo);
		List<MedioNotificacion> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}

}
