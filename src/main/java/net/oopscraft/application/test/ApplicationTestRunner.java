package net.oopscraft.application.test;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.runners.model.InitializationError;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import net.oopscraft.application.Application;
import net.oopscraft.application.ApplicationContainer;

public class ApplicationTestRunner {
	
	public EntityManager entityManager;
	
	/**
	 * @param clazz
	 * @throws InitializationError
	 */
	public ApplicationTestRunner() throws Exception {
		try {
			ApplicationContainer.getApplication();
		}catch(Exception e) {
			// prints application error.
			e.printStackTrace(System.err);
		}
	}
	
	@Before
	public final void beforeSuper() throws Exception {
		// Creates entityManager
		Application application = ApplicationContainer.getApplication();
		LocalContainerEntityManagerFactoryBean entityManagerFactory = application.getEntityManagerFactory("entityManagerFactory");
		entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
	}
	
	@After
	public final void afterSuper() {
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	public final EntityManager getEntityManager() {
		return entityManager;
	}
	
	public final <T> T getJpaRepository(Class<T> jpaRepositoryType) {
		return new JpaRepositoryFactory(entityManager).getRepository(jpaRepositoryType);
	}

}

