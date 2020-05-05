package net.oopscraft.application.board;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.message.MessageException;
import net.oopscraft.application.security.SecurityPolicyEvaluator;
import net.oopscraft.application.security.UserDetails;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	ArticleService articleService;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	/**
	 * index
	 * @param boardId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{boardId}", method = RequestMethod.GET)
	public ModelAndView index(
		 @PathVariable("boardId")String boardId
		,@AuthenticationPrincipal UserDetails userDetails
	) throws Exception {
		ModelAndView modelAndView = new ModelAndView("board/board.html");
		Board board = boardService.getBoard(boardId);
		SecurityPolicyEvaluator.checkPolicyAuthority(board.getAccessPolicy(), board.getAccessAuthorities(), userDetails);
		modelAndView.addObject("board", board);
		return modelAndView;
	}
	
	/**
	 * Reads article page
	 * @param boardId
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{boardId}/readArticle", method = RequestMethod.GET)
	public ModelAndView readArticle(
		 @PathVariable("boardId")String boardId
		,@RequestParam("id")String id
		,@AuthenticationPrincipal UserDetails userDetails
	) throws Exception {
		ModelAndView modelAndView = new ModelAndView("board/readArticle.html");
		Board board = boardService.getBoard(boardId);
		SecurityPolicyEvaluator.checkPolicyAuthority(board.getReadPolicy(), board.getReadAuthorities(), userDetails);
		modelAndView.addObject("board", board);
		Article article = articleService.getArticle(id);
		modelAndView.addObject("article", article);
		return modelAndView;
	}
	
	/**
	 * Writes article page
	 * @param boardId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{boardId}/writeArticle", method = RequestMethod.GET)
	public ModelAndView writeArticle(
		 @PathVariable("boardId")String boardId
		,@RequestParam(value="id",required=false)String id
		,@RequestParam(value="password",required=false)String password
		,@AuthenticationPrincipal UserDetails userDetails
	) throws Exception {
		ModelAndView modelAndView = new ModelAndView("board/writeArticle.html");
		Board board = boardService.getBoard(boardId);
		SecurityPolicyEvaluator.checkPolicyAuthority(board.getWritePolicy(), board.getWriteAuthorities(), userDetails);
		modelAndView.addObject("board", board);
		
		// 게시글 수정인 경우
		if(StringUtils.isNotBlank(id)) {
			Article article = articleService.getArticle(id);
			
			// 로그인사용자가 작성한 게시글인 경우
			if(StringUtils.isNotEmpty(article.getUserId())) {
				if(userDetails == null
				|| !article.getUserId().contentEquals(userDetails.getUsername())) {
					throw new MessageException("글쓴이만 수정이 가능합니다.");	
				}
			}
			// 익명사용자가 작성한 게시글인 경우 
			else {
				if(!passwordEncoder.matches(password, article.getPassword())) {
					throw new MessageException("게시글 패스워드와 일치하지 않습니다.");
				}
			}
			
			// adds article object.
			modelAndView.addObject("article", article);
		}
		
		// returns model and view
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