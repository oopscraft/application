package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

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

import net.oopscraft.application.core.JsonUtility;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.StringUtility;
import net.oopscraft.application.message.MessageService;
import net.oopscraft.application.message.MessageService.MessageSearchType;
import net.oopscraft.application.message.entity.Message;

@PreAuthorize("hasAuthority('ADMIN_MESSAGE')")
@Controller
@RequestMapping("/admin/message")
public class MessageController {

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
	public String getMessages(
		@RequestParam(value = "rows")Integer rows,
		@RequestParam(value = "page") Integer page,
		@RequestParam(value = "searchType", required = false) String searchType,
		@RequestParam(value = "searchValue", required = false) String searchValue
	) throws Exception {
		PageInfo pageInfo = new PageInfo(rows, page, true);
		MessageSearchType messageSearchType= null;
		if(StringUtility.isNotEmpty(searchType)) {
			messageSearchType = MessageSearchType.valueOf(searchType);
		}
		List<Message> messages = messageService.getMessages(pageInfo, messageSearchType, searchValue);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonUtility.toJson(messages);
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
	public String getMessage(@RequestParam(value = "id") String id) throws Exception {
		Message message = messageService.getMessage(id);
		return JsonUtility.toJson(message);
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
	public void saveMessage(@RequestBody String payload) throws Exception {
		Message message = JsonUtility.toObject(payload, Message.class);
		messageService.saveMessage(message);
	}

	/**
	 * Deletes message.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteMessage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteMessage(@RequestParam(value = "id") String id) throws Exception {
		messageService.deleteMessage(id);
	}

}