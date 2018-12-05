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
		System.out.println("#map" + new TextTable(map));
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
		System.out.println("#list:" + new TextTable(list));
	}
	
	@Test
	public void testNull() {
		ValueMap map = null;
		System.out.println("map null:" + new TextTable(map));
		
		List<ValueMap> list = null;
		System.out.println("list null:" + new TextTable(list));
	}

}
