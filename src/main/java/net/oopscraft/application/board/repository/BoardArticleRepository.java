package net.oopscraft.application.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.board.BoardArticle;

@Repository
public interface BoardArticleRepository extends JpaRepository<BoardArticle,Long>{
	
	public Page<BoardArticle> findByBoardIdOrderByNoDesc(String boardId, Pageable pageable) throws Exception;
	
	public Page<BoardArticle> findByBoardIdAndCategoryIdOrderByNoDesc(String boardId, String categoryId, Pageable pageable) throws Exception;
	
	public Page<BoardArticle> findByBoardIdAndTitleContainingOrderByNoDesc(String boardId, String title, Pageable pageable) throws Exception;
	
	public Page<BoardArticle> findByBoardIdAndCategoryIdAndTitleContainingOrderByNoDesc(String boardId, String categoryId, String title, Pageable pageable) throws Exception;
	
	public Page<BoardArticle> findByBoardIdAndTitleContainingOrContentsContainingOrderByNoDesc(String boardId, String title, String contents, Pageable pageable) throws Exception;
	
	public Page<BoardArticle> findByBoardIdAndCategoryIdAndTitleContainingOrContentsContainingOrderByNoDesc(String boardId, String categoryId, String title, String contents, Pageable pageable) throws Exception;
	
	public Page<BoardArticle> findByBoardIdAndUserIdContainingOrUserNicknameContainingOrderByNoDesc(String boardId, String userId, String userNickname, Pageable pageable) throws Exception;

	public Page<BoardArticle> findByBoardIdAndCategoryIdAndUserIdContainingOrUserNicknameContainingOrderByNoDesc(String boardId, String categoryId, String userId, String userNickname, Pageable pageable) throws Exception;

}
