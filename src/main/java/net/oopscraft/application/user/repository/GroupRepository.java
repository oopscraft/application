package net.oopscraft.application.user.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group,String> {
	
	public Page<Group> findByUpperIdIsNull(Pageable pageable) throws Exception;

	public Page<Group> findByIdLike(String id, Pageable pageable) throws Exception;
	
	public Page<Group> findByNameLike(String name, Pageable pageable) throws Exception;
	
	public List<Group> findByUpperId(String upperId) throws Exception;

}
