package net.oopscraft.application.code;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import net.oopscraft.application.Application;
import net.oopscraft.application.ApplicationContainer;
import net.oopscraft.application.code.repository.CodeRepository;

public class CodeUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CodeUtils.class);
	
	private static EntityManager entityManager = null;
	private static CodeRepository codeRepository = null;
	
	static {
		try {
			Application application = ApplicationContainer.getApplication();
			LocalContainerEntityManagerFactoryBean entityManagerFactory = application.getEntityManagerFactory("entityManagerFactory");
			entityManager = entityManagerFactory.getObject().createEntityManager();
			codeRepository = new JpaRepositoryFactory(entityManager).getRepository(CodeRepository.class);
		}catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * getCode
	 * @param id
	 * @return
	 */
	public static Code getCode(String id) {
		Code code = codeRepository.findOne(id);
		return code;
	}
	
}
