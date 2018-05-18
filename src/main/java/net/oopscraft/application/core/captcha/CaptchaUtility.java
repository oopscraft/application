/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.core.captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chomookun@gmail.com
 *
 */
public class CaptchaUtility {
	
	public static void prepareCaptcha(String answer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Captcha captcha = new CaptchaBuilder(answer).build();
		request.getSession(true).setAttribute("captcha", captcha);
	}
	
	public static void writeCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Captcha captcha = (Captcha) request.getSession(true).getAttribute("captcha");
		byte[] bytes = captcha.getImage();
		response.setHeader("Cache-Control", "private,no-cache,no-store");
		response.setContentType("image/png");
		response.setContentLength(bytes.length);
		response.getOutputStream().write(bytes);
	}
	
	public static void writeCaptchaAudio(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Captcha captcha = (Captcha) request.getSession(true).getAttribute("captcha");
		byte[] bytes = captcha.getAudio();
		response.setHeader("Cache-Control", "private,no-cache,no-store");
		response.setContentType("audio/wav");
		response.setContentLength(bytes.length);
		response.getOutputStream().write(bytes);
	}
	
	public static void checkCaptcha(String answer, HttpServletRequest request, HttpServletResponse response) throws CaptchaException {
		Captcha captcha = (Captcha) request.getSession(true).getAttribute("captcha");
		if(!answer.equals(captcha.getAnswer())){
			throw new CaptchaException("Invalid Captcha Answer.");
		}
	}

}
