package net.oopscraft.application.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.oopscraft.application.board.ArticleReply;

public interface ArticleReplyRepository extends JpaRepository<ArticleReply, ArticleReply.Pk>{

	public List<ArticleReply> findByArticleNoOrderBySequenceAscLevelAsc(long articleNo) throws Exception;

	@Query("SELECT MAX(sequence) FROM ArticleReply WHERE ATCL_NO = :articleNo")
	public Integer getMaxSequence(@Param("articleNo")long articleNo) throws Exception;
	
	@Query("SELECT MAX(level) FROM ArticleReply WHERE UPER_RPLY_NO = :upperNo")
	public String getSiblingMaxLevel(@Param("upperNo")long upperNo) throws Exception;
	
}
