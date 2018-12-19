package net.oopscraft.application.code;

import org.junit.Test;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.core.TextTable;

public class CodeUtilsTest extends ApplicationTestRunner {

	public CodeUtilsTest() throws Exception {
		super();
	}
	
	@Test
	public void testGetCode() throws Exception {
		Code code = CodeUtils.getCode("SAMPLE_CD");
		System.out.println(new TextTable(code.getItem("ITEM_3")));
		System.out.println(new TextTable(code));
		System.out.println(new TextTable(code.getItems()));
		assert(true);
	}

}
