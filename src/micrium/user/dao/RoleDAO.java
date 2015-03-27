package micrium.user.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import micrium.user.model.Rol;
import micrium.user.model.RolFormulario;

@Named
public class RoleDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public void save(Rol dato) throws Exception {
		transaction.begin();
		entityManager.persist(dato);
		transaction.commit();
	}

	public void saveRolForulario(RolFormulario dato) throws Exception {

		transaction.begin();
		entityManager.persist(dato);
		transaction.commit();
	}

	public Rol get(int id) {
		return entityManager.find(Rol.class, id);
	}

	public void update(Rol dato) throws Exception {
		transaction.begin();
		entityManager.merge(dato);
		transaction.commit();
	}

	public void updateRolFormulario(RolFormulario dato) throws Exception {
		transaction.begin();
		entityManager.merge(dato);
		transaction.commit();
	}

	public void updateRolForulario(RolFormulario dato) throws Exception {

		transaction.begin();
		entityManager.merge(dato);
		transaction.commit();
	}

	@SuppressWarnings("unchecked")
	public List<Rol> getList() {
		return entityManager.createQuery("SELECT r FROM Rol r   WHERE  r.estado=true Order By r.nombre").getResultList();
	}

	@SuppressWarnings("unchecked")
	public Rol getName(String name) {

		String consulta = "SELECT r FROM Rol r WHERE r.nombre = :name and r.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("name", name);
		List<Rol> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<RolFormulario> getRolFormulario(int id) {
		String consulta = "FROM RolFormulario r WHERE r.rol.rolId = :id  " + "ORDER BY r.formulario.nivel";
		Query qu = entityManager.createQuery(consulta).setParameter("id", id);
		return qu.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<RolFormulario> getRolFormularioDelete(int idRol) {
		String consulta = "SELECT r FROM RolFormulario r WHERE r.rol.rolId = :idRol and r.rol.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("idRol", idRol);
		return qu.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<RolFormulario> getRolFormularioUser(int id) {
		String consulta = "SELECT r FROM RolFormulario r WHERE  r.rol.rolId = :id  " + "ORDER BY r.formulario.posicionColumna , r.formulario.posicionFila   ";
		Query qu = entityManager.createQuery(consulta).setParameter("id", id);
		return qu.getResultList();
	}

}
