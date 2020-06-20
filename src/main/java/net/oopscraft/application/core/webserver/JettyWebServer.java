package net.oopscraft.application.core.webserver;

import java.net.URI;
import java.net.URL;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

public class JettyWebServer extends WebServer {
	
	Server server = null;

	@Override
	public void start() throws Exception {
		
		// creates server
		server = new Server();
		
		// adds connector
        HttpConfiguration httpConfig = new HttpConfiguration();
        if(this.isSsl()) { 
	        httpConfig.setSecureScheme("https");
	        httpConfig.setSecurePort(443);
        }
		ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory(httpConfig));
		connector.setPort(this.getPort());
		server.addConnector(connector);
		
		// adds context
		HandlerCollection handlerCollection = new ContextHandlerCollection();
		for(WebServerContext context : this.getContexts()){
			WebAppContext webAppContext = new WebAppContext();
			
			// context path
			webAppContext.setContextPath(context.getContextPath());
			
			// resource path
			URL url = this.getClass().getResource("/META-INF/resources/WEB-INF");
			URI baseURI = url.toURI().resolve("./");
			ResourceCollection resourceCollections = new ResourceCollection(
				Resource.newResource(context.getResourceBase()),
				Resource.newResource(baseURI)
			);
			webAppContext.setBaseResource(resourceCollections);
			
			// descriptor
			if(context.getDescriptor() != null && context.getDescriptor().trim().length() > 0) {
				webAppContext.setDescriptor(context.getDescriptor());
			}
			
			// initial parameter
			webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
			if(context.getParameter() != null) {
				for(String name : context.getParameter().keySet()){ 
					String value = context.getParameter(name);
					webAppContext.setInitParameter(name, value);
				}
			}
			
			// sets additional configuration
			webAppContext.setParentLoaderPriority(true);
			webAppContext.setConfigurations(new Configuration[]{
				new AnnotationConfiguration(),
				new WebInfConfiguration(),
				new WebXmlConfiguration(),
				new MetaInfConfiguration(),
				new FragmentConfiguration(),
				new EnvConfiguration(),
				new PlusConfiguration(),
				new JettyWebXmlConfiguration()
			});
			
			// adds handler
			ServletHolder staticHolder = new ServletHolder("default", DefaultServlet.class);
			webAppContext.addServlet(staticHolder, "/");
			handlerCollection.addHandler(webAppContext);
			handlerCollection.addHandler(new DefaultHandler()); // always last handler
		}
		server.setHandler(handlerCollection); 

		// starts server
		server.start();
	}

	@Override
	public void stop() throws Exception {
		if(server != null) {
			server.stop();
		}
	}

}
