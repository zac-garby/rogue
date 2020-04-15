package uk.co.zacgarby.roguelike;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Font {
	public static Font normal = new Font("fonts/font.png", "abcdefghijklmnopqrstuvwxyz0123456789", new Color(0xcbdbfcff));
	
	public int spaceWidth = 3;
	public boolean caseSensitive = false;
	
	private Map<Character, Texture> glyphs = new HashMap<>();
	
	// Loads a font from a file where each character is separated by a vertical line of invisible
	// pixels, and the characters are arranged horizontally according to the sequence in 'layout'.
	public Font(String filename, String layout, Color bg) {
		Pixmap p = new Pixmap(Gdx.files.internal(filename));
		
		int start = 0;
		for (char c : layout.toCharArray()) {
			int end = start + 1;
			
			boolean empty;
			
			do {
				empty = true;
				
				for (int y = 0; y < p.getHeight(); y++) {
					if ((p.getPixel(end, y) & 0xFF) > 0) { // If the pixel is visible
						empty = false;
					}
				}
				
				end++;
			} while (!empty && end < p.getWidth());
			
			end -= 2; // decrement twice for the excess end++ and also that it includes an empty line
			
			int width = (end - start) + 1;
			
			Pixmap cp = new Pixmap(width, p.getHeight(), p.getFormat());
			
			cp.setColor(bg);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < cp.getHeight(); y++) {
					if ((p.getPixel(x + start, y) & 0xFF) > 0) {
						cp.drawPixel(x, y);
					}
				}
			}
			
			glyphs.put(c, new Texture(cp));
			
			start = end + 2;
		}
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
			}
			
			batch.draw(t, x, y);
			x += t.getWidth() + 1;
		}
	}
}
