package net.oopscraft.application.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.board.Article;
import net.oopscraft.application.board.Article.Pk;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Pk>{

}
