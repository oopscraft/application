package net.oopscraft.application.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group,String>, JpaSpecificationExecutor<Group> {
	
	@Query("select a from Group a where a.upperId is null order by a.sequence")
	public List<Group> findRootGroups() throws Exception;
	
	@Query("select a from Group a where a.upperId = :id order by a.sequence")
	public List<Group> findChildGroups(@Param("id")String id) throws Exception;
	
	@Query("select max(a.sequence) from Group a where a.upperId is null")
	public Integer findRootMaxSequence() throws Exception;
	
	@Query("select max(a.sequence) from Group a where a.upperId = :id")
	public Integer findChildMaxSequence(@Param("id")String id) throws Exception;

}
