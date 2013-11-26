package net.project.mini.game;

import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.project.mini.chat.Client;
import db.GameDAO;
import db.GameVo;
import db.LogInfo;

public class Login {
	Login pp = this;
	TextField tid, tpw;
	JFrame lf;
	JLabel Id_1lb, Pw_1lb;
	JButton btn1, btn2, btn3;
	Font font;

	public Login() {
		font = new Font("굴림체", Font.BOLD, 15);

		lf = new JFrame(" 로 그 인 ");
		lf.setBounds(500, 490, 400, 290);
		lf.setLayout(null);
		lf.setResizable(false);
		
		Id_1lb = new JLabel("ID : ");
		Id_1lb.setFont(font);
		Id_1lb.setBounds(80, 50, 50, 30);
		lf.add(Id_1lb);

		tid = new TextField(10);
		tid.setBounds(150, 50, 150, 30);
		lf.add(tid);

		Pw_1lb = new JLabel("PW : ");
		Pw_1lb.setFont(font);
		Pw_1lb.setBounds(80, 80, 50, 30);
		lf.add(Pw_1lb);

		tpw = new TextField(10);
		tpw.setBounds(150, 80, 150, 30);
		tpw.setEchoChar('*');
		lf.add(tpw);

		btn1 = new JButton("로그인");
		btn1.setBounds(80, 120, 80, 50);
		lf.add(btn1);

		btn2 = new JButton("회원가입");
		btn2.setBounds(180, 120, 120, 50);
		lf.add(btn2);

		btn3 = new JButton("아이디 & 비밀번호찾기");
		btn3.setBounds(80, 180, 220, 50);
		lf.add(btn3);

		btn1.setFont(font);
		btn2.setFont(font);
		btn3.setFont(font);

		lf.setVisible(true);

		btn1.addActionListener(new Loginf(pp));
		btn2.addActionListener(new Join());
		btn3.addActionListener(new Identity(pp));
		

	}

	class Loginf implements ActionListener {
		GameDAO gd;
		GameVo vo;
		Login pp;
		JDialog dd1, dd2, sam;
		JLabel la = new JLabel();
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		String id, pw;
		GameVo gg;

		public Loginf(Login pp) {
			this.pp = pp;
		}

		public void actionPerformed(ActionEvent e) {

			gd = new GameDAO();

			id = tid.getText();
			pw = tpw.getText();

			vo = gd.ac(id, pw); // 아이디 비번 확인
			gg = gd.samsam(id);// 접속자 아이디와 일치하면 가져온다.

			dd1 = new JDialog();
			dd2 = new JDialog();
			sam = new JDialog();

			LogInfo li = new LogInfo(vo);

			// gg = new GameVo();

			if (vo.getId() != null && vo.getPw() != null) {
				System.out.println("입력한 아이디 : " + vo.getId());
				if (gg.getId() != null) {
					System.out.println("접속중인 아이디 : " + gg.getId());
					sam.setBounds(100, 100, 400, 150);
					la.setText("현재 접속중인 아이디 입니다.");
					sam.add(la);
					sam.setVisible(true);

				} else {	// 아이디 비번 정보가 디비의 정보와 일치하고 접속중인 아이디가 없다면 로그인!
					gd.insertTwo(vo.getId()); // 로그인 테이블에 접속자 아이디 생성
					new Client(vo);
					pp.lf.setVisible(false);//로그인창은 꺼줘야제,.^^

				}

			} else if (id.equals("") || pw.equals("")) {
				dd1.setBounds(100, 100, 400, 150);
				la.setText("아이디와 비밀번호 모두 입력하세요.");
				dd1.add(la);
				dd1.setVisible(true);

			} else {
				dd2.setBounds(100, 100, 400, 150);
				la.setText("아이디 혹은 패스워드가 일치하지 않습니다.");
				dd2.add(la);
				dd2.setVisible(true);
				pp.tid.setText("");
				pp.tpw.setText("");
			}
		}
	}

	public static void main(String[] args) {
		new Login();

		// Register rg = new Register();
	}
}