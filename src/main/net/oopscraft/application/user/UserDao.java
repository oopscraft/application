package net.oopscraft.application.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserDao {
	
	public List<User> selectUserList(@Param("key")String key, @Param("value")String value, @Param("rows")int rows, @Param("page")int page) throws Exception;
	
	public User selectUser(@Param("userId")String userId) throws Exception;
	
	public int insertUser(User user) throws Exception;
	
	public List<Group> selectAssignedGroupList(@Param("userId")String userId) throws Exception;
	
	public List<Role> selectAvailableRoleList(@Param("userId")String userId) throws Exception;
	
	public List<Privilege> selectAvailableGroupList(@Param("userId")String userId) throws Exception;

}
