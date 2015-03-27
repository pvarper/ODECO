package micrium.odeco.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import micrium.odeco.model.FormularioOdeco;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

@Named
public class FormularioOdecoDAO implements Serializable {
	public static Logger log = Logger.getLogger(FormularioOdecoDAO.class);

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	public synchronized void  save(FormularioOdeco object) throws Exception{
		
			log.debug("[save] se va guardar el formulario odeco "+object.toString());
			transaction.begin();
			entityManager.persist(object);
			transaction.commit();
			log.debug("[save] se guardo el formulario odeco "+object.toString());
	
		
	}

	public synchronized void remove(FormularioOdeco object) throws Exception{

			log.debug("[remove] se va eliminar el formulario odeco "+object.toString());
			transaction.begin();
			entityManager.remove(object);
			transaction.commit();
			log.debug("[remove] se elimino el formulario odeco "+object.toString());
		
		
	}

	public FormularioOdeco find(Integer id) {
		return entityManager.find(FormularioOdeco.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<FormularioOdeco> getLista() {
		log.info("[getLista] Obtiendo la lista de Formularios ODECO. ");
		return entityManager.createQuery("SELECT t FROM FormularioOdeco t ORDER BY id DESC").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<FormularioOdeco> getListaResponsable(String login) {
		log.info("[getLista] Obtiendo la lista de Formularios ODECO. ");
		return entityManager.createQuery("SELECT t FROM FormularioOdeco t where t.revisar='"+StringEscapeUtils.escapeSql(login) +"' ORDER BY t.formularioOdecoId DESC").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public FormularioOdeco getFormularioPI(String ciudad) {
		String consulta = "SELECT t FROM FormularioOdeco t where t.codigoReclamacion=:ciudad";
		Query qu = entityManager.createQuery(consulta).setParameter("ciudad", ciudad);
		List<FormularioOdeco> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}
	
	@SuppressWarnings("unchecked")
	public FormularioOdeco getFormularioPIidSegunda(int ciudad) {
		String consulta = "SELECT t FROM FormularioOdeco t where t.formularioOdeco.formularioOdecoId=:ciudad";
		Query qu = entityManager.createQuery(consulta).setParameter("ciudad", ciudad);
		List<FormularioOdeco> lista = qu.getResultList();
		return lista.isEmpty() ? null : lista.get(0);

	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getMotivosReclamos(String id) {
		String consulta="select t.motivo_reclamo_id from formulario_motivo_reclamo t where t.formulario_odeco_id="+StringEscapeUtils.escapeSql(id);
		List<Object[]> a=entityManager.createNativeQuery(consulta).getResultList();
		return a;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getObjetosReclamosDescripcion(int id) {
		String consulta="select mr.objeto_reclamo FROM objeto_reclamo mr INNER JOIN formulario_objeto_reclamo fmr on (mr.objeto_reclamo_id=fmr.objeto_reclamo_id) where fmr.formulario_odeco_id="+StringEscapeUtils.escapeSql(String.valueOf(id));
		List<Object[]> a=entityManager.createNativeQuery(consulta).getResultList();
		return a;
	}
	@SuppressWarnings("unchecked")
	public List<Object[]> getMotivosReclamosDescripcion(int id) {
		String consulta="select mr.motivo_reclamo FROM motivo_reclamo mr INNER JOIN formulario_motivo_reclamo fmr on (mr.motivo_reclamo_id=fmr.motivo_reclamo_id) where fmr.formulario_odeco_id="+StringEscapeUtils.escapeSql(String.valueOf(id));
		List<Object[]> a=entityManager.createNativeQuery(consulta).getResultList();

		return a;
	}
	
	public void update(FormularioOdeco object)throws Exception {

			log.debug("[update] Actualizando objeto ODECO: " + object.getCodigoReclamacionInstitucion()+"/"+object.getCodigoReclamacionCiudad()+"/"+object.getCodigoReclamacionCorrelativo());
			transaction.begin();
			entityManager.merge(object);
			transaction.commit();
			log.debug("[update] Se actualizo objeto ODECO: " + object.getCodigoReclamacionInstitucion()+"/"+object.getCodigoReclamacionCiudad()+"/"+object.getCodigoReclamacionCorrelativo());

	
		
	}
	public void eliminar(FormularioOdeco comm) throws Exception {

			log.debug("[remove] se va eliminar el formulario odeco "+comm.toString());
			transaction.begin();
			entityManager.remove(find(comm.getFormularioOdecoId()));
			transaction.commit();
			log.debug("[remove] se elimino el formulario odeco "+comm.toString());
	
	}

	public Long getMaxPosicion(String institucion, String ciudad) {
		Long response = 0L;
		String sql = "SELECT max(codigoReclamacionCorrelativo) FROM FormularioOdeco t where codigoReclamacionCiudad='"+StringEscapeUtils.escapeSql(ciudad)+"' and codigoReclamacionInstitucion='"+StringEscapeUtils.escapeSql(institucion)+"'";
		Query query = entityManager.createQuery(sql);
		List<?> list = query.getResultList();
		if (list.size() == 0L) {
			response = 0L;
		} else {
			if (list.get(0) == null) {
				response = 0L;
			} else {
				String a= list.get(0).toString();
				response = Long.valueOf(a);
			}
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> reclamosAtendidos(Timestamp fi,Timestamp ff){
		
		return entityManager.createNativeQuery("select formulario_odeco_id,fecha_reclamo  from formulario_odeco where '"+fi+"'<= to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') and to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') <='"+ff+"' GROUP BY formulario_odeco_id,fecha_reclamo").getResultList();		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> reclamosPlazoEstablecido(Timestamp fi,Timestamp ff){
		
		return entityManager.createNativeQuery("select formulario_odeco_id,fecha_reclamo  from formulario_odeco where '"+fi+"'<= to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') and to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') <='"+ff+"' and to_date(to_char(fecha_cerrado, 'yyyy-MM-dd'), 'yyyy-MM-dd') <=to_date(to_char(fecha_resolucion, 'yyyy-MM-dd'), 'yyyy-MM-dd') and id_tipo_formulario=1 and estado_respuesta<>'ANULADO' GROUP BY formulario_odeco_id,fecha_reclamo").getResultList();		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> reclamosPendientes(Timestamp fi,Timestamp ff){
		
		return entityManager.createNativeQuery("select formulario_odeco_id,fecha_reclamo from formulario_odeco where estado_respuesta!='CERRADO' and estado_respuesta!='ANULADO' and id_tipo_formulario=1 and '"+fi+"'<=to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') and to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') <='"+ff+"' GROUP BY formulario_odeco_id,fecha_reclamo").getResultList();
	
	}
	@SuppressWarnings("unchecked")
	public List<Object[]> reclamosAnulados(Timestamp fi,Timestamp ff){
		
		return entityManager.createNativeQuery("select formulario_odeco_id,fecha_reclamo from formulario_odeco where estado_respuesta='ANULADO' and id_tipo_formulario=1 and '"+fi+"'<=to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') and to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') <='"+ff+"' GROUP BY formulario_odeco_id,fecha_reclamo").getResultList();

		
		
	}
	@SuppressWarnings("unchecked")
	public List<Object[]> reclamosAdministrativos(Timestamp fi,Timestamp ff){
		
		return entityManager.createNativeQuery("select  formulario_odeco_id,fecha_reclamo from formulario_odeco where id_tipo_formulario=2 and '"+fi+"'<=to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') and to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') <='"+ff+"' GROUP BY formulario_odeco_id,fecha_reclamo").getResultList();
	
		
		
	}
	@SuppressWarnings("unchecked")
	public List<Object[]> antiguedadPendientes(Timestamp fi,Timestamp ff){
		
		return entityManager.createNativeQuery("select formulario_odeco_id,fecha_reclamo  from formulario_odeco where '"+fi+"'<=to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') and to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') <='"+ff+"' and fecha_cerrado >fecha_resolucion and id_tipo_formulario=1 GROUP BY formulario_odeco_id,fecha_reclamo").getResultList();		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> reporte2(Timestamp fi,Timestamp ff){
		
		return entityManager.createNativeQuery("SELECT MT.motivo_reclamo_id,"+
			" MR.motivo_reclamo,"+
			" MT.formulario_odeco_id,"+
			"FI.codigo_reclamacion,"+
			"FI.FECHA_RECLAMO,"+
				"CASE WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='01' THEN 'ENERO' "+
						"WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='02' THEN 'FEBRERO' "+
						"WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='03' THEN 'MARZO' "+
						"WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='04' THEN 'ABRIL' "+
						"WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='05' THEN 'MAYO' "+
						"WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='06' THEN 'JUNIO' "+
						"WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='07' THEN 'JULIO' "+
						"WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='08' THEN 'AGOSTO' "+
						"WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='09' THEN 'SEPTIEMBRE' "+
						"WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='10' THEN 'OCTUBRE' "+
						"WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='11' THEN 'NOVIEMBRE' "+
						"WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='12' THEN 'DICIEMBRE' "+
			" END AS MES "+
"from formulario_motivo_reclamo MT INNER JOIN motivo_reclamo MR ON(MR.motivo_reclamo_id=MT.motivo_reclamo_id)"+
																	" INNER JOIN formulario_odeco FI ON(FI.FORMULARIO_ODECO_ID=MT.FORMULARIO_ODECO_ID) "+
" where  TO_DATE(TO_CHAR(FI.fecha_reclamo,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN TO_DATE('"+fi+"','yyyy-MM-dd') and TO_DATE('"+ff+"','yyyy-MM-dd') "+
" order by FI.FECHA_RECLAMO").getResultList();		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> reporte4(Timestamp fi,Timestamp ff, int objetoReclamoId){
		
		return entityManager.createNativeQuery("SELECT OBJ.objeto_reclamo_id,"+
			 "OBJ.objeto_reclamo,"+
			 "MT.motivo_reclamo_id,"+
			 "MR.motivo_reclamo,"+
			 "MT.formulario_odeco_id,"+
			 "FI.codigo_reclamacion,"+
				" CASE WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='01' THEN 'ENERO' "+
						" WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='02' THEN 'FEBRERO'"+
						" WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='03' THEN 'MARZO'"+
						" WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='04' THEN 'ABRIL'"+
						" WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='05' THEN 'MAYO'"+
						" WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='06' THEN 'JUNIO'"+
						" WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='07' THEN 'JULIO'"+
						" WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='08' THEN 'AGOSTO'"+
						" WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='09' THEN 'SEPTIEMBRE'"+
						" WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='10' THEN 'OCTUBRE'"+
						" WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='11' THEN 'NOVIEMBRE'"+
						" WHEN TO_CHAR(FI.FECHA_RECLAMO,'MM')='12' THEN 'DICIEMBRE'"+
			 " END AS MES"+
" FROM objeto_reclamo OBJ INNER JOIN formulario_objeto_reclamo FROBJ ON(FROBJ.OBJETO_RECLAMO_ID=OBJ.objeto_reclamo_id)"+
												" INNER JOIN formulario_odeco FI ON(FI.FORMULARIO_ODECO_ID=FROBJ.FORMULARIO_ODECO_ID)"+
												" INNER JOIN formulario_motivo_reclamo MT ON(MT.FORMULARIO_ODECO_ID=FI.FORMULARIO_ODECO_ID)"+
												" INNER JOIN motivo_reclamo MR ON(MR.motivo_reclamo_id=MT.motivo_reclamo_id)"+
" WHERE OBJ.objeto_reclamo_id="+StringEscapeUtils.escapeSql(String.valueOf(objetoReclamoId))+" and"+
      " TO_DATE(TO_CHAR(FI.fecha_reclamo,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN TO_DATE('"+fi+"','yyyy-MM-dd') and TO_DATE('"+ff+"','yyyy-MM-dd')").getResultList();		
		
	}
	
	public Long cantidadFormularios(Timestamp fi,Timestamp ff, int id_ciudad){
		
		Long response = 0L;
		String sql = "SELECT count(*) FROM formulario_odeco where '"+fi+"'<=to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') and to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd')<='"+ff+"' and id_ciudad_reclamacion="+StringEscapeUtils.escapeSql(String.valueOf(id_ciudad));

		Query query = entityManager.createNativeQuery(sql);
		List<?> list = query.getResultList();
		if (list.size() == 0L) {
			response = 0L;
		} else {
			if (list.get(0) == null) {
				response = 0L;
			} else {
				String a= list.get(0).toString();
				response = Long.valueOf(a);
			}
		}
		return response;
	}
	public Long cantidadAbiertos(Timestamp fi,Timestamp ff, int id_ciudad){
		
		Long response = 0L;
		String sql = "SELECT count(*) FROM formulario_odeco where '"+fi+"'<=to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') and to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd')<='"+ff+"' and id_ciudad_reclamacion="+StringEscapeUtils.escapeSql(String.valueOf(id_ciudad))+" and estado_respuesta='ABIERTO'";
		Query query = entityManager.createNativeQuery(sql);
		List<?> list = query.getResultList();
		if (list.size() == 0L) {
			response = 0L;
		} else {
			if (list.get(0) == null) {
				response = 0L;
			} else {
				String a= list.get(0).toString();
				response = Long.valueOf(a);
			}
		}
		return response;
	}
	public Long cantidadCerrado(Timestamp fi,Timestamp ff, int id_ciudad){
	
	Long response = 0L;
	String sql = "SELECT count(*) FROM formulario_odeco where '"+fi+"'<=to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') and to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd')<='"+ff+"' and id_ciudad_reclamacion="+StringEscapeUtils.escapeSql(String.valueOf(id_ciudad))+" and estado_respuesta='CERRADO'";
	Query query = entityManager.createNativeQuery(sql);
	List<?> list = query.getResultList();
	if (list.size() == 0L) {
		response = 0L;
	} else {
		if (list.get(0) == null) {
			response = 0L;
		} else {
			String a= list.get(0).toString();
			response = Long.valueOf(a);
		}
	}
	return response;
}
	public Long cantidadAnulado(Timestamp fi,Timestamp ff, int id_ciudad){
	
	Long response = 0L;
	String sql = "SELECT count(*) FROM formulario_odeco where '"+fi+"'<=to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd') and to_date(to_char(fecha_reclamo, 'yyyy-MM-dd'), 'yyyy-MM-dd')<='"+ff+"' and id_ciudad_reclamacion="+StringEscapeUtils.escapeSql(String.valueOf(id_ciudad))+" and estado_respuesta='ANULADO'";
	Query query = entityManager.createNativeQuery(sql);
	List<?> list = query.getResultList();
	if (list.size() == 0L) {
		response = 0L;
	} else {
		if (list.get(0) == null) {
			response = 0L;
		} else {
			String a= list.get(0).toString();
			response = Long.valueOf(a);
		}
	}
	return response;
}

}
