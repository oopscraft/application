package net.oopscraft.application.sample;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface SampleMapper {
	
	public List<Sample> getSamples(@Param("sample") Sample sample, RowBounds rowBounds) throws Exception;
	
	public List<Map<String,Object>> getSampleSummary(@Param("key1") String key1) throws Exception;

}
