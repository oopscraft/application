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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.core.user.User;
import net.oopscraft.application.core.user.UserDao;
import net.oopscraft.application.core.user.UserManager;

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
		ModelAndView modelAndView = new ModelAndView("console/user.tiles");
		return modelAndView;
	}
	
	@RequestMapping(value="/getUserList", method=RequestMethod.GET, produces="application/json")
	//@Transactional
	public ResponseEntity<?> getUserList(
			 @RequestParam(value="key",required=false)String key
			,@RequestParam(value="value",required=false)String value
			,@RequestParam(value="rows",defaultValue="20")Integer rows
			,@RequestParam(value="page",defaultValue="1")Integer page
	) throws Exception {
		try {
			UserManager userManager = new UserManager();
			User user = new User();
			if("id".equals(key)) {
				user.setId(value);
			}else if("email".equals(key)) {
				user.setEmail(value);
			}else if("mobileNumber".equals(key)) {
				user.setMobileNumber(value);
			}else if("name".equals(key)) {
				user.setName(value);
			}else if("nickname".equals(key)) {
				user.setNickname(value);
			}
			List<User> userList = userManager.getUserList(user, 20, 1);
			return new ResponseEntity<>(JsonConverter.convertObjectToJson(userList), HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/getUser", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getUser(
			 @RequestParam(value="userId")String userId
	) throws Exception {
		return new ResponseEntity<>(new ValueMap(), HttpStatus.OK);
	}
	
}
