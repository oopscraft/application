package net.oopscraft.application.admin.controller;

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

import net.oopscraft.application.board.Board;
import net.oopscraft.application.board.BoardService;
import net.oopscraft.application.board.BoardService.BoardSearchType;
import net.oopscraft.application.core.JsonUtility;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.StringUtility;

@PreAuthorize("hasAuthority('ADMIN_BOARD')")
@Controller
@RequestMapping("/admin/board")
public class BoardController {

	@Autowired
	BoardService boardService;

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
		ModelAndView modelAndView = new ModelAndView("admin/board.tiles");
		modelAndView.addObject("skins", new String[] {"board","blog"});
		modelAndView.addObject("policies", Board.Policy.values());
		modelAndView.addObject("rowsPerPage", new long[] {10,20,30,40,50});
		return modelAndView;
	}

	/**
	 * Gets boards
	 * 
	 * @param searchKey
	 * @param searchValue
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getBoards", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getBoards(
		@RequestParam(value = "rows")Integer rows,
		@RequestParam(value = "page") Integer page,
		@RequestParam(value = "searchType", required = false) String searchType,
		@RequestParam(value = "searchValue", required = false) String searchValue
	) throws Exception {
		PageInfo pageInfo = new PageInfo(rows, page, true);
		BoardSearchType boardSearchType = null;
		if(StringUtility.isNotEmpty(searchType)) {
			boardSearchType = BoardSearchType.valueOf(searchType);
		}
		List<Board> boards = boardService.getBoards(pageInfo, boardSearchType, searchValue);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonUtility.toJson(boards);
	}

	/**
	 * Gets board
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getBoard", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getBoard(@RequestParam(value = "id") String id) throws Exception {
		Board board = boardService.getBoard(id);
		return JsonUtility.toJson(board);
	}

	/**
	 * Saves board
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveBoard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void saveBoard(@RequestBody String payload) throws Exception {
		Board board = JsonUtility.toObject(payload, Board.class);
		boardService.saveBoard(board);
	}

	/**
	 * Deletes board
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteBoard", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteBoard(@RequestParam(value = "id") String id) throws Exception {
		boardService.deleteBoard(id);
	}
	
	/**
	 * Gets Policies
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getPolicies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getPolicies() throws Exception {
		return JsonUtility.toJson(Board.Policy.values());
	}

}