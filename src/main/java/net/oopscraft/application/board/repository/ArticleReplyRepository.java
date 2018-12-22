package net.oopscraft.application.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.board.Article.Pk;
import net.oopscraft.application.board.ArticleReply;

@Repository
public interface ArticleReplyRepository extends JpaRepository<ArticleReply,Pk>{

}
