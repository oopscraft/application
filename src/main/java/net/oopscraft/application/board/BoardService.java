package net.oopscraft.application.board;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.oopscraft.application.board.repository.BoardRepository;

@Service
public class BoardService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(BoardService.class);
	
    @PersistenceContext
    EntityManager entityManager;
	
	@Autowired
	BoardRepository boardRepository;
	
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
	
	public List<Board> getBoards(int page, String searchKey, String searchValue) throws Exception {
		List<Board> boards = new ArrayList<Board>();
		return boards;
	}
	
	public Board getBoard(String id) throws Exception {
		Board board = boardRepository.findOne(id);
		board.setEntityManager(entityManager);
		return board;
	}
	
	public Board saveBoard(Board board) throws Exception {
		return null;
	}
	
	public Board removeBoard(String id) throws Exception {
		return null;
	}

}
