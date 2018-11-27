package net.oopscraft.application.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,String> {

	public Page<Authority> findByIdStartingWith(String id, Pageable pageable) throws Exception;
	
	public Page<Authority> findByNameStartingWith(String name, Pageable pageable) throws Exception;

}
