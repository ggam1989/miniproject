package main;


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

//ȸ������ ������
class Register implements ActionListener {
	Logf pp;
	JFrame f;
	Font f1;
	Label pw, lb, name, id, pw_re, emailLabel_1, email_la, phone;
	TextField pwt, id_t, tf, email_t_1, phone2, phone3, pwt_re, email_tf;
	//�̸� : tf, ���̵� : id_t, ��й�ȣ : pwt, ��й�ȣ2 : pwt_re,
	//�̸��� : email_t_1, ������email_tf, �ڵ��� : phone2, phone3
	CheckboxGroup cg;
	Checkbox mm, ff;
	Button b, b1, b2;
	Choice email, phone1;

	public Register(Logf pp) {
		this.pp = pp;
	}

	public void actionPerformed(ActionEvent e) {
		f = new JFrame();
		f.setBounds(300, 300, 700, 600);
		f.setLayout(null);

		lb = new Label("ȸ �� �� ��");
		f1 = new Font("�ü�ü", Font.BOLD + Font.ITALIC, 20);
		lb.setFont(f1);
		lb.setForeground(new Color(255, 0, 0));
		lb.setBounds(30, 40, 800, 40);

		pw_re = new Label("��й�ȣȮ��:");
		name = new Label("�̸�");
		tf = new TextField();//�̸�
		cg = new CheckboxGroup();
		mm = new Checkbox("����", cg, true);
		ff = new Checkbox("����", cg, false);
		id = new Label("���̵�:");
		id_t = new TextField();
		b = new Button("���̵� �ߺ� Ȯ��");
		pw = new Label("��й�ȣ:");
		pwt = new TextField("");
		pwt_re = new TextField("");
		email_la = new Label("Email�Է� :");
		email_tf = new TextField();
		emailLabel_1 = new Label("@");
		email_t_1 = new TextField();
		phone = new Label("�ڵ��� ��ȣ :");
		phone1 = new Choice();
		phone1.add("010");
		phone1.add("011");
		phone1.add("019");
		phone2 = new TextField();
		phone3 = new TextField();
		b1 = new Button("ȸ������");
		email = new Choice();
		email.add("���� �Է�");
		email.add("naver.com");
		email.add("daum.net");
		email.add("google.com");
		b2 = new Button("���");

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

		f.add(lb);
		f.add(name);
		f.add(tf);
		f.add(mm);
		f.add(ff);
		f.add(id);
		f.add(id_t);
		f.add(b);
		f.add(pw);
		f.add(pwt);
		f.add(pw_re);
		f.add(pwt_re);
		f.add(email_la);
		f.add(email_tf);
		f.add(emailLabel_1);
		f.add(email_t_1);
		f.add(phone);
		f.add(phone1);
		f.add(phone2);
		f.add(phone3);
		f.add(email);
		f.add(b1);
		f.add(b2);

		f.setVisible(true);
	}

	public static void main(String[] args) {
	}
}