package net.oopscraft.application;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
public class ApplicationWebInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		System.err.println("###################");
		
        AnnotationConfigWebApplicationContext ctx =
                new AnnotationConfigWebApplicationContext();
      ctx.register(ApplicationWebContext.class);
      ctx.setParent(Application.context);
      ctx.setServletContext(servletContext);
      

      ServletRegistration.Dynamic servlet =
                servletContext.addServlet("dispatcherServlet",
                                          new DispatcherServlet(ctx));

      servlet.setLoadOnStartup(1);
      servlet.addMapping("/");
	}

}
