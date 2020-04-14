package uk.co.zacgarby.roguelike;

public abstract class Entity {
	public Stats stats;
	
	protected int health;
	protected int x, y;
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int hp) {
		health = hp;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean harm(int hp) {
		if (hp > getHealth()) {
			return false;
		}
		
		setHealth(getHealth() - hp);
		return true;
	}
	
	public void setPos(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public void move(int dx, int dy) {
		setX(getX() + dx);
		setY(getY() + dy);
	}
}
