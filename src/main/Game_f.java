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
	int f_width; // 프레임가로
	int f_height; // 프레임세로
	int x, y; // 플레이어 좌표
	// int p_w, p_h, m_w, m_h, e_w, e_h; // 플레이어,미사일,적 가로세로
	String str;
	int num_img;

	int game_Score = 0;// 게임 스코어 초기화
	double game_Length = 0;// 게임 거리 초기화

	int fire_Speed; // 미사일 연사속도 조절 변수
	int missile_Speed; // 미사일 날라가는 속도 조절 변수
	int back_speed;

	int player_Status = 0; // 유저 캐릭터 상태 체크변수 0:평상시, 1:미사일발사, 2:충돌.
	int player_Life;// 플레이어 캐릭터의 체력
	int boss_Life = 0;// 보스의 체력
	int MEnemy_Life;// 헐크 체력
	int Enemy_Die;// 쫄따구 죽인 수
	int cnt; // 각종 타이밍 조절을 위해 무한 루프를 카운터할 변수
	int delay;// 루프 딜레이. 1/1000초 단위.
	long pretime;// 루프 간격을 조절하기 위한 시간 체크값

	int bg1Y = 0, bg2Y = -1050;// 배경화면 위치
	int[] cy = { 0, 0, 0 }; // 배경 스크롤 속도 제어용 변수
	int by = 0; // 전체 배경 스크롤 용 변수

	// 키세팅 초기화
	boolean KeyUp = false;
	boolean KeyDown = false;
	boolean KeyLeft = false;
	boolean KeyRight = false;
	boolean KeySpace = false;

	boolean roof = true;// 스레드 루프 정보

	Image Player_img;// 플레이어 이미지를 받아들일 이미지 변수
	Image Missile_img;// 미사일 이미지를 받아들일 이미지 변수
	Image Missile_Boss_img; // 보스미사일
	Image Missile_MEnemy_img;// 헐크미사일
	Image Enemy_img; // 적 이미지를 받아들일 이미지 변수
	Image Item_img; // 아이템 이미지를 받아들일 이미지 변수**1
	Image Coin_img; // 아이템 이미지를 받아들일 이미지 변수**1
	Image MEnemy_img;// 헐크소환
	Image boss_img;

	Image buffImage;
	Graphics buffg;

	// 미완
	Image BackGround_img1; // 배경화면 이미지
	Image BackGround_img2; // 배경화면 이미지
	Image fire_img; // 폭발이펙트 이미지
	Image[] P_img; // 플레이어 애니메이션 표현을 위해 이미지를 배열로 받음

	Missile ms; // 미사일 클래스 접근 키
	Enemy en; // 에너미 클래스 접근 키
	Item it; // 아이템 클래스 접근 키
	Coin co;
	MEnemy men;
	Explosion ex; // 폭발 이펙트용 클래스 접근 키
	Boss bs; // 보스 클래스 접근 키
	Toolkit tk = Toolkit.getDefaultToolkit();

	Dimension screen = tk.getScreenSize(); // 스크린 사이즈 가져오기
	int f_xpos = (int) (screen.getWidth() / 2 - f_width / 2);
	int f_ypos = (int) (screen.getHeight() / 2 - f_height / 2);
	// 중앙에 프레임을 배치하기 위해 정의

	/*
	 * ArrayList Missile_Player_List = new ArrayList(); // player 미사일들을 담기위한 배열
	 * ArrayList Missile_Boss_List = new ArrayList(); // 보스 미사일들을 담기위한 배열
	 * ArrayList Missile_MEnemy_List = new ArrayList(); // 헐크 미사일들을 담기위한 배열
	 */
	ArrayList Missile_List = new ArrayList();
	// 다수의 미사일 담을 배열
	ArrayList Explosion_List = new ArrayList();
	// 다수의 폭발 이펙트를 처리하기 위한 배열
	ArrayList Enemy_List = new ArrayList();
	ArrayList Item_List = new ArrayList();
	// 다수의 아이템을 담기위한 배열
	ArrayList Coin_List = new ArrayList();
	// 다수의 아이템을 담기위한 배열
	// 다수의 적을 담기위한 배열
	ArrayList MEnemy_List = new ArrayList();
	// 다수의 적을 담기위한 배열
	ArrayList Boss_List = new ArrayList();
	// 보스 리스트

	Thread th;

	// ------------------------------------------------------------------

	public Game_f(String str) {
		Player_img = tk.getImage(str);// 선택한 캐릭터와 저장할캐릭터배열번호를 받아온다.
		this.str = str;

		init();
		start();
		setTitle("플라이 몬스터");
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

				KeyProcess(); // 키 움직임 처리 메소드 실행
				ExplosionProcess();// 폭파처리 메소드 실행
				MissileProcess(); // 총알 움직임 처리 메소드 실행
				EnemyProcess(); // 적 움직임 처리 메소드 실행
				ItemProcess(); // 아이템 움직임 처리 메소드 실행
				CoinProcess(); // 코인 움직임 처리 메소드 실행
				MEnemyProcess();// 헐크 움직임 처리 메소드 실행
				bossProcess();// 보스 움직임 처리 메소드 실행

				repaint();

				th.sleep(10); // 스레드 10초설정

				if (System.currentTimeMillis() - pretime < delay)
					th.sleep(delay - System.currentTimeMillis() + pretime);
				// 게임 루프를 처리하는데 걸린 시간을 체크해서 딜레이값에서 차감하여 딜레이를 일정하게 유지한다.
				// 루프 실행 시간이 딜레이 시간보다 크다면 게임 속도가 느려지게 된다.

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
		x = 525; // player 시작위치 - 가로
		y = 1000; // player 시작위치 - 세로
		f_width = 1050; // 프레임 가로
		f_height = 1050; // 프레임 세로

		delay = 10; // 10/1000초 = 100 (프레임/초)
		game_Score = 0; // 최초 게임스코어 초기화
		player_Life = 4; // 최초 플레이어 체력 주기
		MEnemy_Life = 2;
		missile_Speed = 5; // 미사일 움직임 속도 설정
		fire_Speed = 20;// 연사속도
		back_speed = 5;
		// Player_img = tk.getImage("img/Player1.png");
		// Enemy_img = tk.getImage("img/Enemy1.png");// 적 이미지 생성
		// Missile_img = tk.getImage("img/Missile1.png");
		/*
		 * p_w = ImageWidthValue(str); p_h = ImageHeightValue(str);
		 * 
		 * e_w = ImageWidthValue("img/Enemy1.png"); // 적이미지의 가로길이 e_h =
		 * ImageHeightValue("img/Enemy1.png"); // 적 이미지의 세로길이
		 * 
		 * m_w = ImageWidthValue("img/Missile1.png"); m_h =
		 * ImageHeightValue("img/Missile1.png");
		 */

		// 이미지 만드는 방식을 ImageIcon으로 변경했음
		// 이미지 크기 계산 메소드를 삭제하고 Swing에서 지원되는 ImageIcon으로
		// 이미지 크기값을 얻는다.

		Enemy_img = new ImageIcon("img/Enemy1.png").getImage();
		Item_img = new ImageIcon("img/heart.png").getImage();
		Coin_img = new ImageIcon("img/coin.png").getImage();
		MEnemy_img = new ImageIcon("img/M_Enemy.png").getImage();
		Missile_img = new ImageIcon("img/Missile1.png").getImage();
		Missile_Boss_img = new ImageIcon("img/asd.png").getImage();
		Missile_MEnemy_img = new ImageIcon("img/Missile_ME.png").getImage();
		boss_img = new ImageIcon("img/king.png").getImage();
		fire_img = new ImageIcon("img/fire1.png").getImage();
		// 배경이미지 받기
		BackGround_img1 = new ImageIcon("img/ground.png").getImage();
		BackGround_img2 = new ImageIcon("img/ground.png").getImage();
		/*
		 * fire_img = new Image[3]; for (int i = 0; i < fire_img.length; ++i) {
		 * fire_img[i] = new ImageIcon("img/fire" + i + ".png").getImage(); }
		 */
		// 폭파현상
	}

	// ------------------------------------------------------------------
	/*
	 * public int ImageWidthValue(String file) {// 이미지의 가로길이를 구한다. int x = 0;
	 * try { File f = new File(file); BufferedImage bi = ImageIO.read(f); x =
	 * bi.getWidth(); } catch (Exception e) { } return x; }
	 * 
	 * public int ImageHeightValue(String file) {// 이미지의 세로길이를 구한다. int y = 0;
	 * try { File f = new File(file); BufferedImage bi = ImageIO.read(f); y =
	 * bi.getHeight(); } catch (Exception e) { } return y; }
	 */

	// -----------------------------------------------------------------그리기설정

	// 상태 체크용 텍스트를 그린다.
	public void Draw_StatusText() {
		buffg.setFont(new Font("Defualt", Font.BOLD, 20));
		// 폰트 설정을 합니다. 기본폰트, 굵게, 사이즈 20
		buffg.drawString(" S C O R E : " + game_Score, 850, 70);
		// 좌표 x : 350, y : 70에 스코어를 표시한다.
		buffg.drawString("L e n g t h : " + (int) game_Length, 853, 90);
		// 좌표 x : 350, y : 90에 거리를 표시한다.
		buffg.drawString(" All Kills  : " + Enemy_Die, 850, 110);

		String[] heart = { "", "♥", "♥♥", "♥♥♥", "♥♥♥♥" };
		buffg.drawString(" My hearts : " + heart[player_Life], 850, 130);
		// 좌표 x : 350, y : 110에 플레이어 체력을 표시한다.
	}

	// 플레이어 이미지 그리기
	public void Draw_Player() {
		switch (player_Status) {

		case 0: // 평상시
			if ((cnt / 5 % 2) == 0) {
				buffg.drawImage(Player_img, x, y, this);
			} else {
				buffg.drawImage(Player_img, x, y, this);
			}
			// 엔진쪽에서 불을 뿜는 이미지를 번갈아 그려준다.
			break;

		case 1: // 미사일발사
			if ((cnt / 5 % 2) == 0) {
				buffg.drawImage(Player_img, x, y, this);
			} else {
				buffg.drawImage(Player_img, x, y, this);
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

	// 미사일 이미지 & 적 die 이미지
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

	// 적 이미지를 그리는 부분
	public void Draw_Enemy() {
		for (int i = 0; i < Enemy_List.size(); ++i) {
			en = (Enemy) (Enemy_List.get(i));
			buffg.drawImage(Enemy_img, en.ex, en.ey, this);
			// 배열에 생성된 각 적을 판별하여 이미지 그리기
		}
	}

	// 아이템 이미지를 그리는 부분
	public void Draw_Item() {
		for (int i = 0; i < Item_List.size(); ++i) {
			it = (Item) (Item_List.get(i));
			buffg.drawImage(Item_img, it.ix, it.iy, this);
			// 배열에 생성된 각 적을 판별하여 이미지 그리기
		}
	}

	// 아이템 이미지를 그리는 부분
	public void Draw_Coin() {
		for (int i = 0; i < Coin_List.size(); ++i) {
			co = (Coin) (Coin_List.get(i));
			buffg.drawImage(Coin_img, co.cx, co.cy, this);
			// 배열에 생성된 각 적을 판별하여 이미지 그리기
		}
	}

	// 헐크를 그리는 부분
	public void Draw_MEnemy() {

		for (int i = 0; i < MEnemy_List.size(); ++i) {
			men = (MEnemy) (MEnemy_List.get(i));

			buffg.drawImage(MEnemy_img, men.x, men.y, this);
		}
	}

	// 적 보스를 그리는 부분
	public void Draw_Boss() {

		for (int i = 0; i < Boss_List.size(); ++i) {
			bs = (Boss) (Boss_List.get(i));
			buffg.drawImage(boss_img, bs.x, bs.y, this);
		}
	}

	// 배경 이미지를 그리는 부분입니다.
	public void Draw_Background() {
		buffg.clearRect(0, 0, f_width, f_height);
		// 화면 지우기 명령은 이제 여기서 실행합니다.
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
		// 폭발 이펙트를 그리는 부분 입니다.
		for (int i = 0; i < Explosion_List.size(); ++i) {
			ex = (Explosion) Explosion_List.get(i);
			if (ex.damage == 0) {
				buffg.drawImage(fire_img, ex.x - fire_img.getWidth(null) / 2,
						ex.y - fire_img.getHeight(null) / 2, this);
				Explosion_List.remove(i);
			}
		}
	}

	// ------------------------------------------------------------------그리기실행

	public void paint(Graphics g) {

		buffImage = createImage(f_width, f_height);
		buffg = buffImage.getGraphics();
		update(g);

	}

	public void update(Graphics g) {

		if (this.player_Life > 0) {
			Draw_Background();// 배경
			Draw_Player();// 플레이어
			Draw_Missile();// 미사일
			Draw_Enemy();// 적
			Draw_Item(); // 아이템
			Draw_Coin(); // 코인
			Draw_Boss();// 보스
			Draw_MEnemy();// 헐크뛰어
			Draw_StatusText();// 상태표시
			Draw_Explosion();// 폭발
			g.drawImage(buffImage, 0, 0, this);

		} else if (player_Life == 0) {
			setVisible(false);
			th.stop();
			new Score();
		}
	}

	// ------------------------------------------------------------------실행
	public void Draw_Char() {
		buffg.clearRect(0, 0, f_width, f_height);
		buffg.drawImage(Player_img, x, y, this);
	}

	public void start() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 종료
		addKeyListener(this); // 키 리스너
		th = new Thread(this); // 스레드 생성
		th.start(); // 스레드 실행
		if (player_Life == 0) {
			th.stop();
			new Score();
		}
	}

	// ------------------------------------------------------------------die 메소드

	public boolean Die(int x1, int y1, int x2, int y2, Image img1, Image img2) {

		boolean check = false;

		if (Math.abs((x1 + img1.getWidth(null) / 2)
				- (x2 + img2.getWidth(null) / 2)) < (img2.getWidth(null) / 2 + img1
				.getWidth(null) / 2)
				&& Math.abs((y1 + img1.getHeight(null) / 2)
						- (y2 + img2.getHeight(null) / 2)) < (img2
						.getHeight(null) / 2 + img1.getHeight(null) / 2)) {
			// 이미지 넓이, 높이값을 바로 받아 계산합니다.
			check = true;
		} else {
			check = false;
		}

		return check;

	}

	// ------------------------------------------------------------------폭파처리
	// 메소드 실행

	public void ExplosionProcess() {
		// 폭발 이펙트 처리용 메소드
		for (int i = 0; i < Explosion_List.size(); ++i) {
			ex = (Explosion) Explosion_List.get(i);
			ex.effect();
			// 이펙트 애니메이션을 나타내기위해
			// 이펙트 처리 추가가 발생하면 해당 메소드를 호출.
		}
	}

	// ------------------------------------------------------------------미사일처리메소드

	public void MissileProcess() {

		if (KeySpace) {
			player_Status = 1;
			if ((cnt % fire_Speed) == 0) {
				if (player_Life > 2) {
					ms = new Missile(x + 20, y + 10, 270, missile_Speed, 0);
					// 직선미사일
					Missile_List.add(ms);
				} else if (player_Life == 2) {
					ms = new Missile(x + 20, y + 10, 270, missile_Speed, 0);
					// 직선미사일
					Missile_List.add(ms);

					ms = new Missile(x + 20, y + 10, 240, missile_Speed, 0);
					// 왼쪽(강) 대각선으로 날라갈 미사일입니다.
					Missile_List.add(ms);

					ms = new Missile(x + 20, y + 10, 300, missile_Speed, 0);
					// 오른쪽(강) 대각선으로 날라갈 미사일입니다.
					Missile_List.add(ms);

				} else if (player_Life == 1) {
					ms = new Missile(x + 20, y + 10, 270, missile_Speed, 0);
					// 직선미사일
					Missile_List.add(ms);

					ms = new Missile(x + 20, y + 10, 210, missile_Speed, 0);
					// 왼쪽(강) 대각선으로 날라갈 미사일입니다.
					Missile_List.add(ms);

					ms = new Missile(x + 20, y + 10, 330, missile_Speed, 0);
					// 오른쪽(강) 대각선으로 날라갈 미사일입니다.
					Missile_List.add(ms);

					ms = new Missile(x + 20, y + 10, 240, missile_Speed, 0);
					// 왼쪽(약) 대각선으로 날라갈 미사일입니다.
					Missile_List.add(ms);

					ms = new Missile(x + 20, y + 10, 300, missile_Speed, 0);
					// 오른쪽(약) 대각선으로 날라갈 미사일입니다.
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

				// 적이 미사일에 맞아 죽을 경우
				if (Die(ms.mx, ms.my, en.ex, en.ey, Missile_img, Enemy_img)
						&& ms.who == 0) {
					Missile_List.remove(i); // 적을 죽였을시 미사일 제거
					Enemy_List.remove(j); // 적을 죽였을시 적 제거
					game_Score += 10; // 적을 죽였을시 추가점수
					Enemy_Die += 1;
					ex = new Explosion(en.ex + Enemy_img.getWidth(null) / 2,
							en.ey + Enemy_img.getHeight(null) / 2, 0);
					Explosion_List.add(ex);
					// 적이 위치해있는 곳의 중심 좌표 x,y 값과
					// 폭발 설정을 받은 값 ( 0 또는 1 )을 받습니다.
					// 폭발 설정 값 - 0 : 폭발 , 1 : 단순 피격
					// 충돌판정으로 사라진 적의 위치에 이펙트를 추가한다.
				}
			}
			// 보스가 미사일에 맞아 죽을 경우
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

			// 중간적 미사일에 맞아 죽을 경우
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

	// ------------------------------------------------------------------보스처리
	// 메소드
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
				// 보스미사일뿌리는 부분.
				ms = new Missile(bs.x, bs.y, (int) (Math.random() * 120) + 210,
						missile_Speed, 1);
				Missile_List.add(ms);

				// }
			}

			// 보스랑 부딪치면
			if (Die(x, y, bs.x, bs.y, Player_img, boss_img)) {
				game_Score += 1000;
				ex = new Explosion(bs.x + boss_img.getWidth(null) / 2, bs.y
						+ boss_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				ex = new Explosion(x, y, 1);
				Explosion_List.add(ex);

				player_Life = 0; // 플레이어 die

			}
		}

		if (cnt % 500 == 0) {

			bs = new Boss(500, 100);
			Boss_List.add(bs);
		}
	}

	public void MEnemyProcess() {
		for (int i = 0; i < MEnemy_List.size(); ++i) {
			men = (MEnemy) (MEnemy_List.get(i));// 배열에 적이 생성되어있을 때 해당되는 적을 판별
			men.move(); // 해당 적을 이동시킨다.
			if (men.x < -200) { // 적의 좌표가 화면 밖으로 넘어가면
				Enemy_List.remove(i); // 해당 적을 배열에서 삭제
			}

			if (cnt % 100 == 0) {
				// 헐크 미사일뿌리는 부분.
				ms = new Missile(men.x, men.y,
						(int) (Math.random() * 120) + 210, missile_Speed, 2);
				Missile_List.add(ms);

				// }
			}

			// 적과의 충돌시 Die메소드
			if (Die(x, y, en.ex, en.ey, Player_img, MEnemy_img)) {
				MEnemy_List.remove(i); // 부딪힌 적을 제거합니다.
				player_Life -= 1;
				ex = new Explosion(men.x + MEnemy_img.getWidth(null) / 2, men.y
						+ MEnemy_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				ex = new Explosion(x, y, 1);
				Explosion_List.add(ex);
			}
		}
		if (cnt % 200 == 0) { // 헐크 내려올때 x좌표, y좌표

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

	// ------------------------------------------------------------------적처리 메소드

	public void EnemyProcess() {// 적 행동 처리 메소드
		for (int i = 0; i < Enemy_List.size(); ++i) {
			en = (Enemy) (Enemy_List.get(i));// 배열에 적이 생성되어있을 때 해당되는 적을 판별
			en.move(); // 해당 적을 이동시킨다.
			if (en.ex < -200) { // 적의 좌표가 화면 밖으로 넘어가면
				Enemy_List.remove(i); // 해당 적을 배열에서 삭제
			}

			// 적과의 충돌시 Die메소드
			if (Die(x, y, en.ex, en.ey, Player_img, Enemy_img)) {
				player_Life -= 1; // 플레이어의 체력 1을 깍는다.
				Enemy_List.remove(i); // 부딪힌 적을 제거합니다.
				game_Score += 10;
				ex = new Explosion(en.ex + Enemy_img.getWidth(null) / 2, en.ey
						+ Enemy_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				ex = new Explosion(x, y, 1);
				Explosion_List.add(ex);
				Enemy_Die += 1;
			}
		}

		if (cnt % 200 == 0) { // 적 내려올때 x좌표, y좌표

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

	// 아이템 처리 메소드
	public void ItemProcess() {// 적 행동 처리 메소드
		for (int i = 0; i < Item_List.size(); ++i) {
			it = (Item) (Item_List.get(i));// 배열에 적이 생성되어있을 때 해당되는 적을 판별
			it.move(); // 해당 적을 이동시킨다.
			if (it.ix < -200) { // 적의 좌표가 화면 밖으로 넘어가면
				Item_List.remove(i); // 해당 적을 배열에서 삭제
			}

			// 적과의 충돌시 Die메소드
			if (Die(x, y, it.ix, it.iy, Player_img, Item_img)) {
				player_Life += 1; // 플레이어의 체력 1을 깍는다.
				Item_List.remove(i); // 부딪힌 적을 제거합니다.
				// game_Score += 10;
				ex = new Explosion(it.ix + Item_img.getWidth(null) / 2, it.iy
						+ Item_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				ex = new Explosion(x, y, 1);
				Explosion_List.add(ex);
				// Enemy_Die += 1;
			}
		}

		if (cnt % 1500 == 0) { // 적 내려올때 x좌표, y좌표

			it = new Item(540, 0);
			Item_List.add(it);

		}
	}

	// 아이템 처리 메소드
	public void CoinProcess() {// 적 행동 처리 메소드
		for (int i = 0; i < Coin_List.size(); ++i) {
			co = (Coin) (Coin_List.get(i));// 배열에 적이 생성되어있을 때 해당되는 적을 판별
			co.move(); // 해당 적을 이동시킨다.
			if (co.cx < -200) { // 적의 좌표가 화면 밖으로 넘어가면
				Coin_List.remove(i); // 해당 적을 배열에서 삭제
			}

			// 적과의 충돌시 Die메소드
			if (Die(x, y, co.cx, co.cy, Player_img, Coin_img)) {
				// player_Life += 1; // 플레이어의 체력 1을 깍는다.
				Coin_List.remove(i); // 부딪힌 적을 제거합니다.
				game_Score += 1000;
				ex = new Explosion(co.cx + Coin_img.getWidth(null) / 2, co.cy
						+ Coin_img.getHeight(null) / 2, 0);
				Explosion_List.add(ex);
				ex = new Explosion(x, y, 1);
				Explosion_List.add(ex);
				// Enemy_Die += 1;
			}
		}

		if (cnt %800 == 0) { // 적 내려올때 x좌표, y좌표

			co = new Coin(700, 0);
			Coin_List.add(co);

		}
	}

	// ------------------------------------------------------------------미사일 클래스

	class Missile {
		int mx, my, angle;
		int speed; // 미사을 스피드 변수(매개받기)
		int who;// player,boss 미사일 구분

		public Missile(int x, int y, int angle, int speed, int who) {

			this.mx = x;
			this.my = y;
			this.angle = angle;
			this.speed = speed; // 객체생성시 속도값을 추가로 받는다.
			this.who = who;
		}

		public void move() {
			// my -= speed;
			if (ms.who == 0) {
				mx += Math.cos(Math.toRadians(angle)) * speed;
				// 해당 방향으로 미사일 발사.
				my += Math.sin(Math.toRadians(angle)) * speed;
				// 해당 방향으로 미사일 발사.
			}
			if (ms.who == 1) {
				mx -= Math.cos(Math.toRadians(angle)) * speed;
				// 해당 방향으로 미사일 발사.
				my -= Math.sin(Math.toRadians(angle)) * speed;
				// 해당 방향으로 미사일 발사.
			}
			if (ms.who == 2) {
				mx -= Math.cos(Math.toRadians(angle)) * speed;
				// 해당 방향으로 미사일 발사.
				my -= Math.sin(Math.toRadians(angle)) * speed;
				// 해당 방향으로 미사일 발사.
			}
		}
	}

	// ------------------------------------------------------------------보스 클래스
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

	// ------------------------------------------------------------------아이템 클래스
	class Item { // 적 위치 파악 및 이동을 위한 클래스
		int ix;
		int iy;

		Item(int x, int y) { // 적좌표를 받아 객체화 시키기 위한 메소드
			this.ix = x;
			this.iy = y;
		}

		public void move() { // x좌표 -3 만큼 이동 시키는 명령 메소드
			// ix = (int)(Math.random() * 20 - 5);
			iy += 5; // x면 가로로 움직이고 y면 세로로 움직임

		}
	}

	// ------------------------------------------------------------------아이템 클래스
	class Coin { // 적 위치 파악 및 이동을 위한 클래스
		int cx;
		int cy;

		Coin(int x, int y) { // 적좌표를 받아 객체화 시키기 위한 메소드
			this.cx = x;
			this.cy = y;
		}

		public void move() { // x좌표 -3 만큼 이동 시키는 명령 메소드
			// ix = (int)(Math.random() * 20 - 5);
			cy += 5; // x면 가로로 움직이고 y면 세로로 움직임

		}
	}

	// ------------------------------------------------------------------헐크클래스

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

	// ------------------------------------------------------------------적 클래스

	class Enemy { // 적 위치 파악 및 이동을 위한 클래스
		int ex;
		int ey;

		Enemy(int x, int y) { // 적좌표를 받아 객체화 시키기 위한 메소드
			this.ex = x;
			this.ey = y;
		}

		public void move() { // x좌표 -3 만큼 이동 시키는 명령 메소드
			ey += (int) (Math.random() * 20 - 5); // x면 가로로 움직이고 y면 세로로 움직임
			game_Length += 0.01;
			game_Score += 0.01;
		}
	}

	// ------------------------------------------------------------------폭파 클래스
	class Explosion {
		// 여러개의 폭발 이미지를 그리기위해 클래스를 추가하여 객체관리

		int x; // 이미지를 그릴 x 좌표
		int y; // 이미지를 그릴 y 좌표
		int damage = 0; // 이미지 종류를 구분하기 위한 변수값
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

	// ------------------------------------------------------------------키보드 메소드

	public void KeyProcess() {
		if (KeyUp == true) {
			if (y > 20)
				y -= 5;
			// 캐릭터가 보여지는 화면 위로 못 넘어가게 합니다.

			player_Status = 0;
			// 이동키가 눌려지면 플레이어 상태를 0으로 돌립니다.
		}

		if (KeyDown == true) {
			if (y + Player_img.getHeight(null) < f_height)
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
			if (x + Player_img.getWidth(null) < f_width)
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

	public void keyReleased(KeyEvent e) { // 안눌렀을때
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
	 * public void KeyProcess() { // 키 움직임 설정 if (KeyUp == true) y -= 5; if
	 * (KeyDown == true) y += 5; if (KeyLeft == true) x -= 5; if (KeyRight ==
	 * true) x += 5; }
	 */

}// class game_f 끝
