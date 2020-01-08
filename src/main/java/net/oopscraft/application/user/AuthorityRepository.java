package net.oopscraft.application.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.entity.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,String> {
	
	public Page<Authority> findAllByOrderBySystemInsertDateDesc(Pageable pageable) throws Exception;

	public Page<Authority> findByIdContaining(String id, Pageable pageable) throws Exception;
	
	public Page<Authority> findByNameContaining(String name, Pageable pageable) throws Exception;

}
