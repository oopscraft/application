package net.oopscraft.application.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.board.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>{
	
	public Page<Article> findByBoardIdOrderByNoDesc(String boardId, Pageable pageable) throws Exception;

}
