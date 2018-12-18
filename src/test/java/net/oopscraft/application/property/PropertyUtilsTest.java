package net.oopscraft.application.property;

import org.junit.Test;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.core.TextTable;

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
	
	@Test
	public void testGetValue() throws Exception {
		String value = PropertyUtils.getValue("TEST");
		System.out.println(String.format("value is [%s]", value));
		assert(true);
	}
	

}
