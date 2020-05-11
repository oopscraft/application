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
import javax.validation.Valid;

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

import com.fasterxml.jackson.annotation.JsonView;

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.user.Authority;
import net.oopscraft.application.user.User;
import net.oopscraft.application.user.UserService;

@PreAuthorize("hasAuthority('ADMN_USER')")
@Controller
@RequestMapping("/admin/user")
public class UserController {
	
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
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@JsonView(List.class)
	public List<User> getUsers(@ModelAttribute User user,@ModelAttribute Pagination pagination, HttpServletResponse response) throws Exception {
		pagination.setEnableTotalCount(true);
		List<User> users = userService.getUsers(user, pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
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
	 * Return user by email
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getUserByEmail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public User getUserByEmail(@ModelAttribute User user) throws Exception {
		return userService.getUserByEmail(user.getEmail());
	}
	
	
	/**
	 * Saves specified user.
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_USER_EDIT')")
	@RequestMapping(value = "saveUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public User saveUser(@RequestBody @Valid User user) throws Exception {
		return userService.saveUser(user);
	}
	
	/**
	 * Deletes specified user
	 * @param user
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_USER_EDIT')")
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
	@PreAuthorize("hasAuthority('ADMN_USER_EDIT')")
	@RequestMapping(value = "changePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void changePassword(@RequestBody ValueMap payload) throws Exception {
		String id = payload.getString("id");
		String password = payload.getString("password");
		userService.changePassword(id, password);
	}

	/**
	 * Returns available authorities
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getAvailableAuthorities", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Authority> getAvailableAuthorities(@ModelAttribute User user) throws Exception {
		user = userService.getUser(user.getId());
		return user.getAvailableAuthorities();
	}
	
	
}
