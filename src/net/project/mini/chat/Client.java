package net.project.mini.chat;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import net.project.mini.game.Game;
import net.project.mini.game.Login;
import net.project.mini.game.Score;
import net_p.Chat_frame;
import db.GameDAO;
import db.GameVo;

public class Client implements ActionListener {

	// TextArea textArea; /*tf_user;*/
	TextField textField;
	Socket socket;
	BufferedReader br;
	PrintWriter printWriter;
	String str;
	int num;
	String Client_Chat;
	JFrame frame;
	Label label;
	Label select;
	Panel panel;

	JButton characterBt1;
	JButton characterBt2;
	JButton characterBt3;
	JButton bye; // 종료버튼
	JButton score; // 스코어
	JButton chatting;// 채팅
	JButton logout; // 로그아웃

	ImageIcon characterImg1, characterImg2, characterImg3;

	ArrayList<GameVo> al;
	GameVo vob;
	GameDAO gd;
	Chat_frame cf;

	/*
	 * public void Client() { new Client(); }
	 */
	public Client(GameVo vo) {
		vob = vo;
		gd = new GameDAO();

		al = gd.Select_c();
		/*
		 * try { Chat_frame cf = new Chat_frame(vo); } catch (Exception e1) { //
		 * TODO Auto-generated catch block e1.printStackTrace(); }
		 */
		characterImg1 = new ImageIcon("img/1.png");
		characterImg2 = new ImageIcon("img/2.png");
		characterImg3 = new ImageIcon("img/3.png");

		bye = new JButton("종료");
		score = new JButton("스코어");
		chatting = new JButton("채팅");// 채팅
		logout = new JButton("로그아웃");// 로그아웃

		frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		// textArea = new TextArea(15, 40);
		// tf_user = new TextArea(15, 40);
		textField = new TextField(50);
		label = new Label("  Game  Fly Monster  ");
		select = new Label(" Choice Game Character ");

		label.setFont(new Font("Serif", Font.BOLD, 30));
		select.setFont(new Font("Serif", Font.BOLD, 15));
		panel = new Panel();
		characterBt1 = new JButton(characterImg1);
		characterBt2 = new JButton(characterImg2);
		characterBt3 = new JButton(characterImg3);

		frame.setBounds(240, 200, 310, 360);
		label.setBounds(10, 20, 300, 50);
		textField.setBounds(0, 450, 300, 30);
		// textArea.setBounds(0, 300, 300, 150);
		// tf_user.setBounds(0, 50, 150, 100);

		bye.setBounds(160, 125, 100, 40);
		score.setBounds(160, 75, 100, 40);
		chatting.setBounds(40, 75, 100, 40);
		logout.setBounds(40, 125, 100, 40);
		select.setBounds(20, 170, 200, 30);
		characterBt1.setBounds(0, 200, 100, 130);
		characterBt2.setBounds(100, 200, 100, 130);
		characterBt3.setBounds(200, 200, 100, 130);

		panel.add(textField);
		frame.add(BorderLayout.SOUTH, panel);
		frame.add(label);
		// frame.add(textArea);
		// frame.add(tf_user);
		frame.add(textField);
		frame.add(characterBt1);
		frame.add(characterBt2);
		frame.add(characterBt3);
		frame.add(bye);
		frame.add(score);
		frame.add(chatting);
		frame.add(logout);
		frame.add(select);

		textField.addActionListener(this);
		textField.requestFocus();

		frame.setVisible(true);

		characterBt1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Game("img/1.png");
				// frame.setv
			}
		});

		characterBt2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Game("img/2.png");
			}
		});
		characterBt3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Game("img/3.png");
			}
		});

		score.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Score();
			}
		});

		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println(vob.getId());	//접속자아이디
				gd.bye(vob.getId()); // 접속아이디를 제거
				frame.setVisible(false);	//대기창 끄고
				frame.dispose();
				if (vob.getId() == null) { // 제거했으니까 널일꺼야
					cf.setVisible(false); // 채팅창이 켜져있음 꺼줘
					cf.dispose();
				}
				new Login();		//로그인창 틀꺼야

			}
		});

		bye.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println(vob.getId());	//접속자아이디
				gd.bye(vob.getId());	  // 접속아이디를 제거
				frame.setVisible(false);	//대기창 끄고
				frame.dispose();
				vob = gd.samsam(vob.getId());
				if (vob.getId() == null) {// 제거했으니까 널일꺼야
					System.exit(0);	//대기창이고 로그인이고 그냥 다꺼
				}else{
					cf.setVisible(false); // 채팅창이 켜져있음 꺼줘
					cf.dispose();
					System.exit(0);	//대기창이고 로그인이고 그냥 다꺼
					//난 서버니까 안끌꺼다.
				}

			}
		});

		chatting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					cf = new Chat_frame(vob);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	public static void main(String[] args) {

	}

}
