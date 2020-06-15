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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonView;

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.user.Authority;
import net.oopscraft.application.user.Group;
import net.oopscraft.application.user.GroupService;

@PreAuthorize("hasAuthority('ADMN_GROP')")
@Controller
@RequestMapping("/admin/group")
public class GroupController {

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
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getGroups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@JsonView(List.class)
	public List<Group> getGroups(@ModelAttribute Group group,Pagination pagination, HttpServletResponse response) throws Exception {
		pagination.setEnableTotalCount(true);
		List<Group> groups = groupService.getGroups(group, pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
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
	@PreAuthorize("hasAuthority('ADMN_GROP_EDIT')")
	@RequestMapping(value = "saveGroup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public Group saveGroup(@RequestBody @Valid Group group) throws Exception {
		return groupService.saveGroup(group);
	}
	
	/**
	 * Deletes specified group
	 * @param group
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_GROP_EDIT')")
	@RequestMapping(value = "deleteGroup", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public void deleteGroup(@RequestBody Group group) throws Exception {
		groupService.deleteGroup(group);
	}
	
	/**
	 * Changes group upper id
	 * @param group
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_GROP_EDIT')")
	@RequestMapping(value = "changeUpperId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public Group changeUpperId(@RequestBody Group group) throws Exception {
		return groupService.changeUpperId(group.getId(), group.getUpperId());
	}
	
	@PreAuthorize("hasAuthority('ADMN_GROP_EDIT')")
	@RequestMapping(value = "changeSequence", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public Group changeSequence(@RequestParam("id")String id, @RequestParam("ascend")Boolean ascend) throws Exception {
		return groupService.changeSequence(id, ascend);
	}

	/**
	 * Returns available authorities
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getAvailableAuthorities", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Authority> getAvailableAuthorities(@ModelAttribute Group group) throws Exception {
		group = groupService.getGroup(group.getId());
		return group.getAvailableAuthorities();
	}
	
}
