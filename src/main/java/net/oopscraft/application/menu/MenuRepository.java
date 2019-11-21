package net.oopscraft.application.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.menu.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu,String> {
	
	public List<Menu> findByUpperIdIsNullOrderByDisplaySeqAsc() throws Exception;
	
	public List<Menu> findByUpperIdOrderByDisplaySeqAsc(String upperId) throws Exception;

}
