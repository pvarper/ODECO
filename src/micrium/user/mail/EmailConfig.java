package micrium.user.mail;

import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
//import micrium.calldetail.bussines.SysParameter;

public class EmailConfig extends HtmlEmail  {
	

	public EmailConfig(String mailSmtlHost,String mailSmtlUserName,
			int mailSmtlPort,boolean mailSmtlAuth,boolean mailSmtlStarttlsEnable,boolean mailSmtlSslEnable,String mailSmtlUser,String mailSmtlPassword,String mailSmtlFrom ) throws EmailException {

		setCharset("UTF-8");
		setHostName(mailSmtlHost);
		
		setSmtpPort(mailSmtlPort);

		if (mailSmtlAuth) {
			// setAuthentication(mailSmtlUser, mailSmtlPassword);
			setAuthenticator(new DefaultAuthenticator(mailSmtlUser, mailSmtlPassword));
		}

		setSSLOnConnect(mailSmtlSslEnable);
		setStartTLSEnabled(mailSmtlStarttlsEnable);

		addFrom(mailSmtlFrom, mailSmtlUserName);
	}

	public void addFrom(String mailSmtlUser, String mailSmtlUserName) throws EmailException {
		setFrom(mailSmtlUser, mailSmtlUserName);
	}

	public void agregarDestinatarios(List<String> destinos, List<String> copias, List<String> ocultos) throws EmailException {
		if (destinos != null) {
			for (String emailDst : destinos) {
				// if (EmailUtils.isEmailValid(emailDst)) {
				addTo(emailDst);
				// }
			}
		}

		if (copias != null) {
			for (String emailDst : copias) {
				// if (EmailUtils.isEmailValid(emailDst)) {
				addCc(emailDst);
				// }
			}
		}

		if (ocultos != null) {
			for (String emailDst : ocultos) {
				// if (EmailUtils.isEmailValid(emailDst)) {
				addBcc(emailDst);
				// }
			}
		}
	}

}
