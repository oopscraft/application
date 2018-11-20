package net.oopscraft.application.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group,String> {
	
	public List<Group> findByUpperId(String upperId) throws Exception;

	public List<Group> findByIdLike(String id) throws Exception;
	
	public List<Group> findByNameLike(String name) throws Exception;
	
}
