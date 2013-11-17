package main;


import java.awt.Dialog;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

//로그인프레임
public class Logf {
	Logf pp = this;
	TextField tid, tpw;
	JFrame lf;
	JLabel Id_1lb, Pw_1lb;
	JButton btn1, btn2, btn3;

	public Logf() {
		lf = new JFrame("회원가입");
		lf.setBounds(500, 490, 440, 290);
		lf.setLayout(null);

		Id_1lb = new JLabel("ID : ");
		Id_1lb.setBounds(80, 50, 50, 30);
		lf.add(Id_1lb);

		tid = new TextField(10);
		tid.setBounds(150, 50, 200, 30);
		lf.add(tid);

		Pw_1lb = new JLabel("PW : ");
		Pw_1lb.setBounds(80, 80, 50, 30);
		lf.add(Pw_1lb);

		tpw = new TextField(10);
		tpw.setBounds(150, 80, 200, 30);
		tpw.setEchoChar('*');
		lf.add(tpw);

		btn1 = new JButton("로그인");
		btn1.setBounds(70, 120, 80, 50); // 버튼 위치 설정.
		lf.add(btn1);

		btn2 = new JButton("아이디 / 비밀번호찾기");
		btn2.setBounds(170, 120, 180, 50); // 버튼 위치 설정.
		lf.add(btn2);

		btn3 = new JButton("회원가입");
		btn3.setBounds(70, 180, 280, 30);
		lf.add(btn3);

		lf.setVisible(true);

		btn1.addActionListener(new Loginf(pp));
		btn3.addActionListener(new Register(pp));

	}

	// 로그인버튼(btn1)클릭시.
	class Loginf implements ActionListener {

		Logf pp;
		Dialog dd;
		Label la = new Label();
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		String id, pw;
		
		public Loginf(Logf pp) {
			this.pp = pp;
		}

		public void actionPerformed(ActionEvent e) {

			map.put("aaa", new String[] { "11", "가가" });
			map.put("bbb", new String[] { "22", "나나" });
			map.put("ccc", new String[] { "33", "다다" });
			map.put("ddd", new String[] { "44", "라라" });
			map.put("eee", new String[] { "55", "마마" });

			id = pp.tid.getText();
			pw = tpw.getText();
			tpw.setText("");

			dd = new Dialog(pp.lf,"불일치!",true);

			if (!map.containsKey(id)) {
				// System.out.println("존재하지 않는 ID 입니다.");
				dd.setBounds(100, 100, 200, 150);
				la.setText("아이디가 존재하지 않습니다.");
				dd.add(la);

				dd.addWindowListener(new Win_c());
				dd.setVisible(true);

				tid.setText("");
				tid.requestFocus();
			} else if (!map.get(id)[0].equals(pw)) {
				dd.setBounds(100, 100, 200, 150);

				la.setText("암호가 일치하지 않습니다.");
				dd.add(la);

				dd.addWindowListener(new Win_c());
				dd.setVisible(true);
				tpw.requestFocus();

			} else {
				// setVisible(false);
				new Client(pp);
			
		}
	}
	}
	public static void main(String[] args) {

		new Logf();
		// Register rg = new Register();
	}
}