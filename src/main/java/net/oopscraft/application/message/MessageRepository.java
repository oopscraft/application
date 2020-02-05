package net.oopscraft.application.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.oopscraft.application.message.entity.Message;

public interface MessageRepository extends JpaRepository<Message,String>,JpaSpecificationExecutor<Message> {
	
}
