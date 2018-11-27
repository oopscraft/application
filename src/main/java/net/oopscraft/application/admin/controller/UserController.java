/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.user.User;
import net.oopscraft.application.user.UserService;

@Controller
@RequestMapping("/admin/user")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;

	@Autowired
	HttpServletResponse response;

	/**
	 * Forwards user page
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView user() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/user.tiles");
		return modelAndView;
	}

	/**
	 * Gets users
	 * 
	 * @param key
	 * @param value
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getUsers", method = RequestMethod.GET)
	@ResponseBody
	@Transactional
	public String getUsers(@RequestParam(value = "searchKey", required = false) String searchKey,
			@RequestParam(value = "searchValue", required = false) String searchValue,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows) throws Exception {
		PageInfo pageInfo = new PageInfo(page.intValue(), rows.intValue(), true);
		List<User> users = userService.getUsers(null, null, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonUtils.toJson(users);
	}

	/**
	 * Gets user details.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getUser", method = RequestMethod.GET)
	@ResponseBody
	@Transactional
	public String getUser(@RequestParam(value = "id") String id) throws Exception {
		User user = userService.getUser(id);
		return JsonUtils.toJson(user);
	}

	/**
	 * Saves user details.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveUser", method = RequestMethod.POST)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String saveUser(@RequestBody String payload) throws Exception {
		User user = JsonUtils.toObject(payload, User.class);
		user = userService.saveUser(user);
		return JsonUtils.toJson(user);
	}

	// @RequestMapping(value="getUserGroups", method=RequestMethod.GET)
	// public String getUserGroups(@RequestParam(value="id")String id) throws
	// Exception {
	// List<Group> userGroups = userService.get
	// return null;
	// }

	// @Autowired
	// GroupService groupService;

	// @RequestMapping(value="", method=RequestMethod.GET)
	// public ModelAndView user() throws Exception {
	// ModelAndView modelAndView = new ModelAndView("admin/userManage.tiles");
	// return modelAndView;
	// }
	//
	// /**
	// * getUserList
	// * @param key
	// * @param value
	// * @param rows
	// * @param page
	// * @return
	// * @throws Exception
	// */
	// @RequestMapping(value="/getUserList", method=RequestMethod.GET,
	// produces="application/json")
	// public ResponseEntity<?> getUserList(
	// @RequestParam(value="key",required=false)String key
	// ,@RequestParam(value="value",required=false)String value
	// ,@RequestParam(value="rows",defaultValue="30")Integer rows
	// ,@RequestParam(value="page",defaultValue="1")Integer page
	// ) throws Exception {
	// User user = new User();
	// if("id".equals(key)) {
	// user.setId(value);
	// }else if("email".equals(key)) {
	// user.setEmail(value);
	// }else if("mobile".equals(key)) {
	// user.setMobile(value);
	// }else if("name".equals(key)) {
	// user.setName(value);
	// }else if("nickname".equals(key)) {
	// user.setNickname(value);
	// }
	// List<User> userList = userService.getUserList(user, rows, page);
	// return new ResponseEntity<>(JsonConverter.convertObjectToJson(userList),
	// HttpStatus.OK);
	// }
	//
	// /**
	// * getUser
	// * @param id
	// * @return
	// * @throws Exception
	// */
	// @RequestMapping(value="/getUser", method=RequestMethod.GET,
	// produces="application/json")
	// public ResponseEntity<?> getUser(
	// @RequestParam(value="id")String id
	// ) throws Exception {
	// User user = userService.getUser(id);
	// return new ResponseEntity<>(JsonConverter.convertObjectToJson(user),
	// HttpStatus.OK);
	// }
	//
	// /**
	// * getUserGroupList
	// * @param id
	// * @return
	// * @throws Exception
	// */
	// @RequestMapping(value="/getUserGroupList", method=RequestMethod.GET,
	// produces="application/json")
	// public ResponseEntity<?> getUserGroupList(
	// @RequestParam(value="id")String id
	// ) throws Exception {
	// User user = new User();
	// user.setId(id);
	// List<Group> userGroupList = userService.getUserGroupList(user);
	// return new ResponseEntity<>(JsonConverter.convertObjectToJson(userGroupList),
	// HttpStatus.OK);
	// }
	//
	// /**
	// * saveUser
	// * @param payload
	// * @return
	// * @throws Exception
	// */
	// @RequestMapping(value="/saveUser", method=RequestMethod.POST,
	// produces="application/json")
	// public ResponseEntity<?> saveUser(
	// @RequestBody String payload
	// ) throws Exception {
	// User user = JsonConverter.convertJsonToObject(payload, User.class);
	// user = userService.saveUser(user);
	// return new ResponseEntity<>(JsonConverter.convertObjectToJson(user),
	// HttpStatus.OK);
	// }
	//
	// /**
	// * removeUser
	// * @param id
	// * @return
	// * @throws Exception
	// */
	// @RequestMapping(value="/removeUser", method=RequestMethod.POST,
	// produces="application/json")
	// public ResponseEntity<?> removeUser(
	// @RequestParam(value="id")String id
	// ) throws Exception {
	// User user = userService.removeUser(id);
	// return new ResponseEntity<>(JsonConverter.convertObjectToJson(user),
	// HttpStatus.OK);
	// }
	//
	// /**
	// * getGroupList
	// * @return
	// * @throws Exception
	// */
	// @RequestMapping(value="/getGroupList", method=RequestMethod.GET,
	// produces="application/json")
	// public ResponseEntity<?> getGroupList() throws Exception {
	// List<Group> groupList = groupService.getGroupList();
	// return new ResponseEntity<>(JsonConverter.convertObjectToJson(groupList),
	// HttpStatus.OK);
	// }
}
