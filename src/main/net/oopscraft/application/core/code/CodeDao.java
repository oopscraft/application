package net.oopscraft.application.core.code;

import java.util.List;

import net.oopscraft.application.core.ValueMap;

public interface CodeDao {

	public List<ValueMap> selectCodeList(String column, String value, int page, int rownum) throws Exception;
	
	public ValueMap selectCode(String id) throws Exception;
	
	public int insertCode(String id, ValueMap code) throws Exception;
	
	public int updateCode(String id, ValueMap code) throws Exception;
	
	public int deleteCode(String id) throws Exception;
	
	public List<ValueMap> selectCodeItemList(String id) throws Exception;
	
	public ValueMap selectCodeItem(String id) throws Exception;
	
	public int insertCodeItem(String id, ValueMap item) throws Exception;
	
	public int updateCodeItem(String id, ValueMap item) throws Exception;
	
	public int deleteCodeItem(String id) throws Exception;
		
}
