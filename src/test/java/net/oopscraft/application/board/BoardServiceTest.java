package net.oopscraft.application.board;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.article.ArticleServiceTest;
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.test.ApplicationTestRunner;

public class BoardServiceTest extends ApplicationTestRunner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleServiceTest.class);
	private static final String BOARD_ID = "BORAD_ID";
	
	@Autowired
	BoardService boardService;
	
	@Test 
	public void saveBoard() throws Exception {
		Board board = new Board();
		board.setId(BOARD_ID);
		board.setName("board name");
		board = boardService.saveBoard(board);
		System.out.println(JsonConverter.toJson(board));
		assert(true);
	}
	
	@Test
	public void getBoard() throws Exception {
		this.saveBoard();
		Board board = boardService.getBoard(BOARD_ID);
		LOGGER.debug("{}", JsonConverter.toJson(board));
		assert(true);
	}
	
	@Test
	public void getBoards() throws Exception {
		this.saveBoard();
		Board board = new Board();
		Pagination pagination = new Pagination();
		List<Board> boards = boardService.getBoards(board, pagination);
		LOGGER.debug("{}", JsonConverter.toJson(boards));
		assert(true);
	}
	
	@Test 
	public void deleteBoard() throws Exception {
		this.saveBoard();
		Board board = new Board(BOARD_ID);
		boardService.deleteBoard(board);
		assert(true);
	}
	
}
