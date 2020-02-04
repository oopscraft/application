package net.oopscraft.application.api;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.message.MessageService;
import net.oopscraft.application.message.entity.Message;

@RestController
@RequestMapping("/api/message")
public class MessageController {
	
	@Autowired
	HttpServletResponse response;
	
	@Autowired
	MessageService messageService;
	
	/**
	 * Returns messages
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Message> getMessages(@ModelAttribute Message message, @ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<Message> messages = messageService.getMessages(message, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return messages;
	}
	
	/**
	 * Returns message
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Message getMessage(@ModelAttribute Message message) throws Exception {
		return messageService.getMessage(message);
	}
	
}
