package net.oopscraft.application.core.jpa;

import javax.persistence.EntityManager;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import net.oopscraft.application.Application;
import net.oopscraft.application.ApplicationContainer;

public class EntityManagerUtils {
	
	/**
	 * Returns entityManager instance.
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static EntityManager getEntityManager(String id) throws Exception {
		Application application = ApplicationContainer.getApplication();
		LocalContainerEntityManagerFactoryBean entityManagerFactory = application.getEntityManagerFactory(id);
		EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		return entityManager;
	}

}
