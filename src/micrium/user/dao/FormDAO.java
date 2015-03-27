package micrium.user.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import micrium.user.model.Formulario;

@Named
public class FormDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private transient EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Formulario> getList() {
		return entityManager.createQuery("SELECT us FROM Formulario us").getResultList();
	}
}
