package net.oopscraft.application.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.user.entity.Group;
import net.oopscraft.application.user.entity.User;

@Service
public class GroupService {

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
	public List<Group> getGroups(final Group group, PageInfo pageInfo) throws Exception {
		Page<Group> usersPage = groupRepository.findAll(new Specification<Group>() {
			@Override
			public Predicate toPredicate(Root<Group> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(group.getId() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("id").as(String.class), group.getId() + '%'));
					predicates.add(predicate);
				}
				if(group.getName() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), group.getName() + '%'));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageInfo.toPageable());
		pageInfo.setTotalCount(usersPage.getTotalElements());
		return usersPage.getContent();
	}
	
	public Group getGroup(Group group) throws Exception {
		return groupRepository.findOne(group.getId());
	}
	
	public Group saveGroup(Group group) throws Exception {
		return groupRepository.save(group);
	}
	
	public void deleteGroup(Group group) throws Exception {
		groupRepository.delete(group);
	}
	
	
	
//
//	/**
//	 * Gets detail of group
//	 * 
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public Group getGroup(String id) throws Exception {
//		Group group = groupRepository.findOne(id);
//		if (group != null) {
//			fillChildGroupRecursively(group);
//		}
//		return group;
//	}

//	/**
//	 * Fills child group recursively
//	 * 
//	 * @param group
//	 * @throws Exception
//	 */
//	private void fillChildGroupRecursively(Group group) throws Exception {
//		List<Group> childGroups = groupRepository.findByUpperId(group.getId());
//		group.setChildGroups(childGroups);
//		for (Group childGroup : childGroups) {
//			fillChildGroupRecursively(childGroup);
//		}
//	}
//
//	/**
//	 * Gets bread crumbs
//	 * 
//	 * @param id
//	 * @return
//	 */
//	public List<Group> getBreadCrumbs(String id) {
//		List<Group> breadCrumbs = new ArrayList<Group>();
//		while (id != null) {
//			Group group = groupRepository.findOne(id);
//			if (group != null) {
//				breadCrumbs.add(group);
//				id = group.getUpperId();
//				continue;
//			}
//			break;
//		}
//		Collections.reverse(breadCrumbs);
//		return breadCrumbs;
//	}

//	/**
//	 * Saves group details
//	 * 
//	 * @param group
//	 * @throws Exception
//	 */
//	public void saveGroup(Group group) throws Exception {
//		Group one = groupRepository.findOne(group.getId());
//		if (one == null) {
//			one = new Group();
//			one.setId(group.getId());
//		}
//
//		// Checks id and upperId is same.
//		if (one.getId().equals(one.getUpperId()) == true) {
//			throw new Exception("Group.id and Group.upperId is same.(infinit loop)");
//		}
//
//		// sets properties
//		one.setUpperId(group.getUpperId());
//		one.setName(group.getName());
//		one.setDescription(group.getDescription());
//
//		// adds roles
//		one.setRoles(group.getRoles());
//
//		// adds authorities
//		one.setAuthorities(group.getAuthorities());
//
//		groupRepository.save(one);
//	}
//
//	/**
//	 * Deletes group
//	 * 
//	 * @param id
//	 * @throws Exception
//	 */
//	public void deleteGroup(String id) throws Exception {
//		Group group = groupRepository.getOne(id);
//
//		// checks child groups
//		List<Group> childGroups = groupRepository.findByUpperId(id);
//		if (childGroups.size() > 0) {
//			throw new Exception("Can not remove.because has child groups.");
//		}
//
//		// deletes group
//		groupRepository.delete(group);
//	}

}
