package net.oopscraft.application.code;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.test.ApplicationTestRunner;

//@Commit
public class CodeRepositoryTest extends ApplicationTestRunner {
	
	private static String CODE_ID = "TEST_ID";
	private static String CODE_NAME = "Test Name";
	private static String CODE_DESCRIPTION = "Test Description";
	
	@Autowired
	CodeRepository codeRepository;
	
	@Test 
	public void save() throws Exception {
		Code code = new Code();
		code.setId(CODE_ID);
		code.setName(CODE_NAME);
		code.setDescription(CODE_DESCRIPTION);
		
		List<CodeItem> items = code.getItems();
		for(int i = 0; i < 10; i ++ ) {
			CodeItem codeItem = new CodeItem();	
			codeItem.setCodeId(CODE_ID);
			codeItem.setId(String.valueOf(i));
			codeItem.setName("item name");
			items.add(codeItem);
		}
		
		List<CodeEntity> entities = code.getEntities();
		for(int i = 0; i < 10; i ++ ) {
			CodeEntity codeEntity = new CodeEntity();	
			codeEntity.setCodeId(CODE_ID);
			codeEntity.setTableName("table name " + i);
			codeEntity.setColumnName("column name " + i);
			entities.add(codeEntity);
		}
		
		code = codeRepository.save(code);
		System.out.println(JsonConverter.toJson(code));
		assert(true);
	}
	
	@Test
	public void findOne() throws Exception {
		this.save();
		Code code = codeRepository.findOne(CODE_ID);
		System.out.println(JsonConverter.toJson(code));
		assert(true);
	}
	
	@Test
	public void findAll() throws Exception {
		this.save();
		List<Code> codes = codeRepository.findAll();
		System.out.println(JsonConverter.toJson(codes));
		assert(true);
	}
	
	@Test 
	public void delete() throws Exception {
		this.save();
		codeRepository.delete(CODE_ID);
	}


}
