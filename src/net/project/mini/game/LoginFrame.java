package net.project.mini.game;

import java.awt.Dialog;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.project.mini.chat.Client;
import net.project.mini.listener.WindowListener;

//로그인 프레임 
public class LoginFrame {

	TextField userId, userPw;
	JFrame loginFrame;
	JLabel idLabel, passwdLabel;
	JButton loginBt, findIdBt, joinBt;

	HashMap<String, String> map = new HashMap<String, String>();

	public LoginFrame() {
		loginFrame = new JFrame("회원가입");
		loginFrame.setBounds(500, 490, 440, 290);
		loginFrame.setLayout(null);

		idLabel = new JLabel("ID : ");
		idLabel.setBounds(80, 50, 50, 30);
		loginFrame.add(idLabel);

		userId = new TextField(10);
		userId.setBounds(150, 50, 200, 30);
		loginFrame.add(userId);

		passwdLabel = new JLabel("PW : ");
		passwdLabel.setBounds(80, 80, 50, 30);
		loginFrame.add(passwdLabel);

		userPw = new TextField(10);
		userPw.setBounds(150, 80, 200, 30);
		userPw.setEchoChar('*');
		loginFrame.add(userPw);

		loginBt = new JButton("로그인");
		loginBt.setBounds(70, 120, 80, 50); // 버튼 위치 설정
		loginFrame.add(loginBt);

		findIdBt = new JButton("아이디 / 비밀번호찾기");
		findIdBt.setBounds(170, 120, 180, 50); // 버튼 위치 설정
		loginFrame.add(findIdBt);

		joinBt = new JButton("회원가입");
		joinBt.setBounds(70, 180, 280, 30);
		loginFrame.add(joinBt);

		map.put("aaa", "11");
		map.put("bbb", "22");
		map.put("ccc", "33");
		map.put("ddd", "44");
		map.put("eee", "55");

		loginFrame.setVisible(true);

		loginBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String id = userId.getText();
				String pw = userPw.getText();
				userPw.setText("");

				Dialog dialog = new Dialog(loginFrame, "불일치!", true);

				if (!map.containsKey(id)) {
					dialog.setBounds(100, 100, 200, 150);
					dialog.add(new Label("아이디가 존재하지 않습니다."));

					dialog.addWindowListener(new WindowListener());
					dialog.setVisible(true);

					userId.setText("");
					userId.requestFocus();
				} else if (!map.get(id).equals(pw)) {
					dialog.setBounds(100, 100, 200, 150);
					dialog.add(new Label("암호가 일치하지 않습니다."));

					dialog.addWindowListener(new WindowListener());
					dialog.setVisible(true);
					userPw.requestFocus();

				} else {
					// setVisible(false);
					loginFrame.dispose();
					new Client();

				}
			}
		});
		joinBt.addActionListener(new Register());

	}
}