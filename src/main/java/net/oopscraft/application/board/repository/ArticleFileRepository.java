package net.oopscraft.application.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.oopscraft.application.board.entity.ArticleFile;
import net.oopscraft.application.board.entity.ArticleFile.Pk;

public interface ArticleFileRepository extends JpaRepository<ArticleFile, ArticleFile.Pk>{

}
