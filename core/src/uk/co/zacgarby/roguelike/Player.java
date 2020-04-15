package uk.co.zacgarby.roguelike;

public class Player extends Entity {
	public String name;
	
	private int xp;
	private int money;
	private int mana;
	
	public Player(String name) {
		this.name = name;
		health = 0;
		mana = 0;
		x = 0;
		y = 0;
		xp = 0;
		money = 0;
		stats = new Stats();
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
	
	public int getSilver() {
		return money % 100;
	}
	
	public int getGold() {
		return (money - getPlatinum()*10000) / 100;
	}
	
	public int getPlatinum() {
		return money / 10000;
	}
	
	public void addMoney(int amount) {
		money += amount;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}
}
