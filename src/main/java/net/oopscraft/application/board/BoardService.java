package net.oopscraft.application.board;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.security.SecurityPolicy;
import net.oopscraft.application.security.UserDetails;
import net.oopscraft.application.user.Authority;

@Service
public class BoardService {
	
	@Autowired
	BoardRepository boardRepository;
	
	/**
	 * Gets list of board by search condition and value
	 * @param searchKey
	 * @param SearchValue
	 * @return
	 * @throws Exception
	 */
	public List<Board> getBoards(final Board board, Pagination pagination) throws Exception {
		Page<Board> boardsPage = boardRepository.findAll(new Specification<Board>() {
			@Override
			public Predicate toPredicate(Root<Board> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(board.getId() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("id").as(String.class), board.getId() + '%'));
					predicates.add(predicate);
				}
				if(board.getName() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), board.getName() + '%'));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pagination.toPageRequest());
		pagination.setTotalCount(boardsPage.getTotalElements());
		return boardsPage.getContent();
	}

	/**
	 * Gets detail of board
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Board getBoard(String id) throws Exception {
		return boardRepository.findOne(id);
	}

	/**
	 * Saves board details
	 * @param board
	 * @throws Exception
	 */
	public Board saveBoard(Board board) throws Exception {
		Board one = boardRepository.findOne(board.getId());
		if(one == null) {
			one = new Board(board.getId());
		}
		one.setName(board.getName());
		one.setIcon(board.getIcon());
		one.setDescription(board.getDescription());
		one.setSkin(board.getSkin());
		one.setRows(board.getRows());
		one.setReplyUse(board.isReplyUse());
		one.setFileUse(board.isFileUse());
		one.setFileAllowCount(board.getFileAllowCount());
		one.setFileAllowSize(board.getFileAllowSize());

		// category
		one.setCategoryUse(board.isCategoryUse());
		one.getCategories().clear();
		for(int i = 0, size = board.getCategories().size(); i < size; i ++ ) {
			BoardCategory category = board.getCategories().get(i);
			category.setBoardId(board.getId());
			category.setDisplayNo(i+1);
			one.getCategories().add(category);
		}
		
		// access policy
		one.setAccessPolicy(board.getAccessPolicy());
		one.getAccessAuthorities().clear();
		one.getAccessAuthorities().addAll(board.getAccessAuthorities());
		
		// read policy
		one.setReadPolicy(board.getReadPolicy());
		one.getReadAuthorities().clear();
		one.getReadAuthorities().addAll(board.getReadAuthorities());
		
		// write policy
		one.setWritePolicy(board.getWritePolicy());
		one.getWriteAuthorities().clear();
		one.getWriteAuthorities().addAll(board.getWriteAuthorities());
		
		// returns
		return boardRepository.save(one);
	}

	/**
	 * Removes board details
	 * @param board
	 * @throws Exception
	 */
	public void deleteBoard(Board board) throws Exception {
		boardRepository.delete(board);
	}	

}
