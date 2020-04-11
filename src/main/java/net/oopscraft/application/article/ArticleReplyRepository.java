package net.oopscraft.application.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.oopscraft.application.article.entity.ArticleReply;

public interface ArticleReplyRepository extends JpaRepository<ArticleReply, ArticleReply.Pk>, JpaSpecificationExecutor<ArticleReply> {
	
}
