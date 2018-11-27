package net.oopscraft.application.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.user.Group;
import net.oopscraft.application.user.GroupService;

@Controller
@RequestMapping("/api/group")
public class GroupController {

	@Autowired
	GroupService groupService;

	/**
	 * Returns list of group
	 * 
	 * @param findBy
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getGroups(@RequestParam(value = "findBy", required = false) String findBy,
			@RequestParam(value = "value", required = false) String value) throws Exception {
		List<Group> groups = null;
		if (findBy != null && findBy.isEmpty() == false) {
			groups = groupService.getGroups(GroupService.SearchKey.valueOf(findBy), value);
		} else {
			groups = groupService.getGroups(null, null);
		}
		return new ResponseEntity<>(JsonUtils.toJson(groups), HttpStatus.OK);
	}

	/**
	 * Returns group details.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getGroup(@PathVariable("id") String id) throws Exception {
		Group group = groupService.getGroup(id);
		return new ResponseEntity<>(JsonUtils.toJson(group), HttpStatus.OK);
	}

	/**
	 * Saves group details.
	 * 
	 * @param id
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> saveGroup(@PathVariable("id") String id, @RequestBody String payload) throws Exception {
		Group group = JsonUtils.toObject(payload, Group.class);
		groupService.saveGroup(group);
		group = groupService.getGroup(id);
		return new ResponseEntity<>(JsonUtils.toJson(group), HttpStatus.OK);
	}

	/**
	 * Removes group details.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> removeGroup(@PathVariable("id") String id) throws Exception {
		Group group = groupService.getGroup(id);
		groupService.removeGroup(id);
		return new ResponseEntity<>(JsonUtils.toJson(group), HttpStatus.OK);
	}

}
