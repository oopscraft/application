package net.oopscraft.application.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.article.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>{

	@Modifying
	@Query("UPDATE Article SET readCount = READ_CNT+1 WHERE ATCL_NO = :no")
	public void increaseReadCount(@Param("no")long no) throws Exception;
	
}
