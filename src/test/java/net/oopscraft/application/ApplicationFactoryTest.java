package net.oopscraft.application;


import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationFactoryTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationFactoryTest.class);
	private static final File APPLICATION_XML = new File("conf/application.xml");
	private static final File APPLICATION_PROPERTIES = new File("conf/application.properties");
	
	@Test
	public void getApplication() {
		try {
			Application application = ApplicationFactory.getApplication(Application.class, APPLICATION_XML, APPLICATION_PROPERTIES);
			LOGGER.info(application.toString());
			assert(true);
		}catch(Exception e) {
			LOGGER.error(e.getMessage(),e);
			assert(false);
		}
	}
	
	
	@Test
	public void test() {
		try {

			Application application = ApplicationFactory.getApplication(Application.class, APPLICATION_XML, APPLICATION_PROPERTIES);
			LOGGER.info("application:{}", application.toString());
			EntityManagerFactory entityManagerFactory = application.getEntityManagerFactory("entityManagerFactory");
			LOGGER.info("entityManagerFactory:{}", entityManagerFactory);
			
			EntityManager entityManager = entityManagerFactory.createEntityManager();

			entityManager.getTransaction().begin();
			entityManager.getTransaction().rollback();
			
			assert(true);
		}catch(Exception e) {
			LOGGER.error(e.getMessage(),e);
			assert(false);
		}
	}

}
