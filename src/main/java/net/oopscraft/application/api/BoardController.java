package net.oopscraft.application.api;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

import net.oopscraft.application.article.entity.ArticleFile;
import net.oopscraft.application.article.entity.ArticleReply;
import net.oopscraft.application.board.BoardService;
import net.oopscraft.application.board.entity.Board;
import net.oopscraft.application.board.entity.BoardArticle;
import net.oopscraft.application.core.IdGenerator;
import net.oopscraft.application.core.Pagination;

@CrossOrigin
@RestController
@RequestMapping("/api/boards")
public class BoardController {
	
    @Value("${application.upload.path}")
    String uploadPath;
    
    @Value("${application.upload.limit}")
    int uploadLimit;
	
	@Autowired
	HttpServletResponse response;
	
	@Autowired
	BoardService boardService;
	
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
	public Board getBoard(
		@PathVariable("boardId")String boardId
	) throws Exception {
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
	public List<BoardArticle> getBoardArticles(
		 @PathVariable("boardId")String boardId
		,@RequestParam(value="categoryId", required=false)String categoryId
		,@RequestParam(value="title", required=false)String title
		,@RequestParam(value="contents", required=false)String contents
		,@RequestParam(value="rows", required=false, defaultValue="100")int rows
		,@RequestParam(value="page", required=false, defaultValue="1")int page
	) throws Exception {
		BoardArticle boardArticle = new BoardArticle();
		boardArticle.setBoardId(boardId);
		boardArticle.setCategoryId(categoryId);
		boardArticle.setTitle(title);
		boardArticle.setContents(contents);
		Pagination pagination = new Pagination(rows, page, true);
		List<BoardArticle> boardArticles = boardService.getBoardArticles(boardArticle, pagination);
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
	public BoardArticle getBoardArticle(
		 @PathVariable("boardId")String boardId
		,@PathVariable("articleId")String articleId
	) throws Exception {
		return boardService.getBoardArticle(boardId, articleId);
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
	public BoardArticle createBoardArticle(
		@PathVariable("boardId")String boardId,
		@RequestBody BoardArticle boardArticle
	) throws Exception {
		boardArticle.setBoardId(boardId);
		return boardService.saveBoardArticle(boardArticle);
	}
	
	/**
	 * Updates board article
	 * @param boardId
	 * @param boardArticle
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{boardId}/articles/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public BoardArticle updateBoardArticle(
		@PathVariable("boardId")String boardId,
		@PathVariable("id")String id,
		@RequestBody BoardArticle boardArticle
	) throws Exception {
		boardArticle.setBoardId(boardId);
		boardArticle.setId(id);
		return boardService.saveBoardArticle(boardArticle);
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
	public void deleteBoardArticle(
		@PathVariable("boardId")String boardId,
		@PathVariable("id")String id
	) throws Exception {
		BoardArticle boardArticle = new BoardArticle();
		boardArticle.setBoardId(boardId);
		boardArticle.setId(id);
		boardService.deleteBoardArticle(boardArticle);
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
	public void downloadBoardArticleFile(
		 @PathVariable("boardId")String boardId
		,@PathVariable("articleId")String articleId
		,@PathVariable("id")String id
	) throws Exception {
		BoardArticle boardArticle = boardService.getBoardArticle(boardId, articleId);
		ArticleFile articleFile = boardArticle.getFile(id);
		
		// sends file
		response.setContentType(articleFile.getType());
		response.setContentLengthLong(articleFile.getSize());
		StringBuffer contentDisposition = new StringBuffer()
			.append("attachment")
			.append(";filename=" + articleFile.getName())
			.append(";filename*=UTF-8''" + URLEncoder.encode(articleFile.getName(),"UTF-8"));
		response.setHeader("Content-Disposition", contentDisposition.toString());
		File file = new File(uploadPath + File.separator + "board" + File.pathSeparator + articleFile.getId());
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
	public List<ArticleReply> getBoardArticleReplies(
		 @PathVariable("boardId")String boardId
		,@PathVariable("articleId")String articleId
	) throws Exception {
		Board board = boardService.getBoard(boardId);
		if(board.isReplyUse() == false) {
			throw new Exception("Reply Usage is false");
		}
		return boardService.getBoardArticleReplies(articleId);
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
	public ArticleReply createBoardArticleReply(
		@PathVariable("boardId")String boardId,
		@PathVariable("articleId")String articleId,
		@RequestBody ArticleReply articleReply
	) throws Exception {
		articleReply.setArticleId(articleId);
		return boardService.saveBoardArticleReply(articleReply);
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
	public ArticleReply updateBoardArticleReply(
		@PathVariable("boardId")String boardId,
		@PathVariable("articleId")String articleId,
		@PathVariable("id")String id,
		@RequestBody ArticleReply articleReply
	) throws Exception {
		articleReply.setArticleId(articleId);
		articleReply.setId(id);
		return boardService.saveBoardArticleReply(articleReply);
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
	public void deleteBoardArticleReply(
		@PathVariable("boardId")String boardId,
		@PathVariable("articleId")String articleId,
		@PathVariable("id")String id
	) throws Exception {
		boardService.deleteBoardArticleReply(boardId, articleId, id);
	}


	
	
//	/**
//	 * Creates board article
//	 * @param article
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "{boardId}/article", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@ResponseBody
//	@Transactional(rollbackFor = Exception.class)
//	public BoardArticle createArticle(@RequestBody BoardArticle boardArticle) throws Exception {
//		return boardService.saveBoardArticle(boardArticle);
//	}
//	
//	/**
//	 * Updates board article
//	 * @param article
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "{boardId}/article/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@ResponseBody
//	@Transactional(rollbackFor = Exception.class)
//	public BoardArticle updateArticle(@RequestBody BoardArticle boardArticle) throws Exception {
//		return boardService.saveBoardArticle(boardArticle);
//	}
//	
//	/**
//	 * Deletes board article
//	 * @param article
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "{boardId}/article/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@ResponseBody
//	@Transactional(rollbackFor = Exception.class)
//	public void deleteArticle(@RequestBody BoardArticle boardArticle) throws Exception {
//		boardService.deleteBoardArticle(boardArticle);
//	}
	
	
	
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
//		pagination pagination = new pagination(rows, page, true);
//		List<Article> latestArticles = articleService.getLatestArticles(pagination, boardId);
//		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
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
//		pagination pagination = new pagination(rows, page, true);
//		List<Article> bestArticles = articleService.getBestArticles(pagination, boardId);
//		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
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
//		pagination pagination = new pagination(board.getRowsPerPage(), page, true);
//		ArticleSearchType articleSearchType;
//		if(StringUtility.isNotEmpty(searchType)) {
//			articleSearchType = ArticleSearchType.valueOf(searchType);
//		}else {
//			articleSearchType = null;
//		}
//		List<Article> articles = articleService.getArticles(pagination, boardId, categoryId, articleSearchType, searchValue);
//		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
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
