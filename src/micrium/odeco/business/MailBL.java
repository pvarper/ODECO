package micrium.odeco.business;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;






import micrium.aes.AlgoritmoAES;
import micrium.odeco.model.Adjunto;
import micrium.odeco.model.FormularioOdeco;
import micrium.user.controler.ControlParametro;
//import micrium.calldetail.bussines.SysParameter;
import micrium.user.mail.Mail;
import micrium.user.mail.MailManager;
import micrium.util.Code;
import micrium.util.Result;

import org.apache.log4j.Logger;




@Named
public class MailBL implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(MailBL.class);
	
	@Inject
	 ControlParametro controlParametro;
	
	public Result sendEmail(List<String> correo,List<Adjunto> listaAdj, FormularioOdeco form) {
		log.info("Se va enviar al correo "+ correo );
		String mensaje="";
		Result result= new Result();
		AlgoritmoAES aes = new AlgoritmoAES();
		try {
			//hash=DigestUtil.generarHash(pathDetalle);
			mensaje=controlParametro.getTemplateEnvio();//SysParameter.getProperty(SysParameter.TEMPLATE_ENVIO);
			//mensaje="<b><h3>Favor revisar el siguiente ODECO:</h3></b><b>Codigo Reclamacion:</b> {CODIGO}<br/><b>Link Acceso al Sistema Web:</b> {LINK}<br/><b>Adjuntos:</b> {ADJUNTO}<br/>  <b><h3>Saludos. TIGO</h3></b> ";
			
				mensaje=mensaje.replace("{CODIGO}",form.getCodigoReclamacion());
				mensaje=mensaje.replace("{LINK}",controlParametro.getLinkSistema());
				//mensaje=mensaje.replace("{LINK}","aa");
				mensaje=mensaje.replace("{ADJUNTO}",controlParametro.getDescripcionAdjunto());
				//mensaje=mensaje.replace("{ADJUNTO}","bb");
				
			

			Mail mail = MailManager.getMail(listaAdj,controlParametro.getAsunto()+" "+form.getTipoFormulario().getDescripcion()+" - "+ form.getCodigoReclamacion(),mensaje, correo);
			//Mail mail = MailManager.getMail(listaAdj,"asunto"+" "+form.getTipoFormulario().getDescripcion()+" - "+ form.getCodigoReclamacion(),mensaje, correo);
			result=MailManager.sendEmailDetalle(mail,controlParametro.getMailSmtpHost(),controlParametro.getMailTituloFrom(),Integer.valueOf(controlParametro.getMailSmtpPort()),Boolean.valueOf(controlParametro.getMailSmtpAuth()),Boolean.valueOf(controlParametro.getMailSmtpStartTlsEnable()),Boolean.valueOf(controlParametro.getMailSmtpSslEnable()),controlParametro.getMailSmtpUser(),aes.desencriptar(controlParametro.getMailSmtpPw()),controlParametro.getMailSmtpFrom());
			
			
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				log.info("No se envio al correo " + correo);
				result.error("No se envio al correo " + correo);
				return result;
			}
			log.debug("se envio el correo " + correo);
			result.ok("se envio el correo " + correo);
			
			return result;
			
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("No se envio el correo " + correo);
			result.error("No se envio el correo " + correo);
			return result;
		}

	}





}
