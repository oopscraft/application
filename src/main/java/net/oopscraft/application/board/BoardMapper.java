package net.oopscraft.application.board;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import net.oopscraft.application.board.entity.BoardArticle;

@Mapper
public interface BoardMapper {
	
	public List<BoardArticle> selectLatestArticles(@Param("boardId")String boardId, RowBounds rowBounds) throws Exception;
	
	public List<BoardArticle> selectBestArticles(@Param("boardId")String boardId, RowBounds rowBounds) throws Exception;

}
