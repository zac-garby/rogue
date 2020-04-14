package uk.co.zacgarby.roguelike;

public class Player extends Entity {
	public String name;
	private int xp;
	
	public Player(String name) {
		this.name = name;
		health = 0;
		x = 0;
		y = 0;
		xp = 0;
	}

	public int getXP() {
		return xp;
	}

	public void addXP(int xp) {
		this.xp += xp;
	}
	
	public int getLevel() {
		return Math.max((int) Math.floor(5 * Math.log10((double)xp / 10.0 + 1)), 1);
	}
}
