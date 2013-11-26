package net.project.mini.game.object;

public class Item  extends AbstractObjectPosition{
	public Item() {}
	
	public Item(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public void move() { // 
		// ix = (int)(Math.random() * 20 - 5);
		y += 5; 
	}
}
