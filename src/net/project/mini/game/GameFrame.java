package net.project.mini.game;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import net.project.mini.game.object.Boss;
import net.project.mini.game.object.Coin;
import net.project.mini.game.object.Enemy;
import net.project.mini.game.object.Explosion;
import net.project.mini.game.object.Item;
import net.project.mini.game.object.MEnemy;
import net.project.mini.game.object.Missile;

// ------------------------------------------------------------------

public class GameFrame extends JFrame implements KeyListener, Runnable {
	private static final long serialVersionUID = -3418149196259509684L;
	
	int f_width; // 프레임가로
	int f_height; // 프레임세로
	int playerX, playerY; // 플레이어 좌표
	// int p_w, p_h, m_w, m_h, e_w, e_h; // 플레이어,미사일,적 가로세로
	int num_img;

	int game_Score = 0;// 게임 스코어 초기화
	int gameDistance = 0;// 게임 거리 초기화

	int fire_Speed; // 미사일 연사속도 조절 변수
	int missile_Speed; // 미사일 날라가는 속도 조절 변수
	int back_speed;

	boolean playerIsDied = false;
	int player_Status = 0; // 유저 캐릭터 상태 체크변수 0:평상시, 1:미사일발사, 2:충돌.
	int playerLife;// 플레이어 캐릭터의 체력
	int boss_Life = 0;// 보스의 체력
	int enemyKillCount;// 쫄따구 죽인 수
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

	Image playerImg;// 플레이어 이미지를 받아들일 이미지 변수
	Image missileImg;// 미사일 이미지를 받아들일 이미지 변수
	Image missileBossImg; // 보스미사일
	Image Missile_MEnemy_img;// 헐크미사일
	Image enemyImg; // 적 이미지를 받아들일 이미지 변수
	Image itemImg; // 아이템 이미지를 받아들일 이미지 변수**1
	Image Coin_img; // 아이템 이미지를 받아들일 이미지 변수**1
	Image MEnemy_img;// 헐크소환
	Image bossImg;

	Image buffImage;
	Graphics buffg;

	// 미완
	Image BackGround_img1; // 배경화면 이미지
	Image BackGround_img2; // 배경화면 이미지
	Image fire_img; // 폭발이펙트 이미지
	Image[] P_img; // 플레이어 애니메이션 표현을 위해 이미지를 배열로 받음

//	private Missile missile; // 미사일 클래스 접근 키
//	Enemy enemy; // 에너미 클래스 접근 키
//	Item item; // 아이템 클래스 접근 키
//	Coin coin;
//	MEnemy mEnemy;
//	Explosion explosion; // 폭발 이펙트용 클래스 접근 키
//	Boss boss; // 보스 클래스 접근 키
	Toolkit toolkit = Toolkit.getDefaultToolkit();

	Dimension screen = toolkit.getScreenSize(); // 스크린 사이즈 가져오기
	int f_xpos = (int) (screen.getWidth() / 2 - f_width / 2);
	int f_ypos = (int) (screen.getHeight() / 2 - f_height / 2);
	// 중앙에 프레임을 배치하기 위해 정의

	/*
	 * ArrayList Missile_Player_List = new ArrayList(); // player 미사일들을 담기위한 배열
	 * ArrayList Missile_Boss_List = new ArrayList(); // 보스 미사일들을 담기위한 배열
	 * ArrayList Missile_MEnemy_List = new ArrayList(); // 헐크 미사일들을 담기위한 배열
	 */
	List<Missile> missileList = new ArrayList<Missile>();
	// 다수의 미사일 담을 배열
	List<Explosion> explosionList = new ArrayList<Explosion>();
	// 다수의 폭발 이펙트를 처리하기 위한 배열
	List<Enemy> enemyList = new ArrayList<Enemy>();
	List<Item> itemList = new ArrayList<Item>();
	// 다수의 아이템을 담기위한 배열
	List<Coin> coinList = new ArrayList<Coin>();
	// 다수의 아이템을 담기위한 배열
	// 다수의 적을 담기위한 배열
	List<MEnemy> mEnemyList = new ArrayList<MEnemy>();
	// 다수의 적을 담기위한 배열
	Boss boss;

	Thread th;
	
	Random rand = new Random(System.currentTimeMillis());

	// ------------------------------------------------------------------

	public GameFrame(String imagePath) {
		playerImg = toolkit.getImage(imagePath);// 선택한 캐릭터와 저장할캐릭터배열번호를 받아온다.
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
		int delayCount = 200;
		try {
			while (roof) {
				if(playerLife <= 0) {
					delayCount--;
					if(!playerIsDied) {
						playerIsDied = true;
						playerImg = null;
					}
				}
				
				if(delayCount == 0) {
					roof = false;
					setVisible(false);
					th.interrupt();
					new Score();
					break;
				}
				
				pretime = System.currentTimeMillis();
				
				if (!playerIsDied) {
					KeyProcess(); // 키 움직임 처리 메소드 실행
					ExplosionProcess();// 폭파처리 메소드 실행
				}
				MissileProcess(); // 총알 움직임 처리 메소드 실행
				EnemyProcess(); // 적 움직임 처리 메소드 실행
				ItemProcess(); // 아이템 움직임 처리 메소드 실행
				CoinProcess(); // 코인 움직임 처리 메소드 실행
				
				MEnemyProcess();// 헐크 움직임 처리 메소드 실행

				bossProcess();// 보스 움직임 처리 메소드 실행

				repaint();

				Thread.sleep(10); // 스레드 10초설정

				if (System.currentTimeMillis() - pretime < delay)
					Thread.sleep(delay - System.currentTimeMillis() + pretime);
				// 게임 루프를 처리하는데 걸린 시간을 체크해서 딜레이값에서 차감하여 딜레이를 일정하게 유지한다.
				// 루프 실행 시간이 딜레이 시간보다 크다면 게임 속도가 느려지게 된다.
				cnt++;
				if (cnt > 0 && cnt % 10 == 0) {
					gameDistance += 1; 
				}

			}
		} catch (InterruptedException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------

	public void init() {
		playerX = 525; // player 시작위치 - 가로
		playerY = 930; // player 시작위치 - 세로
		f_width = 1050; // 프레임 가로
		f_height = 1050; // 프레임 세로

		delay = 10; // 10/1000초 = 100 (프레임/초)
		game_Score = 0; // 최초 게임스코어 초기화
		playerLife = 4; // 최초 플레이어 체력 주기
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

		enemyImg = new ImageIcon("img/Enemy1.png").getImage();
		itemImg = new ImageIcon("img/heart.png").getImage();
		Coin_img = new ImageIcon("img/coin.png").getImage();
		MEnemy_img = new ImageIcon("img/M_Enemy.png").getImage();
		missileImg = new ImageIcon("img/Missile1.png").getImage();
		missileBossImg = new ImageIcon("img/asd.png").getImage();
		Missile_MEnemy_img = new ImageIcon("img/Missile_ME.png").getImage();
		bossImg = new ImageIcon("img/king.png").getImage();
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
		buffg.drawString("SCORE : " + game_Score, 850, 70);
		// 좌표 x : 350, y : 70에 스코어를 표시한다.
		buffg.drawString("Distance : " + gameDistance, 853, 90);
		// 좌표 x : 350, y : 90에 거리를 표시한다.
		buffg.drawString("All Kills  : " + enemyKillCount, 850, 110);

		String[] heart = { "", "♥", "♥♥", "♥♥♥", "♥♥♥♥" };
		if (playerLife > 4) {
			playerLife = 4;
		}
		buffg.drawString(" My hearts : " + heart[playerLife <= 0 ? 0 : playerLife], 850, 130);
		// 좌표 x : 350, y : 110에 플레이어 체력을 표시한다.
	}

	// 플레이어 이미지 그리기
	public void Draw_Player() {
		switch (player_Status) {
			case 0: // 평상시
				if ((cnt / 5 % 2) == 0) {
					buffg.drawImage(playerImg, playerX, playerY, this);
				} else {
					buffg.drawImage(playerImg, playerX, playerY, this);
				}
				// 엔진쪽에서 불을 뿜는 이미지를 번갈아 그려준다.
				break;
	
			case 1: // 미사일발사
				if ((cnt / 5 % 2) == 0) {
					buffg.drawImage(playerImg, playerX, playerY, this);
				} else {
					buffg.drawImage(playerImg, playerX, playerY, this);
				}
				player_Status = 0;
				break;
			case 2: // 충돌
				break;

		}

	}

	// 미사일 이미지 & 적 die 이미지
	public void Draw_Missile() {
		for (int i = 0; i < missileList.size(); i++) {
			Missile missile = missileList.get(i);
			
			if (missile.getWho() == 0)
				buffg.drawImage(missileImg, missile.getX(), missile.getY(), this);
			if (missile.getWho() == 1)
				buffg.drawImage(missileBossImg, missile.getX(), missile.getY(), this);
			if (missile.getWho() == 2)
				buffg.drawImage(Missile_MEnemy_img, missile.getX(), missile.getY(), this);
		}
	}

	// 적 이미지를 그리는 부분
	public void Draw_Enemy() {
		for (int i = 0; i < enemyList.size(); ++i) {
			Enemy enemy = enemyList.get(i);
			buffg.drawImage(enemyImg, enemy.getX(), enemy.getY(), this);
			// 배열에 생성된 각 적을 판별하여 이미지 그리기
		}
	}

	// 아이템 이미지를 그리는 부분
	public void Draw_Item() {
		for (int i = 0; i < itemList.size(); ++i) {
			Item item = itemList.get(i);
			buffg.drawImage(itemImg, item.getX(), item.getY(), this);
			// 배열에 생성된 각 적을 판별하여 이미지 그리기
		}
	}

	// 아이템 이미지를 그리는 부분
	public void Draw_Coin() {
		for (int i = 0; i < coinList.size(); ++i) {
			Coin coin = coinList.get(i);
			buffg.drawImage(Coin_img, coin.getX(), coin.getY(), this);
			// 배열에 생성된 각 적을 판별하여 이미지 그리기
		}
	}

	// 헐크를 그리는 부분
	public void Draw_MEnemy() {
		for (int i = 0; i < mEnemyList.size(); ++i) {
			MEnemy mEnemy = mEnemyList.get(i);
			buffg.drawImage(MEnemy_img, mEnemy.getX(), mEnemy.getY(), this);
		}
	}

	// 적 보스를 그리는 부분
	public void Draw_Boss() {
		if (boss != null) { 
			buffg.drawImage(bossImg, boss.getX(), boss.getY(), this);
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
		for (int i = 0; i < explosionList.size(); ++i) {
			Explosion explosion = explosionList.get(i);
			if (explosion.getDamage() == 0) {
				buffg.drawImage(fire_img, explosion.getX() - fire_img.getWidth(null) / 2, explosion.getY() - fire_img.getHeight(null) / 2, this);
				explosionList.remove(i);
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
	}

	// ------------------------------------------------------------------실행
	public void Draw_Char() {
		buffg.clearRect(0, 0, f_width, f_height);
		buffg.drawImage(playerImg, playerX, playerY, this);
	}

	public void start() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 종료
		addKeyListener(this); // 키 리스너
		th = new Thread(this); // 스레드 생성
		th.start(); // 스레드 실행
	}

	// ------------------------------------------------------------------die 메소드

	public boolean Die(int x1, int y1, int x2, int y2, Image img1, Image img2) {

		boolean check = false;

		if (Math.abs((x1 + img1.getWidth(null) / 2) - (x2 + img2.getWidth(null) / 2)) < (img2.getWidth(null) / 2 + img1
				.getWidth(null) / 2) && Math.abs((y1 + img1.getHeight(null) / 2)
				- (y2 + img2.getHeight(null) / 2)) < (img2.getHeight(null) / 2 + img1.getHeight(null) / 2)) {
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
		for (int i = 0; i < explosionList.size(); ++i) {
			Explosion explosion = explosionList.get(i);
			explosion.effect();
			// 이펙트 애니메이션을 나타내기위해
			// 이펙트 처리 추가가 발생하면 해당 메소드를 호출.
		}
	}

	// ------------------------------------------------------------------미사일처리메소드

	public void MissileProcess() {

		if (!playerIsDied && KeySpace) {
			player_Status = 1;
			if ((cnt % fire_Speed) == 0) {
				if (playerLife > 2) {
					// 직선미사일
					missileList.add(new Missile(playerX + 20, playerY + 10, 270, missile_Speed, 0));
				} else if (playerLife == 2) {
					// 직선미사일
					missileList.add(new Missile(playerX + 20, playerY + 10, 270, missile_Speed, 0));

					// 왼쪽(강) 대각선으로 날라갈 미사일입니다.
					missileList.add(new Missile(playerX + 20, playerY + 10, 240, missile_Speed, 0));

					// 오른쪽(강) 대각선으로 날라갈 미사일입니다.
					missileList.add(new Missile(playerX + 20, playerY + 10, 300, missile_Speed, 0));

				} else if (playerLife == 1) {
					// 직선미사일
					missileList.add(new Missile(playerX + 20, playerY + 10, 270, missile_Speed, 0));

					// 왼쪽(강) 대각선으로 날라갈 미사일입니다.
					missileList.add(new Missile(playerX + 20, playerY + 10, 210, missile_Speed, 0));

					// 오른쪽(강) 대각선으로 날라갈 미사일입니다.
					missileList.add(new Missile(playerX + 20, playerY + 10, 330, missile_Speed, 0));

					// 왼쪽(약) 대각선으로 날라갈 미사일입니다.
					missileList.add(new Missile(playerX + 20, playerY + 10, 240, missile_Speed, 0));

					// 오른쪽(약) 대각선으로 날라갈 미사일입니다.
					missileList.add(new Missile(playerX + 20, playerY + 10, 300, missile_Speed, 0));
				}
			}
		}

		for (int i = 0; i < missileList.size(); ++i) {
			Missile missile = missileList.get(i);
			missile.move();
			
			if (!playerIsDied && Die(playerX, playerY, missile.getX(), missile.getY(), playerImg, missileBossImg) && missile.getWho() == 1) {
				playerLife -= 1;
				explosionList.add( new Explosion(playerX + playerImg.getWidth(null) / 2, playerY + playerImg.getHeight(null) / 2, 0));
				missileList.remove(i);
			}
			
			if (!playerIsDied && Die(playerX, playerY, missile.getX(), missile.getY(), playerImg, Missile_MEnemy_img) && missile.getWho() == 2) {
				playerLife -= 1;
				explosionList.add( new Explosion(playerX + playerImg.getWidth(null) / 2, playerY + playerImg.getHeight(null) / 2, 0));
				missileList.remove(i);
			}

			for (int j = 0; j < enemyList.size(); ++j) {
				Enemy enemy = enemyList.get(j);

				// 적이 미사일에 맞아 죽을 경우
				if (Die(missile.getX(), missile.getY(), enemy.getX(), enemy.getY(), missileImg, enemyImg) && missile.getWho() == 0) {
					missileList.remove(i); // 적을 죽였을시 미사일 제거
					enemyList.remove(j); // 적을 죽였을시 적 제거
					game_Score += 10; // 적을 죽였을시 추가점수
					enemyKillCount += 1;
					explosionList.add(new Explosion(enemy.getX() + enemyImg.getWidth(null) / 2, enemy.getY() + enemyImg.getHeight(null) / 2, 0));
					// 적이 위치해있는 곳의 중심 좌표 x,y 값과
					// 폭발 설정을 받은 값 ( 0 또는 1 )을 받습니다.
					// 폭발 설정 값 - 0 : 폭발 , 1 : 단순 피격
					// 충돌판정으로 사라진 적의 위치에 이펙트를 추가한다.
				}
			}

			// 중간적 미사일에 맞아 죽을 경우
			for (int k = 0; k < mEnemyList.size(); ++k) {
				MEnemy mEnemy = mEnemyList.get(k);

				if (Die(missile.getX(), missile.getY(), mEnemy.getX(), mEnemy.getY(), missileImg, MEnemy_img) && missile.getWho() == 0) {
					missileList.remove(i);
					game_Score += 100;
					explosionList.add(new Explosion(mEnemy.getX() + MEnemy_img.getWidth(null) / 2, mEnemy.getY() + MEnemy_img.getHeight(null) / 2, 0));

					if (mEnemy.getLife() > 0) {
						mEnemy.decreaseLife();
					} else {
						mEnemyList.remove(k);
					}
				}
			}
			
			if(boss != null) {
				// 보스가 미사일에 맞아 죽을 경우
				if (Die(missile.getX(), missile.getY(), boss.getX(), boss.getY(), missileImg, bossImg) && missile.getWho() == 0) {
					missileList.remove(i);
					game_Score += 100;
					explosionList.add(new Explosion(boss.getX() + bossImg.getWidth(null) / 2, boss.getY() + bossImg.getHeight(null) / 2, 0));
					
					if (boss.getLife() > 0) {
						boss.decreaseLife();
					} else {
						boss = null;
					}
				}
			}
			
			if (missile.getX() > f_width - 20 || missile.getY() < 0 || missile.getY() > f_height) {
				missileList.remove(i);
			}
		}
	}

	// ------------------------------------------------------------------보스처리
	// 메소드
	public void bossProcess() {
		if (boss != null) {
			boss.move();
			if (cnt % 15 == 0) {
				// 보스미사일뿌리는 부분.
				missileList.add(new Missile(boss.getX(), boss.getY(), (int) (Math.random() * 120) + 210, missile_Speed, 1));
			}
			
			// 보스랑 부딪치면
			if (!playerIsDied && Die(playerX, playerY, boss.getX(), boss.getY(), playerImg, bossImg)) {
				game_Score += 1000;
				explosionList.add(new Explosion(boss.getX() + bossImg.getWidth(null) / 2, boss.getY() + bossImg.getHeight(null) / 2, 0));
				explosionList.add(new Explosion(playerX, playerY, 1));
				playerLife = 0; // 플레이어 die
			}
		}

		if (enemyKillCount > 0 && enemyKillCount == 150) {
			boss = new Boss(500, 100);
		}
	}

	public void MEnemyProcess() {
		for (int i = 0; i < mEnemyList.size(); ++i) {
			MEnemy mEnemy = mEnemyList.get(i);// 배열에 적이 생성되어있을 때 해당되는 적을 판별
			mEnemy.move(); // 해당 적을 이동시킨다.
			if (mEnemy.getX() < -200) { // 적의 좌표가 화면 밖으로 넘어가면
				mEnemyList.remove(i); // 해당 적을 배열에서 삭제
			}

			if (cnt % 100 == 0) {
				// 헐크 미사일뿌리는 부분.
				missileList.add(new Missile(mEnemy.getX(), mEnemy.getY(), (int) (Math.random() * 120) + 210, missile_Speed, 2));
			}
			// 적과의 충돌시 Die메소드
			if (!playerIsDied && Die(playerX, playerY, mEnemy.getX(), mEnemy.getY(), playerImg, MEnemy_img)) {
				mEnemyList.remove(i); // 부딪힌 적을 제거합니다.
				playerLife -= 1;
				explosionList.add(new Explosion(mEnemy.getX() + MEnemy_img.getWidth(null) / 2, mEnemy.getY() + MEnemy_img.getHeight(null) / 2, 0));
				explosionList.add(new Explosion(playerX, playerY, 1));
			}
		}
		if (enemyKillCount > 50 && cnt % 200 == 0) { // 헐크 내려올때 x좌표, y좌표
//		if (cnt % 200 == 0) { // 헐크 내려올때 x좌표, y좌표
			mEnemyList.add(new MEnemy(50, 400));
			mEnemyList.add(new MEnemy(250, 400));
			mEnemyList.add(new MEnemy(450, 400));
			mEnemyList.add(new MEnemy(650, 400));
			mEnemyList.add(new MEnemy(850, 400));
		}
	}

	// ------------------------------------------------------------------적처리 메소드

	public void EnemyProcess() {// 적 행동 처리 메소드
		for (int i = 0; i < enemyList.size(); ++i) {
			Enemy enemy = enemyList.get(i);// 배열에 적이 생성되어있을 때 해당되는 적을 판별
			enemy.move(); // 해당 적을 이동시킨다.
			if (enemy.getX() < -200) { // 적의 좌표가 화면 밖으로 넘어가면
				enemyList.remove(i); // 해당 적을 배열에서 삭제
			}

			// 적과의 충돌시 Die메소드
			if (!playerIsDied && Die(playerX, playerY, enemy.getX(), enemy.getY(), playerImg, enemyImg)) {
				playerLife -= 1; // 플레이어의 체력 1을 깍는다.
				enemyList.remove(i); // 부딪힌 적을 제거합니다.
				game_Score += 10;
				explosionList.add(new Explosion(enemy.getX() + enemyImg.getWidth(null) / 2, enemy.getY() + enemyImg.getHeight(null) / 2, 0));
				explosionList.add(new Explosion(playerX, playerY, 1));
				enemyKillCount += 1;
			}
		}

		if (cnt % 200 == 0) { // 적 내려올때 x좌표, y좌표
			
			int[] enemyPositionArray = new int[]{60, 140, 220, 300, 380, 460, 540, 620, 700, 780, 860};
			int[] memo = new int[11];
			
			int enemyRegenCount = rand.nextInt(6)+5; 
			
			for (int i=0; i < enemyRegenCount; i++){
				int tempIndex = rand.nextInt(11);
				for(int j = 0; j < i; j++){
					if (memo[j] == tempIndex) {
						tempIndex = rand.nextInt(11);
						j=-1;
					}
				}
				memo[i] = tempIndex;
				enemyList.add(new Enemy(enemyPositionArray[memo[i]], 0));
			}
		}
	}

	// 아이템 처리 메소드
	public void ItemProcess() {// 적 행동 처리 메소드
		for (int i = 0; i < itemList.size(); ++i) {
			Item item = itemList.get(i);// 배열에 적이 생성되어있을 때 해당되는 적을 판별
			item.move(); // 해당 적을 이동시킨다.

			// 적과의 충돌시 Die메소드
			if (!playerIsDied && Die(playerX, playerY, item.getX(), item.getY(), playerImg, itemImg)) {
				playerLife += 1; // 플레이어의 체력 1을 깍는다.
				itemList.remove(i); // 부딪힌 적을 제거합니다.
			}
			
			if (item.getX() < -200) { // 적의 좌표가 화면 밖으로 넘어가면
				itemList.remove(i); // 해당 적을 배열에서 삭제
			}
		}

		if (cnt != 0 && cnt % 1500 == 0) { // 적 내려올때 x좌표, y좌표
			itemList.add(new Item(rand.nextInt(1000), 0));
		}
	}

	// 아이템 처리 메소드
	public void CoinProcess() {// 적 행동 처리 메소드
		for (int i = 0; i < coinList.size(); ++i) {
			Coin coin = coinList.get(i);// 배열에 적이 생성되어있을 때 해당되는 적을 판별
			coin.move(); // 해당 적을 이동시킨다.
			if (coin.getX() < -200) { // 적의 좌표가 화면 밖으로 넘어가면
				coinList.remove(i); // 해당 적을 배열에서 삭제
			}

			// 적과의 충돌시 Die메소드
			if (!playerIsDied && Die(playerX, playerY, coin.getX(), coin.getY(), playerImg, Coin_img)) {
				coinList.remove(i); // 부딪힌 적을 제거합니다.
				game_Score += 100;
			}
		}

		if (cnt != 0 && cnt % 500 == 0) { // 코인 내려올때 x좌표, y좌표
			coinList.add(new Coin(rand.nextInt(1000), 0));
		}
	}
	// ------------------------------------------------------------------키보드 메소드

	public void KeyProcess() {
		if (KeyUp == true) {
			if (playerY > 20)
				playerY -= 5;
			// 캐릭터가 보여지는 화면 위로 못 넘어가게 합니다.

			player_Status = 0;
			// 이동키가 눌려지면 플레이어 상태를 0으로 돌립니다.
		}

		if (KeyDown == true) {
			if (playerY + playerImg.getHeight(null) < f_height)
				playerY += 5;
			// 캐릭터가 보여지는 화면 아래로 못 넘어가게 합니다.

			player_Status = 0;
			// 이동키가 눌려지면 플레이어 상태를 0으로 돌립니다.
		}

		if (KeyLeft == true) {
			if (playerX > 0)
				playerX -= 5;
			// 캐릭터가 보여지는 화면 왼쪽으로 못 넘어가게 합니다.

			player_Status = 0;
			// 이동키가 눌려지면 플레이어 상태를 0으로 돌립니다.
		}

		if (KeyRight == true) {
			if (playerX + playerImg.getWidth(null) < f_width)
				playerX += 5;
			// 캐릭터가 보여지는 화면 오른쪽으로 못 넘어가게 합니다.

			player_Status = 0;
			// 이동키가 눌려지면 플레이어 상태를 0으로 돌립니다.
		}
	}

	@Override
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

	@Override
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

	@Override
	public void keyTyped(KeyEvent e) {
	}

	// ------------------------------------------------------------------미사일 클래스

//	class Missile {
//		int mx, my, angle;
//		int speed; // 미사을 스피드 변수(매개받기)
//		int who;// player,boss 미사일 구분
//
//		public Missile(int x, int y, int angle, int speed, int who) {
//
//			this.mx = x;
//			this.my = y;
//			this.angle = angle;
//			this.speed = speed; // 객체생성시 속도값을 추가로 받는다.
//			this.who = who;
//		}
//
//		public void move() {
//			// my -= speed;
//			if (missile.getWho() == 0) {
//				mx += Math.cos(Math.toRadians(angle)) * speed;
//				// 해당 방향으로 미사일 발사.
//				my += Math.sin(Math.toRadians(angle)) * speed;
//				// 해당 방향으로 미사일 발사.
//			}
//			if (missile.getWho() == 1) {
//				mx -= Math.cos(Math.toRadians(angle)) * speed;
//				// 해당 방향으로 미사일 발사.
//				my -= Math.sin(Math.toRadians(angle)) * speed;
//				// 해당 방향으로 미사일 발사.
//			}
//			if (missile.getWho() == 2) {
//				mx -= Math.cos(Math.toRadians(angle)) * speed;
//				// 해당 방향으로 미사일 발사.
//				my -= Math.sin(Math.toRadians(angle)) * speed;
//				// 해당 방향으로 미사일 발사.
//			}
//		}
//	}

	// ------------------------------------------------------------------보스 클래스
//	class Boss {
//		int x, y;
//
//		Boss(int x, int y) {
//			this.x = x;
//			this.y = y;
//		}
//
//		public void move() {
//			y += 0;
//		}
//	}

	// ------------------------------------------------------------------아이템 클래스
//	class Item { // 적 위치 파악 및 이동을 위한 클래스
//		int ix;
//		int iy;
//
//		Item(int x, int y) { // 적좌표를 받아 객체화 시키기 위한 메소드
//			this.ix = x;
//			this.iy = y;
//		}
//
//		public void move() { // x좌표 -3 만큼 이동 시키는 명령 메소드
//			// ix = (int)(Math.random() * 20 - 5);
//			iy += 5; // x면 가로로 움직이고 y면 세로로 움직임
//
//		}
//	}

	// ------------------------------------------------------------------아이템 클래스
//	class Coin { // 적 위치 파악 및 이동을 위한 클래스
//		int cx;
//		int cy;
//
//		Coin(int x, int y) { // 적좌표를 받아 객체화 시키기 위한 메소드
//			this.cx = x;
//			this.cy = y;
//		}
//
//		public void move() { // x좌표 -3 만큼 이동 시키는 명령 메소드
//			// ix = (int)(Math.random() * 20 - 5);
//			cy += 5; // x면 가로로 움직이고 y면 세로로 움직임
//
//		}
//	}

	// ------------------------------------------------------------------헐크클래스

//	class MEnemy {
//		int x;
//		int y;
//
//		MEnemy(int x, int y) {
//			this.x = x;
//			this.y = y;
//		}
//
//		public void move() {
//
//			y -= (int) (Math.random() * 5 - 1);
//
//		}
//	}

	// ------------------------------------------------------------------적 클래스

//	class Enemy { // 적 위치 파악 및 이동을 위한 클래스
//		int ex;
//		int ey;
//
//		Enemy(int x, int y) { // 적좌표를 받아 객체화 시키기 위한 메소드
//			this.ex = x;
//			this.ey = y;
//		}
//
//		public void move() { // x좌표 -3 만큼 이동 시키는 명령 메소드
//			ey += (int) (Math.random() * 20 - 5); // x면 가로로 움직이고 y면 세로로 움직임
//			game_Length += 0.01;
//			game_Score += 0.01;
//		}
//	}

	// ------------------------------------------------------------------폭파 클래스
//	class Explosion {
//		// 여러개의 폭발 이미지를 그리기위해 클래스를 추가하여 객체관리
//
//		int x; // 이미지를 그릴 x 좌표
//		int y; // 이미지를 그릴 y 좌표
//		int damage = 0; // 이미지 종류를 구분하기 위한 변수값
//		int ex_cnt;
//
//		Explosion(int x, int y, int damage) {
//			this.x = x;
//			this.y = y;
//			this.damage = damage;
//
//		}
//
//		public void effect() {
//			ex_cnt++;
//		}
//	}
}// class game_f 끝
