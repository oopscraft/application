package net.oopscraft.application.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
	
	public Page<Role> findAllByOrderBySystemDataYnDescSystemInsertDateDesc(Pageable pageable) throws Exception;

	public Page<Role> findByIdStartingWith(String id, Pageable pageable) throws Exception;
	
	public Page<Role> findByNameStartingWith(String name, Pageable pageable) throws Exception;
	
}
