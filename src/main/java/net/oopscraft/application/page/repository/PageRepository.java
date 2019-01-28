package net.oopscraft.application.page.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.page.Page;

@Repository
public interface PageRepository extends JpaRepository<Page, String> {
	
	public org.springframework.data.domain.Page<Page> findByIdContaining(String id, Pageable pageable) throws Exception;
	
	public org.springframework.data.domain.Page<Page> findByNameContaining(String name, Pageable pageable) throws Exception;

}
