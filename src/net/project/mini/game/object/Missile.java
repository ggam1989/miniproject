package net.project.mini.game.object;

// ------------------------------------------------------------------미사일 클래스
public class Missile extends AbstractObjectPosition{
	private int angle;
	private int speed; // 미사을 스피드 변수(매개받기)
	private int who;// player,boss 미사일 구분

	public Missile(){}
	
	public Missile(int x, int y, int angle, int speed, int who) {

		this.x = x;
		this.y = y;
		this.angle = angle;
		this.speed = speed; // 객체생성시 속도값을 추가로 받는다.
		this.who = who;
	}

	public int getAngle() {
		return angle;
	}
	public void setAngle(int angle) {
		this.angle = angle;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getWho() {
		return who;
	}
	public void setWho(int who) {
		this.who = who;
	}
	
	public void move() {
		// my -= speed;
		if (this.who == 0) {
			this.x += Math.cos(Math.toRadians(angle)) * speed;
			// 해당 방향으로 미사일 발사.
			this.y += Math.sin(Math.toRadians(angle)) * speed;
			// 해당 방향으로 미사일 발사.
		}
		if (this.who == 1) {
			this.x -= Math.cos(Math.toRadians(angle)) * speed;
			// 해당 방향으로 미사일 발사.
			this.y -= Math.sin(Math.toRadians(angle)) * speed;
			// 해당 방향으로 미사일 발사.
		}
		if (this.who == 2) {
			this.x -= Math.cos(Math.toRadians(angle)) * speed;
			// 해당 방향으로 미사일 발사.
			this.y -= Math.sin(Math.toRadians(angle)) * speed;
			// 해당 방향으로 미사일 발사.
		}
	}
}
