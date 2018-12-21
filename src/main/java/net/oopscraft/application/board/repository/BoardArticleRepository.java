package net.oopscraft.application.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.board.BoardArticle;
import net.oopscraft.application.board.BoardArticle.Pk;

@Repository
public interface BoardArticleRepository extends JpaRepository<BoardArticle,Pk>{

}
