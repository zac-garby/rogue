package uk.co.zacgarby.roguelike.world.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uk.co.zacgarby.roguelike.world.Tile;

public class Ground extends Tile {
	private static Texture normal = new Texture(Gdx.files.internal("images/tiles/ground.png"));
	private static Texture grassy = new Texture(Gdx.files.internal("images/tiles/grassy-ground.png"));
	private boolean isGrassy;
	
	public Ground(int x, int y) {
		this.x = x;
		this.y = y;
		isGrassy = Math.random() < 0.05;
		passable = true;
	}
	
	@Override
	public void draw(SpriteBatch batch, int x, int y) {
		if (isGrassy) {
			batch.draw(grassy, x, y);
		} else {
			batch.draw(normal, x, y);
		}
	}
}
