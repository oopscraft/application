package net.oopscraft.application.board.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.board.Board;
import net.oopscraft.application.core.TextTable;

public class BoardRepositoryTest extends ApplicationTestRunner {
	
	private static String TEST_ID = "TEST_ID";
	private static String TEST_NAME = "TEST_NAME";
	
	BoardRepository boardRepository;
	
	public BoardRepositoryTest() throws Exception {
		super();
	}
	
	@Before
	public void before() throws Exception {
		boardRepository = this.getJpaRepository(BoardRepository.class);
	}
	
	@Test 
	public void testSave() throws Exception {
		Board board = new Board();
		board.setId(TEST_ID);
		board.setName(TEST_NAME);
		board = boardRepository.saveAndFlush(board);
		System.out.println(new TextTable(board));
		assert(true);
	}
	
	@Test
	public void testFindOne() throws Exception {
		this.testSave();
		Board board = boardRepository.findOne(TEST_ID);
		System.out.println(new TextTable(board));
		assert(true);
	}
	
	@Test
	public void testFindAll() throws Exception {
		this.testSave();
		List<Board> boards = boardRepository.findAll();
		System.out.println(new TextTable(boards));
		assert(true);
	}
	
	


}
