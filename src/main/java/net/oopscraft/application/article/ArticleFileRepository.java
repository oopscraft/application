package net.oopscraft.application.article;

import org.springframework.data.jpa.repository.JpaRepository;

import net.oopscraft.application.article.entity.ArticleFile;
import net.oopscraft.application.article.entity.ArticleFile.Pk;

public interface ArticleFileRepository extends JpaRepository<ArticleFile, ArticleFile.Pk>{

}
