package net.oopscraft.application.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.PageInfo;
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
	@RequestMapping(value = "getGroups", method = RequestMethod.GET)
	@ResponseBody
	@Transactional
	public String getUsers(@RequestParam(value = "searchKey", required = false) String searchKey,
			@RequestParam(value = "searchValue", required = false) String searchValue,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows) throws Exception {
		PageInfo pageInfo = new PageInfo(page.intValue(), rows.intValue(), true);
		List<Group> groups = groupService.getGroups(null, null, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonUtils.toJson(groups);
	}
	
	/**
	 * Gets user details.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getGroup", method = RequestMethod.GET)
	@ResponseBody
	@Transactional
	public String getUser(@RequestParam(value = "id") String id) throws Exception {
		Group group = groupService.getGroup(id);
		return JsonUtils.toJson(group);
	}


}