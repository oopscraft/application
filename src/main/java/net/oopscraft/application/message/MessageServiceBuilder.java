package net.oopscraft.application.message;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import net.oopscraft.application.message.repository.MessageRepository;

public class MessageServiceBuilder {

	EntityManager entityManager;
	
	public MessageServiceBuilder setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
		return this;
	}
	
	public MessageService build() {
		MessageService messageService = new MessageService();
		JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);
		messageService.messageRepository = jpaRepositoryFactory.getRepository(MessageRepository.class);
		return messageService;
	}
}
