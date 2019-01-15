package net.oopscraft.application.board.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.article.Article;
import net.oopscraft.application.article.repository.ArticleRepository;
import net.oopscraft.application.board.Board;
import net.oopscraft.application.board.BoardArticle;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.user.User;

public class BoardArticleRepositoryTest extends ApplicationTestRunner {
	
	private static String TEST_BOARD_ID = "JUnit";
	private static String TEST_ARTICLE_TITLE = "JUnit test case";
	
	BoardRepository boardRepository;
	BoardArticleRepository boardArticleRepository;
	
	public BoardArticleRepositoryTest() throws Exception {
		super();
	}
	
	@Before
	public void before() throws Exception {
		boardRepository = this.getJpaRepository(BoardRepository.class);
		boardArticleRepository = this.getJpaRepository(BoardArticleRepository.class);
	}
	
	@Test 
	public void testSave() throws Exception {
		// Inserts board data.
		Board board = new Board();
		board.setId(TEST_BOARD_ID);
		boardRepository.saveAndFlush(board);
		
		// Inserts article data.
		BoardArticle boardArticle = new BoardArticle();
		boardArticle.setBoardId(TEST_BOARD_ID);
		boardArticle.setTitle(TEST_ARTICLE_TITLE);
		boardArticle = boardArticleRepository.saveAndFlush(boardArticle);
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
		BoardArticle boardArticle = new BoardArticle();
		boardArticle.setBoardId(TEST_BOARD_ID);
		boardArticle.setTitle(TEST_ARTICLE_TITLE);
		boardArticle = boardArticleRepository.saveAndFlush(boardArticle);
		System.out.println(new TextTable(boardArticle));
		
		// delete
		boardArticleRepository.delete(boardArticle.getId());
		boardArticleRepository.flush();
		assert(true);
	}

	@Test
	public void testFindOne() throws Exception {
		BoardArticle boardArticle = boardArticleRepository.findOne(Long.valueOf(1));
		System.out.println(new TextTable(boardArticle));
		assert(true);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<BoardArticle> boardArticles = boardArticleRepository.findAll();
		System.out.println(new TextTable(boardArticles));
		assert(true);
	}
	
	@Test
	public void testFindByBoardIdOrderByNoDesc() throws Exception {
		Pageable pageable = new PageRequest(0, 10);
		Page<BoardArticle> page = boardArticleRepository.findByBoardIdOrderByNoDesc(TEST_BOARD_ID, pageable);
		List<BoardArticle> boardArticles = page.getContent();
		System.out.println(new TextTable(boardArticles));
		assert(true);
	}

}
