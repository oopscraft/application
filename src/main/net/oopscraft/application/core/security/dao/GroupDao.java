package net.oopscraft.application.core.security.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.oopscraft.application.core.security.Group;
import net.oopscraft.application.core.security.User;

public interface GroupDao {
	
	public List<Group> selectGroupList() throws Exception;

	public User selectGroup(@Param("id")String id) throws Exception;
	
	public int insertGroup(@Param("group")Group group) throws Exception;
	
	public int updateGroup(@Param("group")Group group) throws Exception;
	
	public int deleteGroup(@Param("id")String id) throws Exception;

}
