package net.project.mini.game;

import java.awt.Dialog;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

//	HashMap<String, String> map = new HashMap<String, String>();

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

//		map.put("aaa", "11");
//		map.put("bbb", "22");
//		map.put("ccc", "33");
//		map.put("ddd", "44");
//		map.put("eee", "55");
		
		
		insertTestUsers();

		loginFrame.setVisible(true);

		loginBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String id = userId.getText();
				String pw = userPw.getText();
				userPw.setText("");

				Dialog dialog = new Dialog(loginFrame, "불일치!", true);

				try {
					Connection conn = getConnection();
					PreparedStatement ps = conn.prepareStatement("SELECT * FROM User WHERE userId = ?");
					ps.setString(1, id);
					ResultSet rs = ps.executeQuery();
					if (!rs.next()) {
//					if (!map.containsKey(id)) {
						dialog.setBounds(100, 100, 200, 150);
						dialog.add(new Label("아이디가 존재하지 않습니다."));
						
						dialog.addWindowListener(new WindowListener());
						dialog.setVisible(true);
						
						userId.setText("");
						userId.requestFocus();
					} else if (!rs.getString("password").equals(pw)) {
//					} else if (!map.get(id).equals(pw)) {
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
					ps.close();
					conn.close();
				} catch (Exception exeption) {
					exeption.printStackTrace();
				}
			}
		});
		joinBt.addActionListener(new Register());

	}
	
	
//	CREATE TABLE `User` (
//			  `id` bigint(20) NOT NULL AUTO_INCREMENT,
//			  `userId` varchar(255) DEFAULT NULL,
//			  `password` varchar(255) DEFAULT NULL,
//			  PRIMARY KEY (`id`)
//			  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
	private void insertTestUsers() {
		initDb();
		insertUser("aaa", "11");
		insertUser("bbb", "22");
		insertUser("ccc", "33");
	}
	
	private void initDb() {
		Connection conn = getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("DROP TABLE IF EXISTS User");
			ps.executeUpdate();
			
			ps = conn.prepareStatement("CREATE TABLE User (id bigint(20) NOT NULL AUTO_INCREMENT, userId varchar(255) DEFAULT NULL, password varchar(255) DEFAULT NULL, PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8");
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	
	}


	private void insertUser(String userId, String password){
		Connection conn = getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO User(userId, password) VALUES(?,?)");
			ps.setString(1, userId);
			ps.setString(2, password);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	private Connection getConnection(){
		try {
			return DriverManager.getConnection("jdbc:mysql://localhost/minisol", "root", "");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return null;
	}
}