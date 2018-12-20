package net.oopscraft.application.message;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import net.oopscraft.application.Application;
import net.oopscraft.application.ApplicationContainer;
import net.oopscraft.application.message.repository.MessageRepository;

public class MessageUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtils.class);
	
	private static EntityManager entityManager = null;
	private static MessageRepository messageRepository = null;
	
	static {
		try {
			Application application = ApplicationContainer.getApplication();
			LocalContainerEntityManagerFactoryBean entityManagerFactory = application.getEntityManagerFactory("entityManagerFactory");
			entityManager = entityManagerFactory.getObject().createEntityManager();
			messageRepository = new JpaRepositoryFactory(entityManager).getRepository(MessageRepository.class);
		}catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * getProperty
	 * @param id
	 * @return
	 */
	public static Message getMessage(String id) {
		Message property = messageRepository.findOne(id);
		return property;
	}
	
}
