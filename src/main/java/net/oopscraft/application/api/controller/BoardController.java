package net.oopscraft.application.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.oopscraft.application.core.JsonUtils;

@Controller
@RequestMapping("/api/board")
public class BoardController {
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getBoard(@PathVariable("id") String id) throws Exception {
		return new ResponseEntity<>(JsonUtils.toJson(null), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/articles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getArticles(@PathVariable("id") String id) throws Exception {
		return new ResponseEntity<>(JsonUtils.toJson(null), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{boardId}/article/{no}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getArticles(
		@PathVariable("boardId") String id,
		@PathVariable("no") long no
	) throws Exception {
		return new ResponseEntity<>(JsonUtils.toJson(null), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{boardId}/article", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> saveArticles() throws Exception {
		return new ResponseEntity<>(JsonUtils.toJson(null), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{boardId}/article/{no}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> deleteArticles(
		@PathVariable("id") String id
	) throws Exception {
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
