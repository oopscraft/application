/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.core.security;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import net.oopscraft.application.ApplicationContext;
import net.oopscraft.application.core.SqlSessionProxy;
import net.oopscraft.application.core.SqlSessionProxyFactory;
import net.oopscraft.application.core.security.dao.UserDao;

/**
 * @author chomookun@gmail.com
 *
 */
@Service
public class UserService {
	
	private static Log LOG = LogFactory.getLog(UserService.class);
	SqlSessionProxyFactory sqlSessionFactory;
	
	public UserService() throws Exception {
		sqlSessionFactory = ApplicationContext.getInstance().getSqlSessionProxyFactory("application");
	}
	
	/**
	 * getting user list
	 * @param user
	 * @param rows
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<User> getUserList(User user, int rows, int page) throws Exception {
		List<User> userList = null;
		SqlSessionProxy sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.openSession();
			UserDao userDao = sqlSession.getMapper(UserDao.class);
			userList = userDao.selectUserList(user, rows, page);
		}catch(Exception e) {
			LOG.error(e.getMessage(),e);
			sqlSession.rollback();
			throw e;
		}finally {
			sqlSession.close();
		}
		return userList;
	}
	
	/**
	 * getting user
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public User getUser(String id) throws Exception {
		User user = null;
		SqlSessionProxy sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.openSession();
			UserDao userDao = sqlSession.getMapper(UserDao.class);
			user = userDao.selectUser(id);
		}catch(Exception e) {
			LOG.error(e.getMessage(),e);
			sqlSession.rollback();
			throw e;
		}finally {
			sqlSession.close();
		}
		return user;
	}
	
	/**
	 * saveUser
	 * @param user
	 * @return User
	 * @throws Exception
	 */
	public User saveUser(User user) throws Exception {
		SqlSessionProxy sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.openSession();
			UserDao userDao = sqlSession.getMapper(UserDao.class);
			if(userDao.selectUser(user.getId()) == null) {
				userDao.insertUser(user);
			}else {
				userDao.updateUser(user);
			}
			user = userDao.selectUser(user.getId());
			sqlSession.commit();
		}catch(Exception e) {
			LOG.error(e.getMessage(),e);
			sqlSession.rollback();
			throw e;
		}finally {
			sqlSession.close();
		}
		return user;
	}
	
	/**
	 * removeUser
	 * @param id
	 * @return User
	 * @throws Exception
	 */
	public User removeUser(String id) throws Exception {
		User user = null;
		SqlSessionProxy sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.openSession();
			UserDao userDao = sqlSession.getMapper(UserDao.class);
			user = userDao.selectUser(id);
			userDao.deleteUser(id);
			sqlSession.commit();
		}catch(Exception e) {
			LOG.error(e.getMessage(),e);
			sqlSession.rollback();
			throw e;
		}finally {
			sqlSession.close();
		}
		return user;
	}

	/**
	 * getUserGroupList
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Group> getUserGroupList(User user) throws Exception {
		List<Group> userGroupList = null;
		SqlSessionProxy sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.openSession();
			UserDao userDao = sqlSession.getMapper(UserDao.class);
			userGroupList = userDao.selectUserGroupList(user);
		}catch(Exception e) {
			LOG.error(e.getMessage(),e);
			sqlSession.rollback();
			throw e;
		}finally {
			sqlSession.close();
		}
		return userGroupList;
	}


}