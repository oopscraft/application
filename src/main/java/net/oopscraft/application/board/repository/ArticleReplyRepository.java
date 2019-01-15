package net.oopscraft.application.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.oopscraft.application.board.ArticleReply;
import net.oopscraft.application.board.ArticleReply.Pk;

public interface ArticleReplyRepository extends JpaRepository<ArticleReply, ArticleReply.Pk>{
	
	public List<ArticleReply> findByArticleIdOrderBySequenceAscLevelAsc(String articleId) throws Exception;
	
	@Modifying
	@Query("DELETE FROM ArticleReply WHERE articleId = :articleId")
	public void deleteByArticleId(@Param("articleId")String articleId) throws Exception;

	@Query("SELECT MAX(sequence) FROM ArticleReply WHERE ATCL_ID = :articleId")
	public Integer getMaxSequence(@Param("articleId")String articleId) throws Exception;
	
	@Query("SELECT MAX(level) FROM ArticleReply WHERE UPER_RPLY_ID = :upperId")
	public String getSiblingMaxLevel(@Param("upperId")String upperId) throws Exception;
	
}
