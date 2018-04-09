/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.core.captcha;

/**
 * @author chomookun@gmail.com
 *
 */
public class CaptchaBuilder {
	
	String answer;
	
	public CaptchaBuilder(String answer){
		this.answer = answer;
	}

	public Captcha build() throws Exception {
		Captcha captcha = new Captcha(this.answer);
		return captcha;
	}

}
