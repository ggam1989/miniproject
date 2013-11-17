package net.project.mini.game.object;

public class Item {
	private int x;
	private int y;
	
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

	public void move() { // x좌표 -3 만큼 이동 시키는 명령 메소드
		// ix = (int)(Math.random() * 20 - 5);
		y += 5; // x면 가로로 움직이고 y면 세로로 움직임
	}
}
