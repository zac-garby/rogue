package uk.co.zacgarby.roguelike;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public final class Sidebar {
	static Texture background = new Texture(Gdx.files.local("images/sidebar.png"));
	
	static void dispose() {
		background.dispose();
	}
	
	static void draw(SpriteBatch batch) {
		batch.draw(Sidebar.background, 170, 0);
		
		Font.gold.draw(String.valueOf(Main.player.getXP()), batch, 197, 128);
		Font.gold.draw(String.valueOf(Main.player.getLevel()), batch, 233, 128);
		
		Font.normal.draw(String.valueOf(Main.player.stats.getPhysique()), batch, 193, 117);
		Font.normal.draw(String.valueOf(Main.player.stats.getAgility()), batch, 216, 117);
		Font.normal.draw(String.valueOf(Main.player.stats.getDexterity()), batch, 239, 117);
		Font.normal.draw(String.valueOf(Main.player.stats.getIntelligence()), batch, 193, 106);
		Font.normal.draw(String.valueOf(Main.player.stats.getMemory()), batch, 216, 106);
		Font.normal.draw(String.valueOf(Main.player.stats.getPerception()), batch, 239, 106);
		Font.normal.draw(String.valueOf(Main.player.stats.getLuck()), batch, 193, 95);
		Font.normal.draw(String.valueOf(Main.player.stats.getCuriosity()), batch, 216, 95);
		Font.normal.draw(String.valueOf(Main.player.stats.getCharisma()), batch, 239, 95);
		
		Font.normal.draw(String.valueOf(Main.player.getSilver()), batch, 191, 80);
		Font.normal.draw(String.valueOf(Main.player.getGold()), batch, 214, 80);
		Font.normal.draw(String.valueOf(Main.player.getPlatinum()), batch, 237, 80);
	}
}
