package net.oopscraft.application.layout.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.board.Board;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.layout.Layout;

public class LayoutRepositoryTest extends ApplicationTestRunner {
	
	private static String TEST_ID = "TEST_ID";
	private static String TEST_NAME = "TEST_NAME";
	
	LayoutRepository layoutRepository;
	
	public LayoutRepositoryTest() throws Exception {
		super();
	}
	
	@Before
	public void before() throws Exception {
		layoutRepository = this.getJpaRepository(LayoutRepository.class);
	}
	
	@Test 
	public void testSave() throws Exception {
		Layout layout = new Layout();
		layout.setId(TEST_ID);
		layout.setName(TEST_NAME);
		layout = layoutRepository.saveAndFlush(layout);
		System.out.println(new TextTable(layout));
		assert(true);
	}
	
	@Test
	public void testFindOne() throws Exception {
		this.testSave();
		Layout layout = layoutRepository.findOne(TEST_ID);
		System.out.println(new TextTable(layout));
		assert(true);
	}
	
	@Test
	public void testFindAll() throws Exception {
		this.testSave();
		List<Layout> layouts = layoutRepository.findAll();
		System.out.println(new TextTable(layouts));
		assert(true);
	}
	
	


}
