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
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.user.User;
import net.oopscraft.application.user.UserService;
import net.oopscraft.application.user.UserStatusCd;
import net.oopscraft.application.user.UserStatusCdService;

@PreAuthorize("hasAuthority('ADMIN_USER')")
@Controller
@RequestMapping("/admin/user")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;
	
	@Autowired
	UserStatusCdService userStatusCdService;

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
	@RequestMapping(value = "getUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUsers(@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "value", required = false) String value,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows) throws Exception {
		UserService.SearchCondition searchCondition = userService.new SearchCondition();
		switch ((key == null ? "" : key)) {
		case "id":
			searchCondition.setId(value);
			break;
		case "name":
			searchCondition.setName(value);
			break;
		case "email":
			searchCondition.setEmail(value);
			break;
		case "phone":
			searchCondition.setPhone(value);
			break;
		}
		PageInfo pageInfo = new PageInfo(page.intValue(), rows.intValue(), true);
		List<User> users = userService.getUsers(searchCondition, pageInfo);
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
	@RequestMapping(value = "getUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUser(@RequestParam(value = "id") String id) throws Exception {
		User user = userService.getUser(id);
		LOGGER.debug("{}", new TextTable(user));
		return JsonUtils.toJson(user);
	}

	/**
	 * Saves user details.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String saveUser(@RequestBody String payload) throws Exception {
		User user = JsonUtils.toObject(payload, User.class);
		user = userService.saveUser(user);
		return JsonUtils.toJson(user);
	}
	
	/**
	 * Removes user.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "removeUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String removeRole(@RequestParam(value = "id") String id) throws Exception {
		User user = userService.removeUser(id);
		return JsonUtils.toJson(user);
	}
	
	/**
	 * getUserStatusCds
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getUserStatusCds", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserStatusCds() throws Exception {
		List<UserStatusCd> userStatusCds = userStatusCdService.getUserStatusCds();
		return JsonUtils.toJson(userStatusCds);
	}

}
