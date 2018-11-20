package net.oopscraft.application.user.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.oopscraft.application.user.User;

public interface UserMapper {
	
	public List<User> selectUserList(@Param("user")User user) throws SQLException;

	public int insertUser(@Param("user")User user) throws SQLException;
	
}
