package net.oopscraft.application.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	public List<User> findByIdLike(String id) throws Exception;
	
	public List<User> findByNameLike(String name) throws Exception;

}
