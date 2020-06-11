package net.oopscraft.application.api;

import java.io.File;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;

import net.oopscraft.application.board.Article;
import net.oopscraft.application.board.ArticleFile;
import net.oopscraft.application.board.ArticleReply;
import net.oopscraft.application.board.ArticleService;
import net.oopscraft.application.board.Board;
import net.oopscraft.application.board.BoardService;
import net.oopscraft.application.core.IdGenerator;
import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.property.PropertyService;
import net.oopscraft.application.security.UserDetails;
import net.oopscraft.application.user.User;
import net.oopscraft.application.user.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/boards")
public class BoardRestController {
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	ArticleService articleService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PropertyService propertyService;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	/**
	 * Returns boards
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Board> getBoards(
		 @RequestParam(value="id", required=false)String id
		,@RequestParam(value="name", required=false)String name
		,@RequestParam(value="rows", required=false, defaultValue="100")int rows
		,@RequestParam(value="page", required=false, defaultValue="1")int page
		,HttpServletResponse response
	) throws Exception {
		Board board = new Board();
		board.setId(id);
		board.setName(name);
		Pagination pagination = new Pagination(rows, page, true);
		List<Board> boards = boardService.getBoards(board, pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
		return boards;
	}
	
	/**
	 * Returns board
	 * @param boardId
	 * @return
	 * @throws Exception
	 */
	//@PreAuthorize("this.hasAccessAuthority(#boardId)")
	@RequestMapping(value="{boardId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Board getBoard(@PathVariable("boardId")String boardId) throws Exception {
		return boardService.getBoard(boardId);
	}
	
	/**
	 * Returns board articles
	 * @param role
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{boardId}/articles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@JsonView(List.class)
	public List<Article> getArticles(
		 @PathVariable("boardId")String boardId
		,@RequestParam(value="categoryId", required=false)String categoryId
		,@RequestParam(value="title", required=false)String title
		,@RequestParam(value="contents", required=false)String contents
		,@RequestParam(value="rows", required=false, defaultValue="100")int rows
		,@RequestParam(value="page", required=false, defaultValue="1")int page
		,HttpServletResponse response
	) throws Exception {
		Article article = new Article();
		article.setBoardId(boardId);
		article.setCategoryId(categoryId);
		article.setTitle(title);
		article.setContents(contents);
		Pagination pagination = new Pagination(rows, page, true);
		List<Article> boardArticles = articleService.getArticles(article, pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
		return boardArticles;
	}
	
	/**
	 * Returns board articles
	 * @param boardArticle
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{boardId}/articles/{articleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Article getArticle(
		 @PathVariable("boardId")String boardId
		,@PathVariable("articleId")String articleId
	) throws Exception {
		return articleService.getArticle(articleId);
	}
	
	/**
	 * Creates board article
	 * @param boardId
	 * @param boardArticle
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{boardId}/articles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Article createArticle(
		@PathVariable("boardId")String boardId,
		@RequestBody Article boardArticle,
		@AuthenticationPrincipal UserDetails userDetails
	) throws Exception {
		boardArticle.setBoardId(boardId);
		if(userDetails == null) {
			boardArticle.setUserId(null);
			boardArticle.setAuthor(boardArticle.getAuthor());
			boardArticle.setPassword(boardArticle.getPassword()==null?null:passwordEncoder.encode(boardArticle.getPassword()));
		}else {
			User user = userService.getUser(userDetails.getUsername());
			boardArticle.setUserId(user.getId());
			boardArticle.setPassword(null);
		}
		return articleService.saveArticle(boardArticle);
	}
	
	/**
	 * Updates board article
	 * @param boardId
	 * @param article
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{boardId}/articles/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Article updateArticle(
		@PathVariable("boardId")String boardId,
		@PathVariable("id")String id,
		@RequestBody Article article
	) throws Exception {
		article.setBoardId(boardId);
		article.setId(id);
		return articleService.saveArticle(article);
	}
	
	/**
	 * Deletes board article
	 * @param boardId
	 * @param id
	 * @param boardArticle
	 * @throws Exception
	 */
	@RequestMapping(value = "{boardId}/articles/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteArticle(
		@PathVariable("boardId")String boardId,
		@PathVariable("id")String id
	) throws Exception {
		Article article = new Article();
		article.setBoardId(boardId);
		article.setId(id);
		articleService.deleteArticle(article);
	}
	
	/**
	 * Uploads file
	 * @throws Exception
	 */
	@RequestMapping(value="{boardId}/files", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ArticleFile uploadBoardFile(
		@PathVariable("boardId")String boardId,
		@RequestParam("file")MultipartFile multipartFile
	) throws Exception {

		// writes file
		String temporaryId = IdGenerator.uuid();
		String uploadPath = propertyService.getProperty("uploadPath").getValue();
		File temporaryFile = new File(uploadPath + File.separator + temporaryId);
		FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), temporaryFile);
		
		// sends response
		ArticleFile articleFile = new ArticleFile();
		articleFile.setId(temporaryId);
		articleFile.setName(multipartFile.getOriginalFilename());
		articleFile.setType(multipartFile.getContentType());
		articleFile.setSize(multipartFile.getSize());
		return articleFile;
	}
	
	/**
	 * Downloads files
	 * @throws Exception
	 */
	@RequestMapping(value="{boardId}/articles/{articleId}/files/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void downloadArticleFile(
		 @PathVariable("boardId")String boardId
		,@PathVariable("articleId")String articleId
		,@PathVariable("id")String id
		,HttpServletResponse response
	) throws Exception {
		Article article = articleService.getArticle(articleId);
		ArticleFile articleFile = article.getFile(id);
		
		// sends file
		response.setContentType(articleFile.getType());
		response.setContentLengthLong(articleFile.getSize());
		StringBuffer contentDisposition = new StringBuffer()
			.append("attachment")
			.append(";filename=" + articleFile.getName())
			.append(";filename*=UTF-8''" + URLEncoder.encode(articleFile.getName(),"UTF-8"));
		response.setHeader("Content-Disposition", contentDisposition.toString());
		String uploadPath = propertyService.getProperty("uploadPath").getValue();
		File file = new File(uploadPath + File.separator + "board" + File.separator + articleFile.getId());
		FileUtils.copyFile(file, response.getOutputStream());
	}
	
	/**
	 * Returns board article replies
	 * @param boardId
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{boardId}/articles/{articleId}/replies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<ArticleReply> getArticleReplies(
		 @PathVariable("boardId")String boardId
		,@PathVariable("articleId")String articleId
	) throws Exception {
		Article article = articleService.getArticle(articleId);
		return article.getReplies();
	}
	
	/**
	 * Creates board article reply
	 * @param articleReply
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{boardId}/articles/{articleId}/replies", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public ArticleReply createArticleReply(
		@PathVariable("boardId")String boardId,
		@PathVariable("articleId")String articleId,
		@RequestBody ArticleReply articleReply,
		@AuthenticationPrincipal UserDetails userDetails
	) throws Exception {
		Article article = articleService.getArticle(articleId);
		articleReply.setArticleId(article.getId());
		articleReply.setId(IdGenerator.uuid());
		articleReply.setDate(new Date());
		if(userDetails == null) {
			articleReply.setUserId(null);
			articleReply.setAuthor(articleReply.getAuthor());
			articleReply.setPassword(articleReply.getPassword()==null?null:passwordEncoder.encode(articleReply.getPassword()));
		}else {
			User user = userService.getUser(userDetails.getUsername());
			articleReply.setUserId(user.getId());
			articleReply.setPassword(null);
		}
		article.addReply(articleReply);
		articleService.saveArticle(article);
		return articleReply;
	}
	
	/**
	 * Updates board article reply
	 * @param boardId
	 * @param articleId
	 * @param id
	 * @param articleReply
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{boardId}/articles/{articleId}/replies/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public ArticleReply updateArticleReply(
		@PathVariable("boardId")String boardId,
		@PathVariable("articleId")String articleId,
		@PathVariable("id")String id,
		@RequestBody ArticleReply articleReply
	) throws Exception {
		Article article = articleService.getArticle(articleId);
		ArticleReply one = article.getReply(id);
		one.setContents(articleReply.getContents());
		articleService.saveArticle(article);
		return one;
	}
	
	/**
	 * Deletes board article reply
	 * @param boardId
	 * @param articleId
	 * @param id
	 * @throws Exception
	 */
	@RequestMapping(value="{boardId}/articles/{articleId}/replies/{id}", method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional(rollbackFor = Exception.class)
	public void deleteArticleReply(
		@PathVariable("boardId")String boardId,
		@PathVariable("articleId")String articleId,
		@PathVariable("id")String id
	) throws Exception {
		Article article = articleService.getArticle(articleId);
		article.removeReply(id);
		articleService.saveArticle(article);
	}

}
