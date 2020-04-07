package net.oopscraft.application.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.board.entity.BoardArticle;

@Repository
public interface BoardArticleRepository extends JpaRepository<BoardArticle, BoardArticle.Pk>, JpaSpecificationExecutor<BoardArticle> {
		
//	public Page<Article> findByBoardIdOrderByRegistDateDesc(String boardId, Pageable pageable) throws Exception;
//
//	public Page<Article> findByBoardIdAndCategoryIdOrderByRegistDateDesc(String boardId, String categoryId, Pageable pageable) throws Exception;
//	
//	public Page<Article> findByBoardIdAndTitleContainingOrderByRegistDateDesc(String boardId, String title, Pageable pageable) throws Exception;
//	
//	public Page<Article> findByBoardIdAndCategoryIdAndTitleContainingOrderByRegistDateDesc(String boardId, String categoryId, String title, Pageable pageable) throws Exception;
//	
//	public Page<Article> findByBoardIdAndTitleContainingOrContentsContainingOrderByRegistDateDesc(String boardId, String title, String contents, Pageable pageable) throws Exception;
//	
//	public Page<Article> findByBoardIdAndCategoryIdAndTitleContainingOrContentsContainingOrderByRegistDateDesc(String boardId, String categoryId, String title, String contents, Pageable pageable) throws Exception;
//
//	public Page<Article> findByBoardIdAndUserIdContainingOrUserNicknameContainingOrderByRegistDateDesc(String boardId, String userId, String userNickname, Pageable pageable) throws Exception;
//
//	public Page<Article> findByBoardIdAndCategoryIdAndUserIdContainingOrUserNicknameContainingOrderByRegistDateDesc(String boardId, String categoryId, String userId, String userNickname, Pageable pageable) throws Exception;
//	
//	public Page<Article> findByOrderByRegistDateDesc(Pageable pageable) throws Exception;
//	
//	public Page<Article> findByOrderByReadCountDesc(Pageable pageable) throws Exception;

//	@Modifying
//	@Query("UPDATE Article SET readCount = READ_CNT+1 WHERE ATCL_ID = :id")
//	public void increaseReadCount(@Param("id")String id) throws Exception;
	
}
