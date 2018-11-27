package net.oopscraft.application.core;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TextTableTest {
	
	@Test
	public void testObject() {
		ValueMap map = new ValueMap();
		map.setString("id", "james");
		map.setString("name", "Name");
		System.out.println(new TextTable(map));
	}
	
	@Test
	public void testList() {
		List<ValueMap> list = new ArrayList<ValueMap>();
		for(int i = 0; i < 10; i ++) {
			ValueMap map = new ValueMap();
			map.setString("id", String.format("james[%d]",i));
			map.setString("name", String.format("Name-%d",i));
			list.add(map);
		}
		System.out.println(new TextTable(list));
	}

}
