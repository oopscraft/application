package net.oopscraft.application.message;

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

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.message.entity.Message;

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
		Page<Message> messagesPage = messageRepository.findAll(new  Specification<Message>() {
			@Override
			public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(message.getId() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("id").as(String.class), message.getId() + '%'));
					predicates.add(predicate);
				}
				if(message.getName() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), message.getName() + '%'));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pagination.toPageRequest());
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
		one.setValue(message.getValue());
		one.setDescription(message.getDescription());
		return messageRepository.save(one);
	}
	
	/**
	 * Deletes message
	 * @param message
	 * @throws Exception
	 */
	public void deleteMessage(Message message) throws Exception {
		messageRepository.delete(message);
	}

//	/**
//	 * Gets messages
//	 * @param searchType
//	 * @param searchValue
//	 * @param pagination
//	 * @return
//	 * @throws Exception
//	 */
//	public List<Message> getMessages(pagination pagination, MessageSearchType searchType, String searchValue) throws Exception {
//		Pageable pageable = pagination.toPageable();
//		Page<Message> messagesPage = null;
//		if(searchType == null) {
//			messagesPage = messageRepository.findAll(pageable);
//		}else {
//			switch(searchType) {
//				case ID :
//					messagesPage = messageRepository.findByIdContaining(searchValue, pageable);
//				break;
//				case NAME :
//					messagesPage = messageRepository.findByNameContaining(searchValue, pageable);
//				break;
//			}
//		}
//		if (pagination.isEnableTotalCount() == true) {
//			pagination.setTotalCount(messagesPage.getTotalElements());
//		}
//		List<Message> messages = messagesPage.getContent();
//		return messages;
//	}
//	
//	/**
//	 * Gets detail of message
//	 * 
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public Message getMessage(String id) throws Exception {
//		Message Message = messageRepository.findOne(id);
//		return Message;
//	}
//	
//	/**
//	 * Saves message
//	 * 
//	 * @param message
//	 * @return
//	 * @throws Exception
//	 */
//	public void saveMessage(Message message) throws Exception {
//		Message one = messageRepository.findOne(message.getId());
//		if(one == null) {
//			one = new Message();
//			one.setId(message.getId());
//		}
//		one.setName(message.getName());
//		one.setValue(message.getValue());
//		one.setDescription(message.getDescription());
//		messageRepository.save(one);
//	}
//	
//	/**
//	 * Removes message
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public void deleteMessage(String id) throws Exception {
//		Message message = messageRepository.getOne(id);
//		messageRepository.delete(message);
//	}
}
