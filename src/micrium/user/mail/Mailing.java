/*-*
 *
 * FILENAME  :
 *    Mailing.java
 *
 * STATUS  :	
 *    2013 23:17:57  
 *
 *    
 * Copyright (c) 2012 SystemSoft Ltda. All rights reserved.
 *
 ****************************************************************/

package micrium.user.mail;

import java.util.List;

import micrium.util.Result;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;
public class Mailing {

	private static Logger log = Logger.getLogger(Mailing.class);

	public synchronized Result sendMail(String subject, String mensaje, List<Attachment> attachment, List<String> lstEmailsTo, List<String> lstEmailsCc,
			List<String> lstEmailsCco,String mailSmtlHost,String mailSmtlUserName,
			int mailSmtlPort,boolean mailSmtlAuth,boolean mailSmtlStarttlsEnable,boolean mailSmtlSslEnable,String mailSmtlUser,String mailSmtlPassword,String mailSmtlFrom ) {

		EmailConfig email = null;
		Result result= new Result();
		try {

			email = new EmailConfig( mailSmtlHost, mailSmtlUserName,
					 mailSmtlPort, mailSmtlAuth, mailSmtlStarttlsEnable, mailSmtlSslEnable, mailSmtlUser, mailSmtlPassword, mailSmtlFrom  );
			
			// Create the attachment
			EmailAttachment emailAttachment = new EmailAttachment();
			
			for (Attachment a : attachment) {
				emailAttachment.setPath(a.getPath());
				emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
				emailAttachment.setDescription(a.getDescription());
				emailAttachment.setName(a.getName());
				email.attach(emailAttachment);
				
			}
			

			// Create the email message
			
			email.agregarDestinatarios(lstEmailsTo, lstEmailsCc, lstEmailsCco);
			email.setSubject(subject);
			email.setHtmlMsg(mensaje);

			// add the attachment
			

			// send the email
			email.send();
			

			result.ok("Email enviado From:" + email.getFromAddress() + " To:" + email.getToAddresses() + " adjunto:" 
					+ " subject:" + subject + ", mensaje:" + mensaje);
			log.info("Email enviado From:" + email.getFromAddress() + " To:" + email.getToAddresses() + " adjunto:"
					+ " subject:" + subject + ", mensaje:" + mensaje);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Email fallido  To:" + lstEmailsTo + " adjunto:" + " subject:" + subject + ", mensaje:" + mensaje, e);
			result.error("Email fallido  To:" + lstEmailsTo + " adjunto:" + " subject:" + subject + ", mensaje:" + mensaje);
			return result;
		}

		
	}

	public synchronized Result sendMail2(String subject, String mensaje,List<String> lstEmailsTo, List<String> lstEmailsCc,
			List<String> lstEmailsCco,String mailSmtlHost,String mailSmtlUserName,
			int mailSmtlPort,boolean mailSmtlAuth,boolean mailSmtlStarttlsEnable,boolean mailSmtlSslEnable,String mailSmtlUser,String mailSmtlPassword,String mailSmtlFrom ) {
		EmailConfig email = null;
		Result result= new Result();
		try {

			// Create the email message
			email = new EmailConfig( mailSmtlHost, mailSmtlUserName,
					 mailSmtlPort, mailSmtlAuth, mailSmtlStarttlsEnable, mailSmtlSslEnable, mailSmtlUser, mailSmtlPassword, mailSmtlFrom  );
			
			email.agregarDestinatarios(lstEmailsTo, lstEmailsCc, lstEmailsCco);
			email.setSubject(subject);
			email.setHtmlMsg(mensaje);

			// add the attachment

			// send the email
			email.send();

			result.ok("Email enviado From:" + email.getFromAddress() + " To:" + email.getToAddresses() 
					+ " subject:" + subject + ", mensaje:" + mensaje);
			log.info("Email enviado From:" + email.getFromAddress() + " To:" + email.getToAddresses()  
					+ " subject:" + subject + ", mensaje:" + mensaje);
			return result;
		} catch (Exception e) {
			log.error("Email fallido  To:" + lstEmailsTo +  " subject:" + subject + ", mensaje:" + mensaje, e);
			result.error("Email fallido  To:" + lstEmailsTo + " subject:" + subject + ", mensaje:" + mensaje);
			return result;
		}

		
	}
	// public static void enviarCorreoCuentaErick() {
	// Email email = new SimpleEmail();
	// email.setHostName("73.20.0.80");
	//
	// email.setSmtpPort(25);
	// email.setAuthenticator(new DefaultAuthenticator("hinojosae", "Faber788"));
	//
	// email.setStartTLSEnabled(false);
	// // email.setSSLOnConnect(false);
	// // email.setStartTLSRequired(false);
	// try {
	// email.setFrom("hinojosae@tigo.net.bo");
	// email.setSubject("TestMail");
	// email.setMsg("This is a test mail ... :-)");
	// email.addTo("marioae.inf@gmail.com");
	// email.send();
	//
	// System.out.println("enviado!");
	// } catch (EmailException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	public static void enviarCorreoCuentaCallDetail1() {
		for (int i = 0; i < 10; i++) {

			Email email = new SimpleEmail();
			email.setHostName("73.20.0.206");

			email.setSmtpPort(25);
			email.setAuthenticator(new DefaultAuthenticator("web_detalle", "W3b_d3t4ll3"));

			email.setStartTLSEnabled(false);
			// email.setSSLOnConnect(false);
			email.setStartTLSRequired(false);
			try {
				email.setFrom("web_detalle@tigo.net.bo");
				email.setSubject("TestMail");
				email.setMsg("This is a test mail ... :-)");
				// email.addTo("marioae.inf@gmail.com");
				email.addTo("hinojosae@tigo.net.bo");
				email.send();

				System.out.println("enviado!");
			} catch (EmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void enviarCorreoCuentaCallDetail() {
		for (int i = 0; i < 1; i++) {

			Email email = new SimpleEmail();
			email.setHostName("73.20.0.80");

			email.setSmtpPort(25);
			email.setAuthenticator(new DefaultAuthenticator("servicio", "S3rv1c10"));

			email.setStartTLSEnabled(false);
			// email.setSSLOnConnect(false);
			email.setStartTLSRequired(false);
			try {
				email.setFrom("servicio@tigo.net.bo");
				email.setSubject("TestMail");
				email.setMsg("This is a test mail ... :-)");
				// email.addTo("marioae.inf@gmail.com");
				email.addTo("marioae.inf@gmail.com");
				email.send();

				System.out.println("enviado!");
			} catch (EmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/*public static boolean openPopConnection() {
		try {

			log.info("Abriendo Bandeja de Entrada de: " + "pizarroc@tigo.net.bo");
			Properties props = System.getProperties();
			props.setProperty("mail.store.protocol", "pop3");
			// props.setProperty("http.proxyHost", "73.24.0.131");
			// props.setProperty("http.proxyPort", "8080");
			// props.setProperty("mail.pop3.starttls.enable", "false");
			// props.setProperty("mail.pop3.port", "25");
			// props.setProperty("mail.pop3.socketFactory.port", "25");

			// sessionPop = Session.getInstance(props, new MailAuthenticator(username, password));
			// System.out.println("SESSION "+sessionPop);
			Session sessionPop = Session.getDefaultInstance(props, null);
			sessionPop.setDebug(true);
			Store store = sessionPop.getStore("pop3");

			sessionPop.setPasswordAuthentication(new URLName("pop3", "73.20.0.80", 110, null, "servicio", null), new PasswordAuthentication(
					"servicio@tigo.net.bo", "S3rv1c10"));

			store.connect("73.20.0.80", "servicio@tigo.net.bo", "S3rv1c10");
			Folder folder = store.getFolder("Inbox");
			folder.open(Folder.READ_WRITE);
			return true;

		} catch (NoSuchProviderException e) {
			log.error("Error al Abrir la Bandeja de Entrada", e);
			return false;

		} catch (MessagingException e) {
			log.error("Error al Abrir la Bandeja de Entrada", e);
			return false;
		}
	}
*/


}