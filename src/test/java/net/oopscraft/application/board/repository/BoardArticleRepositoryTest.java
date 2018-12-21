package net.oopscraft.application.board.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.board.BoardArticle;
import net.oopscraft.application.core.TextTable;

public class BoardArticleRepositoryTest extends ApplicationTestRunner {
	
	private static long TEST_NO = System.currentTimeMillis();
	private static String TEST_TITLE = "Test Article";
	private static String TEST_CONTENTS = "Test Contents";
	
	BoardArticleRepository boardArticleRepository;
	
	public BoardArticleRepositoryTest() throws Exception {
		super();
	}
	
	@Before
	public void before() throws Exception {
		boardArticleRepository = this.getJpaRepository(BoardArticleRepository.class);
	}
	
	@Test 
	public void testSave() throws Exception {
		BoardArticle article = new BoardArticle();
		article.setNo(TEST_NO);
		article.setTitle(TEST_TITLE);
		article.setContents(TEST_CONTENTS);
		article = articleRepository.saveAndFlush(article);
		System.out.println(new TextTable(article));
		assert(true);
	}
	
//	@Test
//	public void testFindOne() throws Exception {
//		this.testSave();
//		Article articles = articleRepository.findOne(TEST_NO);
//		System.out.println(new TextTable(articles));
//		assert(true);
//	}
//	
//	@Test
//	public void testFindAll() throws Exception {
//		this.testSave();
//		List<Article> articles = articleRepository.findAll();
//		System.out.println(new TextTable(articles));
//		assert(true);
//	}
	
	


}
