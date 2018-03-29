package net.oopscraft.application.core.security.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.oopscraft.application.core.security.Group;
import net.oopscraft.application.core.security.User;

public interface UserDao {
	
	public List<User> selectUserList(@Param("user")User user, @Param("rows")int rows, @Param("page")int page) throws Exception;

	public User selectUser(@Param("id")String id) throws Exception;
	
	public int insertUser(@Param("user")User user) throws Exception;
	
	public int updateUser(@Param("user")User user) throws Exception;
	
	public int deleteUser(@Param("id")String id) throws Exception;
	
	public List<Group> selectUserGroupList(@Param("user")User user) throws Exception;

	public List<Group> selectGroupList() throws Exception;

	public User selectGroup(@Param("id")String id) throws Exception;
	
	public int insertGroup(@Param("group")Group group) throws Exception;
	
	public int updateGroup(@Param("Group")Group group) throws Exception;
	
	public int deleteGroup(@Param("id")String id) throws Exception;

}
