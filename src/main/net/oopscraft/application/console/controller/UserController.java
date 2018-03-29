/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.console.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.security.Group;
import net.oopscraft.application.core.security.User;
import net.oopscraft.application.core.security.UserFactory;
import net.oopscraft.application.core.security.dao.UserDao;

/**
 * @author chomookun@gmail.com
 *
 */
@Controller
@RequestMapping("/console/user")
public class UserController {
	
	@Autowired
	UserDao userDao;

	@RequestMapping(value="", method=RequestMethod.GET)
	public ModelAndView user() throws Exception {
		ModelAndView modelAndView = new ModelAndView("console/User.tiles");
		return modelAndView;
	}

	/**
	 * getUserList
	 * @param key
	 * @param value
	 * @param rows
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getUserList", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getUserList(
		 @RequestParam(value="key",required=false)String key
		,@RequestParam(value="value",required=false)String value
		,@RequestParam(value="rows",defaultValue="30")Integer rows
		,@RequestParam(value="page",defaultValue="1")Integer page
	) throws Exception {
		UserFactory userManager = new UserFactory();
		User user = new User();
		if("id".equals(key)) {
			user.setId(value);
		}else if("email".equals(key)) {
			user.setEmail(value);
		}else if("mobile".equals(key)) {
			user.setMobile(value);
		}else if("name".equals(key)) {
			user.setName(value);
		}else if("nickname".equals(key)) {
			user.setNickname(value);
		}
		List<User> userList = userManager.getUserList(user, rows, page);
		return new ResponseEntity<>(JsonConverter.convertObjectToJson(userList), HttpStatus.OK);
	}
	
	/**
	 * getUser
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getUser", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getUser(
		 @RequestParam(value="id")String id
	) throws Exception {
		UserFactory userManager = new UserFactory();
		User user = userManager.getUser(id);
		return new ResponseEntity<>(JsonConverter.convertObjectToJson(user), HttpStatus.OK);
	}
	
	/**
	 * getUserGroupList
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getUserGroupList", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getUserGroupList(
		@RequestParam(value="id")String id
	) throws Exception {
		UserFactory userManager = new UserFactory();
		User user = new User();
		user.setId(id);
		List<Group> userGroupList = userManager.getUserGroupList(user);
		return new ResponseEntity<>(JsonConverter.convertObjectToJson(userGroupList), HttpStatus.OK);
	}

	/**
	 * saveUser
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveUser", method=RequestMethod.POST, produces="application/json")
	public ResponseEntity<?> saveUser(
		@RequestBody String payload
	) throws Exception {
		User user = JsonConverter.convertJsonToObject(payload, User.class);
		UserFactory userManager = new UserFactory();
		user = userManager.saveUser(user);
		return new ResponseEntity<>(JsonConverter.convertObjectToJson(user), HttpStatus.OK);
	}
	
	/**
	 * removeUser
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/removeUser", method=RequestMethod.POST, produces="application/json")
	public ResponseEntity<?> removeUser(
		@RequestParam(value="id")String id
	) throws Exception {
		UserFactory userManager = new UserFactory();
		User user = userManager.removeUser(id);
		return new ResponseEntity<>(JsonConverter.convertObjectToJson(user), HttpStatus.OK);
	}
	
	/**
	 * getGroupList
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getGroupList", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getGroupList() throws Exception {
		UserFactory userManager = new UserFactory();
		List<Group> groupList = userManager.getGroupList();
		return new ResponseEntity<>(JsonConverter.convertObjectToJson(groupList), HttpStatus.OK);
	}
}
