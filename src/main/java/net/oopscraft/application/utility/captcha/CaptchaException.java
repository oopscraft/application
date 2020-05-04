/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.utility.captcha;

/**
 * @author chomookun@gmail.com
 *
 */
public class CaptchaException extends Exception {

	private static final long serialVersionUID = 7180103316465755233L;
	
	/**
	 * @param string
	 */
	public CaptchaException(String string) {
		super(string);
	}

}
