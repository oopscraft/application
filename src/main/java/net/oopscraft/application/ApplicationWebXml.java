package net.oopscraft.application;

import java.util.Set;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import springfox.documentation.spi.service.contexts.SecurityContext;

/**
 * Implementation of javax.servlet.ServletContainerInitializer
 * @version 0.0.1
 * @see    None
 */
public class ApplicationWebXml implements ServletContainerInitializer {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ApplicationWebXml.class);

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {
		LOGGER.info("ApplicationWebXml start...");
		
		// invokes application context
		if(Application.applicationContext == null) {
			Application.applicationContext = new AnnotationConfigApplicationContext(ApplicationContext.class);
		}

		
		// adds application context
		AnnotationConfigWebApplicationContext webAapplicationContext = new AnnotationConfigWebApplicationContext();
		webAapplicationContext.setParent(Application.applicationContext);
        webAapplicationContext.register(ApplicationWebContext.class);
        webAapplicationContext.register(ApplicationWebSocketContext.class);
        servletContext.addListener(new ContextLoaderListener(webAapplicationContext));
        webAapplicationContext.setServletContext(servletContext);
        
        // add dispatcher servlet
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherServlet", new DispatcherServlet(webAapplicationContext));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
        
        // add JPA transaction filter
        OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();
        FilterRegistration.Dynamic openEntityManagerInViewFilter = servletContext.addFilter("openEntityManagerInViewFilter", filter);
        openEntityManagerInViewFilter.addMappingForUrlPatterns(null, true, "/*");
        
        // adds spring security filter chain
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
        FilterRegistration.Dynamic springSecurityFilterChain = servletContext.addFilter("springSecurityFilterChain", delegatingFilterProxy);
        springSecurityFilterChain.addMappingForUrlPatterns(null, true, "/*");
	}

}
