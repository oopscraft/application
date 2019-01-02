package net.oopscraft.application.board.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.oopscraft.application.board.Article;
import net.oopscraft.application.board.Board;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.test.ApplicationTestRunner;

public class ArticleRepositoryTest extends ApplicationTestRunner {
	
	private static String TEST_BOARD_ID = "JUnit";
	private static long TEST_ARTICLE_NO = System.currentTimeMillis();
	
	BoardRepository boardRepository;
	ArticleRepository articleRepository;
	
	public ArticleRepositoryTest() throws Exception {
		super();
	}
	
	@Before
	public void before() throws Exception {
		boardRepository = this.getJpaRepository(BoardRepository.class);
		articleRepository = this.getJpaRepository(ArticleRepository.class);
	}
	
	@Test 
	public void testSave() throws Exception {
		
		// Inserts board data.
		Board board = new Board();
		board.setId(TEST_BOARD_ID);
		boardRepository.saveAndFlush(board);
		
		// Inserts article data.
		Article article = new Article();
		article.setBoardId(TEST_BOARD_ID);
		article = articleRepository.saveAndFlush(article);
		System.out.println(new TextTable(article));
		
		// assert
		assert(true);
	}
	
	@Test
	public void testFindOne() throws Exception {
		this.testSave();
		Article article = articleRepository.findOne(TEST_ARTICLE_NO);
		System.out.println(new TextTable(article));
		assert(true);
	}
	
	@Test
	public void testFindAll() throws Exception {
		this.testSave();
		List<Article> articles = articleRepository.findAll();
		System.out.println(new TextTable(articles));
		assert(true);
	}

}
