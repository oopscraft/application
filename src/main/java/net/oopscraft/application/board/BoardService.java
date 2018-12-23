package net.oopscraft.application.board;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.board.repository.ArticleRepository;
import net.oopscraft.application.board.repository.BoardRepository;
import net.oopscraft.application.code.CodeService;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;

public class BoardService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(CodeService.class);
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	ArticleRepository articleRepository;
	
	public class BoardSearch {
		
	}
	
	/**
	 * Search condition class 
	 *
	 */
	public class ArticleSearch {
		String id;
		String name;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	public List<Board> getBoards(BoardSearch boardSearch) throws Exception {
		List<Board> boards = new ArrayList<Board>();
		return boards;
	}
	
	public Board getBoard(String id) throws Exception {
		Board board = boardRepository.findOne(id);
		return board;
	}
	
	public Board saveBoard(Board board) throws Exception {
		return null;
	}
	
	public Board removeBoard(String id) throws Exception {
		return null;
	}
	
	public List<Article> getArticles(String boardId, ArticleSearch articleSearch) throws Exception {
		return null;
	}
	
	public Article getArticle(String boardId, long no) throws Exception {
		Article article = articleRepository.findOne(new Article.Pk(boardId, no));
		LOGGER.debug("{}", new TextTable(article));
		return article;
	}
	
	public Article saveArticle(Article article) throws Exception {
		return null;
	}
	
	public Article removeArticle(String boardId, long no) throws Exception {
		Article article = articleRepository.findOne(new Article.Pk(boardId,no));
		articleRepository.delete(article);
		return null;
	}
	
	public List<Reply> getArticleReplies(String boardId, long no, PageInfo pageInfo) throws Exception {
		return null;
	}

//	/**
//	 * Search condition class 
//	 *
//	 */
//	public class SearchCondition {
//		String title;
//
//		public String getTitle() {
//			return title;
//		}
//
//		public void setTitle(String title) {
//			this.title = title;
//		}
//	}
//	
//	/**
//	 * Gets Articles
//	 * @param searchType
//	 * @param searchValue
//	 * @param pageInfo
//	 * @return
//	 * @throws Exception
//	 */
//	public List<Article> getArticles(SearchCondition searchCondition, PageInfo pageInfo ) throws Exception {
//		List<Article> articles = null;
//		Page<Article> page = null;
//		Pageable pageable = new PageRequest(pageInfo.getPage() - 1, pageInfo.getRows());
//		if(!StringUtils.isEmpty(searchCondition.getTitle())) {
//			page = articleRepository.findAll(pageable);
//		}else {
//			page = articleRepository.findAll(pageable);
//		}
//		articles = page.getContent();
//		if (pageInfo.isEnableTotalCount() == true) {
//			pageInfo.setTotalCount(page.getTotalElements());
//		}
//		LOGGER.debug("+ codes: {}", new TextTable(articles));
//		return page.getContent();
//	}
	
//	/**
//	 * Gets detail of code
//	 * 
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public Article getArticle(String id) throws Exception {
//		Article Article = articleRepository.findOne(id);
//		return Article;
//	}
//	
//	/**
//	 * Saves code
//	 * 
//	 * @param code
//	 * @return
//	 * @throws Exception
//	 */
//	public Article saveArticle(Article code) throws Exception {
//		Article one = articleRepository.findOne(code.getId());
//		if(one == null) {
//			one = new Article();
//			one.setId(code.getId());
//		}
//		one.setName(code.getName());
//		one.setDescription(code.getDescription());
//		
//		codeRepository.save(one);
//		return codeRepository.findOne(code.getId());
//	}
//	
//	/**
//	 * Removes code
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public Article removeArticle(String id) throws Exception {
//		Article code = codeRepository.getOne(id);
//		codeRepository.delete(code);
//		return code;
//	}
}
