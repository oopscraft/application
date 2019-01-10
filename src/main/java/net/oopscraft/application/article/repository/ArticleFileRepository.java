package net.oopscraft.application.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.oopscraft.application.article.ArticleFile;

public interface ArticleFileRepository extends JpaRepository<ArticleFile, ArticleFile.Pk>{

}
