package net.oopscraft.application.board.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.article.Article;
import net.oopscraft.application.article.repository.ArticleRepository;
import net.oopscraft.application.core.TextTable;

public class ArticleRepositoryTest extends ApplicationTestRunner {
	
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
		Article article = new Article();
		article.setNo(TEST_ARTICLE_NO);
		article = articleRepository.saveAndFlush(article);
		System.out.println(new TextTable(article));
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
