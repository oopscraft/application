package net.oopscraft.application.locale;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.ValueMap;

@Service
public class LocaleService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LocaleService.class);
	
    @Value("${application.locales}")
    private String locales;

	/**
	 * Gets available Locale list
	 * @return
	 * @throws Exception
	 */
	private static List<Locale> getAvailableLocales() throws Exception {
		List<Locale> locales = new ArrayList<Locale>();
		for(Locale locale : Locale.getAvailableLocales()) {
			if(locale.toString().length() == 5) {
				locales.add(locale);
			}
		}
		return locales;
	}
	
	private List<Locale> getConfiguredLocales() throws Exception {
		List<Locale> availableLocales = new ArrayList<Locale>();
		for(String element : this.locales.split(",")) {
			try {
				availableLocales.add(LocaleUtils.toLocale(element));
			}catch(Exception ignore) {
				LOGGER.warn(ignore.getMessage());
			}
		}
		return availableLocales;
	}
	
	/**
	 * Returns default locale
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private Locale getDefaultLocaleIfNotExist(Locale locale) throws Exception {
		if(locale == null) {
			try {
				locale = LocaleUtils.toLocale(locales.split(",")[0]);
			}catch(Exception e) {
				locale = Locale.getDefault();
			}
		}
		return locale;
	}
	
	/**
	 * Returns locale list
	 * @return
	 * @throws Exception
	 */
	public List<ValueMap> getLocales(Locale inLocale) throws Exception {
		inLocale = getDefaultLocaleIfNotExist(inLocale);
		List<ValueMap> locales = new ArrayList<ValueMap>();
		for(Locale locale : getAvailableLocales()) {
			ValueMap localeInfo = new ValueMap();
			localeInfo.set("locale", locale.toString());
			localeInfo.set("displayName", locale.getDisplayName(inLocale));
			localeInfo.set("displayNameByLocale", locale.getDisplayName(locale));
			localeInfo.set("country", locale.getCountry());
			localeInfo.set("displayCountry", locale.getDisplayCountry(inLocale));
			localeInfo.set("displayCountryByLocale", locale.getDisplayCountry(locale));
			localeInfo.set("language", locale.getLanguage());
			localeInfo.set("displayLanguage", locale.getDisplayLanguage(inLocale));
			localeInfo.set("displayLanguageByLocale", locale.getDisplayLanguage(locale));
			locales.add(localeInfo);
		}
		locales.sort(new Comparator<ValueMap>() {
			@Override
			public int compare(ValueMap o1, ValueMap o2) {
				return o1.getString("displayName").compareTo(o2.getString("displayName"));
			}
		});
		return locales;
	}
	
	/**
	 * Returns countries
	 * @return
	 * @throws Exception
	 */
	public List<ValueMap> getCountries(Locale inLocale) throws Exception {
		inLocale = getDefaultLocaleIfNotExist(inLocale);
		List<ValueMap> countries = new ArrayList<ValueMap>();
		Vector<String> keys = new Vector<String>();
		for(Locale locale : getAvailableLocales()) {
			String country = locale.getCountry();
			if(!keys.contains(country)) {
				keys.add(country);
				ValueMap countryInfo = new ValueMap();
				countryInfo.set("country", country);
				countryInfo.set("displayCountry", locale.getDisplayCountry(inLocale));
				countryInfo.set("displayCountryByLocale", locale.getDisplayCountry(locale));
				countries.add(countryInfo);
			}
		}
		countries.sort(new Comparator<ValueMap>() {
			@Override
			public int compare(ValueMap o1, ValueMap o2) {
				return o1.getString("displayCountry").compareTo(o2.getString("displayCountry"));
			}
		});
		return countries;
	}
	
	/**
	 * Returns languages
	 * @return
	 * @throws Exception
	 */
	public List<ValueMap> getLanguages(Locale inLocale) throws Exception {
		inLocale = getDefaultLocaleIfNotExist(inLocale);
		List<ValueMap> languages = new ArrayList<ValueMap>();
		Vector<String> keys = new Vector<String>();
		List<Locale> configuredLocales = this.getConfiguredLocales();
		for(Locale locale : getAvailableLocales()) {
			if(configuredLocales.contains(locale)) {
				String language = locale.getLanguage();
				if(!keys.contains(language)) {
					keys.add(language);
					ValueMap languageInfo = new ValueMap();
					languageInfo.set("language", language);
					languageInfo.set("displayLanguage", locale.getDisplayLanguage(inLocale));
					languageInfo.set("displayLanguageByLocale", locale.getDisplayName(locale).replaceAll("\\(.*\\)", "").trim());
					languages.add(languageInfo);
				}
			}
		}
		languages.sort(new Comparator<ValueMap>() {
			@Override
			public int compare(ValueMap o1, ValueMap o2) {
				return o1.getString("displayLanguage").compareTo(o2.getString("displayLanguage"));
			}
		});
		return languages;
	}
	
}
