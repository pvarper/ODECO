package micrium.odeco.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.apache.commons.lang.StringEscapeUtils;

import micrium.odeco.model.Parametro;

@Named
public class ParametroDAO implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private transient EntityManager entityManager;

	@Resource
	private transient UserTransaction transaction;

	/*public ParametroDAO() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(null);
		entityManager = factory.createEntityManager();
	}*/
	
	public void save(Parametro dato) throws Exception {
		transaction.begin();
		entityManager.persist(dato);
		transaction.commit();
	}

	public Parametro get(long id) throws Exception {
		return entityManager.find(Parametro.class, id);
	}

	public void update(Parametro dato) throws Exception {
		transaction.begin();
		entityManager.merge(dato);
		transaction.commit();
	}

	@SuppressWarnings("unchecked")
	public List<Parametro> getList() throws Exception {
		return entityManager.createQuery("SELECT p FROM Parametro p")
				.getResultList();
	}
	

	public Parametro getParametro(long Id) {
		Query q = entityManager
				.createQuery("SELECT p FROM Parametro p where p.id = :Id");
		q.setParameter("Id", Id);
		return (Parametro) q.getSingleResult();
	}
	
	public String getValorParametro(long id) {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.id = :id");
		q.setParameter("id", id);
		return (String) q.getSingleResult();
	}
	public String getValorInitialContextFactory() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("DIRECTORIO_ACTIVO.initial_context_factory")+"'");
		return (String) q.getSingleResult();
	}
	public String getValorProviderUrl() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("DIRECTORIO_ACTIVO.provider_url")+"'");
		return (String) q.getSingleResult();
	}
	public String getValorSecurityAutenticacion() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("DIRECTORIO_ACTIVO.security_authentication")+"'");
		return (String) q.getSingleResult();
	}
	public String getValorSecurityPrincipal() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("DIRECTORIO_ACTIVO.security_principal")+"'");
		return (String) q.getSingleResult();
	}
	public String getValorSecurityCredential() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("DIRECTORIO_ACTIVO.security_credentials")+"'");
		return (String) q.getSingleResult();
	}
	public String getValorSecurityUser() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("DIRECTORIO_ACTIVO.security_user")+"'");
		return (String) q.getSingleResult();
	}
	public String getValorSecurityDominio() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("DIRECTORIO_ACTIVO.dominio")+"'");
		return (String) q.getSingleResult();
	}
	public String getValorUsuarioActiveDirectory() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("USUARIO.sw_active_directory")+"'");
		return (String) q.getSingleResult();
	}
	public String getValorUsuarioLogin() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("USUARIO.login")+"'");
		return (String) q.getSingleResult();
	}
	public String getValorUsuarioPassword() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("USUARIO.password")+"'");
		return (String) q.getSingleResult();
	}
	public String getValorUsuarioOpciones() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("USUARIO.nro_opciones")+"'");
		return (String) q.getSingleResult();
	}
	public String getValorUsuarioTiempoFuera() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("USUARIO.tiempo_fuera")+"'");
		return (String) q.getSingleResult();
	}
	public String getRutaAdjunto() {
		
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("RUTA ADJUNTOS")+"'");
		return (String) q.getSingleResult();
	}
	public String getCorreoRegulacion() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("CORREO REGULACION")+"'");
		return (String) q.getSingleResult();
	}
	public String getCorreoATT() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("CORREO ATT")+"'");
		return (String) q.getSingleResult();
	}
	public String getTemplateEnvio() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("TEMPLATE ENVIO")+"'");
		return (String) q.getSingleResult();
	}
	public String getLinkSistema() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("LINK AL SISTEMA")+"'");
		return (String) q.getSingleResult();
	}
	public String getDescripcionAdjunto() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("DESCRIPCION ADJUNTO")+"'");
		return (String) q.getSingleResult();
	}
	public String getAsunto() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("ASUNTO")+"'");
		return (String) q.getSingleResult();
	}
	public String getMailSmtpHost() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("MAIL SMTP HOST")+"'");
		return (String) q.getSingleResult();
	}
	public String getMailTituloFrom() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("TITULO FROM")+"'");
		return (String) q.getSingleResult();
	}
	public String getMailSmtpPort() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("MAIL SMTP PORT")+"'");
		return (String) q.getSingleResult();
	}
	public String getMailSmtpAuth() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("MAIL SMTP AUTHENTICATION")+"'");
		return (String) q.getSingleResult();
	}
	public String getMailSmtpStartTlsEnable() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("MAIL SMTP START TLS ENABLE")+"'");
		return (String) q.getSingleResult();
	}
	public String getMailSmtpSslEnable() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("MAIL SMTP SSL ENABLE")+"'");
		return (String) q.getSingleResult();
	}
	public String getMailSmtpUser() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("MAIL SMTP USER")+"'");
		return (String) q.getSingleResult();
	}
	public String getMailSmtpPw() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("MAIL SMTP PW")+"'");
		return (String) q.getSingleResult();
	}
	public String getMailSmtpFrom() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("MAIL SMTP FROM")+"'");
		return (String) q.getSingleResult();
	}
	public String getRutaReporte() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("RUTA REPORTES")+"'");
		return (String) q.getSingleResult();
	}
	public String getRutaLogoOdeco() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("RUTA LOGO ODECO")+"'");
		return (String) q.getSingleResult();
	}
	public String getRutaLogoTigo() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("RUTA LOGO TIGO")+"'");
		return (String) q.getSingleResult();
	}
	public String getRutaExportarReporte() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("RUTA EXPORTAR REPORTES")+"'");
		return (String) q.getSingleResult();
	}
	public String getFormaRegistro() {
		Query q = entityManager
				.createQuery("SELECT p.valor FROM Parametro p where p.descripcion ='"+StringEscapeUtils.escapeSql("Formas de Registro")+"'");
		return (String) q.getSingleResult();
	}
	
	
	
	

}
