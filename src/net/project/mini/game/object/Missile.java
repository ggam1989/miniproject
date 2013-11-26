package net.project.mini.game.object;

// ------------------------------------------------------------------
public class Missile extends AbstractObjectPosition{
	private int angle;
	private int speed; 
	private int who;

	public Missile(){}
	
	public Missile(int x, int y, int angle, int speed, int who) {

		this.x = x;
		this.y = y;
		this.angle = angle;
		this.speed = speed; 
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
			
			this.y += Math.sin(Math.toRadians(angle)) * speed;
			
		}
		if (this.who == 1) {
			this.x -= Math.cos(Math.toRadians(angle)) * speed;
		
			this.y -= Math.sin(Math.toRadians(angle)) * speed;
		
		}
		if (this.who == 2) {
			this.x -= Math.cos(Math.toRadians(angle)) * speed;
		
			this.y -= Math.sin(Math.toRadians(angle)) * speed;
		
		}
	}
}
