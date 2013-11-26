package net.project.mini.game;

import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import db.GameDAO;
import db.GameVo;

public class Identity implements ActionListener {
	Login pp;
	JFrame f;
	JLabel lb1, lb2, lb3;
	TextField ph, pw2;
	JButton btn1;
	Identity ppp = this;
	

	public Identity(Login pp) {
		this.pp = pp;
	}

	public void actionPerformed(ActionEvent e) {
		f = new JFrame();
		f.setLayout(null);
		f.setBounds(100, 100, 380, 300);

		lb1 = new JLabel("아이디 & 비밀번호찾기");
		lb2 = new JLabel(" 핸드폰 번호 : ");
		lb3 = new JLabel("2차 비밀번호 : ");
		ph = new TextField();
		pw2 = new TextField();
		pw2.setEchoChar('*');
		btn1 = new JButton(" 확  인  ");

		lb1.setBounds(50, 15, 200, 30);
		lb2.setBounds(50, 70, 100, 30);
		lb3.setBounds(50, 130, 100, 30);
		ph.setBounds(170, 70, 120, 30);
		pw2.setBounds(170, 130, 120, 30);
		btn1.setBounds(120, 180, 100, 50);

		f.add(lb1);
		f.add(lb2);
		f.add(lb3);
		f.add(ph);
		f.add(pw2);
		f.add(btn1);
		
		
		btn1.addActionListener(new selectOne(this));
		f.setVisible(true);
	}


public class selectOne implements ActionListener{
	Identity it;
	GameDAO gd;
	GameVo vo;
	
	JDialog dl1 = new JDialog();
	JDialog dl2 = new JDialog();
	JLabel lb1,lb2 ;
	Font font;
	
	public selectOne(Identity it) {
		this.it = it;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		gd = new GameDAO();
		String phone = it.ph.getText();
		String pw2 = it.pw2.getText();
		
		vo = gd.selectOne(phone,pw2);
		
		if(vo.getId() !=null && vo.getPw()!=null){
			font = new Font("Serif", Font.BOLD, 15);
			lb1 = new JLabel("\t"+"id : " +vo.getId() +"\t" + "pw : " + vo.getPw() + "\t");
			lb1.setFont(font);
			dl1.setBounds(100, 100, 500, 150);
			dl1.add(lb1);
			dl1.setVisible(true);
		}else{
			font = new Font("Serif", Font.BOLD, 15);
			lb2 = new JLabel("\t"+"잘못입력하였거나, 찾으시는 아이디가 존재하지 않습니다.");
			lb2.setFont(font);
			dl2.setBounds(100, 100, 500, 150);
			dl2.add(lb2);
			dl2.setVisible(true);
		}
		
		
	}
}
public static void main(String[] args) {
	
}
}