package net.oopscraft.application.core.mybatis;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.board.Article;
import net.oopscraft.application.board.mapper.ArticleMapper;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.test.ApplicationTestRunnerWithSpring;

public class PageInterceptorTest extends ApplicationTestRunnerWithSpring {

	@Autowired
	ArticleMapper articleMapper;

	@Test
	public void test() throws Exception {
		PageInfo pageInfo = new PageInfo(2,1);
		List<Article> articles = articleMapper.selectLatestArticles(null, pageInfo.toPageRowBounds());
		System.out.println(new TextTable(articles));
	}
	
}
