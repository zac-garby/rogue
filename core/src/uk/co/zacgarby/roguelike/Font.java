package uk.co.zacgarby.roguelike;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Font {
	public static Font normal = new Font("fonts/font.png", "abcdefghijklmnopqrstuvwxyz0123456789!.,:;()[]-+*÷", new Color(0xcbdbfcff));
	public static Font gold = new Font("fonts/font.png", "abcdefghijklmnopqrstuvwxyz0123456789!.,:;()[]-+*÷", new Color(0xeae352ff), new Color(0xdfae26ff));
	public static Font name = new Font("fonts/name-font.png", "abcdefghijklmnopqrstuvwxyzàáâãäåçèéêëìíîïñòóôõöøùúûüýÿ", new Color(0xcbdbfcff), new Color(0xffffffff));
	
	public int spaceWidth = 3;
	public boolean caseSensitive = false;
	
	private Map<Character, Texture> glyphs = new HashMap<>();
	
	// Loads a font from a file where each character is separated by a vertical line of invisible
	// pixels, and the characters are arranged horizontally according to the sequence in 'layout'.
	public Font(String filename, String layout, Color c1, Color c2) {
		Pixmap p = new Pixmap(Gdx.files.internal(filename));
		
		int start = 0;
		for (char c : layout.toCharArray()) {
			int end = start + 1;
			
			boolean empty;
			
			do {
				empty = true;
				
				for (int y = 0; y < p.getHeight(); y++) {
					int pixel = p.getPixel(end, y);
					if (pixel == Color.rgba8888(Color.WHITE) || pixel == Color.rgba8888(Color.BLACK)) { // If the pixel is visible
						empty = false;
						break;
					}
				}
				
				end++;
			} while (!empty && end < p.getWidth());
			
			end -= 2; // decrement twice for the excess end++ and also that it includes an empty line
			
			int width = (end - start) + 1;
			
			Pixmap cp = new Pixmap(width, p.getHeight(), p.getFormat());
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < cp.getHeight(); y++) {
					int pixel = p.getPixel(x + start, y);
					if ((pixel & 0xFF) == 0) {
						continue;
					}
					
					if (pixel == Color.rgba8888(Color.WHITE)) {
						cp.setColor(c1);
					} else if (pixel == Color.rgba8888(Color.BLACK)) {
						cp.setColor(c2);
					}
					
					cp.drawPixel(x, y);
				}
			}
			
			glyphs.put(c, new Texture(cp));
			
			start = end + 2;
		}
	}
	
	public Font(String filename, String layout, Color c) {
		this(filename, layout, c, c);
	}
	
	public Texture get(char c) {
		return glyphs.get(c);
	}
	
	public void draw(String text, SpriteBatch batch, int x, int y) {		
		if (!caseSensitive) {
			text = text.toLowerCase();
		}
		
		for (char c : text.toCharArray()) {
			if (c == ' ') {
				x += spaceWidth;
				continue;
			}
			
			Texture t = get(c);
			
			if (t == null) {
				System.out.println("error: unknown character " + String.valueOf(c));
				continue;
			}
			
			batch.draw(t, x, y);
			x += t.getWidth() + 1;
		}
	}
	
	public void drawCentered(String text, SpriteBatch batch, int x, int y) {
		if (!caseSensitive) {
			text = text.toLowerCase();
		}
		
		int width = 0;
		
		for (char c : text.toCharArray()) {
			if (c == ' ') {
				width += spaceWidth;
				continue;
			}
			
			Texture t = get(c);
			
			if (t == null) {
				System.out.println("error: unknown character " + String.valueOf(c));
				continue;
			}
			
			width += t.getWidth();
		}
		
		draw(text, batch, x - width/2 - 1, y);
	}
}
