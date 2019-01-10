package net.oopscraft.application.board;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.oopscraft.application.board.repository.BoardRepository;
import net.oopscraft.application.core.PageInfo;

@Service
public class BoardService {
	
    @PersistenceContext
    EntityManager entityManager;
	
	@Autowired
	BoardRepository boardRepository;
	
	public enum BoardSearchType {
		ID,
		NAME
	}
	
	public List<Board> getBoards(PageInfo pageInfo, BoardSearchType searchType, String searchValue) throws Exception {
		Pageable pageable = pageInfo.toPageable();
		Page<Board> boardsPage = null;
		if(searchType == null) {
			boardsPage = boardRepository.findAll(pageable);
		}
		pageInfo.setTotalCount(boardsPage.getTotalElements());
		return boardsPage.getContent();
	}
	
	public Board getBoard(String id) throws Exception {
		Board board = boardRepository.findOne(id);
		board.setEntityManager(entityManager);
		return board;
	}
	
	public void saveBoard(Board board) throws Exception {
		boardRepository.saveAndFlush(board);
	}
	
	public void deleteBoard(String id) throws Exception {
		boardRepository.delete(id);
	}

}
