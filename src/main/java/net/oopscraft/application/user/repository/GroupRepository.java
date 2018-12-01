package net.oopscraft.application.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group,String> {
	
	public List<Group> findByUpperIdIsNullOrderByDisplaySeqAsc() throws Exception;
	
	public List<Group> findByUpperIdOrderByDisplaySeqAsc(String upperId) throws Exception;

}
