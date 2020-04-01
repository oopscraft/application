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
import net.oopscraft.application.user.GroupService;
import net.oopscraft.application.user.entity.Group;

@PreAuthorize("hasAuthority('ADMN_GROP')")
@Controller
@RequestMapping("/admin/group")
public class GroupController {
	
	@Autowired
	HttpServletResponse response;
	
	@Autowired
	GroupService groupService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/group.html");
		return modelAndView;
	}
	
	/**
	 * Returns list of groups
	 * @param user
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getGroups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Group> getGroups(@ModelAttribute Group group,@ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<Group> groups = groupService.getGroups(group, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return groups;
	}

	/**
	 * Returns specified group.
	 * @param group
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getGroup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Group getGroup(@ModelAttribute Group group) throws Exception {
		return groupService.getGroup(group.getId());
	}
	
	/**
	 * Saves specified group.
	 * @param group
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveGroup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Group saveGroup(@RequestBody Group group) throws Exception {
		return groupService.saveGroup(group);
	}
	
	/**
	 * Deletes specified group
	 * @param group
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteGroup", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteGroup(@RequestBody Group group) throws Exception {
		groupService.deleteGroup(group);
	}
	
	/**
	 * Changes group upper id
	 * @param group
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "changeUpperId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Group changeUpperId(@RequestBody Group group) throws Exception {
		return groupService.changeUpperId(group.getId(), group.getUpperId());
	}

}
