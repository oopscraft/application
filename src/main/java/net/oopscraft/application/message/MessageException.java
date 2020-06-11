package net.oopscraft.application.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.oopscraft.application.ApplicationContext;
import net.oopscraft.application.ApplicationWebContext;

public class MessageException extends Exception {

	private static final long serialVersionUID = 668103176640687919L;
	
	String message;
	
	/**
	 * constructor
	 * @param id
	 * @param arguments
	 * @param request
	 */
	public MessageException(String id, String[] arguments, HttpServletRequest request) {
		Locale locale = ApplicationWebContext.localeResolver.resolveLocale(request);
		List<String> messageArguments = new ArrayList<String>();
		if(arguments != null) {
			for(String argument : arguments) {
				messageArguments.add(ApplicationContext.messageSource.getMessage(argument, null, locale));
			}
		}
		message = ApplicationContext.messageSource.getMessage(id, messageArguments.toArray(new String[messageArguments.size()]), locale);
	}

	@Override
    public String getMessage() {
		return message;
    }

	
}
