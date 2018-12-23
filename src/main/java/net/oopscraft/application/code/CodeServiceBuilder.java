package net.oopscraft.application.code;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import net.oopscraft.application.code.repository.CodeRepository;

public class CodeServiceBuilder {

	EntityManager entityManager;
	
	public CodeServiceBuilder setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
		return this;
	}
	
	public CodeService build() {
		CodeService codeService = new CodeService();
		JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);
		codeService.codeRepository = jpaRepositoryFactory.getRepository(CodeRepository.class);
		return codeService;
	}
}
