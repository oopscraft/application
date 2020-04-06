package net.oopscraft.application.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.board.entity.Board;
import net.oopscraft.application.core.PageInfo;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	BoardService boardService;
	
//	@PreAuthorize("this.hasAccessAuthority(#boardId)")
	@RequestMapping(value="{boardId}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("boardId")String boardId) throws Exception {
		Board board = boardService.getBoard(boardId);
		ModelAndView modelAndView = new ModelAndView("board/board.html");
		modelAndView.addObject("board", board);
		return modelAndView;
	}
	
	
	
//	@Autowired
//	HttpServletRequest request;
//
//	@Autowired
//	HttpServletResponse response;
//	
//	@Autowired
//	BoardService boardService;
//	
//	@Autowired
//	ArticleService articleService;
//	
//    @Autowired
//    ApplicationContext applicationContext;
    
//	/**
//	 * checks access authority
//	 * @param boardId
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean hasAccessAuthority(String boardId) throws Exception {
//		return boardService.hasAccessAuthority(boardId);
//	}
//	
//	/**
//	 * checks read authority
//	 * @param boardId
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean hasReadAuthority(String boardId) throws Exception {
//		return boardService.hasReadAuthority(boardId);
//	}
//	
//	/**
//	 * checks write authority
//	 * @param boardId
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean hasWriteAuthority(String boardId) throws Exception {
//		return boardService.hasWriteAuthority(boardId);
//	}

//	/**
//	 * list
//	 * 
//	 * @return
//	 * @throws Exception
//	 */
//	@PreAuthorize("this.hasAccessAuthority(#boardId)")
//	@RequestMapping(value="{boardId}", method = RequestMethod.GET)
//	public ModelAndView list(@PathVariable("boardId")String boardId) throws Exception {
//		Board board = boardService.getBoard(boardId);
//		ModelAndView modelAndView = new ModelAndView("board/list.tiles");
//		modelAndView.addObject("boardController", this);
//		modelAndView.addObject("board", board);
//		return modelAndView;
//	}
	
	/**
	 * read
	 * @param boardId
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
//	@PreAuthorize("this.hasReadAuthority(#boardId)")
//	@RequestMapping(value="{boardId}/read", method = RequestMethod.GET)
//	@Transactional(rollbackFor = Exception.class)
//	public ModelAndView read(
//		@PathVariable("boardId")String boardId,
//		@RequestParam(value="articleId", required=false)String articleId
//	) throws Exception {
//		Board board = boardService.getBoard(boardId);
//		Article article = articleService.getArticle(articleId);
//		//article.increaseReadCount();
//		ModelAndView modelAndView = new ModelAndView("board/read.tiles");
//		modelAndView.addObject("this", this);
//		modelAndView.addObject("board", board);
//		return modelAndView;
//	}
	
//	/**
//	 * write
//	 * @param boardId
//	 * @param articleId
//	 * @return
//	 * @throws Exception
//	 */
//	@PreAuthorize("this.hasWriteAuthority(#boardId)")
//	@RequestMapping(value="{boardId}/write", method = RequestMethod.GET)
//	public ModelAndView write(
//		@PathVariable("boardId")String boardId,
//		@RequestParam(value="articleId", required=false)String articleId
//	) throws Exception {
//		Board board = boardService.getBoard(boardId);
//		ModelAndView modelAndView = new ModelAndView("board/write.tiles");
//		modelAndView.addObject("this", this);
//		modelAndView.addObject("board", board);
//		return modelAndView;
//	}

}