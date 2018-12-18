package net.oopscraft.application.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.oopscraft.application.security.Acl;
import net.oopscraft.application.security.Acl.AclId;
import net.oopscraft.application.user.Role;

public interface AclRepository extends JpaRepository<Acl,AclId>{

	public Page<Acl> findAllByOrderBySystemDataYnDescUriAsc(Pageable pageable) throws Exception;

	public Page<Acl> findByUriStartingWith(String uri, Pageable pageable) throws Exception;
	
	public Page<Acl> findByNameStartingWith(String name, Pageable pageable) throws Exception;
	
}
