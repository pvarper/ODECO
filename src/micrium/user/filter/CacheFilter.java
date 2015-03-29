package micrium.user.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class TestFilter
 */
@WebFilter("/CacheFilter")
public class CacheFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public CacheFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		// HttpServletRequest req = (HttpServletRequest) request;
		//
		// String path = req.getRequestURI();
		// if (req.getParameterMap().size() > 0) {
		//
		// chain.doFilter(request, response);
		//
		// } else {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setHeader("x-ua-compatible", "IE=8");
		httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		httpResponse.setHeader("Pragma", "no-cache");
		httpResponse.setHeader("X-Frame-Options", "deny");
		httpResponse.setDateHeader("Expires", 0);
		
		chain.doFilter(request, response);
		// }

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
