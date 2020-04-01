package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.article.ArticleService;
import net.oopscraft.application.article.entity.Article;


@PreAuthorize("hasAuthority('ADMN_ATCL')")
@Controller
@RequestMapping("/admin/article")
public class ArticleController {

	@Autowired
	HttpServletResponse response;

	@Autowired
	ArticleService articleService;

	/**
	 * Forwards page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/article.html");
		return modelAndView;
	}
	
	/**
	 * Returns articles
	 * @param role
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getArticles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Article> getArticles(@ModelAttribute Article article, @ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<Article> articles = articleService.getArticles(article, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return articles;
	}
	
	/**
	 * Returns article
	 * @param article
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getArticle", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Article getArticle(@ModelAttribute Article article) throws Exception {
		return articleService.getArticle(article.getId());
	}

	/**
	 * Saves article
	 * @param article
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveArticle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Article saveArticle(@RequestBody Article article) throws Exception {
		return articleService.saveArticle(article);
	}
	
	/**
	 * Deletes article
	 * @param article
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteArticle", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteArticle(@RequestBody Article article) throws Exception {
		articleService.deleteArticle(article);
	}

}