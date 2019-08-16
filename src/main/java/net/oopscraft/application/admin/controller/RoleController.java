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
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.StringUtility;
import net.oopscraft.application.user.Role;
import net.oopscraft.application.user.RoleService;
import net.oopscraft.application.user.RoleService.RoleSearchType;

@PreAuthorize("hasAuthority('ADMIN_ROLE')")
@Controller
@RequestMapping("/admin/role")
public class RoleController {

	@Autowired
	RoleService roleService;
	
	@Autowired
	HttpServletResponse response;

	/**
	 * Forwards user page
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView role() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/role.tiles");
		return modelAndView;
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
	public String getRoles(
		@RequestParam(value = "rows")Integer rows,
		@RequestParam(value = "page") Integer page,
		@RequestParam(value = "searchType", required = false) String searchType,
		@RequestParam(value = "searchValue", required = false) String searchValue
	) throws Exception {
		PageInfo pageInfo = new PageInfo(rows, page, true);
		RoleSearchType roleSearchType= null;
		if(StringUtility.isNotEmpty(searchType)) {
			roleSearchType = RoleSearchType.valueOf(searchType);
		}
		List<Role> roles = roleService.getRoles(pageInfo, roleSearchType, searchValue);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonUtility.toJson(roles);
	}
	
	/**
	 * Gets user details.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getRole", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getRoles(@RequestParam(value = "id") String id) throws Exception {
		Role role = roleService.getRole(id);
		return JsonUtility.toJson(role);
	}
	
	/**
	 * Saves role.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void saveRole(@RequestBody String payload) throws Exception {
		Role role = JsonUtility.toObject(payload, Role.class);
		roleService.saveRole(role);
	}
	
	/**
	 * Removes role.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteRole", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteRole(@RequestParam(value = "id") String id) throws Exception {
		roleService.deleteRole(id);
	}

}
