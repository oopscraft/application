package net.oopscraft.application.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
	
	@Autowired
	private GroupRepository groupRepository;
	
	public enum FindBy { ID_LIKE, NAME_LIKE } 
	
	/**
	 * Gets list of group by search condition and value
	 * @param findBy
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public List<Group> getGroups(FindBy findBy, String value) throws Exception {
		List<Group> groups = null;
		if(findBy == null) {
			groups = groupRepository.findAll();
		}else {
			switch(findBy) {
			case ID_LIKE:
				groups = groupRepository.findByIdLike(value);
			break;
			case NAME_LIKE:
				groups = groupRepository.findByNameLike(value);
			break;
			}
		}
		for(Group group : groups) {
			fillChildGroupRecursively(group);
		}
		return groups;
	}
	
	/**
	 * Gets detail of group
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
	 * @param group
	 * @throws Exception
	 */
	private void fillChildGroupRecursively(Group group) throws Exception {
		List<Group> childGroups = groupRepository.findByUpperId(group.getId());
		group.setChildGroups(childGroups);
		for(Group childGroup : childGroups) {
			fillChildGroupRecursively(childGroup);
		}
	}
	
	/**
	 * Saves group details
	 * @param group
	 * @throws Exception
	 */
	public void saveGroup(Group group) throws Exception {
		groupRepository.saveAndFlush(group);
	}
	
	/**
	 * Removes group details
	 * @param id
	 * @throws Exception
	 */
	public void removeGroup(String id) throws Exception {
		groupRepository.delete(id);
	}

}
