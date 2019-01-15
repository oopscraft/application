package net.oopscraft.application.api.controller;

import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import net.oopscraft.application.article.ArticleFile;
import net.oopscraft.application.article.ArticleReply;
import net.oopscraft.application.board.Board;
import net.oopscraft.application.board.BoardArticle;
import net.oopscraft.application.board.BoardArticleService;
import net.oopscraft.application.board.BoardArticleService.ArticleSearchType;
import net.oopscraft.application.board.BoardService;
import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;

@Controller
@RequestMapping("/api/board")
public class BoardController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	BoardService boardService;
	
	@Autowired
	BoardArticleService boardArticleService;
	
	@Autowired
	HttpServletResponse response;
	
	/**
	 * Gets board info
	 * @param boardId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{boardId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getBoard(@PathVariable("boardId") String boardId) throws Exception {
		Board board = boardService.getBoard(boardId);
		return new ResponseEntity<>(JsonUtils.toJson(board), HttpStatus.OK);
	}
	
	/**
	 * Gets article list.
	 * @param boardId
	 * @param page
	 * @param searchKey
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{boardId}/articles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getArticles(
		@PathVariable("boardId") String boardId,
		@RequestParam(value = "page", required = false, defaultValue="1")Integer page,
		@RequestParam(value = "categoryId", required = false)String categoryId,
		@RequestParam(value = "searchType", required = false)String searchType,
		@RequestParam(value = "searchValue", required = false)String searchValue
	) throws Exception {
		Board board = boardService.getBoard(boardId);
		PageInfo pageInfo = new PageInfo(page, board.getRowsPerPage(),true);
		ArticleSearchType articleSearchType;
		if(StringUtils.isNotBlank(searchType)) {
			articleSearchType = ArticleSearchType.valueOf(searchType);
		}else {
			articleSearchType = null;
		}
		List<BoardArticle> articles = boardArticleService.getArticles(pageInfo, boardId, categoryId, articleSearchType, searchValue);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return new ResponseEntity<>(JsonUtils.toJson(articles), HttpStatus.OK);
	}
	
	/**
	 * Gets article detail
	 * @param boardId
	 * @param articleNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{boardId}/article/{articleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getArticle(
		@PathVariable("boardId") String boardId,
		@PathVariable("articleId") String articleId
	) throws Exception {
		BoardArticle article = boardArticleService.getArticle(articleId);
		return new ResponseEntity<>(JsonUtils.toJson(article), HttpStatus.OK);
	}
	
	/**
	 * Saves articles
	 * @param boardId
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{boardId}/article", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> saveArticles(
		@PathVariable("boardId") String boardId,
		@RequestBody String payload
	) throws Exception {
		BoardArticle article = JsonUtils.toObject(payload, BoardArticle.class);
		article.setBoardId(boardId);
		boardArticleService.saveArticle(article);
		return new ResponseEntity<>(JsonUtils.toJson(article), HttpStatus.OK);
	}
	
	/**
	 * Deletes article
	 * @param boardId
	 * @param articleNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{boardId}/article/{articleId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> deleteArticle(
		@PathVariable("boardId") String boardId,
		@PathVariable("articleId") String articleId
	) throws Exception {
		BoardArticle article = boardArticleService.getArticle(articleId);
		boardArticleService.deleteArticle(article);
		return new ResponseEntity<>(JsonUtils.toJson(null), HttpStatus.OK);
	}

	/**
	 * Gets article replies	
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{boardId}/article/{articleNo}/replies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getArticleReplies(
		@PathVariable("boardId") String boardId,
		@PathVariable("articleId")String articleId
	) throws Exception {
		BoardArticle article = boardArticleService.getArticle(articleId);
		List<ArticleReply> articleReplies = article.getReplies();
		return new ResponseEntity<>(JsonUtils.toJson(articleReplies), HttpStatus.OK);
	}
	
	/**
	 * Saves article reply
	 * @param boardId
	 * @param articleNo
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{boardId}/article/{articleNo}/reply", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> saveArticleReply(
		@PathVariable("boardId")String boardId,
		@PathVariable("articleId")String articleId,
		@RequestBody String payload
	) throws Exception {
		ArticleReply articleReply = JsonUtils.toObject(payload, ArticleReply.class);
		BoardArticle article = boardArticleService.getArticle(articleId);
		articleReply = article.saveReply(articleReply);
		return new ResponseEntity<>(JsonUtils.toJson(articleReply), HttpStatus.OK);
	}
	
	/**
	 * Deletes article reply
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{boardId}/article/{articleNo}/reply/{replyNo}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> deleteArticleReply(
		@PathVariable("boardId")String boardId,
		@PathVariable("articleId")String articleId,
		@PathVariable("replyId")String replyId
	) throws Exception {
		BoardArticle article = boardArticleService.getArticle(articleId);
		article.deleteReply(replyId);
		return new ResponseEntity<>(JsonUtils.toJson(null), HttpStatus.OK);
	}
	
	/**
	 * Uploads file
	 * @param multipartFile
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{boardId}/article/{articleNo}/file", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> uploadArticleFile(
		@PathVariable("boardId")String boardId,
		@PathVariable("articleId")String articleId,
		@RequestParam("file")MultipartFile multipartFile,
		MultipartHttpServletRequest request
	) throws Exception {
		
		// creates id
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString().replaceAll("-", "");
		
		// defines object
		ArticleFile articleFile = new ArticleFile();
		articleFile.setArticleId(articleId);
		articleFile.setId(id);
		articleFile.setName(multipartFile.getOriginalFilename());
		articleFile.setType(multipartFile.getContentType());
		articleFile.setSize(multipartFile.getSize());

		// writes file
		FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), articleFile.getTemporaryFile());
		
		// sends response
		LOGGER.debug("{}", new TextTable(articleFile));
		return new ResponseEntity<>(JsonUtils.toJson(articleFile), HttpStatus.OK);
	}
	

	/**
	 * Downloads file.
	 * @param boardId
	 * @param articleNo
	 * @param fileId
	 * @throws Exception
	 */
	@RequestMapping(value = "/{boardId}/article/{articleNo}/file/{fileId}", method = RequestMethod.GET)
	public void downloadArticleFile(
		@PathVariable("boardId")String boardId,
		@PathVariable("articleId")String articleId,	
		@PathVariable("fileId")String fileId
	) throws Exception {
		
		BoardArticle article = boardArticleService.getArticle(articleId);
		ArticleFile articleFile = article.getFile(fileId);
		
		// sends file
		response.setContentType(articleFile.getType());
		response.setContentLengthLong(articleFile.getSize());
		StringBuffer contentDisposition = new StringBuffer()
			.append("attachment")
			.append(";filename=" + articleFile.getName())
			.append(";filename*=UTF-8''" + URLEncoder.encode(articleFile.getName(),"UTF-8"));
		response.setHeader("Content-Disposition", contentDisposition.toString());
		FileUtils.copyFile(articleFile.getRealFile(), response.getOutputStream());
	}
	

}
