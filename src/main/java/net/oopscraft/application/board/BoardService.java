package net.oopscraft.application.board;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import net.oopscraft.application.board.dao.BoardRepository;
import net.oopscraft.application.board.entity.Board;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.security.UserDetails;
import net.oopscraft.application.user.entity.Authority;

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
		}else {
			switch(searchType) {
				case ID :
					boardsPage = boardRepository.findByIdContaining(searchValue, pageable);
				break;
				case NAME :
					boardsPage = boardRepository.findByNameContaining(searchValue, pageable);
				break;
			}
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
			one.setSkin(board.getSkin());
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
	
	/**
	 * Checks has access authority
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean hasAccessAuthority(String id) throws Exception {
		boolean hasAccessAuthority = false;
		Board board = getBoard(id);
		switch(board.getAccessPolicy()) {
			case ANONYMOUS :
				hasAccessAuthority = true; 
			break;
			case AUTHENTICATED :
				if(isAuthenticated()) {
					hasAccessAuthority = true;
				}
			break;
			case AUTHORIZED :
				if(hasAuthority(board.getAccessAuthorities())) {
					hasAccessAuthority = true;	
				}
			break;
		}
		return hasAccessAuthority;
	}
	
	/**
	 * Checks has read authority
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean hasReadAuthority(String id) throws Exception {
		boolean hasReadAuthority = false;
		Board board = getBoard(id);
		switch(board.getReadPolicy()) {
			case ANONYMOUS :
				hasReadAuthority = true; 
			break;
			case AUTHENTICATED :
				if(isAuthenticated()) {
					hasReadAuthority = true;
				}
			break;
			case AUTHORIZED :
				if(hasAuthority(board.getReadAuthorities())) {
					hasReadAuthority = true;	
				}
			break;
		}
		return hasReadAuthority;
	}
	
	/**
	 * Checks has write authority
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean hasWriteAuthority(String id) throws Exception {
		boolean hasWriteAuthority = false;
		Board board = getBoard(id);
		switch(board.getWritePolicy()) {
			case ANONYMOUS :
				hasWriteAuthority = true; 
			break;
			case AUTHENTICATED :
				if(isAuthenticated()) {
					hasWriteAuthority = true;
				}
			break;
			case AUTHORIZED :
				if(hasAuthority(board.getWriteAuthorities())) {
					hasWriteAuthority = true;	
				}
			break;
		}
		return hasWriteAuthority;
	}
	
	/**
	 * Checks login user
	 * @return
	 */
	private boolean isAuthenticated() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if(securityContext.getAuthentication() instanceof AnonymousAuthenticationToken == false) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Checks login user has specific authorities.
	 * @param authorities
	 * @return
	 */
	private boolean hasAuthority(List<Authority> authorities) {
		boolean hasAuthority = false;
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if(securityContext.getAuthentication().getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails)securityContext.getAuthentication().getPrincipal();
			if(userDetails.hasAuthority(authorities) == true) {
				hasAuthority = true;
			}
		}
		return hasAuthority;
	}

}
