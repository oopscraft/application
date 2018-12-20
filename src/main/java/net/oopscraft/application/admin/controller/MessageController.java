package net.oopscraft.application.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.message.Message;
import net.oopscraft.application.message.MessageService;

@PreAuthorize("hasAuthority('ADMIN_MESSAGE')")
@Controller
@RequestMapping("/admin/message")
public class MessageController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	MessageService messageService;

	@Autowired
	HttpServletResponse response;

	/**
	 * Forwards page
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/message.tiles");
		return modelAndView;
	}

	/**
	 * Gets messages
	 * 
	 * @param searchKey
	 * @param searchValue
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getMessages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public String getMessages(@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "value", required = false) String value,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows) throws Exception {
		MessageService.SearchCondition searchCondition = messageService.new SearchCondition();
		switch ((key == null ? "" : key)) {
		case "id":
			searchCondition.setId(value);
			break;
		case "name":
			searchCondition.setName(value);
			break;
		}
		PageInfo pageInfo = new PageInfo(page.intValue(), rows.intValue(), true);
		List<Message> roles = messageService.getMessages(searchCondition, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonUtils.toJson(roles);
	}

	/**
	 * Gets message details.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getMessage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public String getMessage(@RequestParam(value = "id") String id) throws Exception {
		Message message = messageService.getMessage(id);
		return JsonUtils.toJson(message);
	}

	/**
	 * Saves message.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveMessage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String saveMessage(@RequestBody String payload) throws Exception {
		Message message = JsonUtils.toObject(payload, Message.class);
		message = messageService.saveMessage(message);
		return JsonUtils.toJson(message);
	}

	/**
	 * Removes message.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "removeMessage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String removeMessage(@RequestParam(value = "id") String id) throws Exception {
		Message message = messageService.removeMessage(id);
		return JsonUtils.toJson(message);
	}

}