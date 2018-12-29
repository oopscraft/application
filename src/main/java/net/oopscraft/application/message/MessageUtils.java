package net.oopscraft.application.message;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import net.oopscraft.application.Application;
import net.oopscraft.application.ApplicationContainer;

public class MessageUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtils.class);
	
	/**
	 * getProperty
	 * @param id
	 * @return
	 */
	public static Message getMessage(String id) throws Exception {
		EntityManager entityManager = null;
		try {
			Application application = ApplicationContainer.getApplication();
			LocalContainerEntityManagerFactoryBean entityManagerFactory = application.getEntityManagerFactory("entityManagerFactory");
			entityManager = entityManagerFactory.getObject().createEntityManager();
			MessageService messageService = new MessageServiceBuilder().setEntityManager(entityManager).build();
			return messageService.getMessage(id);
		}catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}finally {
			if(entityManager != null) {
				entityManager.close();
			}
		}
	}
	
}
