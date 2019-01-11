package net.oopscraft.application.board.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.board.Board;
import net.oopscraft.application.board.BoardArticle;
import net.oopscraft.application.board.BoardArticleService;
import net.oopscraft.application.board.BoardService;
import net.oopscraft.application.layout.Layout;
import net.oopscraft.application.layout.LayoutService;


@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	HttpServletResponse response;
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	BoardArticleService boardArticleService;
	
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
		modelAndView.addObject("layout", layout);
		modelAndView.addObject("board", board);
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
	@Transactional(rollbackFor = Exception.class)
	public ModelAndView view(
		@PathVariable("boardId")String boardId,
		@RequestParam(value="articleNo", required=false)Integer articleNo
	) throws Exception {
		Board board = boardService.getBoard(boardId);
		BoardArticle article = boardArticleService.getArticle(articleNo);
		article.increaseReadCount();
		Layout layout = layoutService.getAvailableLayout(board.getLayoutId());
		ModelAndView modelAndView = new ModelAndView("board/__view.tiles");
		modelAndView.addObject("layout", layout);
		modelAndView.addObject("board", board);
		return modelAndView;
	}
	
	/**
	 * write
	 * @param boardId
	 * @param articleNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{boardId}/write", method = RequestMethod.GET)
	public ModelAndView write(
		@PathVariable("boardId")String boardId,
		@RequestParam(value="articleNo", required=false)Integer articleNo
	) throws Exception {
		Board board = boardService.getBoard(boardId);
		Layout layout = layoutService.getAvailableLayout(board.getLayoutId());
		ModelAndView modelAndView = new ModelAndView("board/__write.tiles");
		modelAndView.addObject("layout", layout);
		modelAndView.addObject("board", board);
		return modelAndView;
	}

}