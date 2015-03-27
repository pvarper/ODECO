package micrium.user.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import micrium.aes.AlgoritmoAES;
import micrium.user.controler.ControlTimeOutImpl;
import micrium.user.controler.NodoClient;
import micrium.user.controler.ParametersCM;
import micrium.util.Parameter;

public class loginFilter implements Filter {

	private FilterConfig filterConfig = null;

	private static final String pathRaiz = "/ODECO_DIGITAL/";
	private static final String pathLogin = "/ODECO_DIGITAL/view/Login.xhtml";
	private static final String rederingMenu = "/ODECO_DIGITAL/view/Menu.xhtml";

	@Inject
	private ControlTimeOutImpl controlTA;
	
	//@Inject
	//ControlParametro controlParametro;

	public loginFilter() {
		//controlParametro= new ControlParametro();
		AlgoritmoAES aes = new AlgoritmoAES();
		/*ParametersCM.LOGIN = aes.desencriptar(Parameters.login);
		ParametersCM.PASSWORD_USER = aes.desencriptar(Parameters.password);

		ParametersCM.NRO_INTENTOS = Integer.parseInt(Parameters.nroOption);
		ParametersCM.MINUTOS_FUERA = Integer.parseInt(Parameters.timeOut);*/
		ParametersCM.LOGIN = aes.desencriptar(Parameter.login);
		ParametersCM.PASSWORD_USER = aes.desencriptar(Parameter.password);

		ParametersCM.NRO_INTENTOS = Integer.parseInt(Parameter.nroOption);
		ParametersCM.MINUTOS_FUERA = Integer.parseInt(Parameter.timeOut);
	/*	ParametersCM.LOGIN = aes.desencriptar("RIYKKLzFsFJ7Af2HbSEa/w==");
		ParametersCM.PASSWORD_USER = aes.desencriptar("IEG0pRXmPtR+5EdE5gQZGA==");

		ParametersCM.NRO_INTENTOS = Integer.parseInt("20");
		ParametersCM.MINUTOS_FUERA = Integer.parseInt("1");*/

	}

	/**
	 * Init method for this filter
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
		
			HttpSession session = ((HttpServletRequest) request).getSession();
			HttpServletRequest req = (HttpServletRequest) request;

			String path = req.getRequestURI();
			String usuario = (String) req.getSession().getAttribute("TEMP$USER_NAME");

			if (path.equals(pathRaiz)) {
				if (usuario == null)
					chain.doFilter(request, response);
				else {
					HttpServletResponse hres = (HttpServletResponse) response;
					hres.sendRedirect(rederingMenu);
				}
				return;
			}

			if (path.equals(pathLogin)) {
				if (request.getContentLength() != -1)
					chain.doFilter(request, response);
				else {
					HttpServletResponse hres = (HttpServletResponse) response;
					hres.sendRedirect(pathRaiz);

				}
				return;
			}

			if (usuario != null) {

				String addressIP = request.getRemoteAddr();
				String addressUser = controlTA.getAddressIP(usuario);

				if (!addressIP.equals(addressUser)) {
					session.setAttribute("TEMP$USER_NAME", "");
					session.setAttribute("TEMP$GROUP", "");
					session.invalidate();
					HttpServletResponse hres = (HttpServletResponse) response;
					hres.sendRedirect(pathRaiz);
				} else {
					NodoClient nd = controlTA.getNodoClient(usuario);
					String pageRequest = path;
					int k = pageRequest.lastIndexOf("/");
					String strPg = pageRequest.substring(k + 1);
					if (nd.existeUrl(strPg)) {
						long tp = session.getLastAccessedTime();
						controlTA.setDatos(usuario, tp);
						chain.doFilter(request, response);
					} else {
						RequestDispatcher dispatcher = request.getRequestDispatcher(pathRaiz);
						dispatcher.forward(request, response);
					}
				}

			} else {
				long timeMax = session.getMaxInactiveInterval() * 1000;
				controlTA.registerOutTime(timeMax);
				session.setAttribute("TEMP$ACTION_MESSAGE_ID", "");
				session.setAttribute("TEMP$USER_NAME", "");
				session.setAttribute("TEMP$GROUP", "");
				session.invalidate();
				HttpServletResponse hres = (HttpServletResponse) response;
				hres.sendRedirect(pathRaiz);
			}
		
	
	}

	@Override
	public void destroy() {
	}

	@Override
	public String toString() {
		if (filterConfig == null) {
			return ("loginFilter()");
		}
		StringBuffer sb = new StringBuffer("loginFilter(");
		sb.append(filterConfig);
		sb.append(")");
		return (sb.toString());
	}

	public void log(String msg) {
		filterConfig.getServletContext().log(msg);
	}

}
