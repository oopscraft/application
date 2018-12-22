package net.oopscraft.application.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.board.ArticleContents;

@Repository
public interface ArticleContentsRepository extends JpaRepository<ArticleContents, ArticleContents.Pk>{

}
