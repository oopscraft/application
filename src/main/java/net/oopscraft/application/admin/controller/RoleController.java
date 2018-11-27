package net.oopscraft.application.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import net.oopscraft.application.user.Authority;
import net.oopscraft.application.user.AuthorityService;
import net.oopscraft.application.user.Role;
import net.oopscraft.application.user.RoleService;
import net.oopscraft.application.user.AuthorityService.SearchCondition;

@Controller
@RequestMapping("/admin/role")
public class RoleController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

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
	@RequestMapping(value = "getRoles", method = RequestMethod.GET)
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
	 * Gets user details.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getRole", method = RequestMethod.GET)
	@ResponseBody
	@Transactional
	public String getRoles(@RequestParam(value = "id") String id) throws Exception {
		Role role = roleService.getRole(id);
		return JsonUtils.toJson(role);
	}
	
	/**
	 * Saves role.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveRole", method = RequestMethod.POST)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String saveRole(@RequestBody String payload) throws Exception {
		Role role = JsonUtils.toObject(payload, Role.class);
		role = roleService.saveRole(role);
		return JsonUtils.toJson(role);
	}


}