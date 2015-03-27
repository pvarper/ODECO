package micrium.user.mail;

import java.util.List;

public class Mail {

	private String subject;
	private String content;
	private List<Attachment> attachment;
	private List<String> lstEmailsTo;
	private List<String> lstEmailsCc;
	private List<String> lstEmailsCco;

	public Mail() {
		// TODO Auto-generated constructor stub
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Attachment> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<Attachment> attachment) {
		this.attachment = attachment;
	}

	public List<String> getLstEmailsTo() {
		return lstEmailsTo;
	}

	public void setLstEmailsTo(List<String> lstEmailsTo) {
		this.lstEmailsTo = lstEmailsTo;
	}

	public List<String> getLstEmailsCc() {
		return lstEmailsCc;
	}

	public void setLstEmailsCc(List<String> lstEmailsCc) {
		this.lstEmailsCc = lstEmailsCc;
	}

	public List<String> getLstEmailsCco() {
		return lstEmailsCco;
	}

	public void setLstEmailsCco(List<String> lstEmailsCco) {
		this.lstEmailsCco = lstEmailsCco;
	}

	@Override
	public String toString() {
		return "{subject:" + subject + ", content:" + content + ", attachment:" + attachment + ", lstEmailsTo:" + lstEmailsTo + ", lstEmailsCc:"
				+ lstEmailsCc + "}";
	}

}
