package net.oopscraft.application.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.UserStatus;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, String> {
	
	public List<UserStatus> findAllByOrderByDisplaySeqAsc() throws Exception;
	
}
