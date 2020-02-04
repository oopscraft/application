package net.oopscraft.application.api;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.oopscraft.application.article.entity.Article;
import net.oopscraft.application.board.BoardArticleService;
import net.oopscraft.application.board.BoardService;
import net.oopscraft.application.board.entity.Board;
import net.oopscraft.application.board.entity.BoardArticle;
import net.oopscraft.application.core.PageInfo;

@RestController
@RequestMapping("/api/board")
public class BoardController {
	
	@Autowired
	HttpServletResponse response;
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	BoardArticleService boardArticleService;
	
	/**
	 * Returns boards
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Board> getBoards(@ModelAttribute Board board, @ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<Board> boards = boardService.getBoards(board, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return boards;
	}
	
	/**
	 * Returns board
	 * @param boardId
	 * @return
	 * @throws Exception
	 */
	//@PreAuthorize("this.hasAccessAuthority(#boardId)")
	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Board getBoard(@ModelAttribute Board board) throws Exception {
		return boardService.getBoard(board.getId());
	}
	
	/**
	 * Returns board articles
	 * @param role
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{boardId}/article", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<BoardArticle> getBoardArticles(@ModelAttribute BoardArticle boardArticle, @ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<BoardArticle> boardArticles = boardArticleService.getBoardArticles(boardArticle, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return boardArticles;
	}
	
	/**
	 * Returns board articles
	 * @param boardArticle
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{boardId}/article/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public BoardArticle getBoardArticle(@ModelAttribute BoardArticle boardArticle) throws Exception {
		return boardArticleService.getBoardArticle(boardArticle);
	}
	
	/**
	 * Creates board article
	 * @param article
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{boardId}/article", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Article createArticle(@RequestBody BoardArticle boardArticle) throws Exception {
		return boardArticleService.saveBoardArticle(boardArticle);
	}
	
	/**
	 * Updates board article
	 * @param article
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{boardId}/article/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Article updateArticle(@RequestBody BoardArticle boardArticle) throws Exception {
		return boardArticleService.saveBoardArticle(boardArticle);
	}
	
	/**
	 * Deletes board article
	 * @param article
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{boardId}/article/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteArticle(@RequestBody BoardArticle boardArticle) throws Exception {
		boardArticleService.deleteBoardArticle(boardArticle);
	}
	
	
	
//	/**
//	 * Returns article
//	 * @param article
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "getArticle", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@ResponseBody
//	public Article getArticle(@ModelAttribute Article article) throws Exception {
//		return articleService.getArticle(article.getId());
//	}
//
//	/**
//	 * Saves article
//	 * @param article
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "saveArticle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@ResponseBody
//	@Transactional(rollbackFor = Exception.class)
//	public Article saveArticle(@RequestBody Article article) throws Exception {
//		return articleService.saveArticle(article);
//	}
//	
//	/**
//	 * Deletes article
//	 * @param article
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "deleteArticle", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@ResponseBody
//	@Transactional(rollbackFor = Exception.class)
//	public void deleteArticle(@RequestBody Article article) throws Exception {
//		articleService.deleteArticle(article);
//	}
	
	
	
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
//	 * Gets latest articles
//	 * @param page
//	 * @param rows
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "/articles/latest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public ResponseEntity<?> getLatestArticles(
//		@RequestParam(value = "rows", required = false, defaultValue = "10")Integer rows,
//		@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
//		@RequestParam(value = "boardId", required = false)String boardId
//	) throws Exception {
//		PageInfo pageInfo = new PageInfo(rows, page, true);
//		List<Article> latestArticles = articleService.getLatestArticles(pageInfo, boardId);
//		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
//		return new ResponseEntity<>(JsonConverter.toJson(latestArticles), HttpStatus.OK);
//	}
	
//	/**
//	 * Gets best articles
//	 * @param page
//	 * @param rows
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "/articles/best", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public ResponseEntity<?> getBestArticles(
//		@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
//		@RequestParam(value = "rows", required = false, defaultValue = "10")Integer rows,
//		@RequestParam(value = "boardId", required = false)String boardId
//	) throws Exception {
//		PageInfo pageInfo = new PageInfo(rows, page, true);
//		List<Article> bestArticles = articleService.getBestArticles(pageInfo, boardId);
//		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
//		return new ResponseEntity<>(JsonConverter.toJson(bestArticles), HttpStatus.OK);
//	}
//	
//	/**
//	 * Gets board info
//	 * @param boardId
//	 * @return
//	 * @throws Exception
//	 */
//	@PreAuthorize("this.hasAccessAuthority(#boardId)")
//	@RequestMapping(value = "/{boardId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public ResponseEntity<?> getBoard(@PathVariable("boardId") String boardId) throws Exception {
//		Board board = boardService.getBoard(boardId);
//		return new ResponseEntity<>(JsonConverter.toJson(board), HttpStatus.OK);
//	}
//	
//	/**
//	 * Gets article list.
//	 * @param boardId
//	 * @param page
//	 * @param searchKey
//	 * @param value
//	 * @return
//	 * @throws Exception
//	 */
//	@PreAuthorize("this.hasAccessAuthority(#boardId)")
//	@RequestMapping(value = "/{boardId}/articles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public ResponseEntity<?> getArticles(
//		@PathVariable("boardId") String boardId,
//		@RequestParam(value = "page", required = false, defaultValue="1")Integer page,
//		@RequestParam(value = "categoryId", required = false)String categoryId,
//		@RequestParam(value = "searchType", required = false)String searchType,
//		@RequestParam(value = "searchValue", required = false)String searchValue
//	) throws Exception {
//		Board board = boardService.getBoard(boardId);
//		PageInfo pageInfo = new PageInfo(board.getRowsPerPage(), page, true);
//		ArticleSearchType articleSearchType;
//		if(StringUtility.isNotEmpty(searchType)) {
//			articleSearchType = ArticleSearchType.valueOf(searchType);
//		}else {
//			articleSearchType = null;
//		}
//		List<Article> articles = articleService.getArticles(pageInfo, boardId, categoryId, articleSearchType, searchValue);
//		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
//		return new ResponseEntity<>(JsonConverter.toJson(articles), HttpStatus.OK);
//	}
	
//	/**
//	 * Gets article detail
//	 * @param boardId
//	 * @param articleId
//	 * @return
//	 * @throws Exception
//	 */
//	@PreAuthorize("this.hasReadAuthority(#boardId)")
//	@RequestMapping(value = "/{boardId}/article/{articleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public ResponseEntity<?> getArticle(
//		@PathVariable("boardId") String boardId,
//		@PathVariable("articleId") String articleId
//	) throws Exception {
//		Article article = articleService.getArticle(articleId);
//		return new ResponseEntity<>(JsonConverter.toJson(article), HttpStatus.OK);
//	}
	
	/**
	 * Saves articles
	 * @param boardId
	 * @param payload
	 * @return
	 * @throws Exception
	 */
//	@PreAuthorize("this.hasWriteAuthority(#boardId)")
//	@RequestMapping(value = "/{boardId}/article", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@Transactional(rollbackFor = Exception.class)
//	public ResponseEntity<?> saveArticles(
//		@PathVariable("boardId") String boardId,
//		@RequestBody String payload
//	) throws Exception {
//		Article article = JsonConverter.toObject(payload, Article.class);
//		article.setBoardId(boardId);
//		articleService.saveArticle(article);
//		return new ResponseEntity<>(JsonConverter.toJson(article), HttpStatus.OK);
//	}
	
//	/**
//	 * Deletes article
//	 * @param boardId
//	 * @param articleId
//	 * @return
//	 * @throws Exception
//	 */
//	@PreAuthorize("this.hasWriteAuthority(#boardId)")
//	@RequestMapping(value = "/{boardId}/article/{articleId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@Transactional(rollbackFor = Exception.class)
//	public ResponseEntity<?> deleteArticle(
//		@PathVariable("boardId") String boardId,
//		@PathVariable("articleId") String articleId
//	) throws Exception {
//		Article article = articleService.getArticle(articleId);
//		articleService.deleteArticle(article);
//		return new ResponseEntity<>(JsonConverter.toJson(null), HttpStatus.OK);
//	}

//	/**
//	 * Gets article replies	
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	@PreAuthorize("this.hasReadAuthority(#boardId)")
//	@RequestMapping(value = "/{boardId}/article/{articleId}/replies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public ResponseEntity<?> getArticleReplies(
//		@PathVariable("boardId") String boardId,
//		@PathVariable("articleId")String articleId
//	) throws Exception {
//		Article article = articleService.getArticle(articleId);
//		List<ArticleReply> articleReplies = article.getReplies();
//		return new ResponseEntity<>(JsonConverter.toJson(articleReplies), HttpStatus.OK);
//	}
	
//	/**
//	 * Saves article reply
//	 * @param boardId
//	 * @param articleId
//	 * @param payload
//	 * @return
//	 * @throws Exception
//	 */
//	@PreAuthorize("this.hasWriteAuthority(#boardId)")
//	@RequestMapping(value = "/{boardId}/article/{articleId}/reply", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@Transactional(rollbackFor = Exception.class)
//	public ResponseEntity<?> saveArticleReply(
//		@PathVariable("boardId")String boardId,
//		@PathVariable("articleId")String articleId,
//		@RequestBody String payload
//	) throws Exception {
//		ArticleReply articleReply = JsonConverter.toObject(payload, ArticleReply.class);
//		Article article = articleService.getArticle(articleId);
//		articleReply = article.saveReply(articleReply);
//		return new ResponseEntity<>(JsonConverter.toJson(articleReply), HttpStatus.OK);
//	}
//	
//	/**
//	 * Deletes article reply
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	@PreAuthorize("this.hasWriteAuthority(#boardId)")
//	@RequestMapping(value = "/{boardId}/article/{articleId}/reply/{replyId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@Transactional(rollbackFor = Exception.class)
//	public ResponseEntity<?> deleteArticleReply(
//		@PathVariable("boardId")String boardId,
//		@PathVariable("articleId")String articleId,
//		@PathVariable("replyId")String replyId
//	) throws Exception {
//		Article article = articleService.getArticle(articleId);
//		article.deleteReply(replyId);
//		return new ResponseEntity<>(JsonConverter.toJson(null), HttpStatus.OK);
//	}
	
//	/**
//	 * Uploads file
//	 * @param multipartFile
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@PreAuthorize("this.hasWriteAuthority(#boardId)")
//	@RequestMapping(value = "/{boardId}/article/{articleId}/file", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public ResponseEntity<?> uploadArticleFile(
//		@PathVariable("boardId")String boardId,
//		@PathVariable("articleId")String articleId,
//		@RequestParam("file")MultipartFile multipartFile,
//		MultipartHttpServletRequest request
//	) throws Exception {
//		
//		// defines object
//		ArticleFile articleFile = new ArticleFile();
//		articleFile.setArticleId(articleId);
//		articleFile.setId(EncodeUtility.generateUUID());
//		articleFile.setName(multipartFile.getOriginalFilename());
//		articleFile.setType(multipartFile.getContentType());
//		articleFile.setSize(multipartFile.getSize());
//
//		// writes file
//		FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), articleFile.getTemporaryFile());
//		
//		// sends response
//		LOGGER.debug("{}", new TextTable(articleFile));
//		return new ResponseEntity<>(JsonConverter.toJson(articleFile), HttpStatus.OK);
//	}
	

//	/**
//	 * Downloads file.
//	 * @param boardId
//	 * @param articleId
//	 * @param fileId
//	 * @throws Exception
//	 */
//	@PreAuthorize("this.hasReadAuthority(#boardId)")
//	@RequestMapping(value = "/{boardId}/article/{articleId}/file/{fileId}", method = RequestMethod.GET)
//	public void downloadArticleFile(
//		@PathVariable("boardId")String boardId,
//		@PathVariable("articleId")String articleId,	
//		@PathVariable("fileId")String fileId
//	) throws Exception {
//		
//		Article article = articleService.getArticle(articleId);
//		ArticleFile articleFile = article.getFile(fileId);
//		
//		// sends file
//		response.setContentType(articleFile.getType());
//		response.setContentLengthLong(articleFile.getSize());
//		StringBuffer contentDisposition = new StringBuffer()
//			.append("attachment")
//			.append(";filename=" + articleFile.getName())
//			.append(";filename*=UTF-8''" + URLEncoder.encode(articleFile.getName(),"UTF-8"));
//		response.setHeader("Content-Disposition", contentDisposition.toString());
//		FileUtils.copyFile(articleFile.getRealFile(), response.getOutputStream());
//	}
	

}
