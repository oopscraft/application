/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.user;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import net.oopscraft.application.ApplicationContext;
import net.oopscraft.application.core.mybatis.SqlSessionProxy;
import net.oopscraft.application.core.mybatis.SqlSessionProxyFactory;
import net.oopscraft.application.user.dao.GroupDao;
import net.oopscraft.application.user.dao.UserDao;

/**
 * @author chomookun@gmail.com
 *
 */
@Service
public class GroupService {
	
	private static Log LOG = LogFactory.getLog(GroupService.class);
	SqlSessionProxyFactory sqlSessionFactory;
	
	public GroupService() throws Exception {
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
	public List<Group> getGroupList() throws Exception {
		List<Group> groupList = null;
		SqlSessionProxy sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.openSession();
			GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
			groupList = groupDao.selectGroupList();
		}catch(Exception e) {
			LOG.error(e.getMessage(),e);
			sqlSession.rollback();
			throw e;
		}finally {
			sqlSession.close();
		}
		return groupList;
	}
	
	/**
	 * getting user
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public User getGroup(String id) throws Exception {
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
	public User saveGroup(User user) throws Exception {
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
	public User removeGroup(String id) throws Exception {
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

}
