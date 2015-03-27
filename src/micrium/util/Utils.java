package micrium.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

public class Utils {

	private static Logger log = Logger.getLogger(Utils.class);

	public static void showInfo(String mensaje) {
		log.debug("[showInfo] mensaje=" + mensaje + "");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, ""));
	}

	public static void showWarn(String mensaje) {
		log.debug("[showWarn] mensaje=" + mensaje + "");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, mensaje, ""));
	}

	public static void showError(String mensaje) {
		log.debug("[showError] mensaje=" + mensaje + "");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, ""));
	}

	public static boolean esNumeroTIGO(String dato) {
		log.debug("[esNumeroTIGO]Iniciando validacion nro=" + dato);
		Pattern pat = Pattern.compile("");//Parameters.ExprRegularValidarNroTIGO
		Matcher mat = pat.matcher(dato);
		if (mat.find()) {
			log.debug("[esNumeroTIGO] nro=" + dato + " es valido");
			return true;
		} else {
			log.debug("[esNumeroTIGO] nro=" + dato + " NO es valido");
			return false;
		}

	}

	public static boolean isNumber(String cad) {
		try {
			Integer.parseInt(cad);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	/*private static String replace(String cad) {
		cad = cad.replace("Ã±", "ñ");
		cad = cad.replace("Ã", "Ñ");
		return cad;
	}*/

	// cambio por pedro
	public static String parsearComment(String s) {

		String delims = "[||]";
		String resultado = "";
		String[] token = s.split(delims);
		for (int i = 0; i < token.length; i++) {
			resultado = token[i];

		}

		return resultado;
	}

}
