package net.oopscraft.application.message;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.message.repository.MessageRepository;

@Service
public class MessageService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
	
	@Autowired
	MessageRepository messageRepository;
	

	/**
	 * Search condition class 
	 *
	 */
	public class SearchCondition {
		String id;
		String name;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	/**
	 * Gets messages
	 * @param searchType
	 * @param searchValue
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<Message> getMessages(SearchCondition searchCondition, PageInfo pageInfo ) throws Exception {
		List<Message> messages = null;
		Page<Message> page = null;
		Pageable pageable = new PageRequest(pageInfo.getPage() - 1, pageInfo.getRows());
		if(!StringUtils.isEmpty(searchCondition.getId())) {
			page = messageRepository.findByIdStartingWith(searchCondition.getId(), pageable);
		}else if(!StringUtils.isEmpty(searchCondition.getId())) {
			page = messageRepository.findByNameStartingWith(searchCondition.getName(), pageable);
		}else {
			page = messageRepository.findAllByOrderBySystemDataYn(pageable);
		}
		messages = page.getContent();
		if (pageInfo.isEnableTotalCount() == true) {
			pageInfo.setTotalCount(page.getTotalElements());
		}
		LOGGER.debug("+ messages: {}", new TextTable(messages));
		return page.getContent();
	}
	
	/**
	 * Gets detail of message
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Message getMessage(String id) throws Exception {
		Message Message = messageRepository.findOne(id);
		return Message;
	}
	
	/**
	 * Saves message
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public Message saveMessage(Message message) throws Exception {
		Message one = messageRepository.findOne(message.getId());
		if(one == null) {
			one = new Message();
			one.setId(message.getId());
		}
		one.setName(message.getName());
		one.setValue(message.getValue());
		one.setDescription(message.getDescription());
		messageRepository.save(one);
		return messageRepository.findOne(message.getId());
	}
	
	/**
	 * Removes message
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Message removeMessage(String id) throws Exception {
		Message message = messageRepository.getOne(id);
		messageRepository.delete(message);
		return message;
	}
}
