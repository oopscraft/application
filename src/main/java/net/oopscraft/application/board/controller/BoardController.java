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

import net.oopscraft.application.board.Article;
import net.oopscraft.application.board.ArticleService;
import net.oopscraft.application.board.Board;
import net.oopscraft.application.board.BoardService;


@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	HttpServletResponse response;
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	ArticleService articleService;

	/**
	 * list
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{boardId}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("boardId")String boardId) throws Exception {
		Board board = boardService.getBoard(boardId);
		ModelAndView modelAndView = new ModelAndView("board/list.tiles");
		modelAndView.addObject("board", board);
		return modelAndView;
	}
	
	/**
	 * read
	 * @param boardId
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{boardId}/read", method = RequestMethod.GET)
	@Transactional(rollbackFor = Exception.class)
	public ModelAndView read(
		@PathVariable("boardId")String boardId,
		@RequestParam(value="articleId", required=false)String articleId
	) throws Exception {
		Board board = boardService.getBoard(boardId);
		Article article = articleService.getArticle(articleId);
		article.increaseReadCount();
		ModelAndView modelAndView = new ModelAndView("board/read.tiles");
		modelAndView.addObject("board", board);
		return modelAndView;
	}
	
	/**
	 * write
	 * @param boardId
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{boardId}/write", method = RequestMethod.GET)
	public ModelAndView write(
		@PathVariable("boardId")String boardId,
		@RequestParam(value="articleId", required=false)String articleId
	) throws Exception {
		Board board = boardService.getBoard(boardId);
		ModelAndView modelAndView = new ModelAndView("board/write.tiles");
		modelAndView.addObject("board", board);
		return modelAndView;
	}

}