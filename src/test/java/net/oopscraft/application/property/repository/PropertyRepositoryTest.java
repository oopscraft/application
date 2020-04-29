package net.oopscraft.application.property.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.property.PropertyRepository;
import net.oopscraft.application.property.entity.Property;

public class PropertyRepositoryTest extends ApplicationTestRunner {
	
	private static String TEST_ID = "TEST_HOST";
	private static String TEST_NAME = "Test Property Name";
	private static String TEST_VALUE = "127.0.0.1";
	private static String TEST_DESCRIPTION = "Test property";
	
	PropertyRepository propertyRepository;
	
	public PropertyRepositoryTest() throws Exception {
		super();
	}
	
	@Test 
	public void testSave() throws Exception {
		Property property = new Property();
		property.setId(TEST_ID);
		property.setValue(TEST_VALUE);
		property.setDescription(TEST_DESCRIPTION);
		property = propertyRepository.saveAndFlush(property);
		System.out.println(new TextTable(property));
		assert(true);
	}
	
	@Test
	public void testFindOne() throws Exception {
		this.testSave();
		Property property = propertyRepository.findOne(TEST_ID);
		System.out.println(new TextTable(property));
		assert(true);
	}

	
	@Test
	public void testFindAll() throws Exception {
		this.testSave();
		List<Property> properties = propertyRepository.findAll();
		System.out.println(new TextTable(properties));
		assert(true);
	}
	
	


}
