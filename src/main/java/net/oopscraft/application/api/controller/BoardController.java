package net.oopscraft.application.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import net.oopscraft.application.board.Board;
import net.oopscraft.application.board.BoardService;
import net.oopscraft.application.core.JsonUtils;

@Controller
@RequestMapping("/api/board")
public class BoardController {

	@Autowired
	BoardService boardService;
	
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
	 * @param searchValue
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{boardId}/articles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getArticles(
		@PathVariable("boardId") String boardId,
		@RequestParam(value = "page", required = true, defaultValue="1")int page,
		@RequestParam(value = "searchKey", required = false)String searchKey,
		@RequestParam(value = "searchValue", required = false)String searchValue
	) throws Exception {
		Board board = boardService.getBoard(boardId);
		List<Article> articles = board.getArticles(page, searchKey, searchValue);
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

	
	
	
	@RequestMapping(value = "/{boardId}/article/{articleNo}/replies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getArticleReplies(
		@PathVariable("id") String id
	) throws Exception {
		return new ResponseEntity<>(JsonUtils.toJson(null), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{boardId}/article/{articleNo}/reply", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> addArticleReply(
		@PathVariable("id") String id
	) throws Exception {
		return new ResponseEntity<>(JsonUtils.toJson(null), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{boardId}/article/{articleNo}/reply/{replyNo}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> saveArticleReply(
		@PathVariable("id") String id
	) throws Exception {
		return new ResponseEntity<>(JsonUtils.toJson(null), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{boardId}/article/{articleNo}/reply/{replyNo}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> deleteArticleReply(
		@PathVariable("id") String id
	) throws Exception {
		return new ResponseEntity<>(JsonUtils.toJson(null), HttpStatus.OK);
	}

}
