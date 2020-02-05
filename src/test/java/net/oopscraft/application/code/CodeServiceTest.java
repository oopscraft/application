package net.oopscraft.application.code;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.code.entity.Code;
import net.oopscraft.application.core.TextTable;

public class CodeServiceTest extends ApplicationTestRunner {
	
	@Autowired
	CodeService codeService;
	
	/**
	 * Tests getCode method
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		Code code = codeService.getCode("TSET");
		System.out.println(new TextTable(code));
		assert(true);
	}

}
