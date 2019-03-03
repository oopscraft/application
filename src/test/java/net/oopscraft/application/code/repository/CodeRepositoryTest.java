package net.oopscraft.application.code.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.oopscraft.application.code.Code;
import net.oopscraft.application.code.repository.CodeRepository;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.test.ApplicationTestRunner;

public class CodeRepositoryTest extends ApplicationTestRunner {
	
	private static String TEST_ID = "TEST_ID";
	private static String TEST_NAME = "Test Name";
	private static String TEST_DESCRIPTION = "Test Description";
	
	CodeRepository codeRepository;
	
	public CodeRepositoryTest() throws Exception {
		super();
	}
	
	@Before
	public void before() throws Exception {
		codeRepository = this.getJpaRepository(CodeRepository.class);
	}
	
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
