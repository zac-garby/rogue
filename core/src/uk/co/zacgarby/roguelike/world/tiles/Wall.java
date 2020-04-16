package uk.co.zacgarby.roguelike.world.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uk.co.zacgarby.roguelike.world.Tile;

public class Wall extends Tile {
	private static Texture template = new Texture(Gdx.files.internal("images/tiles/wall.png"));
	private Texture tex;
	
	public Wall(int x, int y) {
		this.x = x;
		this.y = y;
		this.passable = false;
		
		generateTexture();
	}
	
	@Override
	public void draw(SpriteBatch batch, int x, int y) {
		batch.draw(tex, x, y);
	}
	
	private void generateTexture() {
		tex = new Texture(template.getTextureData());
		
	}
}
