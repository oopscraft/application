package net.oopscraft.application;


import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import net.oopscraft.application.core.TextTableBuilder;
import net.oopscraft.application.core.WebServer;
import net.oopscraft.application.core.XPathReader;
import net.oopscraft.application.user.User;

public class ApplicationBuilderTest {
	
	private static final File APPLICATION_XML = new File("conf/application.xml");
	private static final File APPLICATION_PROPERTIES = new File("conf/application.properties");
	
	private static Application application = null;
	private static XPathReader xPathReader = null;
	private static Properties properties = null;

	static {
		try {
			application = new Application();
			xPathReader = new XPathReader(APPLICATION_XML);
			properties = new Properties();
			properties.load(new FileInputStream(APPLICATION_PROPERTIES));
		}catch(Exception e) {
			assert(false);
		}
	}
	
	@Test
	public void testBuildWebServers() {
		try {
			ApplicationBuilder applicationBuilder = new ApplicationBuilder();
			applicationBuilder.buildWebServers(application, xPathReader, properties);
			Map<String,WebServer> webServers = application.getWebServers();
			for(String id : webServers.keySet()) {
				WebServer webServer = webServers.get(id);
				System.out.println("+ id:" + id);
				System.out.println("+ webServer:" + webServer);
			}
			assert(true);
		}catch(Exception e) {
			assert(false);
		}
	}

	@Test
	public void testBuildDataSources() {
		try {
			ApplicationBuilder applicationBuilder = new ApplicationBuilder();
			applicationBuilder.buildDataSources(application, xPathReader, properties);
			Map<String,DataSource> dataSources = application.getDataSources();
			for(String id : dataSources.keySet()) {
				DataSource dataSource = dataSources.get(id);
				System.out.println("+ id:" + id);
				System.out.println("+ dataSource:" + dataSource);
			}
			assert(true);
		}catch(Exception e) {
			assert(false);
		}
	}
	
	@Test
	public void testBuildEntityManagerFactories() {
		try {
			ApplicationBuilder applicationBuilder = new ApplicationBuilder();
			applicationBuilder.buildDataSources(application, xPathReader, properties);
			applicationBuilder.buildEntityManagerFactories(application, xPathReader, properties);
			Map<String,LocalContainerEntityManagerFactoryBean> entityManagerFactories = application.getEntityManagerFactories();
			for(String id : entityManagerFactories.keySet()) {
				LocalContainerEntityManagerFactoryBean entityManagerFactory = entityManagerFactories.get(id);
				System.out.println(String.format("+ id[%s]",id));
				System.out.println(String.format("entityManagerFactory[%s]",entityManagerFactory));
				EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
				System.out.println(String.format("entityManager[%s]",entityManager));
				
				User user = new User();
				user.setId("admin");
				user = entityManager.find(User.class, user.getId());
				System.out.println(TextTableBuilder.build(user));
			}
			assert(true);
		}catch(Exception e) {
			e.printStackTrace(System.err);
			assert(false);
		}
	}
	
//	@Test
//	public void getApplication() {
//		try {
//			Application application = ApplicationFactory.getApplication(Application.class, APPLICATION_XML, APPLICATION_PROPERTIES);
//			LOGGER.info(application.toString());
//			assert(true);
//		}catch(Exception e) {
//			LOGGER.error(e.getMessage(),e);
//			assert(false);
//		}
//	}
//	
//	
//	@Test
//	public void test() {
//		try {
//
//			Application application = ApplicationFactory.getApplication(Application.class, APPLICATION_XML, APPLICATION_PROPERTIES);
//			LOGGER.info("application:{}", application.toString());
//			EntityManagerFactory entityManagerFactory = application.getEntityManagerFactory("entityManagerFactory");
//			LOGGER.info("entityManagerFactory:{}", entityManagerFactory);
//			
//			EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//			entityManager.getTransaction().begin();
//			entityManager.getTransaction().rollback();
//			
//			assert(true);
//		}catch(Exception e) {
//			LOGGER.error(e.getMessage(),e);
//			assert(false);
//		}
//	}

}
