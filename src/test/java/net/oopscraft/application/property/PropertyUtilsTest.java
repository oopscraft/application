package net.oopscraft.application.property;

import org.junit.Test;

import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.test.ApplicationTestRunner;

public class PropertyUtilsTest extends ApplicationTestRunner {

	public PropertyUtilsTest() throws Exception {
		super();
	}
	
	@Test
	public void testGetProperty() throws Exception {
		Property property = PropertyUtils.getProperty("TEST");
		System.out.println(new TextTable(property));
		assert(true);
	}

}
