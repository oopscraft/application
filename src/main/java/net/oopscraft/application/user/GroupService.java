package net.oopscraft.application.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.user.repository.GroupRepository;

@Service
public class GroupService {

	@Autowired
	private GroupRepository groupRepository;

	public enum SearchKey {
		ID, NAME
	}

	/**
	 * Gets list of group by search condition and value
	 * 
	 * @param searchKey
	 * @param SearchValue
	 * @return
	 * @throws Exception
	 */
	public List<Group> getGroups(SearchKey searchKey, String SearchValue, PageInfo pageInfo) throws Exception {
		Page<Group> groups = null;
		Pageable pageable = new PageRequest(pageInfo.getPage() - 1, pageInfo.getRows());
		if (searchKey == null) {
			groups = groupRepository.findByUpperIdIsNull(pageable);
		} else {
			switch (searchKey) {
			case ID:
				groups = groupRepository.findByIdLike(SearchValue, pageable);
				break;
			case NAME:
				groups = groupRepository.findByNameLike(SearchValue, pageable);
				break;
			}
		}
		for (Group group : groups) {
			fillChildGroupRecursively(group);
		}
		if (pageInfo.isEnableTotalCount() == true) {
			pageInfo.setTotalCount(groups.getTotalElements());
		}
		return groups.getContent();
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
		List<Group> childGroups = groupRepository.findByUpperId(group.getId());
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
	public void saveGroup(Group group) throws Exception {
		groupRepository.saveAndFlush(group);
	}

	/**
	 * Removes group details
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void removeGroup(String id) throws Exception {
		groupRepository.delete(id);
	}

}
