package net.oopscraft.application.code;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.code.entity.Code;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.test.ApplicationTestRunner;

public class CodeServiceTest extends ApplicationTestRunner {
	
	@Autowired
	CodeService codeService;
	
	/**
	 * Tests getCode method
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		Code code = codeService.getCode("TEST");
		System.out.println(new TextTable(code));
		assert(true);
	}

}
