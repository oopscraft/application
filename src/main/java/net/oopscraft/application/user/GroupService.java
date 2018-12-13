package net.oopscraft.application.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.user.repository.GroupRepository;

@Service
public class GroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

	@Autowired
	private GroupRepository groupRepository;

	/**
	 * Gets list of group by search condition and value
	 * 
	 * @param searchKey
	 * @param SearchValue
	 * @return
	 * @throws Exception
	 */
	public List<Group> getGroups() throws Exception {
		List<Group> groups = groupRepository.findByUpperIdIsNullOrderByDisplaySeqAsc();
		for (Group group : groups) {
			fillChildGroupRecursively(group);
		}
		LOGGER.debug("groups {}", new TextTable(groups));
		return groups;
	}

	/**
	 * Gets detail of group
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Group getGroup(String id) throws Exception {
		Group group = groupRepository.findOne(id);
		if (group != null) {
			fillChildGroupRecursively(group);
		}
		return group;
	}

	/**
	 * Fills child group recursively
	 * 
	 * @param group
	 * @throws Exception
	 */
	private void fillChildGroupRecursively(Group group) throws Exception {
		List<Group> childGroups = groupRepository.findByUpperIdOrderByDisplaySeqAsc(group.getId());
		group.setChildGroups(childGroups);
		for (Group childGroup : childGroups) {
			fillChildGroupRecursively(childGroup);
		}
	}

	/**
	 * Gets bread crumbs
	 * 
	 * @param id
	 * @return
	 */
	public List<Group> getBreadCrumbs(String id) {
		List<Group> breadCrumbs = new ArrayList<Group>();
		while (id != null) {
			Group group = groupRepository.findOne(id);
			if (group != null) {
				breadCrumbs.add(group);
				id = group.getUpperId();
				continue;
			}
			break;
		}
		Collections.reverse(breadCrumbs);
		return breadCrumbs;
	}

	/**
	 * Saves group details
	 * 
	 * @param group
	 * @throws Exception
	 */
	public Group saveGroup(Group group) throws Exception {
		Group one = groupRepository.findOne(group.getId());
		if (one == null) {
			one = new Group();
			one.setId(group.getId());
		}

		// Checks id and upperId is same.
		if (one.getId().equals(one.getUpperId()) == true) {
			throw new Exception("Group.id and Group.upperId is same.(infinit loop)");
		}

		// sets properties
		one.setUpperId(group.upperId);
		one.setName(group.getName());
		one.setDescription(group.getDescription());
		one.setDisplaySeq(group.getDisplaySeq());

		// adds roles
		one.setRoles(group.getRoles());

		// adds authorities
		one.setAuthorities(group.getAuthorities());

		groupRepository.save(one);
		return groupRepository.findOne(group.getId());
	}

	/**
	 * Removes group details
	 * 
	 * @param id
	 * @throws Exception
	 */
	public Group removeGroup(String id) throws Exception {
		Group group = groupRepository.getOne(id);

		// checks child groups
		List<Group> childGroups = groupRepository.findByUpperIdOrderByDisplaySeqAsc(id);
		if (childGroups.size() > 0) {
			throw new Exception("Can not remove.because has child groups.");
		}

		// deletes group
		groupRepository.delete(group);
		return group;
	}

}
