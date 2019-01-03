package net.oopscraft.application.board.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.board.Board;
import net.oopscraft.application.board.BoardService;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.layout.Layout;
import net.oopscraft.application.layout.LayoutService;


@Controller
@RequestMapping("/board")
public class BoardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	HttpServletResponse response;
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	LayoutService layoutService;

	/**
	 * list
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{boardId}/list", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("boardId")String boardId) throws Exception {
		Board board = boardService.getBoard(boardId);
		Layout layout = layoutService.getAvailableLayout(board.getLayoutId());
		ModelAndView modelAndView = new ModelAndView("board/__list.tiles");
		modelAndView.addObject("board", board);
		modelAndView.addObject("layout", layout);
		return modelAndView;
	}
	
	/**
	 * view
	 * @param boardId
	 * @param articleNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{boardId}/view", method = RequestMethod.GET)
	public ModelAndView view(
		@PathVariable("boardId")String boardId,
		@RequestParam(value="articleNo", required=false)Integer articleNo
	) throws Exception {
		Board board = boardService.getBoard(boardId);
		Layout layout = layoutService.getAvailableLayout(board.getLayoutId());
		ModelAndView modelAndView = new ModelAndView("board/__view.tiles");
		modelAndView.addObject("board", board);
		modelAndView.addObject("layout", layout);
		return modelAndView;
	}
	
	/**
	 * post
	 * @param boardId
	 * @param articleNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{boardId}/post", method = RequestMethod.GET)
	public ModelAndView post(
		@PathVariable("boardId")String boardId,
		@RequestParam(value="articleNo", required=false)Integer articleNo
	) throws Exception {
		Board board = boardService.getBoard(boardId);
		Layout layout = layoutService.getAvailableLayout(board.getLayoutId());
		ModelAndView modelAndView = new ModelAndView("board/__post.tiles");
		modelAndView.addObject("board", board);
		modelAndView.addObject("layout", layout);
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