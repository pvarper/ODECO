package micrium.user.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.apache.commons.lang.StringEscapeUtils;

import micrium.user.model.Usuario;

@Named
public class UsuarioDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public void save(Usuario dato) throws Exception {
		transaction.begin();
		entityManager.persist(dato);
		transaction.commit();
	}

	public Usuario get(int id) throws Exception {
		return entityManager.find(Usuario.class, id);
	}

	public void update(Usuario dato) throws Exception {
		transaction.begin();
		entityManager.merge(dato);
		transaction.commit();
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> getList() throws Exception {
		return entityManager.createQuery("SELECT us FROM Usuario us WHERE us.estado=true Order by us.login").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> getListNoAdmin() throws Exception {
		return entityManager.createQuery("SELECT us FROM Usuario us WHERE us.login<>'admin' and us.estado=true Order by us.login").getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<Usuario> getListRol(String s) throws Exception {
		return entityManager.createQuery("SELECT us FROM Usuario us WHERE us.rol.rolId="+StringEscapeUtils.escapeSql(s)+" and us.estado=true").getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<Usuario> getListRolCoorEspecial(String s) throws Exception {
		return entityManager.createQuery("SELECT us FROM Usuario us WHERE us.rol.rolId="+StringEscapeUtils.escapeSql(s)+" and us.estado=true and us.especial=true").getResultList();
	}
		

	@SuppressWarnings("unchecked")
	public List<Usuario> getList(int idRol) throws Exception {

		String consulta = "SELECT us FROM Usuario us WHERE us.rol.rolId = :idRol and us.rol.estado = true and us.estado=true";
		Query qu = entityManager.createQuery(consulta).setParameter("idRol", idRol);
		List<Usuario> lista = qu.getResultList();
		return lista;
	}

	@SuppressWarnings("unchecked")
	public Usuario getUsuarioLogin(String login) {

		String consulta = "SELECT us FROM Usuario us WHERE us.login = :login and us.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("login", login);
		List<Usuario> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}
	@SuppressWarnings("unchecked")
	public List<Usuario> getListResponsables(String id) throws Exception {
		return entityManager.createQuery("select us from Usuario us WHERE us.usuario.usuarioId="+StringEscapeUtils.escapeSql(id)+" and us.usuario.usuarioId<>us.usuarioId and us.estado='t'").getResultList();
	}

}
