package net.oopscraft.application.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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

import net.oopscraft.application.board.Article;
import net.oopscraft.application.board.ArticleReply;
import net.oopscraft.application.board.Board;
import net.oopscraft.application.board.Board.ArticleSearchType;
import net.oopscraft.application.board.BoardService;
import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.PageInfo;

@Controller
@RequestMapping("/api/board")
public class BoardController {

	@Autowired
	BoardService boardService;
	
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
		List<Article> articles = board.getArticles(pageInfo, categoryId, articleSearchType, searchValue);
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
	@RequestMapping(value = "/{boardId}/article/{articleNo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getArticle(
		@PathVariable("boardId") String boardId,
		@PathVariable("articleNo") long articleNo
	) throws Exception {
		Board board = boardService.getBoard(boardId);
		Article article = board.getArticle(articleNo);
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
		Article article = JsonUtils.toObject(payload, Article.class);
		Board board = boardService.getBoard(boardId);
		article = board.saveArticle(article);
		return new ResponseEntity<>(JsonUtils.toJson(article), HttpStatus.OK);
	}
	
	/**
	 * Deletes article
	 * @param boardId
	 * @param articleNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{boardId}/article/{articleNo}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> deleteArticle(
		@PathVariable("boardId") String boardId,
		@PathVariable("articleNo") long articleNo
	) throws Exception {
		Board board = boardService.getBoard(boardId);
		board.deleteArticle(articleNo);
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
		@PathVariable("articleNo")long articleNo
	) throws Exception {
		Board board = boardService.getBoard(boardId);
		Article article = board.getArticle(articleNo);
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
		@PathVariable("articleNo")long articleNo,
		@RequestBody String payload
	) throws Exception {
		ArticleReply articleReply = JsonUtils.toObject(payload, ArticleReply.class);
		Board board = boardService.getBoard(boardId);
		Article article = board.getArticle(articleNo);
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
		@PathVariable("articleNo")long articleNo,
		@PathVariable("replyNo")long replyNo
	) throws Exception {
		Board board = boardService.getBoard(boardId);
		Article article = board.getArticle(articleNo);
		article.deleteReply(replyNo);
		return new ResponseEntity<>(JsonUtils.toJson(null), HttpStatus.OK);
	}

}
