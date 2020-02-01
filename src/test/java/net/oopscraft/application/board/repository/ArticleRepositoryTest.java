package net.oopscraft.application.board.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import net.oopscraft.application.article.ArticleRepository;
import net.oopscraft.application.article.entity.Article;
import net.oopscraft.application.board.BoardRepository;
import net.oopscraft.application.board.entity.Board;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.test.ApplicationTestRunner;
import net.oopscraft.application.util.EncodeUtility;

public class ArticleRepositoryTest extends ApplicationTestRunner {
	
	private static String TEST_BOARD_ID = "JUnit";
	private static String TEST_ARTICLE_ID = EncodeUtility.generateUUID();
	private static String TEST_ARTICLE_TITLE = "JUnit test case";
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	ArticleRepository articleRepository;
	
//	@Test 
//	public void testSave() throws Exception {
//		
//		// Inserts board data.
//		Board board = new Board();
//		board.setId(TEST_BOARD_ID);
//		boardRepository.saveAndFlush(board);
//		
//		// Inserts article data.
//		Article article = new Article();
//		article.setId(TEST_ARTICLE_ID);
//		article.setBoardId(TEST_BOARD_ID);
//		article.setTitle(TEST_ARTICLE_TITLE);
//		article = articleRepository.saveAndFlush(article);
//		assert(true);
//	}
//	
//	@Test
//	@Transactional
//	public void testDelete() throws Exception {
//		
//		// Inserts board data.
//		Board board = new Board();
//		board.setId(TEST_BOARD_ID);
//		boardRepository.saveAndFlush(board);
//		
//		// Inserts article data.
//		Article article = new Article();
//		article.setId(TEST_ARTICLE_ID);
//		article.setBoardId(TEST_BOARD_ID);
//		article.setTitle(TEST_ARTICLE_TITLE);
//		article = articleRepository.saveAndFlush(article);
//		System.out.println(new TextTable(article));
//		
//		// delete
//		articleRepository.delete(article.getId());
//		articleRepository.flush();
//		assert(true);
//	}
//
//	@Test
//	public void testFindOne() throws Exception {
//		Article article = articleRepository.findOne(TEST_ARTICLE_ID);
//		System.out.println(new TextTable(article));
//		assert(true);
//	}
//	
//	@Test
//	public void testFindAll() throws Exception {
//		PageInfo pageInfo = new PageInfo(20, 1);
//		Page<Article> articles = articleRepository.findAll(pageInfo.toPageable());
//		System.out.println(articles.getTotalElements());
//		System.out.println(new TextTable(articles.getContent()));
//		assert(true);
//	}
//	
//	@Test
//	public void insertMany() throws Exception {
//		
//		for(int i = 0; i < 100; i ++) {
//			Article article = new Article();
//			article.setId(EncodeUtility.generateUUID());
//			article.setBoardId(TEST_BOARD_ID);
//			article.setTitle(TEST_ARTICLE_TITLE);
//			articleRepository.saveAndFlush(article);
//		}
//
//		
//	}
	

}
