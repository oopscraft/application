package net.oopscraft.application.message;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.core.jpa.SystemEmbeddedException;

@Service
public class MessageService {
	
	@Autowired
	MessageRepository messageRepository;
	
	/**
	 * Returns messages
	 * @param user
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	public List<Message> getMessages(final Message message, Pagination pagination) throws Exception {
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "id"));
		PageRequest pageRequest = pagination.toPageRequest(new Sort(orders));
		Page<Message> messagesPage = messageRepository.findAll(new Specification<Message>() {
			@Override
			public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(StringUtils.isNotBlank(message.getId())) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("id").as(String.class), '%' + message.getId() + '%'));
					predicates.add(predicate);
				}
				if(StringUtils.isNotBlank(message.getName())) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), '%' + message.getName() + '%'));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pageRequest);
		pagination.setTotalCount(messagesPage.getTotalElements());
		return messagesPage.getContent();
	}
	
	/**
	 * Gets message
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public Message getMessage(String id) throws Exception {
		return messageRepository.findOne(id);
	}
	
	/**
	 * Returns message
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public Message getMessage(Message message) throws Exception {
		return getMessage(message.getId());
	}
	
	/**
	 * Saves message
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public Message saveMessage(Message message) throws Exception {
		Message one = messageRepository.findOne(message.getId());
		if(one == null) {
			one = new Message(message.getId());
		}
		one.setName(message.getName());
		one.setDescription(message.getDescription());
		one.setValue(message.getValue());
		return messageRepository.save(one);
	}
	
	/**
	 * Deletes message
	 * @param message
	 * @throws Exception
	 */
	public void deleteMessage(Message message) throws Exception {
		Message one = messageRepository.findOne(message.getId());
		if(one.isSystemEmbedded()) {
			throw new SystemEmbeddedException();
		}
		messageRepository.delete(one);
	}
	
	/**
	 * Returns message i18ns
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<MessageI18n> getMesageI18ns(String id) throws Exception {
		Message one = messageRepository.findOne(id);
		return one.getI18ns();
	}
	
	/**
	 * saveMessageI18ns
	 * @param id
	 * @param messageI18ns
	 * @return
	 * @throws Exception
	 */
	public List<MessageI18n> saveMessageI18ns(String id, List<MessageI18n> messageI18ns) throws Exception {
		Message one = messageRepository.findOne(id);
		one.getI18ns().clear();
		for(MessageI18n element : messageI18ns) {
			element.setId(id);
			one.getI18ns().add(element);
		}
		one = messageRepository.save(one);
		return one.getI18ns();
	}

}
