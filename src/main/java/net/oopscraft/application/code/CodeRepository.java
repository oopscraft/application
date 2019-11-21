package net.oopscraft.application.code;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.code.entity.Code;

@Repository
public interface CodeRepository extends JpaRepository<Code,String> {
	
	public Page<Code> findAllByOrderBySystemDataYn(Pageable pageable) throws Exception;

	public Page<Code> findByIdContaining(String id, Pageable pageable) throws Exception;
	
	public Page<Code> findByNameContaining(String name, Pageable pageable) throws Exception;
	
}
