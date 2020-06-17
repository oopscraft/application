package net.oopscraft.application.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu,String>, JpaSpecificationExecutor<Menu> {

	@Query("select a from Menu a where a.upperId is null order by a.sequence")
	public List<Menu> findRootMenus() throws Exception;
	
	@Query("select a from Menu a where a.upperId = :id order by a.sequence")
	public List<Menu> findChildMenus(@Param("id")String id) throws Exception;
	
	@Query("select max(a.sequence) from Menu a where a.upperId is null")
	public Integer findRootMaxSequence() throws Exception;
	
	@Query("select max(a.sequence) from Menu a where a.upperId = :id")
	public Integer findChildMaxSequence(@Param("id")String id) throws Exception;
	
}
