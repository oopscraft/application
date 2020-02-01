package net.oopscraft.application.code;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.code.entity.Code;

@Repository
public interface CodeRepository extends JpaRepository<Code,String>, JpaSpecificationExecutor<Code> {
	
}
