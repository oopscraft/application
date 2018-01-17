/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * @author chomookun@gmail.com
 *
 */
public class Captcha {
	
	public enum Type { CHAR, NUMERIC, CHAR_NUMERIC }
	
	Type type;
	int size;
	String answer;
	
	public Captcha(Type type, int size) throws Exception {
		this.type = type;
		this.size = size;
		
		// create random answer
		switch(type) {
		case CHAR :
			answer = generateAnswerChar(size);
		break;
		case NUMERIC :
			answer = generateAnswerNumeric(size);
		break;
		case CHAR_NUMERIC :
			answer = generateAnswerCharNumeric(size);
		break;
		}
	}
	
	private String generateAnswerChar(int size) throws Exception {
		StringBuffer buffer = new StringBuffer();
		for(int i = 1; i <= size; i ++ ) {
			char c = (char) ((Math.random() * 26) + 65);
			buffer.append(c);
		}
		return buffer.toString();
	}
	
	private String generateAnswerNumeric(int size) throws Exception {
		StringBuffer buffer = new StringBuffer();
		for(int i = 1; i <= size; i ++ ) {
			int n = (int) (Math.random() * 10);
			buffer.append(n);
		}
		return buffer.toString();
	}
	
	private String generateAnswerCharNumeric(int size) throws Exception {
		StringBuffer buffer = new StringBuffer();
		for(int i = 1; i <= size; i ++ ) {
			if(i%2 == 1) {
				char c = (char) ((Math.random() * 26) + 65);
				buffer.append(c);
			}else {
				int n = (int) (Math.random() * 10);
				buffer.append(n);
			}
		}
		return buffer.toString();
	}


	public String getAnswer() throws Exception {
		return this.answer;
	}
	
	public BufferedImage getImage() throws Exception {
		
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = img.createGraphics();
        Font font = new Font(graphics2D.getFont().getFontName(), Font.ITALIC, 48);
        graphics2D.setFont(font);	
        FontMetrics fm = graphics2D.getFontMetrics();
        int width = fm.stringWidth(this.answer);
        int height = fm.getHeight();
        graphics2D.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics2D = img.createGraphics();
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
        
        return img;
	}
}
