package net.oopscraft.application.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
	
	@Autowired
	private GroupRepository groupRepository;
	
	public enum FindBy { ID_LIKE, NAME_LIKE } 
	
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
			fillChildGroupHierarchy(group);
		}
		return groups;
	}
	
	public Group getGroup(String id) throws Exception {
		Group group = groupRepository.findOne(id);
		fillChildGroupHierarchy(group);
		return group;
	}
	
	private void fillChildGroupHierarchy(Group group) throws Exception {
		List<Group> childGroups = groupRepository.findByUpperId(group.getId());
		group.setChildGroups(childGroups);
		for(Group childGroup : childGroups) {
			fillChildGroupHierarchy(childGroup);
		}
	}
	
	public void saveGroup(Group group) throws Exception {
		groupRepository.save(group);
	}
	
	public void removeGroup(String id) throws Exception {
		groupRepository.delete(id);
	}

}
