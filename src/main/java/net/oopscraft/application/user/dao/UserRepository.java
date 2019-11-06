package net.oopscraft.application.user.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	public Page<User> findAllByOrderBySystemDataYnDescJoinDateDesc(Pageable pageable) throws Exception;
	
	public Page<User> findByIdStartingWith(String id, Pageable pageable) throws Exception;
	
	public Page<User> findByNameStartingWith(String name, Pageable pageable) throws Exception;
	
	public Page<User> findByEmailStartingWith(String email, Pageable pageable) throws Exception;
	
	public Page<User> findByPhoneStartingWith(String phone, Pageable pageable) throws Exception;

}