package net.oopscraft.application.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.IdGenerator;
import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.core.jpa.SystemEmbeddedException;

@Service
public class GroupService {
	
	static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

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
	public List<Group> getGroups(final Group group, Pagination pagination) throws Exception {
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "upperId"));
		orders.add(new Order(Direction.ASC, "sequence"));
		PageRequest pageRequest = pagination.toPageRequest(new Sort(orders));
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
		}, pageRequest);
		pagination.setTotalCount(usersPage.getTotalElements());
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
		Group one = null;
		if(group.getId() == null || (one = groupRepository.findOne(group.getId())) == null) {
			one = new Group(IdGenerator.uuid());
			one.setUpperId(group.getUpperId());
			one.setSequence(this.getSiblingMaxSequence(one) + 1);
		}

		// in case of upper id is changed.
		if(StringUtils.compare(one.getUpperId(), group.getUpperId()) != 0){
			one.setUpperId(group.getUpperId());
			one.setSequence(this.getSiblingMaxSequence(one) + 1);
		}
		
		// sets other properties
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
		Group one = groupRepository.findOne(group.getId());
		if(one.isSystemEmbedded()) {
			throw new SystemEmbeddedException();
		}
		groupRepository.delete(one);
	}
	
	/**
	 * Changes upper id
	 * @param id
	 * @param upperId
	 * @return
	 */
	public Group changeUpperId(String id, String upperId) throws Exception {
		Group one = groupRepository.findOne(id);
		one.setUpperId(upperId);
		Integer siblingMaxSequence = this.getSiblingMaxSequence(one);
		int newSequence = siblingMaxSequence == null ? 1 : siblingMaxSequence + 1;
		one.setSequence(newSequence);
		return groupRepository.save(one);
	}
	
	/**
	 * changeSequencePrevious
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Group changeSequence(String id, boolean ascend) throws Exception {
		Group group = groupRepository.findOne(id);
		List<Group> siblingGroups = this.getSiblingGroups(group);
		int currentIndex = -1;
		for(int i = 0, size = siblingGroups.size(); i < size; i ++ ) {
			if(siblingGroups.get(i).getId().contentEquals(id)) {
				currentIndex = i;
				break;
			}
		}
		int targetIndex = ascend ? currentIndex + 1 : currentIndex - 1;
		if(0 <= targetIndex && targetIndex <= siblingGroups.size()-1) {
			Group targetGroup = siblingGroups.get(targetIndex);
			int targetSequence = targetGroup.getSequence() == null ? 0 : targetGroup.getSequence();
			int sequence = group.getSequence() == null ? 0 : group.getSequence();
			targetGroup.setSequence(sequence);
			group.setSequence(targetSequence);
			groupRepository.save(targetGroup);
			groupRepository.save(group);
		}
		return groupRepository.findOne(id);
	}
	
	/**
	 * getSiblingGroups
	 * @param group
	 * @return
	 * @throws Exception
	 */
	private List<Group> getSiblingGroups(Group group) throws Exception {
		if(group.getUpperId() == null) {
			return groupRepository.findRootGroups();
		}else {
			return groupRepository.findChildGroups(group.getUpperId());
		}
	}
	
	/**
	 * getMaxSiblingMaxSequence
	 * @param group
	 * @return
	 * @throws Exception
	 */
	private Integer getSiblingMaxSequence(Group group) throws Exception {
		Integer siblingMaxSequence = null;
		if(group.getUpperId() == null) {
			siblingMaxSequence = groupRepository.findRootMaxSequence();
		}else {
			siblingMaxSequence = groupRepository.findChildMaxSequence(group.getUpperId());
		}
		return siblingMaxSequence == null ? 0 : siblingMaxSequence;
	}

}
