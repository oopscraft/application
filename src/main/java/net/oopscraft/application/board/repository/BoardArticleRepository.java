package net.oopscraft.application.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.board.BoardArticle;

@Repository
public interface BoardArticleRepository extends JpaRepository<BoardArticle,String>{
	
	public Page<BoardArticle> findByBoardIdOrderByRegistDateDesc(String boardId, Pageable pageable) throws Exception;
	
	public Page<BoardArticle> findByBoardIdAndCategoryIdOrderByRegistDateDesc(String boardId, String categoryId, Pageable pageable) throws Exception;
	
	public Page<BoardArticle> findByBoardIdAndTitleContainingOrderByRegistDateDesc(String boardId, String title, Pageable pageable) throws Exception;
	
	public Page<BoardArticle> findByBoardIdAndCategoryIdAndTitleContainingOrderByRegistDateDesc(String boardId, String categoryId, String title, Pageable pageable) throws Exception;
	
	public Page<BoardArticle> findByBoardIdAndTitleContainingOrContentsContainingOrderByRegistDateDesc(String boardId, String title, String contents, Pageable pageable) throws Exception;
	
	public Page<BoardArticle> findByBoardIdAndCategoryIdAndTitleContainingOrContentsContainingOrderByRegistDateDesc(String boardId, String categoryId, String title, String contents, Pageable pageable) throws Exception;
	
	public Page<BoardArticle> findByBoardIdAndUserIdContainingOrUserNicknameContainingOrderByRegistDateDesc(String boardId, String userId, String userNickname, Pageable pageable) throws Exception;

	public Page<BoardArticle> findByBoardIdAndCategoryIdAndUserIdContainingOrUserNicknameContainingOrderByRegistDateDesc(String boardId, String categoryId, String userId, String userNickname, Pageable pageable) throws Exception;

}
