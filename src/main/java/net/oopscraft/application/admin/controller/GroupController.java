package net.oopscraft.application.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.user.Group;
import net.oopscraft.application.user.GroupService;

@Controller
@RequestMapping("/admin/group")
public class GroupController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	GroupService groupService;
	
	@Autowired
	HttpServletResponse response;

	/**
	 * Forwards user page
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView group() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/group.tiles");
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
	@RequestMapping(value = "getGroups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public String getGroups() throws Exception {
		List<Group> groups = groupService.getGroups();
		return JsonUtils.toJson(groups);
	}
	
	/**
	 * Gets group details.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getGroup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public String getGroup(@RequestParam(value = "id") String id) throws Exception {
		Group group = groupService.getGroup(id);
		return JsonUtils.toJson(group);
	}
	
	/**
	 * Gets bread crumbs
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getBreadCrumbs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public String getBreadCrumbs(@RequestParam(value = "id") String id) throws Exception {
		List<Group> breadCrumbs = groupService.getBreadCrumbs(id);
		return JsonUtils.toJson(breadCrumbs);
	}

	/**
	 * Saves group.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveGroup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String saveGroup(@RequestBody String payload) throws Exception {
		Group role = JsonUtils.toObject(payload, Group.class);
		role = groupService.saveGroup(role);
		return JsonUtils.toJson(role);
	}
	
	/**
	 * Removes group.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "removeGroup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String removeGroup(@RequestParam(value = "id") String id) throws Exception {
		Group role = groupService.removeGroup(id);
		return JsonUtils.toJson(role);
	}

}
