/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.core.user;

import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import net.oopscraft.application.ApplicationContext;
import net.oopscraft.application.core.SqlSessionProxy;
import net.oopscraft.application.core.SqlSessionProxyFactory;

/**
 * @author chomookun@gmail.com
 *
 */
public class UserManager {
	
	SqlSessionProxyFactory sqlSessionFactory;
	
	public UserManager() throws Exception {
		sqlSessionFactory = ApplicationContext.getInstance().getSqlSessionProxyFactory("application");
	}
	
	public List<User> getUserList(User user, int rows, int page) throws Exception {
		List<User> userList = null;
		SqlSessionProxy sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.openSession();
			UserDao userDao = sqlSession.getMapper(UserDao.class);
			userList = userDao.selectUserList(user, rows, page);
		}catch(Exception e) {
			throw e;
		}finally {
			sqlSession.close();
		}
		return userList;
		
	}
	
	public List<Group> getGroupList() throws Exception {
		List<Group> groupList = null;
		SqlSessionProxy sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.openSession();
			UserDao userDao = sqlSession.getMapper(UserDao.class);
			groupList = userDao.selectGroupList();
		}catch(Exception e) {
			throw e;
		}finally {
			sqlSession.close();
		}
		return groupList;
	}
	

	
	public TreeMap<Group,List<Group>> getGroupTreeMap() throws Exception {
//		SqlSessionProxyFactory sqlSessionFactory = ApplicationContext.getInstance().getSqlSessionProxyFactory("core");
		try {
			//TransactionManager.begin();
			for(int idx = 0; idx < 10; idx ++) {
				//SqlSessionProxy sqlSession = sqlSessionFactory.openSession((idx%2==0?"oltp":"olap"));
				//UserDao userDao = sqlSession.getMapper(UserDao.class);
				User user = new User();
				user.setId(UUID.randomUUID().toString().replaceAll("-",""));
				user.setName("Test User");
				//userDao.insertUser(user);
				//sqlSession.commit();
				//sqlSession.close();
			}
			//TransactionManager.commit();
		}catch(Exception e) {
			e.printStackTrace(System.err);
			//TransactionManager.rollback();
			throw e;
		}finally {
			//TransactionManager.close();
		}
		return null;
	}
	
	private List<Group> getChildGroupList(Group group) throws Exception {
		
		return null;
	}

}
