package net.project.mini.game.object;

public class Boss extends AbstractObjectPosition{
	private int life = 20;
	
	public Boss(){}
	public Boss(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}


	public void move() {
		y += 0;
	}
	
	public void decreaseLife() {
		this.life -= 1;
	}
}
