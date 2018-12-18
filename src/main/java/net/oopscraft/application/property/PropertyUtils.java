package net.oopscraft.application.property;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import net.oopscraft.application.Application;
import net.oopscraft.application.ApplicationContainer;
import net.oopscraft.application.core.PasswordBasedEncryptor;
import net.oopscraft.application.property.repository.PropertyRepository;

public class PropertyUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtils.class);
	
	private static EntityManager entityManager = null;
	private static PropertyRepository propertyRepository = null;
	
	static {
		try {
			Application application = ApplicationContainer.getApplication();
			LocalContainerEntityManagerFactoryBean entityManagerFactory = application.getEntityManagerFactory("entityManagerFactory");
			entityManager = entityManagerFactory.getObject().createEntityManager();
			propertyRepository = new JpaRepositoryFactory(entityManager).getRepository(PropertyRepository.class);
		}catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * getProperty
	 * @param id
	 * @return
	 */
	public static Property getProperty(String id) {
		Property property = propertyRepository.findOne(id);
		return property;
	}

	/**
	 * getValue
	 * @param id
	 * @return
	 */
	public static String getValue(String id) {
		Property property = propertyRepository.findOne(id);
		if(property != null && property.getValue() != null) {
			return property.getValue();
		}else {
			return null;
		}
	}
	
}
