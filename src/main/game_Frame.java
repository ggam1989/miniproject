package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

class game_Frame extends JFrame implements KeyListener, Runnable {

	int f_width;
	int f_height;
	int x, y;
	int[] cx = { 0, 0, 0 }; // 배경 스크롤 속도 제어용 변수
	int bx = 0; // 전체 배경 스크롤 용 변수

	boolean KeyUp = false;
	boolean KeyDown = false;
	boolean KeyLeft = false;
	boolean KeyRight = false;
	boolean KeySpace = false;

	int cnt;
	int player_Speed; // 유저의 캐릭터가 움직이는 속도를 조절할 변수
	int missile_Speed; // 미사일이 날라가는 속도 조절할 변수
	int fire_Speed; // 미사일 연사 속도 조절 변수
	int enemy_speed; // 적 이동 속도 설정
	int player_Status = 0;
	// 유저 캐릭터 상태 체크 변수 0 : 평상시, 1: 미사일발사, 2: 충돌
	int game_Score = 0; // 게임 점수 계산
	double game_Length = 0; // 게임 거리 계산
	int player_Hitpoint; // 플레이어 캐릭터의 체력
	int boss;

	Thread th;

	Toolkit tk = Toolkit.getDefaultToolkit();

	// Image[] Player_img;
	// 플레이어 애니메이션 표현을 위해 이미지를 배열로 받음
	Image BackGround_img; // 배경화면 이미지
	Image[] Cloud_img; // 움직이는 배경용 이미지배열
	Image[] Explo_img; // 폭발이펙트용 이미지배열

	Image me_img;
	Image Missile_img;
	Image Enemy_img;
	Image boss_img;

	ArrayList Missile_List = new ArrayList();
	ArrayList Enemy_List = new ArrayList();
	ArrayList Explosion_List = new ArrayList();
	ArrayList Boss_List = new ArrayList();
	// 다수의 폭발 이펙트를 처리하기 위한 배열

	Image buffImage;
	Graphics buffg;

	Missile ms;
	Enemy en;
	Boss bs;

	Explosion ex; // 폭발 이펙트용 클래스 접근 키

	game_Frame(/*String str*/) {
		//me_img = tk.getImage(str);
		init();
		start();

		setTitle("");
		setSize(f_width, f_height);

		Dimension screen = tk.getScreenSize();

		int f_xpos = (int) (screen.getWidth() / 2 - f_width / 2);
		int f_ypos = (int) (screen.getHeight() / 2 - f_height / 2);

		setLocation(f_xpos, f_ypos);
		setResizable(false);
		setVisible(true);
	}

	public void init() {
		x = 220;
		y = 550;
		f_width = 500;
		f_height = 800;

		me_img = new ImageIcon("img/Player1.png").getImage();
		Missile_img = new ImageIcon("img/Missile1.png").getImage();
		Enemy_img = new ImageIcon("img/Enemy1.png").getImage();
		boss_img = new ImageIcon("img/boss.png").getImage();

		// Player_img = new Image[5];
		// for (int i = 0; i < Player_img.length; ++i) {
		// Player_img[i] = new ImageIcon("img/aaa.jpg").getImage();
		// }

		BackGround_img = new ImageIcon("img/cloud1.jpg").getImage();
		// 전체 배경화면 이미지를 받습니다.

		Cloud_img = new Image[3];
		for (int i = 0; i < Cloud_img.length; ++i) {
			Cloud_img[i] = new ImageIcon("cloud1.jpg").getImage();
		}
		// 구름을 3개 동시에 그리는데 편의상 배열로 3개를 동시에 받는다.

		Explo_img = new Image[3];
		for (int i = 0; i < Explo_img.length; ++i) {
			Explo_img[i] = new ImageIcon("img/fire1.jpg").getImage();
		}

		game_Score = 0;// 게임 스코어 초기화
		player_Hitpoint = 3;// 최초 플레이어 체력
		player_Speed = 5; // 유저 캐릭터 움직이는 속도 설정
		missile_Speed = 50; // 미사일 움직임 속도 설정
		fire_Speed = 10; // 미사일 연사 속도 설정
		enemy_speed = 5;// 적이 날라오는 속도 설정
		boss = 5;

	}

	public void start() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		th = new Thread(this);
		th.start();
	}

	public void run() {
		try {
			while (true) {
				bossProcess();
				KeyProcess();
				EnemyProcess();
				MissileProcess();
				ExplosionProcess();// 폭파처리 메소드 실행
				repaint();
				bossProcess();
				Thread.sleep(20);
				cnt++;
			}
		} catch (Exception e) {
		}
	}

	public void MissileProcess() {
		if (KeySpace) {
			player_Status = 1;
			if ((cnt % fire_Speed) == 0) {
				ms = new Missile(x + 20, y + 0,0, missile_Speed);
				Missile_List.add(ms);
		
			ms = new Missile(x + 20, y + 0, 330, missile_Speed);
			// 위쪽 대각선으로 날라갈 미사일입니다.
			Missile_List.add(ms);

			ms = new Missile(x + 20, y + 0, 30, missile_Speed);
			// 아래쪽 대각선으로 날라갈 미사일입니다.
			Missile_List.add(ms);
			}
		}

		for (int i = 0; i < Missile_List.size(); ++i) {
			ms = (Missile) Missile_List.get(i);
			ms.move();
			if (ms.y > f_height - 20) {
				Missile_List.remove(i);
			}

			for (int j = 0; j < Enemy_List.size(); ++j) {
				en = (Enemy) Enemy_List.get(j);
				// bs = (Boss) Boss_List.get(j);

				if (Crash(ms.x, ms.y, en.x, en.y, Missile_img, Enemy_img)) {
					Missile_List.remove(i);
					Enemy_List.remove(j);
					game_Score += 10;
					ex = new Explosion(en.x + Enemy_img.getWidth(null) / 2,
							en.y + Enemy_img.getHeight(null) / 2, 0);
					Explosion_List.add(ex);
				}
			}
		}
		for (int i = 0; i < Missile_List.size(); ++i) {
			ms = (Missile) Missile_List.get(i);
			ms.move();
			if (ms.y > f_height - 20) {
				Missile_List.remove(i);
			}

			for (int j = 0; j < Boss_List.size(); ++j) {
				// en = (Enemy) Enemy_List.get(j);
				bs = (Boss) Boss_List.get(j);

				if (Crash(ms.x, ms.y, bs.x, bs.y, Missile_img, boss_img)) {
					Missile_List.remove(i);
					Boss_List.remove(j);
					game_Score += 100; // 게임 점수를 +10점.
					ex = new Explosion(bs.x + boss_img.getWidth(null) / 2, bs.y
							+ Enemy_img.getHeight(null) / 2, 0);
					Explosion_List.add(ex);
				}
			}
		}
	}

	public void bossProcess() {
		for (int i = 0; i < Boss_List.size(); ++i) {
			bs = (Boss) (Boss_List.get(i));
			bs.move();

			if (bs.x < -200) {
				Boss_List.remove(i);
			}

			if (Crash(x, y, bs.x, bs.y, me_img, boss_img)) {

				player_Hitpoint--; // 플레이어 체력을 1깍습니다.
				Boss_List.remove(i); // 적을 제거합니다.
				game_Score += 10;

				ex = new Explosion(bs.x + boss_img.getWidth(null) / 2, bs.y
						+ boss_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				ex = new Explosion(x, y, 1);
				Explosion_List.add(ex);
			}
		}

		if (cnt % 500 == 0) {
			bs = new Boss(200, 0);
			Boss_List.add(bs);

		}

	}

	public void EnemyProcess() {

		for (int i = 0; i < Enemy_List.size(); ++i) {
			en = (Enemy) (Enemy_List.get(i));
			en.move();
			if (en.x < -200) {
				Enemy_List.remove(i);
			}

			if (Crash(x, y, en.x, en.y, me_img, Enemy_img)) {

				player_Hitpoint--; // 플레이어 체력을 1깍습니다.
				Enemy_List.remove(i); // 적을 제거합니다.
				game_Score += 10;

				ex = new Explosion(en.x + Enemy_img.getWidth(null) / 2, en.y
						+ Enemy_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				ex = new Explosion(x, y, 1);
				Explosion_List.add(ex);
			}
		}
		if (cnt % 200 == 0) {
			en = new Enemy(0, 0);
			Enemy_List.add(en);
			en = new Enemy(100, 0);
			Enemy_List.add(en);
			en = new Enemy(200, 0);
			Enemy_List.add(en);
			en = new Enemy(300, 0);
			Enemy_List.add(en);
			en = new Enemy(400, 0);
			Enemy_List.add(en);
		}

	}

	public void ExplosionProcess() {
		// 폭발 이펙트 처리용 메소드

		for (int i = 0; i < Explosion_List.size(); ++i) {
			ex = (Explosion) Explosion_List.get(i);
			ex.effect();
			// 이펙트 애니메이션을 나타내기위해
			// 이펙트 처리 추가가 발생하면 해당 메소드를 호출.

		}
	}

	public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2) {
		// 기존 충돌 판정 소스를 변경합니다.
		// 이제 이미지 변수를 바로 받아 해당 이미지의 넓이, 높이값을
		// 바로 계산합니다.

		boolean check = false;

		if (Math.abs((x1 + img1.getWidth(null) / 2)
				- (x2 + img2.getWidth(null) / 2)) < (img2.getWidth(null) / 2 + img1
				.getWidth(null) / 2)
				&& Math.abs((y1 + img1.getHeight(null) / 2)
						- (y2 + img2.getHeight(null) / 2)) < (img2
						.getHeight(null) / 2 + img1.getHeight(null) / 2)) {
			// 이미지 넓이, 높이값을 바로 받아 계산합니다.

			check = true;// 위 값이 true면 check에 true를 전달합니다.
		} else {
			check = false;
		}

		return check; // check의 값을 메소드에 리턴 시킵니다.

	}

	public void paint(Graphics g) {
		buffImage = createImage(f_width, f_height);
		buffg = buffImage.getGraphics();

		update(g);
	}

	public void update(Graphics g) {

		Draw_Background(); // 배경 이미지 그리기 메소드 실행
		Draw_Player(); // 플레이어를 그리는 메소드 이름 변경
		Draw_Enemy();
		Draw_Boss();
		Draw_Missile();
		Draw_Explosion();// 폭발이펙트그리기 메소드 실행
		Draw_StatusText();// 상태 표시 텍스트를 그리는 메소드 실행
		g.drawImage(buffImage, 0, 0, this);

		if (player_Hitpoint == 0) {
			new Score();
			th.stop();
			setVisible(false);
		}
	}

	public void Draw_Boss() {
		for (int i = 0; i < Boss_List.size(); ++i) {
			bs = (Boss) (Boss_List.get(i));
			buffg.drawImage(boss_img, bs.x, bs.y, this);
		}
	}

	public void Draw_Background() {
		// 배경 이미지를 그리는 부분입니다.

		buffg.clearRect(0, 0, f_width, f_height);
		// 화면 지우기 명령은 이제 여기서 실행합니다.

		if (bx > -1000) {
			// 기본 값이 0인 bx가 -3500 보다 크면 실행

			buffg.drawImage(BackGround_img, 0, bx, this);
			bx += 2;
			// bx를 0에서 -1만큼 계속 줄이므로 배경이미지의 x좌표는
			// 계속 좌측으로 이동한다. 그러므로 전체 배경은 천천히
			// 좌측으로 움직이게 된다.

		} else {
			bx = 0;
		}
		for (int i = 0; i < cx.length; ++i) {
			if (cx[i] < 800) {
				cx[i] += 5 + i * 3;
			} else {
				cx[i] = 0;
			}
			buffg.drawImage(Cloud_img[i], 1200 - cx[i], 50 + i * 200, this);
			// 3개의 구름 이미지를 각기 다른 속도 값으로 좌측으로 움직임.
		}
	}

	public void Draw_Player() {

		switch (player_Status) {

		case 0: // 평상시
			if ((cnt / 5 % 2) == 0) {
				buffg.drawImage(me_img, x, y, this);
			} else {
				buffg.drawImage(me_img, x, y, this);
			}
			// 엔진쪽에서 불을 뿜는 이미지를 번갈아 그려준다.

			break;

		case 1: // 미사일발사
			if ((cnt / 5 % 2) == 0) {
				buffg.drawImage(me_img, x, y, this);
			} else {

				buffg.drawImage(me_img, x, y, this);
			}
			player_Status = 0;
			break;
		case 2: // 충돌
			if (player_Status == 0) {

				break;
			}
			break;

		}

	}

	public void Draw_Missile() {
		for (int i = 0; i < Missile_List.size(); ++i) {
			ms = (Missile) (Missile_List.get(i));
			buffg.drawImage(Missile_img, ms.x, ms.y, this);
		}
	}

	public void Draw_Enemy() {
		for (int i = 0; i < Enemy_List.size(); ++i) {
			en = (Enemy) (Enemy_List.get(i));
			buffg.drawImage(Enemy_img, en.x, en.y, this);
		}
	}

	public void Draw_Explosion() {
		// 폭발 이펙트를 그리는 부분 입니다.

		for (int i = 0; i < Explosion_List.size(); ++i) {
			ex = (Explosion) Explosion_List.get(i);
			// 폭발 이펙트의 존재 유무를 체크하여 리스트를 받음.

			if (ex.damage == 0) {
				// 설정값이 0 이면 폭발용 이미지 그리기

				if (ex.ex_cnt < 7) {
					buffg.drawImage(Explo_img[0],
							ex.x - Explo_img[0].getWidth(null) / 2, ex.y
									- Explo_img[0].getHeight(null) / 2, this);
				} else if (ex.ex_cnt < 14) {
					buffg.drawImage(Explo_img[1],
							ex.x - Explo_img[1].getWidth(null) / 2, ex.y
									- Explo_img[1].getHeight(null) / 2, this);
				} else if (ex.ex_cnt < 21) {
					buffg.drawImage(Explo_img[2],
							ex.x - Explo_img[2].getWidth(null) / 2, ex.y
									- Explo_img[2].getHeight(null) / 2, this);
				} else if (ex.ex_cnt > 21) {
					Explosion_List.remove(i);
					ex.ex_cnt = 0;
				}
			} else { // 설정값이 1이면 단순 피격용 이미지 그리기
				if (ex.ex_cnt < 7) {
					buffg.drawImage(Explo_img[0], ex.x + 120, ex.y + 15, this);
				} else if (ex.ex_cnt < 14) {
					buffg.drawImage(Explo_img[1], ex.x + 60, ex.y + 5, this);
				} else if (ex.ex_cnt < 21) {
					buffg.drawImage(Explo_img[0], ex.x + 5, ex.y + 10, this);
				} else if (ex.ex_cnt > 21) {
					Explosion_List.remove(i);
					ex.ex_cnt = 0;
				}
			}
		}
	}

	public void Draw_StatusText() { // 상태 체크용 텍스트를 그립니다.

		buffg.setFont(new Font("Defualt", Font.BOLD, 20));
		// 폰트 설정을 합니다. 기본폰트, 굵게, 사이즈 20

		buffg.drawString("SCORE : " + game_Score, 350, 70);
		// 좌표 x : 1000, y : 70에 스코어를 표시합니다.
		buffg.drawString("거리 : " + (int) game_Length, 350, 110);
		buffg.drawString("HitPoint : " + player_Hitpoint, 350, 90);
		// 좌표 x : 1000, y : 90에 플레이어 체력을 표시합니다.

	}

	public void KeyProcess() {
		if (KeyUp == true) {
			if (y > 20)
				y -= 5;
			// 캐릭터가 보여지는 화면 위로 못 넘어가게 합니다.

			player_Status = 0;
			// 이동키가 눌려지면 플레이어 상태를 0으로 돌립니다.
		}

		if (KeyDown == true) {
			if (y + me_img.getHeight(null) < f_height)
				y += 5;
			// 캐릭터가 보여지는 화면 아래로 못 넘어가게 합니다.

			player_Status = 0;
			// 이동키가 눌려지면 플레이어 상태를 0으로 돌립니다.
		}

		if (KeyLeft == true) {
			if (x > 0)
				x -= 5;
			// 캐릭터가 보여지는 화면 왼쪽으로 못 넘어가게 합니다.

			player_Status = 0;
			// 이동키가 눌려지면 플레이어 상태를 0으로 돌립니다.
		}

		if (KeyRight == true) {
			if (x + me_img.getWidth(null) < f_width)
				x += 5;
			// 캐릭터가 보여지는 화면 오른쪽으로 못 넘어가게 합니다.

			player_Status = 0;
			// 이동키가 눌려지면 플레이어 상태를 0으로 돌립니다.
		}
	}

	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			KeyUp = true;
			break;
		case KeyEvent.VK_DOWN:
			KeyDown = true;
			break;
		case KeyEvent.VK_LEFT:
			KeyLeft = true;
			break;
		case KeyEvent.VK_RIGHT:
			KeyRight = true;
			break;

		case KeyEvent.VK_SPACE:
			KeySpace = true;
			break;
		}
	}

	public void keyReleased(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			KeyUp = false;
			break;
		case KeyEvent.VK_DOWN:
			KeyDown = false;
			break;
		case KeyEvent.VK_LEFT:
			KeyLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
			KeyRight = false;
			break;

		case KeyEvent.VK_SPACE:
			KeySpace = false;
			break;

		}
	}

	public void keyTyped(KeyEvent e) {
	}

	/*
	 * Swing의 ImageIcon 사용으로인해 별도로 이미지 넓이, 높이 계산을 할 필요가 없어졌으므로 삭제합니다.
	 * 
	 * public int ImageWidthValue(String file){ // 이미지 넓이 크기 값 계산용 메소드 입니다. //
	 * 파일을 받아들여 그 파일 값을 계산 하도록 하는 것입니다. int x = 0; try{ File f = new File(file);
	 * // 파일을 받습니다. BufferedImage bi = ImageIO.read(f); //받을 파일을 이미지로 읽어들입니다. x
	 * = bi.getWidth(); //이미지의 넓이 값을 받습니다. }catch(Exception e){} return x; //받은
	 * 넓이 값을 리턴 시킵니다. }
	 * 
	 * public int ImageHeightValue(String file){ // 이미지 높이 크기 값 계산 int y = 0;
	 * try{ File f = new File(file); BufferedImage bi = ImageIO.read(f); y =
	 * bi.getHeight(); }catch(Exception e){} return y; }
	 */

	class Missile {
		int x;
		int y;
		int angle;
		int speed; // 미사일 스피드 변수를 추가.

		Missile(int x, int y,int angle, int speed) {
			this.x = x;
			this.y = y;
			this.angle= angle;
			this.speed = speed;
			// 객체 생성시 속도 값을 추가로 받습니다.

		}

		public void move() {
			/*y -= 10;
			// x += speed; // 미사일 스피드 속도 만큼 이동
*/			
			  x += Math.cos(Math.toRadians(angle))*speed;
			  //해당 방향으로 미사일 발사.

			  y += Math.sin(Math.toRadians(angle))*speed;
			  //해당 방향으로 미사일 발사.



		}
	}

	class Boss {
		int x;
		int y;

		// int speed;
		Boss(int x, int y) {
			this.x = x;
			this.y = y;

		}

		public void move() {
			y += 1;
			// x -= speed;// 적이동속도만큼 이동
		}
	}

	class Enemy {
		int x;
		int y;

		// int speed; // 적 이동 속도 변수를 추가

		Enemy(int x, int y) {
			this.x = x;
			this.y = y;

			// this.speed = speed;
			// 객체 생성시 속도 값을 추가로 받습니다.

		}

		public void move() {
			y += 5;
			game_Length += 0.01;
			game_Score += 0.01;

			// x -= speed;// 적이동속도만큼 이동
		}
	}

	class Explosion {
		// 여러개의 폭발 이미지를 그리기위해 클래스를 추가하여 객체관리

		int x; // 이미지를 그릴 x 좌표
		int y; // 이미지를 그릴 y 좌표
		int ex_cnt; // 이미지를 순차적으로 그리기 위한 카운터
		int damage; // 이미지 종류를 구분하기 위한 변수값

		Explosion(int x, int y, int damage) {
			this.x = x;
			this.y = y;
			this.damage = damage;
			ex_cnt = 0;
		}

		public void effect() {
			ex_cnt++; // 해당 메소드 호출 시 카운터를 +1 시킨다.
		}
	}

	// public class game_Frame {
	public static void main(String[] ar) {
		new game_Frame();
	}
}
// }