package net.oopscraft.application.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.oopscraft.application.message.entity.Message;

public interface MessageRepository extends JpaRepository<Message,String>{

	public Page<Message> findAll(Pageable pageable);

	public Page<Message> findByIdContaining(String id, Pageable pageable) throws Exception;
	
	public Page<Message> findByNameContaining(String name, Pageable pageable) throws Exception;
	
}
