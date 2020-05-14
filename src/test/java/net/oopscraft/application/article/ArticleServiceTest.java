package net.oopscraft.application.article;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.board.Article;
import net.oopscraft.application.board.ArticleFile;
import net.oopscraft.application.board.ArticleReply;
import net.oopscraft.application.board.ArticleService;
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.Pagination;

public class ArticleServiceTest extends ApplicationTestRunner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleServiceTest.class);
	
	private static final String ARTICLE_ID = "TEST_ID";
	
	@Autowired
	ArticleService articleService;
	
	@Test 
	public void saveArticle() throws Exception {
		Article article = new Article();
		article.setId(ARTICLE_ID);
		article.setTitle("article title");
		article.setContents("article contents");
		
		for(int i = 0; i < 10; i ++ ) {
			ArticleFile file =  new ArticleFile(ARTICLE_ID, String.valueOf(i));
			file.setName("file name " + i);
			article.addFile(file);
		}
		
		for(int i = 0; i < 10; i ++ ) {
			ArticleReply reply = new ArticleReply(ARTICLE_ID, String.valueOf(i));
			reply.setContents("reply content " + i);
			article.addReply(reply);
		}
		
		article = articleService.saveArticle(article);
		System.out.println(JsonConverter.toJson(article));
		assert(true);
	}
	
	@Test
	public void getArticle() throws Exception {
		this.saveArticle();
		Article article = articleService.getArticle(ARTICLE_ID);
		LOGGER.debug("{}", JsonConverter.toJson(article));
		assert(true);
	}
	
	@Test
	public void getArticles() throws Exception {
		this.saveArticle();
		Article article = new Article();
		Pagination pagination = new Pagination(10, 1, true);
		List<Article> articles = articleService.getArticles(article, pagination);
		LOGGER.debug("{}", JsonConverter.toJson(articles));
		assert(true);
	}
	
	@Test 
	public void deleteArticle() throws Exception {
		this.saveArticle();
		Article article = new Article(ARTICLE_ID);
		articleService.deleteArticle(article);
		assert(true);
	}

}
