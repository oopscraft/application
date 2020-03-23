package net.oopscraft.application.user;

import java.util.ArrayList;
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
	
	/**
	 * Gets group
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Group getGroup(String id) throws Exception {
		return groupRepository.findOne(id);
	}
	
	/**
	 * Saves group.
	 * @param group
	 * @return
	 * @throws Exception
	 */
	public Group saveGroup(Group group) throws Exception {
		Group one = groupRepository.findOne(group.getId());
		if(one == null) {
			one = new Group(group.getId());
		}
		one.setUpperId(group.getUpperId());
		one.setName(group.getName());
		one.setIcon(group.getIcon());
		one.setDescription(group.getDescription());
		one.setRoles(group.getRoles());
		one.setAuthorities(group.getAuthorities());
		return groupRepository.save(one);
	}
	
	/**
	 * Deletes group
	 * @param group
	 * @throws Exception
	 */
	public void deleteGroup(Group group) throws Exception {
		groupRepository.delete(group);
	}
	
	/**
	 * Changes upper id
	 * @param id
	 * @param upperId
	 * @return
	 */
	public Group changeUpperId(String id, String upperId) {
		Group one = groupRepository.findOne(id);
		one.setUpperId(upperId);
		return groupRepository.save(one);
	}

}
