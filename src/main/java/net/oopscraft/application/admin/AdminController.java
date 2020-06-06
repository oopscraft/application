package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.user.Authority;
import net.oopscraft.application.user.AuthorityService;
import net.oopscraft.application.user.Group;
import net.oopscraft.application.user.GroupService;
import net.oopscraft.application.user.Role;
import net.oopscraft.application.user.RoleService;

@PreAuthorize("hasAuthority('ADMN')")
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	GroupService groupService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	AuthorityService authorityService;

	@Autowired
	ApplicationContext context;
	
	/**
	 * Forwards view page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		RedirectView redirectView = new RedirectView("/admin/monitor");
		redirectView.setExposeModelAttributes(false);
		ModelAndView modelAndView = new ModelAndView(redirectView);
		return modelAndView;
	}
	
	/**
	 * Login page
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("permitAll()") 
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public ModelAndView login() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/login.html");
		return modelAndView;
	}
	
	/**
	 * Returns groups
	 * @param group
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getGroups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Group> getGroups(@ModelAttribute Group group, Pagination pagination, HttpServletResponse response) throws Exception {
		pagination.setEnableTotalCount(true);
		List<Group> groups = groupService.getGroups(group,pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
		return groups;
	}
	
	/**
	 * Returns groups
	 * @param role
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getRoles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Role> getUsers(@ModelAttribute Role role, Pagination pagination, HttpServletResponse response) throws Exception {
		pagination.setEnableTotalCount(true);
		List<Role> roles = roleService.getRoles(role,pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
		return roles;
	}
	
	/**
	 * Returns authorities
	 * @param authority
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getAuthorities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Authority> getAuthorities(@ModelAttribute Authority authority, Pagination pagination, HttpServletResponse response) throws Exception {
		pagination.setEnableTotalCount(true);
		List<Authority> authorities = authorityService.getAuthorities(authority, pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE,  pagination.getContentRange());
		return authorities;
	}

}
