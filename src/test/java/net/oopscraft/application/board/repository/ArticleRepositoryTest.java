package net.oopscraft.application.board.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.board.Article;
import net.oopscraft.application.board.Board;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.RandomUtils;
import net.oopscraft.application.core.TextTable;

public class ArticleRepositoryTest extends ApplicationTestRunner {
	
	private static String TEST_BOARD_ID = "JUnit";
	private static String TEST_ARTICLE_ID = RandomUtils.generate();
	private static String TEST_ARTICLE_TITLE = "JUnit test case";
	
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
		article.setId(TEST_ARTICLE_ID);
		article.setBoardId(TEST_BOARD_ID);
		article.setTitle(TEST_ARTICLE_TITLE);
		article = articleRepository.saveAndFlush(article);
		assert(true);
	}
	
	@Test
	@Transactional
	public void testDelete() throws Exception {
		
		// Inserts board data.
		Board board = new Board();
		board.setId(TEST_BOARD_ID);
		boardRepository.saveAndFlush(board);
		
		// Inserts article data.
		Article article = new Article();
		article.setId(TEST_ARTICLE_ID);
		article.setBoardId(TEST_BOARD_ID);
		article.setTitle(TEST_ARTICLE_TITLE);
		article = articleRepository.saveAndFlush(article);
		System.out.println(new TextTable(article));
		
		// delete
		articleRepository.delete(article.getId());
		articleRepository.flush();
		assert(true);
	}

	@Test
	public void testFindOne() throws Exception {
		Article article = articleRepository.findOne(TEST_ARTICLE_ID);
		System.out.println(new TextTable(article));
		assert(true);
	}
	
	@Test
	public void testFindAll() throws Exception {
		PageInfo pageInfo = new PageInfo(20, 1);
		Page<Article> articles = articleRepository.findAll(pageInfo.toPageable());
		System.out.println(articles.getTotalElements());
		System.out.println(new TextTable(articles.getContent()));
		assert(true);
	}
	
	@Test
	public void insertMany() throws Exception {
		
		for(int i = 0; i < 100; i ++) {
			Article article = new Article();
			article.setId(RandomUtils.generate());
			article.setBoardId(TEST_BOARD_ID);
			article.setTitle(TEST_ARTICLE_TITLE);
			articleRepository.saveAndFlush(article);
		}

		
	}
	

}
