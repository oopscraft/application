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

import net.oopscraft.application.core.JsonUtility;
import net.oopscraft.application.core.LocaleUtils;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.StringUtility;
import net.oopscraft.application.user.User;
import net.oopscraft.application.user.UserService;
import net.oopscraft.application.user.UserService.UserSearchType;

@PreAuthorize("hasAuthority('ADMIN_USER')")
@Controller
@RequestMapping("/admin/user")
public class UserController {

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
		modelAndView.addObject("locales", LocaleUtils.getLocales());
		modelAndView.addObject("statuses", User.Status.values());
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
	public String getUsers(
		@RequestParam(value = "rows", required = false, defaultValue = "10")Integer rows,
		@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
		@RequestParam(value = "searchType", required = false) String searchType,
		@RequestParam(value = "searchValue", required = false) String searchValue
	) throws Exception {
		PageInfo pageInfo = new PageInfo(rows, page, true);
		UserSearchType userSearchType= null;
		if(StringUtility.isNotEmpty(searchType)) {
			userSearchType = UserSearchType.valueOf(searchType);
		}
		List<User> users = userService.getUsers(pageInfo, userSearchType, searchValue);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonUtility.toJson(users);
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
		return JsonUtility.toJson(user);
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
		User user = JsonUtility.toObject(payload, User.class);
		user = userService.saveUser(user);
		return JsonUtility.toJson(user);
	}
	
	/**
	 * Deletes user.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteUser(@RequestParam(value = "id") String id) throws Exception {
		userService.deleteUser(id);
	}

}
