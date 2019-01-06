package net.oopscraft.application.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.board.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>{
	
	public Page<Article> findByBoardIdOrderByNoDesc(String boardId, Pageable pageable) throws Exception;

	public Page<Article> findByBoardIdAndCategoryIdOrderByNoDesc(String boardId, String categoryId, Pageable pageable) throws Exception;
	
	public Page<Article> findByBoardIdAndTitleContainingOrderByNoDesc(String boardId, String title, Pageable pageable) throws Exception;
	
	public Page<Article> findByBoardIdAndCategoryIdAndTitleContainingOrderByNoDesc(String boardId, String categoryId, String title, Pageable pageable) throws Exception;
	
	public Page<Article> findByBoardIdAndTitleContainingOrContentsContainingOrderByNoDesc(String boardId, String title, String contents, Pageable pageable) throws Exception;
	
	public Page<Article> findByBoardIdAndCategoryIdAndTitleContainingOrContentsContainingOrderByNoDesc(String boardId, String categoryId, String title, String contents, Pageable pageable) throws Exception;
	
	public Page<Article> findByBoardIdAndUserIdContainingOrUserNicknameContainingOrderByNoDesc(String boardId, String userId, String userNickname, Pageable pageable) throws Exception;

	public Page<Article> findByBoardIdAndCategoryIdAndUserIdContainingOrUserNicknameContainingOrderByNoDesc(String boardId, String categoryId, String userId, String userNickname, Pageable pageable) throws Exception;
	
	@Modifying
	@Query("UPDATE Article SET READ_CNT = READ_CNT+1 WHERE ATCL_NO = :no")
	public void increaseReadCount(@Param("no")long no) throws Exception;
	
}
