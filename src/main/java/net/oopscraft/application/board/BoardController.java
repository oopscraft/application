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
import net.oopscraft.application.security.SecurityEvaluator;
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
		SecurityEvaluator.checkPolicyAuthority(board.getAccessPolicy(), board.getAccessAuthorities());
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
		SecurityEvaluator.checkPolicyAuthority(board.getReadPolicy(), board.getReadAuthorities());
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
		SecurityEvaluator.checkPolicyAuthority(board.getWritePolicy(), board.getWriteAuthorities());
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

}