package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.oopscraft.application.common.JsonConverter;
import net.oopscraft.application.common.PageInfo;
import net.oopscraft.application.common.StringUtility;
import net.oopscraft.application.menu.MenuService;
import net.oopscraft.application.menu.entity.Menu;
import net.oopscraft.application.user.AuthorityService;
import net.oopscraft.application.user.AuthorityService.AuthoritySearchType;
import net.oopscraft.application.user.GroupService;
import net.oopscraft.application.user.RoleService;
import net.oopscraft.application.user.RoleService.RoleSearchType;
import net.oopscraft.application.user.entity.Authority;
import net.oopscraft.application.user.entity.Group;
import net.oopscraft.application.user.entity.Role;
import net.oopscraft.application.user.entity.User;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	RoleService roleService;
	
	@RequestMapping(value = "getRoles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Role> getUsers(Role role, PageInfo pageInfo) throws Exception {
		List<Role> roles = roleService.getRoles(role,pageInfo);
		return roles;
	}
	
	
	
	
//	@Autowired
//	HttpServletResponse response;
//	
//	@Autowired
//	GroupService groupService;
//	
//	@Autowired
//	RoleService roleService;
//	
//	@Autowired
//	AuthorityService authorityService;
//	
//	@Autowired
//	MenuService menuService;
//	
//	@RequestMapping(method=RequestMethod.GET)
//	public ModelAndView admin() throws Exception {
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setView(new RedirectView("/admin/monitor"));
//		return modelAndView;
//	}
//
//	/**
//	 * Gets groups
//	 * @return
//	 * @throws Exception
//	 */
//	@PreAuthorize("hasAuthority('ADMIN')")
//	@RequestMapping(value = "getGroups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@ResponseBody
//	@Transactional
//	public String getGroups() throws Exception {
//		List<Group> groups = groupService.getGroups();
//		return JsonConverter.toJson(groups);
//	}
//
//	/**
//	 * Gets groups
//	 * 
//	 * @param searchKey
//	 * @param searchValue
//	 * @param page
//	 * @param rows
//	 * @return
//	 * @throws Exception
//	 */
//	@PreAuthorize("hasAuthority('ADMIN')")
//	@RequestMapping(value = "getRoles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@ResponseBody
//	@Transactional
//	public String getRoles(
//		@RequestParam(value = "rows")Integer rows,
//		@RequestParam(value = "page") Integer page,
//		@RequestParam(value = "searchType", required = false) String searchType,
//		@RequestParam(value = "searchValue", required = false) String searchValue
//	) throws Exception {
//		PageInfo pageInfo = new PageInfo(rows, page, true);
//		RoleSearchType roleSearchType= null;
//		if(StringUtility.isNotEmpty(searchType)) {
//			roleSearchType = RoleSearchType.valueOf(searchType);
//		}
//		List<Role> roles = roleService.getRoles(pageInfo, roleSearchType, searchValue);
//		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
//		return JsonConverter.toJson(roles);
//	}
//
//	/**
//	 * Gets authorities
//	 * 
//	 * @param searchKey
//	 * @param searchValue
//	 * @param page
//	 * @param rows
//	 * @return
//	 * @throws Exception
//	 */
//	@PreAuthorize("hasAuthority('ADMIN')")
//	@RequestMapping(value = "getAuthorities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@ResponseBody
//	@Transactional
//	public String getAuthorities(
//		@RequestParam(value = "rows")Integer rows,
//		@RequestParam(value = "page") Integer page,
//		@RequestParam(value = "searchType", required = false) String searchType,
//		@RequestParam(value = "searchValue", required = false) String searchValue
//	) throws Exception {
//		PageInfo pageInfo = new PageInfo(rows, page, true);
//		AuthoritySearchType authoritySearchType= null;
//		if(StringUtility.isNotEmpty(searchType)) {
//			authoritySearchType = AuthoritySearchType.valueOf(searchType);
//		}
//		List<Authority> properties = authorityService.getAuthorities(pageInfo, authoritySearchType, searchValue);
//		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
//		return JsonConverter.toJson(properties);
//	}
//
//	/**
//	 * Gets menus
//	 * @return
//	 * @throws Exception
//	 */
//	@PreAuthorize("hasAuthority('ADMIN')")
//	@RequestMapping(value = "getMenus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@ResponseBody
//	@Transactional
//	public String getMenus() throws Exception {
//		List<Menu> groups = menuService.getMenus();
//		return JsonConverter.toJson(groups);
//	}

}
