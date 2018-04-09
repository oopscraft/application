/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.core.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.SequenceInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * @author chomookun@gmail.com
 *
 */
public class Captcha implements Serializable {
	
	private static final long serialVersionUID = -193088373947619053L;

	String answer;
	
	public Captcha(String answer) {
		this.answer = answer;
	}
	
	public String getAnswer() {
		return this.answer;
	}
	
	public byte[] getImage() throws Exception {
		
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        Font font = new Font(graphics2D.getFont().getFontName(), Font.ITALIC, 48);
        graphics2D.setFont(font);	
        FontMetrics fm = graphics2D.getFontMetrics();
        int width = fm.stringWidth(this.answer);
        int height = fm.getHeight();
        graphics2D.dispose();

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics2D = image.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        graphics2D.setFont(font);
        fm = graphics2D.getFontMetrics();
        graphics2D.setColor(Color.GRAY);
        	    
        graphics2D.drawString(this.answer, 0, fm.getAscent());
        graphics2D.dispose();

        ByteArrayOutputStream baos = null;
        byte[] bytes = null;
        try {
        	baos = new ByteArrayOutputStream();
        	ImageIO.write(image, "png", baos);
        	bytes = baos.toByteArray();
        }catch(Exception e) {
        	throw e;
        }finally {
        	baos.close();	
        }
        return bytes;
	}
	
	/**
	 * getAudio
	 * @return
	 * @throws Exception
	 */
	public byte[] getAudio() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		List<AudioInputStream> audioInputStreamList = new ArrayList<AudioInputStream>();
		char[] charArray = this.answer.toLowerCase().toCharArray();
		AudioFormat audioFormat = null;
		int frameLength = 0;
		for(int i = 0; i < charArray.length; i ++) {
			char c = charArray[i];
			String soundFileName = "sound" + File.separator + c + ".wav";
			AudioInputStream ais = AudioSystem.getAudioInputStream(this.getClass().getResource(soundFileName));
			audioInputStreamList.add(ais);
			if(audioFormat == null) {
				audioFormat = ais.getFormat();
			}
			frameLength += ais.getFrameLength();
		}
		AudioInputStream ais = new AudioInputStream(new SequenceInputStream(Collections.enumeration(audioInputStreamList)), audioFormat, frameLength);
		AudioSystem.write(ais, javax.sound.sampled.AudioFileFormat.Type.WAVE,baos);
		return baos.toByteArray();
	}
}
