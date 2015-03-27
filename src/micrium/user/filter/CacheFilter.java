package micrium.user.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author toshiba satelite
 */
@WebFilter(filterName = "CacheFilter", urlPatterns = { "/*" })
public class CacheFilter implements Filter {

	private static final boolean debug = true;
	// The filter configuration object we are associated with. If
	// this value is null, this filter instance is not currently
	// configured.
	private FilterConfig filterConfig = null;

	public CacheFilter() {
	}

/*	private void doBeforeProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {

		HttpSession session = ((HttpServletRequest) request).getSession();
		HttpServletRequest req = (HttpServletRequest) request;
		Object user = session.getAttribute("TEMP$USER_NAME");
		if (request.getContentLength() != -1) {
			Map<String, Object> values = new HashMap<String, Object>();
			for (String name : session.getValueNames()) {
				values.put(name, session.getAttribute(name));
			}

			if (user == null) {
				session.invalidate();
				session = req.getSession(true);
				for (Map.Entry<String, Object> e : values.entrySet()) {
					session.setAttribute(e.getKey(), e.getValue());
				}
				System.out.println("INVALIDATE SESSION");
				log("Requested_SessionId Renovado:" + session.getId());
			}

		}
		String path = req.getRequestURI();
		if ((user == null) && (request.getContentLength() == -1) && (path.contains("/view/Login.xhtml") || path.equals("/ODECO_DIGITAL/"))) {
			session.invalidate();
		}

	}*/

	/*private void doAfterProcessing(ServletRequest servletrequest, ServletResponse servletresponse) throws IOException, ServletException {
		if (debug) {
			log("CacheFilter:DoAfterProcessing");
		}

		// Write code here to process the request and/or response after
		// the rest of the filter chain is invoked.

		// For example, a logging filter might log the attributes on the
		// request object after the request has been processed.
		/*
		 * for (Enumeration en = request.getAttributeNames(); en.hasMoreElements(); ) { String name = (String)en.nextElement(); Object value =
		 * request.getAttribute(name); log("attribute: " + name + "=" + value.toString());
		 * 
		 * }
		 
		HttpServletResponse httpResponse = (HttpServletResponse) servletresponse;
		// System.out.println("Status: " + httpResponse.getStatus());
		int code = httpResponse.getStatus();
		switch (code) {
		case 404: {
			httpResponse.sendRedirect("/ODECO_DIGITAL/view/fail.html");
			return;
		}
		case 500: {
			httpResponse.sendRedirect("/ODECO_DIGITAL/view/fail.html");
			return;
		}
		case 501: {
			httpResponse.sendRedirect("/ODECO_DIGITAL/view/fail.html");
			return;
		}
		}
		// For example, a filter might append something to the response.
		/*
		 * error 501 PrintWriter respOut = new PrintWriter(response.getWriter());
		 * respOut.println("<P><B>This has been appended by an intrusive filter.</B>");
		 
	}*/

	/**
	 * 
	 * @param request The servlet request we are processing
	 * @param response The servlet response we are creating
	 * @param chain The filter chain we are processing
	 * 
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet error occurs
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		if (debug) {
			log("CacheFilter:doFilter()");
		}

		//doBeforeProcessing(request, response);

		Throwable problem = null;
		try {
			// //////////////////////////////////////////
			// System.out.println("************************* CACHE **********************");
			// HttpServletRequest req = (HttpServletRequest) request;
			// String path = req.getRequestURI();
			// System.out.println("URL: " + path);
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setHeader("x-ua-compatible", "IE=8");
			//httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			httpResponse.setHeader("Pragma", "no-cache");
			httpResponse.setHeader("X-Frame-Options", "deny");
			httpResponse.setDateHeader("Expires", 0);

			// /////////////////////////////////////////

			chain.doFilter(request, response);
		} catch (Throwable t) {
			// If an exception is thrown somewhere down the filter chain,
			// we still want to execute our after processing, and then
			// rethrow the problem after that.
			problem = t;
			t.printStackTrace();
		}

		//doAfterProcessing(request, response);

		// If there was a problem, we want to rethrow it if it is
		// a known type, otherwise log it.
		if (problem != null) {
			if (problem instanceof ServletException) {
				throw (ServletException) problem;
			}
			if (problem instanceof IOException) {
				throw (IOException) problem;
			}
			// sendProcessingError(problem, response);
		}
	}

	/**
	 * Return the filter configuration object for this filter.
	 */
	public FilterConfig getFilterConfig() {
		return (this.filterConfig);
	}

	/**
	 * Set the filter configuration object for this filter.
	 * 
	 * @param filterConfig The filter configuration object
	 */
	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	/**
	 * Destroy method for this filter
	 */
	public void destroy() {
	}

	/**
	 * Init method for this filter
	 */
	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
		if (filterConfig != null) {
			if (debug) {
				log("CacheFilter:Initializing filter");
			}
		}
	}

	/**
	 * Return a String representation of this object.
	 */
	@Override
	public String toString() {
		if (filterConfig == null) {
			return ("CacheFilter()");
		}
		StringBuffer sb = new StringBuffer("CacheFilter(");
		sb.append(filterConfig);
		sb.append(")");
		return (sb.toString());
	}

	public static String getStackTrace(Throwable t) {
		String stackTrace = null;
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			pw.close();
			sw.close();
			stackTrace = sw.getBuffer().toString();
		} catch (Exception ex) {
		}
		return stackTrace;
	}

	public void log(String msg) {
		filterConfig.getServletContext().log(msg);
	}
}
