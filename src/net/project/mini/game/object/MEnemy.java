package net.project.mini.game.object;

public class MEnemy extends AbstractObjectPosition {
	private int life = 3;
	
	public MEnemy(int x, int y) {
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
		y -= (int) (Math.random() * 5 - 1);
	}

	public void decreaseLife() {
		this.life -= 1;
	}
}
