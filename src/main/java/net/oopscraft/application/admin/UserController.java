/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.user.UserService;
import net.oopscraft.application.user.entity.User;

@PreAuthorize("hasAuthority('ADMN_USER')")
@Controller
@RequestMapping("/admin/user")
public class UserController {
	
	@Autowired
	HttpServletResponse response;
	
	@Autowired
	UserService userService;
	
	/**
	 * Forwards user management page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/user.html");
		modelAndView.addObject("Status", User.Status.values());
		return modelAndView;
	}
	
	/**
	 * Returns list of users
	 * @param user
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<User> getUsers(@ModelAttribute User user,@ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<User> users = userService.getUsers(user, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return users;
	}
	
	/**
	 * Returns specified user.
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public User getUser(@ModelAttribute User user) throws Exception {
		return userService.getUser(user.getId());
	}
	
	/**
	 * Saves specified user.
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public User saveUser(@RequestBody User user) throws Exception {
		return userService.saveUser(user);
	}
	
	/**
	 * Deletes specified user
	 * @param user
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteUser", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteUser(@RequestBody User user) throws Exception {
		userService.deleteUser(user);
	}

	/**
	 * Changes password
	 * @param payload
	 * @throws Exception
	 */
	@RequestMapping(value = "changePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void changePassword(@RequestBody ValueMap payload) throws Exception {
		String id = payload.getString("id");
		String password = payload.getString("password");
		userService.changePassword(id, password);
	}
	
	/**
	 * Returns available roles
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getAvailableRoles", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<?> getAvailableRoles(@ModelAttribute User user) throws Exception {
		user = userService.getUser(user.getId());
		return user.getAvailableRoles();
	}

	/**
	 * Returns available authorities
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getAvailableAuthorities", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<?> getAvailableAuthorities(@ModelAttribute User user) throws Exception {
		user = userService.getUser(user.getId());
		return user.getAvailableAuthorities();
	}
	
	
}
