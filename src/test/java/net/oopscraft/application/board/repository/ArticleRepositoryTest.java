package net.oopscraft.application.board.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.board.Article;
import net.oopscraft.application.core.TextTable;

public class ArticleRepositoryTest extends ApplicationTestRunner {
	
	private static long TEST_NO = System.currentTimeMillis();
	private static String TEST_TITLE = "Test Article";
	
	ArticleRepository articleRepository;
	
	public ArticleRepositoryTest() throws Exception {
		super();
	}
	
	@Before
	public void before() throws Exception {
		articleRepository = this.getJpaRepository(ArticleRepository.class);
	}
	
//	@Test 
//	public void testSave() throws Exception {
//		Article article = new Article();
//		article.setNo(TEST_NO);
//		article.setTitle(TEST_TITLE);
//		article = articleRepository.saveAndFlush(article);
//		System.out.println(new TextTable(article));
//		assert(true);
//	}
//	
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
//	
//	


}
