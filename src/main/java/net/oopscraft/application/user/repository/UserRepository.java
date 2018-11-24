package net.oopscraft.application.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	public Page<User> findByIdLike(String id, Pageable pageable) throws Exception;
	
	public Page<User> findByNameLike(String name, Pageable pageable) throws Exception;
	
	public Page<User> findByEmailLike(String id, Pageable pageable) throws Exception;
	
	public Page<User> findByPhoneLike(String id, Pageable pageable) throws Exception;

}
