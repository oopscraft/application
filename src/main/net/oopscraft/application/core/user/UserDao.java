package net.oopscraft.application.core.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserDao {
	
	public List<User> selectUserList(@Param("user")User user, @Param("rows")int rows, @Param("page")int page) throws Exception;

	public User selectUser(@Param("id")String id) throws Exception;
	
	public int insertUser(@Param("user")User user) throws Exception;
	
	public int updateUser(@Param("user")User user) throws Exception;
	
	public int deleteUser(@Param("id")String id) throws Exception;
	
	public List<Group> selectUserGroupList(@Param("user")User user) throws Exception;
	
	
	
	
	
	
	public List<Group> selectGroupList() throws Exception;
	
	public List<Group> selectChildGroupList(@Param("id")String id) throws Exception;
	
	public Group selectGroup(@Param("id")String id) throws Exception;
	
	public int insertGroup(Group group) throws Exception;
	
	public int updateGroup(Group group) throws Exception;
	
	public int deleteGroup(Group group) throws Exception;
	
	public Role selectRole(@Param("id")String id) throws Exception;
	
	public int insertRole(Role role) throws Exception;
	
	public int updateRole(Role role) throws Exception;
	
	public int deleteRole(Role role) throws Exception;
	
	public Privilege selectPrivilege(@Param("id")String id) throws Exception;
	
	public int insertPrivilege(Privilege privilege) throws Exception;
	
	public int updatePrivilege(Privilege privilege) throws Exception;
	
	public int deletePrivilege(Privilege privilege) throws Exception;

	public List<Group> selectAssignedGroupList(@Param("userId")String userId) throws Exception;
	
	public List<Role> selectAvailableRoleList(@Param("userId")String userId) throws Exception;
	
	public List<Privilege> selectAvailablePrivilegeList(@Param("userId")String userId) throws Exception;

}
