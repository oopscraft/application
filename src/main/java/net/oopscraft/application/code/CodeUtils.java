package net.oopscraft.application.code;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import net.oopscraft.application.Application;
import net.oopscraft.application.ApplicationContainer;

public class CodeUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CodeUtils.class);
	
	/**
	 * getCode
	 * @param id
	 * @return
	 */
	public static Code getCode(String id) throws Exception {
		try {
			Application application = ApplicationContainer.getApplication();
			LocalContainerEntityManagerFactoryBean entityManagerFactory = application.getEntityManagerFactory("entityManagerFactory");
			EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
			CodeService codeService = new CodeServiceBuilder().setEntityManager(entityManager).build();
			return codeService.getCode(id);
		}catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}
	
}
