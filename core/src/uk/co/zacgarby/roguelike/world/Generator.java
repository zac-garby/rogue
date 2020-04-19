package uk.co.zacgarby.roguelike.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	public int baseRoomRadius = 7;
	public int roomRadiusVariation = 3;
	public int roomRadiusNoise = 1;
	public double roomRadiusNoiseCoefficient = 0.15;
	public double roomDensity = 0.0009;
	
	public double pathWidthCoefficient = 0.4;
	public int pathWidthVariation = 2;
	public int pathWidthNoise = 1;
	public int minPathWidth = 3;
	public double randomPathChance = 0.1;
	public int randomPathMaxDist = 40;
	
	public Generator(int seed, int width, int height) {
		this.width = width;
		this.height = height;
		this.seed = seed;
	}
	
	public Level generate() {
		noise = new PerlinNoise(seed, persistence, frequency, 1.0, octaves);
		
		makeRooms();
		makePaths();
		
		Level level = render();
		level.initialise();
		return level;
	}
	
	private void makeRooms() {
		rooms.add(new Room(width / 2, height / 2, roomRadius(width/2, height/2)));
		
		int numRooms = (int) (roomDensity * width * height);
		System.out.printf("making %d rooms.\n", numRooms);

		int maxRad = baseRoomRadius + roomRadiusVariation + roomRadiusNoise;
		
		for (int i = 0; i < numRooms; i++) {
			int rx = (int) (width * (1 + n(3114 - 50341 * i, 5310 + 1004 * i)) / 2);
			int ry = (int) (height * (1 + n(-3114 - 5534 * i, -5310 + 2530 * i)) / 2);
			if (rx < maxRad || ry < maxRad || width - rx < maxRad || height - ry < maxRad || distSqToAnyRoom(rx, ry) < (maxRad + 3)*(maxRad + 3)) {
				numRooms++;
				continue;
			}
			
			rooms.add(new Room(rx, ry, roomRadius(rx, ry)));
		}
	}
	
	private int distSqToAnyRoom(int x, int y) {
		int d = Integer.MAX_VALUE;
		
		for (Room r : rooms) {
			int dx = r.x - x;
			int dy = r.y - y;
			int dsq = dx*dx + dy*dy;
			
			if (0 < dsq && dsq < d) {
				d = dsq;
			}
		}
		
		return d;
	}
	
	private void makePaths() {
		int n = rooms.size();
		Set<Integer> inc = new HashSet<>();
		int[] keys = new int[n];
		int[] parents = new int[n];
		
		for (int i = 0; i < n; i++) {
			keys[i] = Integer.MAX_VALUE;
			parents[i] = -1;
		}
		keys[0] = 0;
		
		// Find MST
		while (inc.size() < n) {
			int u = -1, minKey = Integer.MAX_VALUE;
			
			for (int i = 0; i < n; i++) {
				int k = keys[i];
				if (k < minKey && !inc.contains(i)) {
					u = i;
					minKey = k;
				}
			}
			
			inc.add(u);
			Room ru = rooms.get(u);
			
			for (int v = 0; v < n; v++) {
				if (u == v) continue;
				
				Room rv = rooms.get(v);
				int dx = rv.x - ru.x;
				int dy = rv.y - ru.y;
				int weight = dx*dx + dy*dy;
				
				if (weight < keys[v] && !inc.contains(v)) {
					keys[v] = weight;
					parents[v] = u;
				}
			}
		}
		
		// Make paths for MST
		for (int v = 0; v < n; v++) {
			int u = parents[v];
			if (u == -1) {
				continue;
			}
			
			Room ru = rooms.get(u);
			Room rv = rooms.get(v);
			
			double rand = n(ru.x + rv.x, ru.y + rv.y);
			int minRad = Math.min(ru.radius, rv.radius);
			int width = Math.max(minPathWidth, (int) (minRad * pathWidthCoefficient + rand * pathWidthVariation));
			
			Path p = new Path(ru.x, ru.y, rv.x, rv.y, width);
			paths.add(p);
		}
		
		// Make random paths
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) continue;
				
				Room ri = rooms.get(i);
				Room rj = rooms.get(j);
				int dx = rj.x - ri.x;
				int dy = rj.y - ri.y;
				int distSq = dx*dx + dy*dy;
				
				if (distSq < randomPathMaxDist*randomPathMaxDist && Math.abs(n(ri.x - rj.x, ri.y - rj.y)) < randomPathChance) {
					double rand = n(ri.x + rj.x, ri.y + rj.y);
					int minRad = Math.min(ri.radius, rj.radius);
					int width = Math.max(minPathWidth, (int) (minRad * pathWidthCoefficient + rand * pathWidthVariation));
					
					Path p = new Path(ri.x, ri.y, rj.x, rj.y, width);
					paths.add(p);
				}
			}
		}
	}
	
	private Level render() {
		Level l = new Level(width, height);
		
		for (Room r : rooms) {
			r.render(l);
		}
		
		for (Path p : paths) {
			p.render(l);
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
						l.set(this.x + x, this.y + y, new Ground(this.x + x, this.y + y));
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
		
		public void render(Level l) {
			double dx = x2 - x1;
			double dy = y2 - y1;
			double len = Math.sqrt(dx*dx + dy*dy);
			dx /= len;
			dy /= len;
						
			double x = (double) x1, y = (double) y1;
			double dist = 0;
			
			while (dist < len) {
				x += dx;
				y += dy;
				dist += 1;
				
				int wid = this.width + (int) (n((int) x, (int) y) * pathWidthNoise);
				
				for (int i = -wid; i < wid; i++) {
					for (int j = -wid; j < wid; j++) {
						if (i*i + j*j < wid*wid) {
							l.set((int) x + i, (int) y + j, new Ground((int) x + i, (int) y + j));
						}
					}
				}
			}
		}
	}
}
