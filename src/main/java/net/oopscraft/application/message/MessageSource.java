package net.oopscraft.application.message;

import java.text.MessageFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.lang.Nullable;

public class MessageSource extends ReloadableResourceBundleMessageSource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageSource.class);
	
	@Autowired
	MessageService messageService;
	
	/**
	 * Resolves the given message code as key in the retrieved bundle files,
	 * returning the value found in the bundle as-is (without MessageFormat parsing).
	 */
	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		String result = super.resolveCodeWithoutArguments(code, locale);
		if(result == null) {
			try {
				MessageLanguage messageLanguage = messageService.getMessageLanguage(code,  locale.getLanguage());
				if(messageLanguage != null) {
					result = messageLanguage.getValue();
				}
			}catch(Exception ignore) {
				LOGGER.warn(ignore.getMessage());	
			}
		}
		return result;
	}

	/**
	 * Resolves the given message code as key in the retrieved bundle files,
	 * using a cached MessageFormat instance per message code.
	 */
	@Override
	@Nullable
	protected MessageFormat resolveCode(String code, Locale locale) {
		MessageFormat messageFormat = super.resolveCode(code, locale);
		if(messageFormat == null) {
			try {
				MessageLanguage messageLanguage = messageService.getMessageLanguage(code,  locale.getLanguage());
				if(messageLanguage != null) {
					messageFormat = new MessageFormat(messageLanguage.getValue(), locale);
				}
			}catch(Exception ignore) {
				LOGGER.warn(ignore.getMessage());	
			}
		}
		return messageFormat;
	}

}
