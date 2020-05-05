package net.oopscraft.application.page;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page, String>, JpaSpecificationExecutor<Page> {
	
}
