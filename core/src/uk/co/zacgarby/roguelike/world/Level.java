package uk.co.zacgarby.roguelike.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uk.co.zacgarby.roguelike.world.tiles.Ground;
import uk.co.zacgarby.roguelike.world.tiles.Wall;

public class Level {
	public Tile[][] tiles;
	public int width, height;
	
	public Level(int w, int h) {
		width = w;
		height = h;
		
		tiles = new Tile[h][w];
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (Math.random() < 0.2) {
					tiles[j][i] = new Wall(i, j);
				} else {
					tiles[j][i] = new Ground(i, j);
				}
			}
		}
	}
	
	public void draw(SpriteBatch batch, int x, int y) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tiles[j][i].draw(batch, x + i * 10, y + j * 10);
			}
		}
	}
}
