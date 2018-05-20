package net.oopscraft.application.user;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
 
@Component
public class UserAuthorizationFilter implements Filter {
	
//	@Autowired
//	ResourceService resourceService;
	
	private static Log LOG = LogFactory.getLog(UserAuthorizationFilter.class);
 
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest) req;
    	HttpServletResponse response = (HttpServletResponse) res;
        String uri = request.getRequestURI();
        String method = request.getMethod();
        LOG.debug(String.format("[%s][%s]",  method, uri));
        
//        // checking source author
//        try {
//        	Resource resource = resourceService.getResource(uri, method);
//
//
//        	if(resource != null) {
//        		if(this.hasAuthority(resource.getAuthorityId()) == false) {
//        			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//        			return;
//        		}
//        	}
//        	
//        }catch(Exception e) {
//        	LOG.error(e.getMessage());
//        	throw new ServletException(e);
//        }
        
        chain.doFilter(req, res);
    }
    
    /**
     * 
     * @param authorityId
     * @return
     * @throws Exception
     */
    private boolean hasAuthority(String authorityId) throws Exception {
//    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    	if(authentication.isAuthenticated() 
//    	&& !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
//    		User user = (User)authentication.getPrincipal();
//    		return user.hasAuthority(authorityId);
//    	}
    	return false;
    }
    

    

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}