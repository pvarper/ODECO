package micrium.user.view;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.business.BitacoraBL;

@Named
public class ControlerBitacora implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	BitacoraBL logBl;

	@SuppressWarnings("rawtypes")
	public void insert(Enum dato, String id, String name) {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");
		String remoteAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
		logBl.accionInsert(strIdUs, remoteAddr, dato, id, name);

	}

	@SuppressWarnings("rawtypes")
	public void update(Enum dato, String id, String name) {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");
		String remoteAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
		logBl.accionUpdate(strIdUs, remoteAddr, dato, id, name);

	}
	
	@SuppressWarnings("rawtypes")
	public void exportar(Enum dato, String id, String name) {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");
		String remoteAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
		logBl.accionExportar(strIdUs, remoteAddr, dato, id, name);

	}

	@SuppressWarnings("rawtypes")
	public void delete(Enum dato, String id, String name) {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");
		String remoteAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
		logBl.accionDelete(strIdUs, remoteAddr, dato, id, name);

	}

	@SuppressWarnings("rawtypes")
	public void find(Enum dato, String id, String name) {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");
		String remoteAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
		logBl.accionFind(strIdUs, remoteAddr, dato, id, name);

	}

	@SuppressWarnings("rawtypes")
	public void cortar(Enum dato, String id) {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");
		String remoteAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
		logBl.accionCortar(strIdUs, remoteAddr, dato, id);

	}

	@SuppressWarnings("rawtypes")
	public void reconectar(Enum dato, String id) {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");
		String remoteAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
		logBl.accionReconectar(strIdUs, remoteAddr, dato, id);

	}

	@SuppressWarnings("rawtypes")
	public void accion(Enum dato, String accion) {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");
		String remoteAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
		logBl.accion(strIdUs, remoteAddr, dato, accion);

	}

}
