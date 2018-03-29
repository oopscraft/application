package net.oopscraft.application.core.security;

import java.util.Date;
import java.util.List;

import net.oopscraft.application.ApplicationContext;
import net.oopscraft.application.core.SqlSessionProxy;
import net.oopscraft.application.core.SqlSessionProxyFactory;
import net.oopscraft.application.core.security.dao.UserDao;

public class User {

	String id;
	String email;
	String mobile;
	String name;
	String nickname;
	String password;
	String image;
	String message;
	String description;
	String statusCode;
	Date joinDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	
	public void save() throws Exception {
		save(this);
	}
	
	public static void save(User user) throws Exception {
		SqlSessionProxyFactory sqlSessionFactory = ApplicationContext.getInstance().getSqlSessionProxyFactory("application");
		SqlSessionProxy sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.openSession();
			UserDao userDao = sqlSession.getMapper(UserDao.class);
			if(userDao.selectUser(user.getId()) == null) {
				userDao.insertUser(user);
			}else {
				userDao.updateUser(user);
			}
			sqlSession.commit();
		}catch(Exception e) {
			sqlSession.rollback();
			throw e;
		}finally {
			sqlSession.close();
		}
	}
	
	public List<Group> getGroupList() {
		return null;
	}

}
