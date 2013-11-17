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
	int[] cx = { 0, 0, 0 }; // ��� ��ũ�� �ӵ� ����� ����
	int bx = 0; // ��ü ��� ��ũ�� �� ����

	boolean KeyUp = false;
	boolean KeyDown = false;
	boolean KeyLeft = false;
	boolean KeyRight = false;
	boolean KeySpace = false;

	int cnt;
	int player_Speed; // ������ ĳ���Ͱ� �����̴� �ӵ��� ������ ����
	int missile_Speed; // �̻����� ���󰡴� �ӵ� ������ ����
	int fire_Speed; // �̻��� ���� �ӵ� ���� ����
	int enemy_speed; // �� �̵� �ӵ� ����
	int player_Status = 0;
	// ���� ĳ���� ���� üũ ���� 0 : ����, 1: �̻��Ϲ߻�, 2: �浹
	int game_Score = 0; // ���� ���� ���
	double game_Length = 0; // ���� �Ÿ� ���
	int player_Hitpoint; // �÷��̾� ĳ������ ü��
	int boss;

	Thread th;

	Toolkit tk = Toolkit.getDefaultToolkit();

	// Image[] Player_img;
	// �÷��̾� �ִϸ��̼� ǥ���� ���� �̹����� �迭�� ����
	Image BackGround_img; // ���ȭ�� �̹���
	Image[] Cloud_img; // �����̴� ���� �̹����迭
	Image[] Explo_img; // ��������Ʈ�� �̹����迭

	Image me_img;
	Image Missile_img;
	Image Enemy_img;
	Image boss_img;

	ArrayList Missile_List = new ArrayList();
	ArrayList Enemy_List = new ArrayList();
	ArrayList Explosion_List = new ArrayList();
	ArrayList Boss_List = new ArrayList();
	// �ټ��� ���� ����Ʈ�� ó���ϱ� ���� �迭

	Image buffImage;
	Graphics buffg;

	Missile ms;
	Enemy en;
	Boss bs;

	Explosion ex; // ���� ����Ʈ�� Ŭ���� ���� Ű

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
		// ��ü ���ȭ�� �̹����� �޽��ϴ�.

		Cloud_img = new Image[3];
		for (int i = 0; i < Cloud_img.length; ++i) {
			Cloud_img[i] = new ImageIcon("cloud1.jpg").getImage();
		}
		// ������ 3�� ���ÿ� �׸��µ� ���ǻ� �迭�� 3���� ���ÿ� �޴´�.

		Explo_img = new Image[3];
		for (int i = 0; i < Explo_img.length; ++i) {
			Explo_img[i] = new ImageIcon("img/fire1.jpg").getImage();
		}

		game_Score = 0;// ���� ���ھ� �ʱ�ȭ
		player_Hitpoint = 3;// ���� �÷��̾� ü��
		player_Speed = 5; // ���� ĳ���� �����̴� �ӵ� ����
		missile_Speed = 50; // �̻��� ������ �ӵ� ����
		fire_Speed = 10; // �̻��� ���� �ӵ� ����
		enemy_speed = 5;// ���� ������� �ӵ� ����
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
				ExplosionProcess();// ����ó�� �޼ҵ� ����
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
			// ���� �밢������ ���� �̻����Դϴ�.
			Missile_List.add(ms);

			ms = new Missile(x + 20, y + 0, 30, missile_Speed);
			// �Ʒ��� �밢������ ���� �̻����Դϴ�.
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
					game_Score += 100; // ���� ������ +10��.
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

				player_Hitpoint--; // �÷��̾� ü���� 1����ϴ�.
				Boss_List.remove(i); // ���� �����մϴ�.
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

				player_Hitpoint--; // �÷��̾� ü���� 1����ϴ�.
				Enemy_List.remove(i); // ���� �����մϴ�.
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
		// ���� ����Ʈ ó���� �޼ҵ�

		for (int i = 0; i < Explosion_List.size(); ++i) {
			ex = (Explosion) Explosion_List.get(i);
			ex.effect();
			// ����Ʈ �ִϸ��̼��� ��Ÿ��������
			// ����Ʈ ó�� �߰��� �߻��ϸ� �ش� �޼ҵ带 ȣ��.

		}
	}

	public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2) {
		// ���� �浹 ���� �ҽ��� �����մϴ�.
		// ���� �̹��� ������ �ٷ� �޾� �ش� �̹����� ����, ���̰���
		// �ٷ� ����մϴ�.

		boolean check = false;

		if (Math.abs((x1 + img1.getWidth(null) / 2)
				- (x2 + img2.getWidth(null) / 2)) < (img2.getWidth(null) / 2 + img1
				.getWidth(null) / 2)
				&& Math.abs((y1 + img1.getHeight(null) / 2)
						- (y2 + img2.getHeight(null) / 2)) < (img2
						.getHeight(null) / 2 + img1.getHeight(null) / 2)) {
			// �̹��� ����, ���̰��� �ٷ� �޾� ����մϴ�.

			check = true;// �� ���� true�� check�� true�� �����մϴ�.
		} else {
			check = false;
		}

		return check; // check�� ���� �޼ҵ忡 ���� ��ŵ�ϴ�.

	}

	public void paint(Graphics g) {
		buffImage = createImage(f_width, f_height);
		buffg = buffImage.getGraphics();

		update(g);
	}

	public void update(Graphics g) {

		Draw_Background(); // ��� �̹��� �׸��� �޼ҵ� ����
		Draw_Player(); // �÷��̾ �׸��� �޼ҵ� �̸� ����
		Draw_Enemy();
		Draw_Boss();
		Draw_Missile();
		Draw_Explosion();// ��������Ʈ�׸��� �޼ҵ� ����
		Draw_StatusText();// ���� ǥ�� �ؽ�Ʈ�� �׸��� �޼ҵ� ����
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
		// ��� �̹����� �׸��� �κ��Դϴ�.

		buffg.clearRect(0, 0, f_width, f_height);
		// ȭ�� ����� ����� ���� ���⼭ �����մϴ�.

		if (bx > -1000) {
			// �⺻ ���� 0�� bx�� -3500 ���� ũ�� ����

			buffg.drawImage(BackGround_img, 0, bx, this);
			bx += 2;
			// bx�� 0���� -1��ŭ ��� ���̹Ƿ� ����̹����� x��ǥ��
			// ��� �������� �̵��Ѵ�. �׷��Ƿ� ��ü ����� õõ��
			// �������� �����̰� �ȴ�.

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
			// 3���� ���� �̹����� ���� �ٸ� �ӵ� ������ �������� ������.
		}
	}

	public void Draw_Player() {

		switch (player_Status) {

		case 0: // ����
			if ((cnt / 5 % 2) == 0) {
				buffg.drawImage(me_img, x, y, this);
			} else {
				buffg.drawImage(me_img, x, y, this);
			}
			// �����ʿ��� ���� �մ� �̹����� ������ �׷��ش�.

			break;

		case 1: // �̻��Ϲ߻�
			if ((cnt / 5 % 2) == 0) {
				buffg.drawImage(me_img, x, y, this);
			} else {

				buffg.drawImage(me_img, x, y, this);
			}
			player_Status = 0;
			break;
		case 2: // �浹
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
		// ���� ����Ʈ�� �׸��� �κ� �Դϴ�.

		for (int i = 0; i < Explosion_List.size(); ++i) {
			ex = (Explosion) Explosion_List.get(i);
			// ���� ����Ʈ�� ���� ������ üũ�Ͽ� ����Ʈ�� ����.

			if (ex.damage == 0) {
				// �������� 0 �̸� ���߿� �̹��� �׸���

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
			} else { // �������� 1�̸� �ܼ� �ǰݿ� �̹��� �׸���
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

	public void Draw_StatusText() { // ���� üũ�� �ؽ�Ʈ�� �׸��ϴ�.

		buffg.setFont(new Font("Defualt", Font.BOLD, 20));
		// ��Ʈ ������ �մϴ�. �⺻��Ʈ, ����, ������ 20

		buffg.drawString("SCORE : " + game_Score, 350, 70);
		// ��ǥ x : 1000, y : 70�� ���ھ ǥ���մϴ�.
		buffg.drawString("�Ÿ� : " + (int) game_Length, 350, 110);
		buffg.drawString("HitPoint : " + player_Hitpoint, 350, 90);
		// ��ǥ x : 1000, y : 90�� �÷��̾� ü���� ǥ���մϴ�.

	}

	public void KeyProcess() {
		if (KeyUp == true) {
			if (y > 20)
				y -= 5;
			// ĳ���Ͱ� �������� ȭ�� ���� �� �Ѿ�� �մϴ�.

			player_Status = 0;
			// �̵�Ű�� �������� �÷��̾� ���¸� 0���� �����ϴ�.
		}

		if (KeyDown == true) {
			if (y + me_img.getHeight(null) < f_height)
				y += 5;
			// ĳ���Ͱ� �������� ȭ�� �Ʒ��� �� �Ѿ�� �մϴ�.

			player_Status = 0;
			// �̵�Ű�� �������� �÷��̾� ���¸� 0���� �����ϴ�.
		}

		if (KeyLeft == true) {
			if (x > 0)
				x -= 5;
			// ĳ���Ͱ� �������� ȭ�� �������� �� �Ѿ�� �մϴ�.

			player_Status = 0;
			// �̵�Ű�� �������� �÷��̾� ���¸� 0���� �����ϴ�.
		}

		if (KeyRight == true) {
			if (x + me_img.getWidth(null) < f_width)
				x += 5;
			// ĳ���Ͱ� �������� ȭ�� ���������� �� �Ѿ�� �մϴ�.

			player_Status = 0;
			// �̵�Ű�� �������� �÷��̾� ���¸� 0���� �����ϴ�.
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
	 * Swing�� ImageIcon ����������� ������ �̹��� ����, ���� ����� �� �ʿ䰡 ���������Ƿ� �����մϴ�.
	 * 
	 * public int ImageWidthValue(String file){ // �̹��� ���� ũ�� �� ���� �޼ҵ� �Դϴ�. //
	 * ������ �޾Ƶ鿩 �� ���� ���� ��� �ϵ��� �ϴ� ���Դϴ�. int x = 0; try{ File f = new File(file);
	 * // ������ �޽��ϴ�. BufferedImage bi = ImageIO.read(f); //���� ������ �̹����� �о���Դϴ�. x
	 * = bi.getWidth(); //�̹����� ���� ���� �޽��ϴ�. }catch(Exception e){} return x; //����
	 * ���� ���� ���� ��ŵ�ϴ�. }
	 * 
	 * public int ImageHeightValue(String file){ // �̹��� ���� ũ�� �� ��� int y = 0;
	 * try{ File f = new File(file); BufferedImage bi = ImageIO.read(f); y =
	 * bi.getHeight(); }catch(Exception e){} return y; }
	 */

	class Missile {
		int x;
		int y;
		int angle;
		int speed; // �̻��� ���ǵ� ������ �߰�.

		Missile(int x, int y,int angle, int speed) {
			this.x = x;
			this.y = y;
			this.angle= angle;
			this.speed = speed;
			// ��ü ������ �ӵ� ���� �߰��� �޽��ϴ�.

		}

		public void move() {
			/*y -= 10;
			// x += speed; // �̻��� ���ǵ� �ӵ� ��ŭ �̵�
*/			
			  x += Math.cos(Math.toRadians(angle))*speed;
			  //�ش� �������� �̻��� �߻�.

			  y += Math.sin(Math.toRadians(angle))*speed;
			  //�ش� �������� �̻��� �߻�.



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
			// x -= speed;// ���̵��ӵ���ŭ �̵�
		}
	}

	class Enemy {
		int x;
		int y;

		// int speed; // �� �̵� �ӵ� ������ �߰�

		Enemy(int x, int y) {
			this.x = x;
			this.y = y;

			// this.speed = speed;
			// ��ü ������ �ӵ� ���� �߰��� �޽��ϴ�.

		}

		public void move() {
			y += 5;
			game_Length += 0.01;
			game_Score += 0.01;

			// x -= speed;// ���̵��ӵ���ŭ �̵�
		}
	}

	class Explosion {
		// �������� ���� �̹����� �׸������� Ŭ������ �߰��Ͽ� ��ü����

		int x; // �̹����� �׸� x ��ǥ
		int y; // �̹����� �׸� y ��ǥ
		int ex_cnt; // �̹����� ���������� �׸��� ���� ī����
		int damage; // �̹��� ������ �����ϱ� ���� ������

		Explosion(int x, int y, int damage) {
			this.x = x;
			this.y = y;
			this.damage = damage;
			ex_cnt = 0;
		}

		public void effect() {
			ex_cnt++; // �ش� �޼ҵ� ȣ�� �� ī���͸� +1 ��Ų��.
		}
	}

	// public class game_Frame {
	public static void main(String[] ar) {
		new game_Frame();
	}
}
// }