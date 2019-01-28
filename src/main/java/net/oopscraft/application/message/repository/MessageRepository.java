package net.oopscraft.application.message.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.oopscraft.application.message.Message;

public interface MessageRepository extends JpaRepository<Message,String>{

	public Page<Message> findAllByOrderBySystemDataYn(Pageable pageable) throws Exception;

	public Page<Message> findByIdContaining(String id, Pageable pageable) throws Exception;
	
	public Page<Message> findByNameContaining(String name, Pageable pageable) throws Exception;
	
}
