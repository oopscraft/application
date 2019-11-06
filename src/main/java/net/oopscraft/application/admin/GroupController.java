package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import net.oopscraft.application.user.Group;
import net.oopscraft.application.user.GroupService;

@PreAuthorize("hasAuthority('ADMIN_GROUP')")
@Controller
@RequestMapping("/admin/group")
public class GroupController {

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
	public String getGroups() throws Exception {
		List<Group> groups = groupService.getGroups();
		return JsonUtility.toJson(groups);
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
	public String getGroup(@RequestParam(value = "id") String id) throws Exception {
		Group group = groupService.getGroup(id);
		return JsonUtility.toJson(group);
	}
	
	/**
	 * Gets bread crumbs
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getBreadCrumbs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getBreadCrumbs(@RequestParam(value = "id") String id) throws Exception {
		List<Group> breadCrumbs = groupService.getBreadCrumbs(id);
		return JsonUtility.toJson(breadCrumbs);
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
	public void saveGroup(@RequestBody String payload) throws Exception {
		Group role = JsonUtility.toObject(payload, Group.class);
		groupService.saveGroup(role);
	}
	
	/**
	 * Removes group.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteGroup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteGroup(@RequestParam(value = "id") String id) throws Exception {
		groupService.deleteGroup(id);
	}

}
