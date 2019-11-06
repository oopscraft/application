package net.oopscraft.application.code.repository;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.code.dao.CodeRepository;
import net.oopscraft.application.code.entity.Code;
import net.oopscraft.application.core.TextTable;

public class CodeRepositoryTest extends ApplicationTestRunner {
	
	private static String TEST_ID = "TEST_ID";
	private static String TEST_NAME = "Test Name";
	private static String TEST_DESCRIPTION = "Test Description";
	
	@Autowired
	CodeRepository codeRepository;
	
	@Test 
	public void testSave() throws Exception {
		Code code = new Code();
		code.setId(TEST_ID);
		code.setName(TEST_NAME);
		code.setDescription(TEST_DESCRIPTION);
		code = codeRepository.saveAndFlush(code);
		System.out.println(new TextTable(code));
		assert(true);
	}
	
	@Test
	public void testFindOne() throws Exception {
		this.testSave();
		Code code = codeRepository.findOne(TEST_ID);
		System.out.println(new TextTable(code));
		assert(true);
	}
	
	@Test
	public void testFindAll() throws Exception {
		this.testSave();
		List<Code> codes = codeRepository.findAll();
		System.out.println(new TextTable(codes));
		assert(true);
	}
	
	


}
