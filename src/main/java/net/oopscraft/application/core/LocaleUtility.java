package net.oopscraft.application.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocaleUtility {
	
	public static List<ValueMap> getLocales() {
		List<ValueMap> locales = new ArrayList<ValueMap>();
		for(Locale locale : Locale.getAvailableLocales()) {
			ValueMap localeMap = new ValueMap();
			if(locale.toString().length() == 5) {
				localeMap.setString("locale", locale.toString());
				localeMap.setString("displayName", locale.getDisplayCountry(locale));
				locales.add(localeMap);
			}
		}
		return locales;
	}
	
	public static List<ValueMap> getLanguages() {
		List<ValueMap> languages = new ArrayList<ValueMap>();
		for(Locale locale : Locale.getAvailableLocales()) {
			ValueMap localeMap = new ValueMap();
			if(locale.toString().length() == 2) {
				localeMap.setString("language", locale.toString());
				localeMap.setString("displayName", locale.getDisplayLanguage(locale));
				languages.add(localeMap);
			}
		}
		return languages;
	}

}
