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

// ------------------------------------------------------------------

class Game_f extends JFrame implements KeyListener, Runnable {
	int f_width; // �����Ӱ���
	int f_height; // �����Ӽ���
	int x, y; // �÷��̾� ��ǥ
	// int p_w, p_h, m_w, m_h, e_w, e_h; // �÷��̾�,�̻���,�� ���μ���
	String str;
	int num_img;

	int game_Score = 0;// ���� ���ھ� �ʱ�ȭ
	double game_Length = 0;// ���� �Ÿ� �ʱ�ȭ

	int fire_Speed; // �̻��� ����ӵ� ���� ����
	int missile_Speed; // �̻��� ���󰡴� �ӵ� ���� ����
	int back_speed;

	int player_Status = 0; // ���� ĳ���� ���� üũ���� 0:����, 1:�̻��Ϲ߻�, 2:�浹.
	int player_Life;// �÷��̾� ĳ������ ü��
	int boss_Life = 0;// ������ ü��
	int MEnemy_Life;// ��ũ ü��
	int Enemy_Die;// �̵��� ���� ��
	int cnt; // ���� Ÿ�̹� ������ ���� ���� ������ ī������ ����
	int delay;// ���� ������. 1/1000�� ����.
	long pretime;// ���� ������ �����ϱ� ���� �ð� üũ��

	int bg1Y = 0, bg2Y = -1050;// ���ȭ�� ��ġ
	int[] cy = { 0, 0, 0 }; // ��� ��ũ�� �ӵ� ����� ����
	int by = 0; // ��ü ��� ��ũ�� �� ����

	// Ű���� �ʱ�ȭ
	boolean KeyUp = false;
	boolean KeyDown = false;
	boolean KeyLeft = false;
	boolean KeyRight = false;
	boolean KeySpace = false;

	boolean roof = true;// ������ ���� ����

	Image Player_img;// �÷��̾� �̹����� �޾Ƶ��� �̹��� ����
	Image Missile_img;// �̻��� �̹����� �޾Ƶ��� �̹��� ����
	Image Missile_Boss_img; // �����̻���
	Image Missile_MEnemy_img;// ��ũ�̻���
	Image Enemy_img; // �� �̹����� �޾Ƶ��� �̹��� ����
	Image Item_img; // ������ �̹����� �޾Ƶ��� �̹��� ����**1
	Image Coin_img; // ������ �̹����� �޾Ƶ��� �̹��� ����**1
	Image MEnemy_img;// ��ũ��ȯ
	Image boss_img;

	Image buffImage;
	Graphics buffg;

	// �̿�
	Image BackGround_img1; // ���ȭ�� �̹���
	Image BackGround_img2; // ���ȭ�� �̹���
	Image fire_img; // ��������Ʈ �̹���
	Image[] P_img; // �÷��̾� �ִϸ��̼� ǥ���� ���� �̹����� �迭�� ����

	Missile ms; // �̻��� Ŭ���� ���� Ű
	Enemy en; // ���ʹ� Ŭ���� ���� Ű
	Item it; // ������ Ŭ���� ���� Ű
	Coin co;
	MEnemy men;
	Explosion ex; // ���� ����Ʈ�� Ŭ���� ���� Ű
	Boss bs; // ���� Ŭ���� ���� Ű
	Toolkit tk = Toolkit.getDefaultToolkit();

	Dimension screen = tk.getScreenSize(); // ��ũ�� ������ ��������
	int f_xpos = (int) (screen.getWidth() / 2 - f_width / 2);
	int f_ypos = (int) (screen.getHeight() / 2 - f_height / 2);
	// �߾ӿ� �������� ��ġ�ϱ� ���� ����

	/*
	 * ArrayList Missile_Player_List = new ArrayList(); // player �̻��ϵ��� ������� �迭
	 * ArrayList Missile_Boss_List = new ArrayList(); // ���� �̻��ϵ��� ������� �迭
	 * ArrayList Missile_MEnemy_List = new ArrayList(); // ��ũ �̻��ϵ��� ������� �迭
	 */
	ArrayList Missile_List = new ArrayList();
	// �ټ��� �̻��� ���� �迭
	ArrayList Explosion_List = new ArrayList();
	// �ټ��� ���� ����Ʈ�� ó���ϱ� ���� �迭
	ArrayList Enemy_List = new ArrayList();
	ArrayList Item_List = new ArrayList();
	// �ټ��� �������� ������� �迭
	ArrayList Coin_List = new ArrayList();
	// �ټ��� �������� ������� �迭
	// �ټ��� ���� ������� �迭
	ArrayList MEnemy_List = new ArrayList();
	// �ټ��� ���� ������� �迭
	ArrayList Boss_List = new ArrayList();
	// ���� ����Ʈ

	Thread th;

	// ------------------------------------------------------------------

	public Game_f(String str) {
		Player_img = tk.getImage(str);// ������ ĳ���Ϳ� ������ĳ���͹迭��ȣ�� �޾ƿ´�.
		this.str = str;

		init();
		start();
		setTitle("�ö��� ����");
		setSize(f_width, f_height);

		f_xpos = (int) (screen.getWidth() / 2 - f_width / 2);
		f_ypos = (int) (screen.getHeight() / 2 - f_height / 2);
		System.out.println(f_xpos);
		System.out.println(f_ypos);

		setLocation(f_xpos, f_ypos);
		setResizable(false);
		setVisible(true);

	}

	@Override
	public void run() {
		try {
			while (roof) {
				pretime = System.currentTimeMillis();

				KeyProcess(); // Ű ������ ó�� �޼ҵ� ����
				ExplosionProcess();// ����ó�� �޼ҵ� ����
				MissileProcess(); // �Ѿ� ������ ó�� �޼ҵ� ����
				EnemyProcess(); // �� ������ ó�� �޼ҵ� ����
				ItemProcess(); // ������ ������ ó�� �޼ҵ� ����
				CoinProcess(); // ���� ������ ó�� �޼ҵ� ����
				MEnemyProcess();// ��ũ ������ ó�� �޼ҵ� ����
				bossProcess();// ���� ������ ó�� �޼ҵ� ����

				repaint();

				th.sleep(10); // ������ 10�ʼ���

				if (System.currentTimeMillis() - pretime < delay)
					th.sleep(delay - System.currentTimeMillis() + pretime);
				// ���� ������ ó���ϴµ� �ɸ� �ð��� üũ�ؼ� �����̰����� �����Ͽ� �����̸� �����ϰ� �����Ѵ�.
				// ���� ���� �ð��� ������ �ð����� ũ�ٸ� ���� �ӵ��� �������� �ȴ�.

				cnt++;

			}
		} catch (Exception e) {

			/*
			 * dispose(); th.stop(); new Score();
			 */
		}
	}

	// ------------------------------------------------------------------

	public void init() {
		x = 525; // player ������ġ - ����
		y = 1000; // player ������ġ - ����
		f_width = 1050; // ������ ����
		f_height = 1050; // ������ ����

		delay = 10; // 10/1000�� = 100 (������/��)
		game_Score = 0; // ���� ���ӽ��ھ� �ʱ�ȭ
		player_Life = 4; // ���� �÷��̾� ü�� �ֱ�
		MEnemy_Life = 2;
		missile_Speed = 5; // �̻��� ������ �ӵ� ����
		fire_Speed = 20;// ����ӵ�
		back_speed = 5;
		// Player_img = tk.getImage("img/Player1.png");
		// Enemy_img = tk.getImage("img/Enemy1.png");// �� �̹��� ����
		// Missile_img = tk.getImage("img/Missile1.png");
		/*
		 * p_w = ImageWidthValue(str); p_h = ImageHeightValue(str);
		 * 
		 * e_w = ImageWidthValue("img/Enemy1.png"); // ���̹����� ���α��� e_h =
		 * ImageHeightValue("img/Enemy1.png"); // �� �̹����� ���α���
		 * 
		 * m_w = ImageWidthValue("img/Missile1.png"); m_h =
		 * ImageHeightValue("img/Missile1.png");
		 */

		// �̹��� ����� ����� ImageIcon���� ��������
		// �̹��� ũ�� ��� �޼ҵ带 �����ϰ� Swing���� �����Ǵ� ImageIcon����
		// �̹��� ũ�Ⱚ�� ��´�.

		Enemy_img = new ImageIcon("img/Enemy1.png").getImage();
		Item_img = new ImageIcon("img/heart.png").getImage();
		Coin_img = new ImageIcon("img/coin.png").getImage();
		MEnemy_img = new ImageIcon("img/M_Enemy.png").getImage();
		Missile_img = new ImageIcon("img/Missile1.png").getImage();
		Missile_Boss_img = new ImageIcon("img/asd.png").getImage();
		Missile_MEnemy_img = new ImageIcon("img/Missile_ME.png").getImage();
		boss_img = new ImageIcon("img/king.png").getImage();
		fire_img = new ImageIcon("img/fire1.png").getImage();
		// ����̹��� �ޱ�
		BackGround_img1 = new ImageIcon("img/ground.png").getImage();
		BackGround_img2 = new ImageIcon("img/ground.png").getImage();
		/*
		 * fire_img = new Image[3]; for (int i = 0; i < fire_img.length; ++i) {
		 * fire_img[i] = new ImageIcon("img/fire" + i + ".png").getImage(); }
		 */
		// ��������
	}

	// ------------------------------------------------------------------
	/*
	 * public int ImageWidthValue(String file) {// �̹����� ���α��̸� ���Ѵ�. int x = 0;
	 * try { File f = new File(file); BufferedImage bi = ImageIO.read(f); x =
	 * bi.getWidth(); } catch (Exception e) { } return x; }
	 * 
	 * public int ImageHeightValue(String file) {// �̹����� ���α��̸� ���Ѵ�. int y = 0;
	 * try { File f = new File(file); BufferedImage bi = ImageIO.read(f); y =
	 * bi.getHeight(); } catch (Exception e) { } return y; }
	 */

	// -----------------------------------------------------------------�׸��⼳��

	// ���� üũ�� �ؽ�Ʈ�� �׸���.
	public void Draw_StatusText() {
		buffg.setFont(new Font("Defualt", Font.BOLD, 20));
		// ��Ʈ ������ �մϴ�. �⺻��Ʈ, ����, ������ 20
		buffg.drawString(" S C O R E : " + game_Score, 850, 70);
		// ��ǥ x : 350, y : 70�� ���ھ ǥ���Ѵ�.
		buffg.drawString("L e n g t h : " + (int) game_Length, 853, 90);
		// ��ǥ x : 350, y : 90�� �Ÿ��� ǥ���Ѵ�.
		buffg.drawString(" All Kills  : " + Enemy_Die, 850, 110);

		String[] heart = { "", "��", "����", "������", "��������" };
		buffg.drawString(" My hearts : " + heart[player_Life], 850, 130);
		// ��ǥ x : 350, y : 110�� �÷��̾� ü���� ǥ���Ѵ�.
	}

	// �÷��̾� �̹��� �׸���
	public void Draw_Player() {
		switch (player_Status) {

		case 0: // ����
			if ((cnt / 5 % 2) == 0) {
				buffg.drawImage(Player_img, x, y, this);
			} else {
				buffg.drawImage(Player_img, x, y, this);
			}
			// �����ʿ��� ���� �մ� �̹����� ������ �׷��ش�.
			break;

		case 1: // �̻��Ϲ߻�
			if ((cnt / 5 % 2) == 0) {
				buffg.drawImage(Player_img, x, y, this);
			} else {
				buffg.drawImage(Player_img, x, y, this);
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

	// �̻��� �̹��� & �� die �̹���
	public void Draw_Missile() {
		for (int i = 0; i < Missile_List.size(); i++) {
			ms = (Missile) (Missile_List.get(i));
			if (ms.who == 0)
				buffg.drawImage(Missile_img, ms.mx, ms.my, this);
			if (ms.who == 1)
				buffg.drawImage(Missile_Boss_img, ms.mx, ms.my, this);
			if (ms.who == 2)
				buffg.drawImage(Missile_MEnemy_img, ms.mx, ms.my, this);
		}
	}

	// �� �̹����� �׸��� �κ�
	public void Draw_Enemy() {
		for (int i = 0; i < Enemy_List.size(); ++i) {
			en = (Enemy) (Enemy_List.get(i));
			buffg.drawImage(Enemy_img, en.ex, en.ey, this);
			// �迭�� ������ �� ���� �Ǻ��Ͽ� �̹��� �׸���
		}
	}

	// ������ �̹����� �׸��� �κ�
	public void Draw_Item() {
		for (int i = 0; i < Item_List.size(); ++i) {
			it = (Item) (Item_List.get(i));
			buffg.drawImage(Item_img, it.ix, it.iy, this);
			// �迭�� ������ �� ���� �Ǻ��Ͽ� �̹��� �׸���
		}
	}

	// ������ �̹����� �׸��� �κ�
	public void Draw_Coin() {
		for (int i = 0; i < Coin_List.size(); ++i) {
			co = (Coin) (Coin_List.get(i));
			buffg.drawImage(Coin_img, co.cx, co.cy, this);
			// �迭�� ������ �� ���� �Ǻ��Ͽ� �̹��� �׸���
		}
	}

	// ��ũ�� �׸��� �κ�
	public void Draw_MEnemy() {

		for (int i = 0; i < MEnemy_List.size(); ++i) {
			men = (MEnemy) (MEnemy_List.get(i));

			buffg.drawImage(MEnemy_img, men.x, men.y, this);
		}
	}

	// �� ������ �׸��� �κ�
	public void Draw_Boss() {

		for (int i = 0; i < Boss_List.size(); ++i) {
			bs = (Boss) (Boss_List.get(i));
			buffg.drawImage(boss_img, bs.x, bs.y, this);
		}
	}

	// ��� �̹����� �׸��� �κ��Դϴ�.
	public void Draw_Background() {
		buffg.clearRect(0, 0, f_width, f_height);
		// ȭ�� ����� ����� ���� ���⼭ �����մϴ�.
		buffg.drawImage(BackGround_img1, 0, bg1Y, this);
		buffg.drawImage(BackGround_img2, 0, bg2Y, this);
		bg1Y += back_speed;
		bg2Y += back_speed;

		if (bg1Y >= 1050)
			bg1Y = -1050;
		if (bg2Y >= 1050)
			bg2Y = -1050;
	}

	public void Draw_Explosion() {
		// ���� ����Ʈ�� �׸��� �κ� �Դϴ�.
		for (int i = 0; i < Explosion_List.size(); ++i) {
			ex = (Explosion) Explosion_List.get(i);
			if (ex.damage == 0) {
				buffg.drawImage(fire_img, ex.x - fire_img.getWidth(null) / 2,
						ex.y - fire_img.getHeight(null) / 2, this);
				Explosion_List.remove(i);
			}
		}
	}

	// ------------------------------------------------------------------�׸������

	public void paint(Graphics g) {

		buffImage = createImage(f_width, f_height);
		buffg = buffImage.getGraphics();
		update(g);

	}

	public void update(Graphics g) {

		if (this.player_Life > 0) {
			Draw_Background();// ���
			Draw_Player();// �÷��̾�
			Draw_Missile();// �̻���
			Draw_Enemy();// ��
			Draw_Item(); // ������
			Draw_Coin(); // ����
			Draw_Boss();// ����
			Draw_MEnemy();// ��ũ�پ�
			Draw_StatusText();// ����ǥ��
			Draw_Explosion();// ����
			g.drawImage(buffImage, 0, 0, this);

		} else if (player_Life == 0) {
			setVisible(false);
			th.stop();
			new Score();
		}
	}

	// ------------------------------------------------------------------����
	public void Draw_Char() {
		buffg.clearRect(0, 0, f_width, f_height);
		buffg.drawImage(Player_img, x, y, this);
	}

	public void start() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ����
		addKeyListener(this); // Ű ������
		th = new Thread(this); // ������ ����
		th.start(); // ������ ����
		if (player_Life == 0) {
			th.stop();
			new Score();
		}
	}

	// ------------------------------------------------------------------die �޼ҵ�

	public boolean Die(int x1, int y1, int x2, int y2, Image img1, Image img2) {

		boolean check = false;

		if (Math.abs((x1 + img1.getWidth(null) / 2)
				- (x2 + img2.getWidth(null) / 2)) < (img2.getWidth(null) / 2 + img1
				.getWidth(null) / 2)
				&& Math.abs((y1 + img1.getHeight(null) / 2)
						- (y2 + img2.getHeight(null) / 2)) < (img2
						.getHeight(null) / 2 + img1.getHeight(null) / 2)) {
			// �̹��� ����, ���̰��� �ٷ� �޾� ����մϴ�.
			check = true;
		} else {
			check = false;
		}

		return check;

	}

	// ------------------------------------------------------------------����ó��
	// �޼ҵ� ����

	public void ExplosionProcess() {
		// ���� ����Ʈ ó���� �޼ҵ�
		for (int i = 0; i < Explosion_List.size(); ++i) {
			ex = (Explosion) Explosion_List.get(i);
			ex.effect();
			// ����Ʈ �ִϸ��̼��� ��Ÿ��������
			// ����Ʈ ó�� �߰��� �߻��ϸ� �ش� �޼ҵ带 ȣ��.
		}
	}

	// ------------------------------------------------------------------�̻���ó���޼ҵ�

	public void MissileProcess() {

		if (KeySpace) {
			player_Status = 1;
			if ((cnt % fire_Speed) == 0) {
				if (player_Life > 2) {
					ms = new Missile(x + 20, y + 10, 270, missile_Speed, 0);
					// �����̻���
					Missile_List.add(ms);
				} else if (player_Life == 2) {
					ms = new Missile(x + 20, y + 10, 270, missile_Speed, 0);
					// �����̻���
					Missile_List.add(ms);

					ms = new Missile(x + 20, y + 10, 240, missile_Speed, 0);
					// ����(��) �밢������ ���� �̻����Դϴ�.
					Missile_List.add(ms);

					ms = new Missile(x + 20, y + 10, 300, missile_Speed, 0);
					// ������(��) �밢������ ���� �̻����Դϴ�.
					Missile_List.add(ms);

				} else if (player_Life == 1) {
					ms = new Missile(x + 20, y + 10, 270, missile_Speed, 0);
					// �����̻���
					Missile_List.add(ms);

					ms = new Missile(x + 20, y + 10, 210, missile_Speed, 0);
					// ����(��) �밢������ ���� �̻����Դϴ�.
					Missile_List.add(ms);

					ms = new Missile(x + 20, y + 10, 330, missile_Speed, 0);
					// ������(��) �밢������ ���� �̻����Դϴ�.
					Missile_List.add(ms);

					ms = new Missile(x + 20, y + 10, 240, missile_Speed, 0);
					// ����(��) �밢������ ���� �̻����Դϴ�.
					Missile_List.add(ms);

					ms = new Missile(x + 20, y + 10, 300, missile_Speed, 0);
					// ������(��) �밢������ ���� �̻����Դϴ�.
					Missile_List.add(ms);
				}
			}
		}

		for (int i = 0; i < Missile_List.size(); ++i) {
			ms = (Missile) Missile_List.get(i);
			ms.move();
			if (ms.mx > f_width - 20 || ms.my < 0 || ms.my > f_height) {
				Missile_List.remove(i);
			}
			if (Die(x, y, ms.mx, ms.my, Player_img, Missile_Boss_img)
					&& ms.who == 1) {
				player_Life -= 1;
				ex = new Explosion(en.ex + Enemy_img.getWidth(null) / 2, en.ey
						+ Enemy_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				Missile_List.remove(i);
			}
			if (Die(x, y, ms.mx, ms.my, Player_img, Missile_MEnemy_img)
					&& ms.who == 2) {
				player_Life -= 1;
				ex = new Explosion(x + Player_img.getWidth(null) / 2, y
						+ Player_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				Missile_List.remove(i);
			}

			for (int j = 0; j < Enemy_List.size(); ++j) {
				en = (Enemy) Enemy_List.get(j);

				// ���� �̻��Ͽ� �¾� ���� ���
				if (Die(ms.mx, ms.my, en.ex, en.ey, Missile_img, Enemy_img)
						&& ms.who == 0) {
					Missile_List.remove(i); // ���� �׿����� �̻��� ����
					Enemy_List.remove(j); // ���� �׿����� �� ����
					game_Score += 10; // ���� �׿����� �߰�����
					Enemy_Die += 1;
					ex = new Explosion(en.ex + Enemy_img.getWidth(null) / 2,
							en.ey + Enemy_img.getHeight(null) / 2, 0);
					Explosion_List.add(ex);
					// ���� ��ġ���ִ� ���� �߽� ��ǥ x,y ����
					// ���� ������ ���� �� ( 0 �Ǵ� 1 )�� �޽��ϴ�.
					// ���� ���� �� - 0 : ���� , 1 : �ܼ� �ǰ�
					// �浹�������� ����� ���� ��ġ�� ����Ʈ�� �߰��Ѵ�.
				}
			}
			// ������ �̻��Ͽ� �¾� ���� ���
			for (int k = 0; k < Boss_List.size(); ++k) {
				bs = (Boss) Boss_List.get(k);

				if (Die(ms.mx, ms.my, bs.x, bs.y, Missile_img, boss_img)
						&& ms.who == 0) {
					Missile_List.remove(i);
					game_Score += 100;
					ex = new Explosion(bs.x + boss_img.getWidth(null) / 2, bs.y
							+ boss_img.getHeight(null) / 2, 0);
					Explosion_List.add(ex);

					if (boss_Life > 0) {
						boss_Life -= 1;
					}
					System.out.println(boss_Life);
					if (boss_Life < 2) {
						Boss_List.remove(k);
					}
				}
			}

			// �߰��� �̻��Ͽ� �¾� ���� ���
			for (int k = 0; k < MEnemy_List.size(); ++k) {
				men = (MEnemy) MEnemy_List.get(k);

				if (Die(ms.mx, ms.my, men.x, men.y, Missile_img, MEnemy_img)
						&& ms.who == 0) {
					Missile_List.remove(i);
					game_Score += 100;
					ex = new Explosion(men.x + MEnemy_img.getWidth(null) / 2,
							men.y + MEnemy_img.getHeight(null) / 2, 0);
					Explosion_List.add(ex);

					if (MEnemy_Life > 0) {
						MEnemy_Life -= 1;
					}
					System.out.println(boss_Life);
					if (MEnemy_Life < 1) {
						MEnemy_List.remove(k);
					}
				}
			}
		}
	}

	// ------------------------------------------------------------------����ó��
	// �޼ҵ�
	public void bossProcess() {
		if (Enemy_Die % 50 == 0)
			this.boss_Life = 20;

		for (int i = 0; i < Boss_List.size(); ++i) {
			bs = (Boss) (Boss_List.get(i));
			bs.move();
			if (bs.x < -200) {
				Boss_List.remove(i);
			}
			if (cnt % 15 == 0) {
				// �����̻��ϻѸ��� �κ�.
				ms = new Missile(bs.x, bs.y, (int) (Math.random() * 120) + 210,
						missile_Speed, 1);
				Missile_List.add(ms);

				// }
			}

			// ������ �ε�ġ��
			if (Die(x, y, bs.x, bs.y, Player_img, boss_img)) {
				game_Score += 1000;
				ex = new Explosion(bs.x + boss_img.getWidth(null) / 2, bs.y
						+ boss_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				ex = new Explosion(x, y, 1);
				Explosion_List.add(ex);

				player_Life = 0; // �÷��̾� die

			}
		}

		if (cnt % 500 == 0) {

			bs = new Boss(500, 100);
			Boss_List.add(bs);
		}
	}

	public void MEnemyProcess() {
		for (int i = 0; i < MEnemy_List.size(); ++i) {
			men = (MEnemy) (MEnemy_List.get(i));// �迭�� ���� �����Ǿ����� �� �ش�Ǵ� ���� �Ǻ�
			men.move(); // �ش� ���� �̵���Ų��.
			if (men.x < -200) { // ���� ��ǥ�� ȭ�� ������ �Ѿ��
				Enemy_List.remove(i); // �ش� ���� �迭���� ����
			}

			if (cnt % 100 == 0) {
				// ��ũ �̻��ϻѸ��� �κ�.
				ms = new Missile(men.x, men.y,
						(int) (Math.random() * 120) + 210, missile_Speed, 2);
				Missile_List.add(ms);

				// }
			}

			// ������ �浹�� Die�޼ҵ�
			if (Die(x, y, en.ex, en.ey, Player_img, MEnemy_img)) {
				MEnemy_List.remove(i); // �ε��� ���� �����մϴ�.
				player_Life -= 1;
				ex = new Explosion(men.x + MEnemy_img.getWidth(null) / 2, men.y
						+ MEnemy_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				ex = new Explosion(x, y, 1);
				Explosion_List.add(ex);
			}
		}
		if (cnt % 200 == 0) { // ��ũ �����ö� x��ǥ, y��ǥ

			men = new MEnemy(100, 400);
			MEnemy_List.add(men);
			/*
			 * men = new MEnemy(200, 400); MEnemy_List.add(men);
			 */
			men = new MEnemy(300, 400);
			MEnemy_List.add(men);
			/*
			 * men = new MEnemy(400, 400); MEnemy_List.add(men);
			 */
			men = new MEnemy(500, 400);
			MEnemy_List.add(men);
			/*
			 * men = new MEnemy(600, 400); MEnemy_List.add(men);
			 */
			men = new MEnemy(700, 400);
			MEnemy_List.add(men);
			/*
			 * men = new MEnemy(800, 400); MEnemy_List.add(men);
			 */
			men = new MEnemy(900, 400);
			MEnemy_List.add(men);

		}
	}

	// ------------------------------------------------------------------��ó�� �޼ҵ�

	public void EnemyProcess() {// �� �ൿ ó�� �޼ҵ�
		for (int i = 0; i < Enemy_List.size(); ++i) {
			en = (Enemy) (Enemy_List.get(i));// �迭�� ���� �����Ǿ����� �� �ش�Ǵ� ���� �Ǻ�
			en.move(); // �ش� ���� �̵���Ų��.
			if (en.ex < -200) { // ���� ��ǥ�� ȭ�� ������ �Ѿ��
				Enemy_List.remove(i); // �ش� ���� �迭���� ����
			}

			// ������ �浹�� Die�޼ҵ�
			if (Die(x, y, en.ex, en.ey, Player_img, Enemy_img)) {
				player_Life -= 1; // �÷��̾��� ü�� 1�� ��´�.
				Enemy_List.remove(i); // �ε��� ���� �����մϴ�.
				game_Score += 10;
				ex = new Explosion(en.ex + Enemy_img.getWidth(null) / 2, en.ey
						+ Enemy_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				ex = new Explosion(x, y, 1);
				Explosion_List.add(ex);
				Enemy_Die += 1;
			}
		}

		if (cnt % 200 == 0) { // �� �����ö� x��ǥ, y��ǥ

			en = new Enemy(60, 0);
			Enemy_List.add(en);
			en = new Enemy(140, 0);
			Enemy_List.add(en);
			en = new Enemy(220, 0);
			Enemy_List.add(en);
			en = new Enemy(300, 0);
			Enemy_List.add(en);
			en = new Enemy(380, 0);
			Enemy_List.add(en);
			en = new Enemy(460, 0);
			Enemy_List.add(en);
			en = new Enemy(540, 0);
			Enemy_List.add(en);
			en = new Enemy(620, 0);
			Enemy_List.add(en);
			en = new Enemy(700, 0);
			Enemy_List.add(en);
			en = new Enemy(780, 0);
			Enemy_List.add(en);
			en = new Enemy(860, 0);
			Enemy_List.add(en);
		}
	}

	// ������ ó�� �޼ҵ�
	public void ItemProcess() {// �� �ൿ ó�� �޼ҵ�
		for (int i = 0; i < Item_List.size(); ++i) {
			it = (Item) (Item_List.get(i));// �迭�� ���� �����Ǿ����� �� �ش�Ǵ� ���� �Ǻ�
			it.move(); // �ش� ���� �̵���Ų��.
			if (it.ix < -200) { // ���� ��ǥ�� ȭ�� ������ �Ѿ��
				Item_List.remove(i); // �ش� ���� �迭���� ����
			}

			// ������ �浹�� Die�޼ҵ�
			if (Die(x, y, it.ix, it.iy, Player_img, Item_img)) {
				player_Life += 1; // �÷��̾��� ü�� 1�� ��´�.
				Item_List.remove(i); // �ε��� ���� �����մϴ�.
				// game_Score += 10;
				ex = new Explosion(it.ix + Item_img.getWidth(null) / 2, it.iy
						+ Item_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				ex = new Explosion(x, y, 1);
				Explosion_List.add(ex);
				// Enemy_Die += 1;
			}
		}

		if (cnt % 1500 == 0) { // �� �����ö� x��ǥ, y��ǥ

			it = new Item(540, 0);
			Item_List.add(it);

		}
	}

	// ������ ó�� �޼ҵ�
	public void CoinProcess() {// �� �ൿ ó�� �޼ҵ�
		for (int i = 0; i < Coin_List.size(); ++i) {
			co = (Coin) (Coin_List.get(i));// �迭�� ���� �����Ǿ����� �� �ش�Ǵ� ���� �Ǻ�
			co.move(); // �ش� ���� �̵���Ų��.
			if (co.cx < -200) { // ���� ��ǥ�� ȭ�� ������ �Ѿ��
				Coin_List.remove(i); // �ش� ���� �迭���� ����
			}

			// ������ �浹�� Die�޼ҵ�
			if (Die(x, y, co.cx, co.cy, Player_img, Coin_img)) {
				// player_Life += 1; // �÷��̾��� ü�� 1�� ��´�.
				Coin_List.remove(i); // �ε��� ���� �����մϴ�.
				game_Score += 1000;
				ex = new Explosion(co.cx + Coin_img.getWidth(null) / 2, co.cy
						+ Coin_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				ex = new Explosion(x, y, 1);
				Explosion_List.add(ex);
				// Enemy_Die += 1;
			}
		}

		if (cnt %800 == 0) { // �� �����ö� x��ǥ, y��ǥ

			co = new Coin(700, 0);
			Coin_List.add(co);

		}
	}

	// ------------------------------------------------------------------�̻��� Ŭ����

	class Missile {
		int mx, my, angle;
		int speed; // �̻��� ���ǵ� ����(�Ű��ޱ�)
		int who;// player,boss �̻��� ����

		public Missile(int x, int y, int angle, int speed, int who) {

			this.mx = x;
			this.my = y;
			this.angle = angle;
			this.speed = speed; // ��ü������ �ӵ����� �߰��� �޴´�.
			this.who = who;
		}

		public void move() {
			// my -= speed;
			if (ms.who == 0) {
				mx += Math.cos(Math.toRadians(angle)) * speed;
				// �ش� �������� �̻��� �߻�.
				my += Math.sin(Math.toRadians(angle)) * speed;
				// �ش� �������� �̻��� �߻�.
			}
			if (ms.who == 1) {
				mx -= Math.cos(Math.toRadians(angle)) * speed;
				// �ش� �������� �̻��� �߻�.
				my -= Math.sin(Math.toRadians(angle)) * speed;
				// �ش� �������� �̻��� �߻�.
			}
			if (ms.who == 2) {
				mx -= Math.cos(Math.toRadians(angle)) * speed;
				// �ش� �������� �̻��� �߻�.
				my -= Math.sin(Math.toRadians(angle)) * speed;
				// �ش� �������� �̻��� �߻�.
			}
		}
	}

	// ------------------------------------------------------------------���� Ŭ����
	class Boss {
		int x, y;

		Boss(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void move() {
			y += 0;
		}
	}

	// ------------------------------------------------------------------������ Ŭ����
	class Item { // �� ��ġ �ľ� �� �̵��� ���� Ŭ����
		int ix;
		int iy;

		Item(int x, int y) { // ����ǥ�� �޾� ��üȭ ��Ű�� ���� �޼ҵ�
			this.ix = x;
			this.iy = y;
		}

		public void move() { // x��ǥ -3 ��ŭ �̵� ��Ű�� ��� �޼ҵ�
			// ix = (int)(Math.random() * 20 - 5);
			iy += 5; // x�� ���η� �����̰� y�� ���η� ������

		}
	}

	// ------------------------------------------------------------------������ Ŭ����
	class Coin { // �� ��ġ �ľ� �� �̵��� ���� Ŭ����
		int cx;
		int cy;

		Coin(int x, int y) { // ����ǥ�� �޾� ��üȭ ��Ű�� ���� �޼ҵ�
			this.cx = x;
			this.cy = y;
		}

		public void move() { // x��ǥ -3 ��ŭ �̵� ��Ű�� ��� �޼ҵ�
			// ix = (int)(Math.random() * 20 - 5);
			cy += 5; // x�� ���η� �����̰� y�� ���η� ������

		}
	}

	// ------------------------------------------------------------------��ũŬ����

	class MEnemy {
		int x;
		int y;

		MEnemy(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void move() {

			y -= (int) (Math.random() * 5 - 1);

		}
	}

	// ------------------------------------------------------------------�� Ŭ����

	class Enemy { // �� ��ġ �ľ� �� �̵��� ���� Ŭ����
		int ex;
		int ey;

		Enemy(int x, int y) { // ����ǥ�� �޾� ��üȭ ��Ű�� ���� �޼ҵ�
			this.ex = x;
			this.ey = y;
		}

		public void move() { // x��ǥ -3 ��ŭ �̵� ��Ű�� ��� �޼ҵ�
			ey += (int) (Math.random() * 20 - 5); // x�� ���η� �����̰� y�� ���η� ������
			game_Length += 0.01;
			game_Score += 0.01;
		}
	}

	// ------------------------------------------------------------------���� Ŭ����
	class Explosion {
		// �������� ���� �̹����� �׸������� Ŭ������ �߰��Ͽ� ��ü����

		int x; // �̹����� �׸� x ��ǥ
		int y; // �̹����� �׸� y ��ǥ
		int damage = 0; // �̹��� ������ �����ϱ� ���� ������
		int ex_cnt;

		Explosion(int x, int y, int damage) {
			this.x = x;
			this.y = y;
			this.damage = damage;

		}

		public void effect() {
			ex_cnt++;
		}
	}

	// ------------------------------------------------------------------Ű���� �޼ҵ�

	public void KeyProcess() {
		if (KeyUp == true) {
			if (y > 20)
				y -= 5;
			// ĳ���Ͱ� �������� ȭ�� ���� �� �Ѿ�� �մϴ�.

			player_Status = 0;
			// �̵�Ű�� �������� �÷��̾� ���¸� 0���� �����ϴ�.
		}

		if (KeyDown == true) {
			if (y + Player_img.getHeight(null) < f_height)
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
			if (x + Player_img.getWidth(null) < f_width)
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

	public void keyReleased(KeyEvent e) { // �ȴ�������
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
	 * public void KeyProcess() { // Ű ������ ���� if (KeyUp == true) y -= 5; if
	 * (KeyDown == true) y += 5; if (KeyLeft == true) x -= 5; if (KeyRight ==
	 * true) x += 5; }
	 */

}// class game_f ��
