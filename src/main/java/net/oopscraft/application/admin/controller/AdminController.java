package net.oopscraft.application.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.user.Authority;
import net.oopscraft.application.user.AuthorityService;
import net.oopscraft.application.user.Group;
import net.oopscraft.application.user.GroupService;
import net.oopscraft.application.user.Role;
import net.oopscraft.application.user.RoleService;

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
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView admin() throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setView(new RedirectView("/admin/dash"));
		return modelAndView;
	}

	/**
	 * Gets groups
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getGroups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public String getGroups() throws Exception {
		List<Group> groups = groupService.getGroups();
		return JsonUtils.toJson(groups);
	}

	/**
	 * Gets groups
	 * 
	 * @param searchKey
	 * @param searchValue
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getRoles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public String getRoles(@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "value", required = false) String value,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows) throws Exception {
		RoleService.SearchCondition searchCondition = roleService.new SearchCondition();
		switch ((key == null ? "" : key)) {
		case "id":
			searchCondition.setId(value);
			break;
		case "name":
			searchCondition.setName(value);
			break;
		}
		PageInfo pageInfo = new PageInfo(page.intValue(), rows.intValue(), true);
		List<Role> roles = roleService.getRoles(searchCondition, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonUtils.toJson(roles);
	}
	
	/**
	 * Gets authorities
	 * 
	 * @param searchKey
	 * @param searchValue
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getAuthorities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public String getAuthorities(@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "value", required = false) String value,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows) throws Exception {
		AuthorityService.SearchCondition searchCondition = authorityService.new SearchCondition();
		switch ((key == null ? "" : key)) {
		case "id":
			searchCondition.setId(value);
			break;
		case "name":
			searchCondition.setName(value);
			break;
		}
		PageInfo pageInfo = new PageInfo(page.intValue(), rows.intValue(), true);
		List<Authority> roles = authorityService.getAuthorities(searchCondition, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonUtils.toJson(roles);
	}
	
}
