package micrium.user.controler;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.odeco.dao.ParametroDAO;
import micrium.odeco.model.Parametro;


@Named
public class ControlParametro implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ParametroDAO dao;
	
	/*public ControlParametro() {
		dao = new ParametroDAO();
	}*/
	
	public void updateParametro(Parametro param) throws Exception {
		dao.update(param);
	}

	public List<Parametro> getParametros() throws Exception {
		return dao.getList();
	}
	

	public Parametro getParametro(long Id) {
		return dao.getParametro(Id);
	}

	public String getValorParametro(long Id){
		return dao.getValorParametro(Id);
	}
	public String getInitialContextFactory(){
		return dao.getValorInitialContextFactory();
	}
	public String getValorProviderUrl(){
		return dao.getValorProviderUrl();
	}
	public String getValorSecurityAutenticacion(){
		return dao.getValorSecurityAutenticacion();
	}
	public String getValorSecurityPrincipal(){
		return dao.getValorSecurityPrincipal();
	}
	public String getValorSecurityCredential(){
		return dao.getValorSecurityCredential();
	}
	public String getValorSecurityUser(){
		return dao.getValorSecurityUser();
	}
	public String getValorSecurityDominio(){
		return dao.getValorSecurityDominio();
	}
	public String getValorUsuarioActiveDirectory(){
		return dao.getValorUsuarioActiveDirectory();
	}
	public String getValorUsuarioLogin(){
		return dao.getValorUsuarioLogin();
	}
	public String getValorUsuarioPassword(){
		return dao.getValorUsuarioPassword();
	}
	public String getValorUsuarioOpciones(){
		return dao.getValorUsuarioOpciones();
	}
	public String getValorUsuarioTiempoFuera(){
		return dao.getValorUsuarioTiempoFuera();
	}
	public String getRutaAdjunto(){
		return dao.getRutaAdjunto();
	}
	public String getCorreoRegulacion(){
		return dao.getCorreoRegulacion();
	}
	public String getCorreoATT(){
		return dao.getCorreoATT();
	}
	public String getTemplateEnvio(){
		return dao.getTemplateEnvio();
	}
	public String getLinkSistema(){
		return dao.getLinkSistema();
	}
	public String getDescripcionAdjunto(){
		return dao.getDescripcionAdjunto();
	}
	public String getAsunto(){
		return dao.getAsunto();
	}
	public String getMailSmtpHost(){
		return dao.getMailSmtpHost();
	}
	public String getMailTituloFrom(){
		return dao.getMailTituloFrom();
	}
	public String getMailSmtpPort(){
		return dao.getMailSmtpPort();
	}
	public String getMailSmtpAuth(){
		return dao.getMailSmtpAuth();
	}
	public String getMailSmtpStartTlsEnable(){
		return dao.getMailSmtpStartTlsEnable();
	}
	public String getMailSmtpSslEnable(){
		return dao.getMailSmtpSslEnable();
	}
	public String getMailSmtpUser(){
		return dao.getMailSmtpUser();
	}
	public String getMailSmtpPw(){
		return dao.getMailSmtpPw();
	}
	public String getMailSmtpFrom(){
		return dao.getMailSmtpFrom();
	}
	public String getRutaReporte(){
		return dao.getRutaReporte();
	}
	public String getRutaLogoOdeco(){
		return dao.getRutaLogoOdeco();
	}
	public String getRutaLogoTigo(){
		return dao.getRutaLogoTigo();
	}
	public String getRutaExportarReporte(){
		return dao.getRutaExportarReporte();
	}
	public String getFormaRegistro(){
		return dao.getFormaRegistro();
	}
	
	
	
}
