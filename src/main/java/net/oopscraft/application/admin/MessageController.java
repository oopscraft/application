package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.message.MessageService;
import net.oopscraft.application.message.entity.Message;

@PreAuthorize("hasAuthority('ADMN_MESG')")
@Controller
@RequestMapping("/admin/message")
public class MessageController {

	@Autowired
	HttpServletResponse response;

	@Autowired
	MessageService messageService;

	/**
	 * Forwards page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/message.html");
		return modelAndView;
	}
	
	/**
	 * Returns messages
	 * @param role
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getMessages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Message> getMessages(@ModelAttribute Message message, @ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<Message> messages = messageService.getMessages(message, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return messages;
	}
	
	/**
	 * Returns message
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getMessage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Message getMessage(@ModelAttribute Message message) throws Exception {
		return messageService.getMessage(message.getId());
	}

	/**
	 * Saves message
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveMessage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Message saveMessage(@RequestBody Message message) throws Exception {
		return messageService.saveMessage(message);
	}
	
	/**
	 * Deletes message
	 * @param message
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteMessage", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteMessage(@RequestBody Message message) throws Exception {
		messageService.deleteMessage(message);
	}

}