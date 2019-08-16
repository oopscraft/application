package net.oopscraft.application.core;

import java.util.List;

import org.junit.Test;

public class LocaleUtilsTest {
	
	@Test
	public void testGetLocales() throws Exception {
		List<ValueMap> locales = LocaleUtility.getLocales();
		System.out.println(new TextTable(locales));
		assert(true);
	}
	
	@Test
	public void testGetLaunguages() throws Exception {
		List<ValueMap> languages = LocaleUtility.getLanguages();
		System.out.println(new TextTable(languages));
		assert(true);
	}

}
