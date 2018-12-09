package net.oopscraft.application.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.UserStatusCd;

@Repository
public interface UserStatusCdRepository extends JpaRepository<UserStatusCd, String> {
	
	public List<UserStatusCd> findAllByOrderByDisplaySeqAsc() throws Exception;
	
}
