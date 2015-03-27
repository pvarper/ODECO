package micrium.user.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import micrium.user.model.GrupoAd;

@Named
public class GrupoAdDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public void save(GrupoAd dato) throws Exception {
		transaction.begin();
		entityManager.persist(dato);
		transaction.commit();
	}

	public GrupoAd get(int id) {
		return entityManager.find(GrupoAd.class, id);
	}

	public void update(GrupoAd dato) throws Exception {
		transaction.begin();
		entityManager.merge(dato);
		transaction.commit();
	}

	@SuppressWarnings("unchecked")
	public List<GrupoAd> getList() {
		return entityManager.createQuery("SELECT us FROM GrupoAd us WHERE  us.estado=true" + " Order By us.nombre").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<GrupoAd> getList(int idRol) {
		String sql = "SELECT us FROM GrupoAd us WHERE us.rol.rolId = :idRol and us.rol.estado = true and us.estado=true";
		Query qu = entityManager.createQuery(sql).setParameter("idRol", idRol);
		List<GrupoAd> lista = qu.getResultList();
		return lista;
	}

	@SuppressWarnings("unchecked")
	public GrupoAd getGroupName(String name) {
		String consulta = "SELECT us FROM GrupoAd us WHERE us.nombre = :name and us.estado = true and us.rol.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("name", name);
		List<GrupoAd> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}
}