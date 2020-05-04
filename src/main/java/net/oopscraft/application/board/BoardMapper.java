package net.oopscraft.application.board;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface BoardMapper {
	
	public List<Article> selectLatestArticles(@Param("boardId")String boardId, RowBounds rowBounds) throws Exception;
	
	public List<Article> selectBestArticles(@Param("boardId")String boardId, RowBounds rowBounds) throws Exception;

}
