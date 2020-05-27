package net.oopscraft.application.article;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.board.Article;
import net.oopscraft.application.board.ArticleFile;
import net.oopscraft.application.board.ArticleReply;
import net.oopscraft.application.board.ArticleRepository;
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.test.ApplicationTestRunner;

public class ArticleRepositoryTest extends ApplicationTestRunner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleRepositoryTest.class);
	
	private static final String ARTICLE_ID = "TEST_ID";
	
	@Autowired
	ArticleRepository articleRepository;
	
	@Test 
	public void save() throws Exception {
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
		
		article = articleRepository.save(article);
		System.out.println(JsonConverter.toJson(article));
		assert(true);
	}
	
	@Test
	public void findOne() throws Exception {
		this.save();
		Article article = articleRepository.findOne(ARTICLE_ID);
		LOGGER.debug("{}", JsonConverter.toJson(article));
		assert(true);
	}
	
	@Test
	public void findAll() throws Exception {
		this.save();
		List<Article> articles = articleRepository.findAll();
		LOGGER.debug("{}", JsonConverter.toJson(articles));
		assert(true);
	}
	
	@Test 
	public void delete() throws Exception {
		this.save();
		articleRepository.delete(ARTICLE_ID);
		assert(true);
	}
}
