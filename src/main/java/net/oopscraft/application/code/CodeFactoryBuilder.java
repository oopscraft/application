package net.oopscraft.application.code;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import net.oopscraft.application.code.repository.CodeRepository;

public class CodeFactoryBuilder {

	EntityManager entityManager;
	
	public CodeFactoryBuilder setEntityManagerFactory(EntityManager entityManager) {
		this.entityManager = entityManager;
		return this;
	}
	
	public CodeFactory build() {
		CodeFactory codeFactory = new CodeFactory();
		JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);
		codeFactory.codeRepository = jpaRepositoryFactory.getRepository(CodeRepository.class);
		return codeFactory;
	}
}
