package net.oopscraft.application.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.board.Contents;

@Repository
public interface ArticleContentsRepository extends JpaRepository<Contents, Contents.Pk>{

}
