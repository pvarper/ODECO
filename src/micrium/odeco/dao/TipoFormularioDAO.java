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

import micrium.odeco.model.TipoFormulario;


@Named
public class TipoFormularioDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;
	
	public static Logger log = Logger.getLogger(TipoFormularioDAO.class);

	public void save(TipoFormulario dato)throws Exception{

			log.debug("[save] se va guardar el tipo formulario "+dato.toString());
			transaction.begin();
			entityManager.persist(dato);
			transaction.commit();
			log.debug("[save] se guardo el tipo formulario "+dato.toString());
	
		
	}

	public TipoFormulario get(int id){
		return entityManager.find(TipoFormulario.class, id);
	}

	public void update(TipoFormulario dato) throws Exception {

			log.debug("[update] se va actualizar el tipo formulario "+dato.toString());
			transaction.begin();
			entityManager.merge(dato);
			transaction.commit();
			log.debug("[update] se actualizo el tipo formulario "+dato.toString());


		
	}

	@SuppressWarnings("unchecked")
	public List<TipoFormulario> getList()  {
		return entityManager.createQuery("SELECT us FROM TipoFormulario us WHERE us.estado=true Order by us.tipoFormularioId").getResultList();
	}
	

	@SuppressWarnings("unchecked")
	public TipoFormulario getCiudadNombre(String ciudad) {
		String consulta = "SELECT us FROM TipoFormulario us WHERE us.descripcion =:ciudad and us.estado = true";
		Query qu = entityManager.createQuery(consulta).setParameter("ciudad", ciudad);
		List<TipoFormulario> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}

}
