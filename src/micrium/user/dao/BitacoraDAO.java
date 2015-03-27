package micrium.user.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.apache.commons.lang.StringEscapeUtils;

import micrium.user.model.Bitacora;

@Named
public class BitacoraDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public void save(Bitacora dato) throws Exception {
		transaction.begin();
		entityManager.persist(dato);
		transaction.commit();
	}

	@SuppressWarnings("unchecked")
	public List<Bitacora> listBitacora() {
		return entityManager.createQuery("SELECT b FROM Bitacora b ORDER BY b.fecha DESC  LIMIT "+StringEscapeUtils.escapeSql("1000")).getResultList();
	}

}
