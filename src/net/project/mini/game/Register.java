package net.project.mini.game;


import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

// 회원가입 프레임
class Register implements ActionListener {
	JFrame frame;
	Font font;
	Label pw, lb, name, id, pw_re, emailLabel_1, email_la, phone;
	TextField pwt, id_t, tf, email_t_1, phone2, phone3, pwt_re, email_tf;
	//이름 : tf, 아이디 : id_t, 비밀번호 : pwt, 비밀번호2 : pwt_re,
    //이메일 : email_t_1, 도메인email_tf, 핸드폰 : phone2, phone3
	CheckboxGroup cg;
	Checkbox mm, ff;
	Button b, b1, b2;
	Choice email, phone1;

	public void actionPerformed(ActionEvent e) {
		frame = new JFrame();
		frame.setBounds(300, 300, 700, 600);
		frame.setLayout(null);

		lb = new Label("회 원 가 입");
		font = new Font("궁서체", Font.BOLD + Font.ITALIC, 20);
		lb.setFont(font);
		lb.setForeground(new Color(255, 0, 0));
		lb.setBounds(30, 40, 800, 40);

		pw_re = new Label("비밀번호확인:");
		name = new Label("이름");
		tf = new TextField();//이름
		cg = new CheckboxGroup();
		mm = new Checkbox("남자", cg, true);
		ff = new Checkbox("여자", cg, false);
		id = new Label("아이디:");
		id_t = new TextField();
		b = new Button("아이디 중복 확인");
		pw = new Label("비밀번호:");
		pwt = new TextField("");
		pwt_re = new TextField("");
		email_la = new Label("Email입력 :");
		email_tf = new TextField();
		emailLabel_1 = new Label("@");
		email_t_1 = new TextField();
		phone = new Label("핸드폰 번호 :");
		phone1 = new Choice();
		phone1.add("010");
		phone1.add("011");
		phone1.add("019");
		phone2 = new TextField();
		phone3 = new TextField();
		b1 = new Button("회원가입");
		email = new Choice();
		email.add("직접 입력");
		email.add("naver.com");
		email.add("daum.net");
		email.add("google.com");
		b2 = new Button("취소");

		name.setBounds(50, 100, 80, 50);
		tf.setBounds(140, 110, 80, 20);
		mm.setBounds(250, 100, 50, 50);
		ff.setBounds(300, 100, 50, 50);
		id.setBounds(50, 150, 80, 50);
		id_t.setBounds(140, 160, 80, 20);
		b.setBounds(230, 160, 130, 20);
		pw.setBounds(50, 200, 80, 50);
		pwt.setBounds(140, 215, 130, 20);
		pw_re.setBounds(50, 260, 80, 50);
		pwt_re.setBounds(140, 270, 130, 20);
		email_la.setBounds(50, 300, 80, 50);
		email_tf.setBounds(140, 310, 100, 20);
		emailLabel_1.setBounds(240, 310, 20, 20);
		email_t_1.setBounds(260, 310, 100, 20);
		phone.setBounds(50, 350, 80, 50);
		phone1.setBounds(140, 360, 50, 20);
		phone2.setBounds(200, 360, 70, 20);
		phone3.setBounds(280, 360, 70, 20);
		email.setBounds(360, 310, 100, 50);
		b1.setBounds(200, 450, 80, 20);
		b2.setBounds(300, 450, 80, 20);

		frame.add(lb);
		frame.add(name);
		frame.add(tf);
		frame.add(mm);
		frame.add(ff);
		frame.add(id);
		frame.add(id_t);
		frame.add(b);
		frame.add(pw);
		frame.add(pwt);
		frame.add(pw_re);
		frame.add(pwt_re);
		frame.add(email_la);
		frame.add(email_tf);
		frame.add(emailLabel_1);
		frame.add(email_t_1);
		frame.add(phone);
		frame.add(phone1);
		frame.add(phone2);
		frame.add(phone3);
		frame.add(email);
		frame.add(b1);
		frame.add(b2);

		frame.setVisible(true);
	}
}