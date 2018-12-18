package net.oopscraft.application.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.menu.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu,String> {
	
	public List<Menu> findByUpperIdIsNullOrderByDisplaySeqAsc() throws Exception;
	
	public List<Menu> findByUpperIdOrderByDisplaySeqAsc(String upperId) throws Exception;

}
