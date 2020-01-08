package net.oopscraft.application.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
	
	public Page<Role> findAllByOrderBySystemInsertDateDesc(Pageable pageable) throws Exception;

	public Page<Role> findByIdContaining(String id, Pageable pageable) throws Exception;
	
	public Page<Role> findByNameContaining(String name, Pageable pageable) throws Exception;
	
}
