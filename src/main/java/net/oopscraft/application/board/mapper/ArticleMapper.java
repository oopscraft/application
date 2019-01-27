package net.oopscraft.application.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import net.oopscraft.application.board.Article;

@Repository
public interface ArticleMapper {
	
	public List<Article> selectLatestArticles(@Param("boardId")String boardId, RowBounds rowBounds) throws Exception;
	
	public List<Article> selectBestArticles(@Param("boardId")String boardId, RowBounds rowBounds) throws Exception;

}