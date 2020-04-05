package net.oopscraft.application.board;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.board.entity.Board;
import net.oopscraft.application.board.entity.BoardArticle;
import net.oopscraft.application.board.entity.BoardCategory;
import net.oopscraft.application.core.IdGenerator;
import net.oopscraft.application.core.PageInfo;

@Service
public class BoardService {
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	BoardArticleRepository boardArticleRepository;

	/**
	 * Gets list of board by search condition and value
	 * @param searchKey
	 * @param SearchValue
	 * @return
	 * @throws Exception
	 */
	public List<Board> getBoards(final Board board, PageInfo pageInfo) throws Exception {
		Page<Board> boardsPage = boardRepository.findAll(new  Specification<Board>() {
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
		}, pageInfo.toPageable());
		pageInfo.setTotalCount(boardsPage.getTotalElements());
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
		one.setRowsPerPage(board.getRowsPerPage());
		one.setReplyUse(board.isReplyUse());
		one.setFileUse(board.isFileUse());

		// category
		one.setCategoryUse(board.isCategoryUse());
		one.getCategories().clear();
		for(int i = 0, size = board.getCategories().size(); i < size; i ++ ) {
			BoardCategory category = board.getCategories().get(i);
			category.setBoardId(board.getId());
			category.setSequence(i+1);
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
	
	
	/**
	 * Returns board articles
	 * @param code
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<BoardArticle> getBoardArticles(final BoardArticle boardArticle, PageInfo pageInfo) throws Exception {
		Page<BoardArticle> boardArticlesPage = boardArticleRepository.findAll(new  Specification<BoardArticle>() {
			@Override
			public Predicate toPredicate(Root<BoardArticle> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				// boardId
				predicates.add(
					criteriaBuilder.and(criteriaBuilder.equal(root.get("boardId").as(String.class), boardArticle.getBoardId()))
				);
				
				// categoryId
				if(boardArticle.getCategoryId() != null) {
					predicates.add(
						criteriaBuilder.and(criteriaBuilder.equal(root.get("categoryId").as(String.class), boardArticle.getCategoryId()))
					);
				}

				// title
				if(boardArticle.getTitle() != null) {
					predicates.add(
						criteriaBuilder.and(criteriaBuilder.like(root.get("title").as(String.class), '%' + boardArticle.getTitle() + '%'))
					);
				}
				
				// contents
				if(boardArticle.getContents() != null) {
					predicates.add(
						criteriaBuilder.and(criteriaBuilder.like(root.get("contents").as(String.class), '%' + boardArticle.getContents() + '%'))
					);
				}

				// returns 
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pageInfo.toPageable());
		pageInfo.setTotalCount(boardArticlesPage.getTotalElements());
		return boardArticlesPage.getContent();
	}
	
	/**
	 * Gets board article
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public BoardArticle getBoardArticle(BoardArticle boardArticle) throws Exception {
		return boardArticleRepository.findOne(boardArticle.getId());
	}
	
	/**
	 * Saves code
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public BoardArticle saveBoardArticle(BoardArticle boardArticle) throws Exception {
		if(StringUtils.isBlank(boardArticle.getId())) {
			boardArticle.setId(IdGenerator.uuid());
		}
		BoardArticle one = boardArticleRepository.findOne(boardArticle.getId());
		if(one == null) {
			one = new BoardArticle(boardArticle.getBoardId(),boardArticle.getId());
		}
		one.setTitle(boardArticle.getTitle());
		one.setContents(boardArticle.getContents());
		
		// save code entity
		return boardArticleRepository.save(one);
	}
	
	/**
	 * Deletes board article
	 * @param code
	 * @throws Exception
	 */
	public void deleteBoardArticle(BoardArticle boardArticle) throws Exception {
		boardArticleRepository.delete(boardArticle);
	}
	
	
	
//    @PersistenceContext
//    EntityManager entityManager;
//    
//	@Autowired
//	BoardRepository boardRepository;
//	
//	public enum BoardSearchType { ID,NAME	}
//	
//	/**
//	 * Gets boards
//	 * @param pageInfo
//	 * @param searchType
//	 * @param searchValue
//	 * @return
//	 * @throws Exception
//	 */
//	public List<Board> getBoards(PageInfo pageInfo, BoardSearchType searchType, String searchValue) throws Exception {
//		Pageable pageable = pageInfo.toPageable();
//		Page<Board> boardsPage = null;
//		if(searchType == null) {
//			boardsPage = boardRepository.findAll(pageable);
//		}else {
//			switch(searchType) {
//				case ID :
//					boardsPage = boardRepository.findByIdContaining(searchValue, pageable);
//				break;
//				case NAME :
//					boardsPage = boardRepository.findByNameContaining(searchValue, pageable);
//				break;
//			}
//		}
//		pageInfo.setTotalCount(boardsPage.getTotalElements());
//		return boardsPage.getContent();
//	}
//	
//	/**
//	 * Gets board
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public Board getBoard(String id) throws Exception {
//		Board board = boardRepository.findOne(id);
//		return board;
//	}
//	
//	/**
//	 * Saves board
//	 * @param board
//	 * @throws Exception
//	 */
//	public void saveBoard(Board board) throws Exception {
//		Board one = boardRepository.findOne(board.getId());
//		if(one == null) {
//			boardRepository.saveAndFlush(board);
//		}else {
//			one.setName(board.getName());
//			one.setIcon(board.getIcon());
//			one.setSkin(board.getSkin());
//			one.setAccessPolicy(board.getAccessPolicy());
//			one.setAccessAuthorities(board.getAccessAuthorities());
//			one.setReadPolicy(board.getReadPolicy());
//			one.setReadAuthorities(board.getReadAuthorities());
//			one.setWritePolicy(board.getWritePolicy());
//			one.setWriteAuthorities(board.getWriteAuthorities());
//			one.setRowsPerPage(board.getRowsPerPage());
//			one.setCategoryUseYn(board.getCategoryUseYn());
//
//			// all replace categories
//			one.getCategories().clear();
//			one.getCategories().addAll(board.getCategories());
//			
//			one.setReplyUseYn(board.getReplyUseYn());
//			one.setFileUseYn(board.getFileUseYn());
//			boardRepository.saveAndFlush(one);
//		}
//	}
//	
//	/**
//	 * Deletes board
//	 * @param id
//	 * @throws Exception
//	 */
//	public void deleteBoard(String id) throws Exception {
//		Board board = boardRepository.findOne(id);
//		boardRepository.delete(board);
//	}
//	
//	/**
//	 * Checks has access authority
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean hasAccessAuthority(String id) throws Exception {
//		boolean hasAccessAuthority = false;
//		Board board = getBoard(id);
//		switch(board.getAccessPolicy()) {
//			case ANONYMOUS :
//				hasAccessAuthority = true; 
//			break;
//			case AUTHENTICATED :
//				if(isAuthenticated()) {
//					hasAccessAuthority = true;
//				}
//			break;
//			case AUTHORIZED :
//				if(hasAuthority(board.getAccessAuthorities())) {
//					hasAccessAuthority = true;	
//				}
//			break;
//		}
//		return hasAccessAuthority;
//	}
//	
//	/**
//	 * Checks has read authority
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean hasReadAuthority(String id) throws Exception {
//		boolean hasReadAuthority = false;
//		Board board = getBoard(id);
//		switch(board.getReadPolicy()) {
//			case ANONYMOUS :
//				hasReadAuthority = true; 
//			break;
//			case AUTHENTICATED :
//				if(isAuthenticated()) {
//					hasReadAuthority = true;
//				}
//			break;
//			case AUTHORIZED :
//				if(hasAuthority(board.getReadAuthorities())) {
//					hasReadAuthority = true;	
//				}
//			break;
//		}
//		return hasReadAuthority;
//	}
//	
//	/**
//	 * Checks has write authority
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean hasWriteAuthority(String id) throws Exception {
//		boolean hasWriteAuthority = false;
//		Board board = getBoard(id);
//		switch(board.getWritePolicy()) {
//			case ANONYMOUS :
//				hasWriteAuthority = true; 
//			break;
//			case AUTHENTICATED :
//				if(isAuthenticated()) {
//					hasWriteAuthority = true;
//				}
//			break;
//			case AUTHORIZED :
//				if(hasAuthority(board.getWriteAuthorities())) {
//					hasWriteAuthority = true;	
//				}
//			break;
//		}
//		return hasWriteAuthority;
//	}
//	
//	/**
//	 * Checks login user
//	 * @return
//	 */
//	private boolean isAuthenticated() {
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//		if(securityContext.getAuthentication() instanceof AnonymousAuthenticationToken == false) {
//			return true;
//		}else {
//			return false;
//		}
//	}
//	
//	/**
//	 * Checks login user has specific authorities.
//	 * @param authorities
//	 * @return
//	 */
//	private boolean hasAuthority(List<Authority> authorities) {
//		boolean hasAuthority = false;
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//		if(securityContext.getAuthentication().getPrincipal() instanceof UserDetails) {
//			UserDetails userDetails = (UserDetails)securityContext.getAuthentication().getPrincipal();
//			if(userDetails.hasAuthority(authorities) == true) {
//				hasAuthority = true;
//			}
//		}
//		return hasAuthority;
//	}

}
