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
	
	public enum BoardSearchType { ID,NAME	}
	
	/**
	 * Gets boards
	 * @param pageInfo
	 * @param searchType
	 * @param searchValue
	 * @return
	 * @throws Exception
	 */
	public List<Board> getBoards(PageInfo pageInfo, BoardSearchType searchType, String searchValue) throws Exception {
		Pageable pageable = pageInfo.toPageable();
		Page<Board> boardsPage = null;
		if(searchType == null) {
			boardsPage = boardRepository.findAll(pageable);
		}
		pageInfo.setTotalCount(boardsPage.getTotalElements());
		return boardsPage.getContent();
	}
	
	/**
	 * Gets board
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Board getBoard(String id) throws Exception {
		Board board = boardRepository.findOne(id);
		return board;
	}
	
	/**
	 * Saves board
	 * @param board
	 * @throws Exception
	 */
	public void saveBoard(Board board) throws Exception {
		Board one = boardRepository.findOne(board.getId());
		if(one == null) {
			boardRepository.saveAndFlush(board);
		}else {
			one.setName(board.getName());
			one.setIcon(board.getIcon());
			one.setLayoutId(board.getLayoutId());
			one.setSkinId(board.getSkinId());
			one.setAccessPolicy(board.getAccessPolicy());
			one.setAccessAuthorities(board.getAccessAuthorities());
			one.setReadPolicy(board.getReadPolicy());
			one.setReadAuthorities(board.getReadAuthorities());
			one.setWritePolicy(board.getWritePolicy());
			one.setWriteAuthorities(board.getWriteAuthorities());
			one.setRowsPerPage(board.getRowsPerPage());
			one.setCategoryUseYn(board.getCategoryUseYn());

			// all replace categories
			one.getCategories().clear();
			one.getCategories().addAll(board.getCategories());
			
			one.setReplyUseYn(board.getReplyUseYn());
			one.setFileUseYn(board.getFileUseYn());
			boardRepository.saveAndFlush(one);
		}
	}
	
	/**
	 * Deletes board
	 * @param id
	 * @throws Exception
	 */
	public void deleteBoard(String id) throws Exception {
		Board board = boardRepository.findOne(id);
		boardRepository.delete(board);
	}

}
