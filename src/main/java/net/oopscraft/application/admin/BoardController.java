package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.board.BoardService;
import net.oopscraft.application.board.entity.Board;
import net.oopscraft.application.core.PageInfo;

//@PreAuthorize("hasBoard('ADMIN_BOARD')")
@Controller
@RequestMapping("/admin/board")
public class BoardController {
	
	@Autowired
	HttpServletResponse response;
	
	@Autowired
	BoardService boardService;
	
	/**
	 * Forwards view page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/board.html");
		return modelAndView;
	}
	
	/**
	 * Returns boards
	 * @param board
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getBoards", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Board> getBoards(@ModelAttribute Board board, @ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<Board> boards = boardService.getBoards(board, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return boards;
	}
	
	/**
	 * Return board
	 * @param board
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getBoard", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Board getBoard(@ModelAttribute Board board) throws Exception {
		return boardService.getBoard(board.getId());
	}

	/**
	 * Saves board
	 * @param board
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveBoard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Board saveBoard(@RequestBody Board board) throws Exception {
		return boardService.saveBoard(board);
	}
	
	/**
	 * Deletes board
	 * @param board
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteBoard", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteBoard(@RequestBody Board board) throws Exception {
		boardService.deleteBoard(board);
	}

}