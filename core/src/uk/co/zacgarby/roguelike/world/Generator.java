package uk.co.zacgarby.roguelike.world;

import java.util.ArrayList;
import java.util.List;

import uk.co.zacgarby.roguelike.util.PerlinNoise;
import uk.co.zacgarby.roguelike.world.tiles.Ground;

public class Generator {
	private PerlinNoise noise;
	private int width, height;
	private List<Room> rooms = new ArrayList<>();
	private List<Path> paths = new ArrayList<>();
	
	public double persistence = 1.0;
	public double frequency = 2.0;
	public int octaves = 4;
	public int seed;
	
	public int baseRoomRadius = 8;
	public int roomRadiusVariation = 5;
	public int roomRadiusNoise = 3;
	public double roomRadiusNoiseCoefficient = 0.25;
	
	public double roomDensity = 0.0006;
	
	public Generator(int seed, int width, int height) {
		this.width = width;
		this.height = height;
		this.seed = seed;
	}
	
	public Level generate() {
		noise = new PerlinNoise(seed, persistence, frequency, 1.0, octaves);
		
		rooms.add(new Room(width / 2, height / 2, roomRadius(width/2, height/2)));
		
		int numRooms = (int) (roomDensity * width * height);
		System.out.printf("making %d rooms.\n", numRooms);

		int maxRad = baseRoomRadius + roomRadiusVariation + roomRadiusNoise;
		
		for (int i = 0; i < numRooms; i++) {
			int rx = (int) (width * (1 + n(3114 - 50341 * i, 5310 + 1004 * i)) / 2);
			int ry = (int) (height * (1 + n(-3114 - 5534 * i, -5310 + 2530 * i)) / 2);
			if (rx < maxRad || ry < maxRad || width - rx < maxRad || height - ry < maxRad) {
				numRooms++;
				continue;
			}
			
			rooms.add(new Room(rx, ry, roomRadius(rx, ry)));
		}
		
		Level level = render();
		level.initialise();
		return level;
	}
	
	private Level render() {
		Level l = new Level(width, height);
		
		for (Room r : rooms) {
			System.out.printf("room at %d, %d. radius: %d\n", r.x, r.y, r.radius);
			r.render(l);
		}
		
		return l;
	}
	
	private double n(int x, int y) {
		double no = noise.getHeight(x, y);
		while (no > 1) { no -= 2; }
		while (no < -1) { no += 2; }
		return no;
	}
	
	private int roomRadius(int x, int y) {
		return baseRoomRadius + (int) (roomRadiusVariation * n(x, y));
	}
	
	private class Room {
		int x, y, radius;
		
		public Room(int x, int y, int radius) {
			this.x = x;
			this.y = y;
			this.radius = radius;
		}
		
		public void render(Level l) {
			for (int x = -radius - roomRadiusNoise; x < radius + roomRadiusNoise; x++) {
				for (int y = -radius - roomRadiusNoise; y < radius + roomRadiusNoise; y++) {
					double angle = Math.atan2(y, x);
					double rad = radius + roomRadiusNoise * n(this.x + (int) (angle * roomRadiusNoiseCoefficient * (double) radius), this.y);
					if (x*x + y*y < rad*rad) {
						l.tiles[this.y + y][this.x + x] = new Ground(this.x + x, this.y + y);
					}
				}
			}
		}
	}
	
	private class Path {
		int x1, y1, x2, y2, width;
		
		public Path(int x1, int y1, int x2, int y2, int width) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.width = width;
		}
	}
}
