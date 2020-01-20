package net.oopscraft.application.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.oopscraft.application.common.PageInfo;
import net.oopscraft.application.message.entity.Message;

@Service
public class MessageService {
	
	@Autowired
	MessageRepository messageRepository;
	
	public enum MessageSearchType { ID,NAME	}

	/**
	 * Gets messages
	 * @param searchType
	 * @param searchValue
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<Message> getMessages(PageInfo pageInfo, MessageSearchType searchType, String searchValue) throws Exception {
		Pageable pageable = pageInfo.toPageable();
		Page<Message> messagesPage = null;
		if(searchType == null) {
			messagesPage = messageRepository.findAll(pageable);
		}else {
			switch(searchType) {
				case ID :
					messagesPage = messageRepository.findByIdContaining(searchValue, pageable);
				break;
				case NAME :
					messagesPage = messageRepository.findByNameContaining(searchValue, pageable);
				break;
			}
		}
		if (pageInfo.isEnableTotalCount() == true) {
			pageInfo.setTotalCount(messagesPage.getTotalElements());
		}
		List<Message> messages = messagesPage.getContent();
		return messages;
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
	public void saveMessage(Message message) throws Exception {
		Message one = messageRepository.findOne(message.getId());
		if(one == null) {
			one = new Message();
			one.setId(message.getId());
		}
		one.setName(message.getName());
		one.setValue(message.getValue());
		one.setDescription(message.getDescription());
		messageRepository.save(one);
	}
	
	/**
	 * Removes message
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void deleteMessage(String id) throws Exception {
		Message message = messageRepository.getOne(id);
		messageRepository.delete(message);
	}
}
