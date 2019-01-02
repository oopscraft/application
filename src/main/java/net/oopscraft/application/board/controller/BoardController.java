package net.oopscraft.application.board.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.property.Property;
import net.oopscraft.application.property.PropertyService;


@Controller
@RequestMapping("/board")
public class BoardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	HttpServletResponse response;

	/**
	 * Forwards page
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{id}", method = RequestMethod.GET)
	public ModelAndView index(@PathVariable("id")String id) throws Exception {
		ModelAndView modelAndView = new ModelAndView("board/board.tiles");
		modelAndView.addObject("skin","default");
		return modelAndView;
	}
//
//	/**
//	 * Gets authorities
//	 * 
//	 * @param searchKey
//	 * @param searchValue
//	 * @param page
//	 * @param rows
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "getArticles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@ResponseBody
//	public String getArticles(@RequestParam(value = "key", required = false) String key,
//			@RequestParam(value = "value", required = false) String value,
//			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
//			@RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows) throws Exception {
//		PropertyService.SearchCondition searchCondition = propertyService.new SearchCondition();
//		switch ((key == null ? "" : key)) {
//		case "id":
//			searchCondition.setId(value);
//			break;
//		case "name":
//			searchCondition.setName(value);
//			break;
//		}
//		PageInfo pageInfo = new PageInfo(page.intValue(), rows.intValue(), true);
//		List<Property> roles = propertyService.getProperties(searchCondition, pageInfo);
//		LOGGER.debug("{}", new TextTable(roles));
//		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
//		return JsonUtils.toJson(roles);
//	}

}