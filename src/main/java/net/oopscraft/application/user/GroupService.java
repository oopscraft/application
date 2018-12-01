package net.oopscraft.application.user;

import java.util.ArrayList;
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
		fillChildGroupRecursively(group);
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
			one.setRoles(new ArrayList<Role>());
			one.setAuthorities(new ArrayList<Authority>());
		}
		one.setName(group.getName());
		one.setDescription(group.getDescription());
		
		// adds roles
		one.getRoles().clear();
		for(Role role : group.getRoles()) {
			one.getRoles().add(role);
		}
		
		// adds authorities
		one.getAuthorities().clear();
		for (Authority authority : group.getAuthorities()) {
			one.getAuthorities().add(authority);
		}

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
		groupRepository.delete(group);
		return group;
	}

}
