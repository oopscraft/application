package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.user.AuthorityService;
import net.oopscraft.application.user.GroupService;
import net.oopscraft.application.user.RoleService;
import net.oopscraft.application.user.entity.Authority;
import net.oopscraft.application.user.entity.Group;
import net.oopscraft.application.user.entity.Role;

//@PreAuthorize("hasAuthority('ADMIN')")
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	HttpServletResponse response;
	
	@Autowired
	GroupService groupService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	AuthorityService authorityService;
	
	/**
	 * Forwards view page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/__admin.html");
		return modelAndView;
	}
	
	/**
	 * Returns groups
	 * @param group
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getGroups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Group> getGroups(@ModelAttribute Group group, @ModelAttribute PageInfo pageInfo) throws Exception {
		List<Group> groups = groupService.getGroups(group,pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return groups;
	}
	
	/**
	 * Returns groups
	 * @param role
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getRoles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Role> getUsers(@ModelAttribute Role role, @ModelAttribute PageInfo pageInfo) throws Exception {
		List<Role> roles = roleService.getRoles(role,pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return roles;
	}
	
	/**
	 * Returns authorities
	 * @param authority
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getAuthorities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Authority> getAuthorities(@ModelAttribute Authority authority, @ModelAttribute PageInfo pageInfo) throws Exception {
		List<Authority> authorities = authorityService.getAuthorities(authority, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE,  pageInfo.getContentRange());
		return authorities;
	}

}
