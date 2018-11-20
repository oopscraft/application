package net.oopscraft.application.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.user.Group;
import net.oopscraft.application.user.User;

public interface UserDao {
	
	public List<User> selectUserList(@Param("user")User user, @Param("rows")int rows, @Param("page")int page) throws Exception;

	public User selectUser(@Param("id")String id) throws Exception;
	
	public int insertUser(@Param("user")User user) throws Exception;
	
	public int updateUser(@Param("user")User user) throws Exception;
	
	public int deleteUser(@Param("id")String id) throws Exception;
	
	public List<Group> selectUserGroupList(@Param("user")User user) throws Exception;

}
