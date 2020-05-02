package net.oopscraft.application.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.oopscraft.application.board.entity.ArticleReply;
import net.oopscraft.application.board.entity.ArticleReply.Pk;

public interface ArticleReplyRepository extends JpaRepository<ArticleReply, ArticleReply.Pk>, JpaSpecificationExecutor<ArticleReply> {
	
}
