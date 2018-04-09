/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.core.captcha;

import java.io.ByteArrayInputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.junit.Test;

import net.oopscraft.application.core.captcha.Captcha;

/**
 * @author chomookun@gmail.com
 *
 */
public class CaptchaTest {

//	@Test
//	public void testChar() {
//		try {
//			Captcha captcha = new Captcha(Type.CHAR, 4);
//			System.out.println("+ answer:" + captcha.getAnswer());
//		}catch(Exception e) {
//			e.printStackTrace();
//			assert(false);
//		}
//		assert(true);
//	}
//
//	@Test
//	public void testNumeric() {
//		try {
//			Captcha captcha = new Captcha(Type.NUMERIC, 4);
//			System.out.println("+ answer:" + captcha.getAnswer());
//		}catch(Exception e) {
//			e.printStackTrace();
//			assert(false);
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testCharNumeric() {
//		try {
//			Captcha captcha = new Captcha(Type.CHAR_NUMERIC, 4);
//			System.out.println("+ answer:" + captcha.getAnswer());
//		}catch(Exception e) {
//			e.printStackTrace();
//			assert(false);
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testGetImage() {
//		try {
//			Captcha captcha = new Captcha(Type.CHAR_NUMERIC, 4);
//			System.out.println("+ image:" + captcha.getImage());
//		}catch(Exception e) {
//			e.printStackTrace();
//			assert(false);
//		}
//		assert(true);
//	}
	
	@Test
	public void testGetAudio() {
		try {
			Captcha captcha = new Captcha("abcd1234567890");
			byte[] bytes = captcha.getAudio();
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(bais));
			clip.start();
			Thread.sleep(1000*30);
		}catch(Exception e) {
			e.printStackTrace();
			assert(false);
		}
		assert(true);
	}
}
