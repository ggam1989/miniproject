package net.project.mini.game.object;

public class Enemy extends AbstractObjectPosition {

	public Enemy(){}
	
	public Enemy(int x, int y) { // 적좌표를 받아 객체화 시키기 위한 메소드
		this.x = x;
		this.y = y;
	}

	public void move() { // x좌표 -3 만큼 이동 시키는 명령 메소드
		y += (int) (Math.random() * 15); // x면 가로로 움직이고 y면 세로로 움직임
	}
}
