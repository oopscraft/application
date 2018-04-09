package net.oopscraft.application.admin;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.oopscraft.application.user.SecurityFilter;

public class ConsoleSecurityFilter implements Filter {
	
	private static final Log LOG = LogFactory.getLog(SecurityFilter.class);

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest) req;
    	HttpServletResponse response = (HttpServletResponse) res;
        LOG.debug(String.format("IP:%s,URL:%s,URI:%s",request.getRemoteAddr(),request.getRequestURL(),request.getRequestURI()));
        
        // checking session
        if(!request.getRequestURI().contains(request.getContextPath() + "/console/login")) {
	        HttpSession session = request.getSession(false);
	        if(session == null || session.getAttribute("admin") == null) {
	        	response.sendRedirect(request.getContextPath() + "/console/login");
	        	return;
	        }
        }
        
        // doFilter
        chain.doFilter(req, res);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
