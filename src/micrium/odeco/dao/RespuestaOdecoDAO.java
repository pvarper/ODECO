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

import micrium.odeco.model.RespuestaOdeco;
import micrium.odeco.view.RespuestaOdecoBean;

@Named
public class RespuestaOdecoDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private transient EntityManager entityManager;

	public static Logger log = Logger.getLogger(RespuestaOdecoBean.class);
	
	@Resource
	private transient UserTransaction transaction;

	public void save(RespuestaOdeco dato)throws Exception {

			log.debug("[save] se va guardar la respuesta Odeco "+dato.toString());
			transaction.begin();
			entityManager.persist(dato);
			transaction.commit();
			log.debug("[save] se guardo la respuesta Odeco "+dato.toString());

		
	}

	public RespuestaOdeco get(int id) {
		return entityManager.find(RespuestaOdeco.class, id);
	}

	public void update(RespuestaOdeco dato) throws Exception {

			log.debug("[update] se va actualizar la respuesta Odeco "+dato.toString());
			transaction.begin();
			entityManager.merge(dato);
			transaction.commit();
			log.debug("[update] se actualizo la respuesta Odeco "+dato.toString());
	
		
	}

	@SuppressWarnings("unchecked")
	public List<RespuestaOdeco> getList()  {
		return entityManager.createQuery("SELECT us FROM RespuestaOdeco us WHERE us.estado=true Order by us.respuestaOdecoId desc").getResultList();
	}
	

	@SuppressWarnings("unchecked")
	public RespuestaOdeco getRespuetsaOdecoNombre(String objetoReclamo) {

		String consulta = "SELECT us FROM RespuestaOdeco us WHERE us.respuestaOdeco =:objetoReclamo and us.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("objetoReclamo", objetoReclamo);
		List<RespuestaOdeco> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}

}
