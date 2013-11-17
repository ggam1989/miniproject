package net.project.mini.game.object;

public class Explosion extends AbstractObjectPosition{
	private int damage = 0; // 이미지 종류를 구분하기 위한 변수값
	private int ex_cnt;

	public Explosion(){}
	public Explosion(int x, int y, int damage) {
		this.x = x;
		this.y = y;
		this.damage = damage;

	}
	
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public int getEx_cnt() {
		return ex_cnt;
	}
	public void setEx_cnt(int ex_cnt) {
		this.ex_cnt = ex_cnt;
	}
	
	public void effect() {
		ex_cnt++;
	}
}
