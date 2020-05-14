package net.oopscraft.application.api;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.message.Message;
import net.oopscraft.application.message.MessageService;

@CrossOrigin
@RestController
@RequestMapping("/api/messages")
public class MessageRestController {
	
	@Autowired
	MessageService messageService;
	
	/**
	 * Returns messages
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Message> getMessages(@ModelAttribute Message message, Pagination pagination, HttpServletResponse response) throws Exception {
		pagination.setEnableTotalCount(true);
		List<Message> messages = messageService.getMessages(message, pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
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
