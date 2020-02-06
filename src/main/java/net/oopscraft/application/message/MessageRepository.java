package net.oopscraft.application.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.message.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message,String>,JpaSpecificationExecutor<Message> {
	
}
