package net.project.mini.chat;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import net.project.mini.game.GameFrame;

public class Client implements ActionListener {

	TextArea textArea;
	TextField textField;
	Socket socket;
	BufferedReader br;
	PrintWriter printWriter;
	String str;
	int num;
	String Client_Chat;
	JFrame frame;
	Label label;
	Panel panel;

	Button sendBt;
	JButton characterBt1;
	JButton characterBt2;
	JButton characterBt3;
	ImageIcon characterImg1, characterImg2, characterImg3;

	public Client() {
		characterImg1 = new ImageIcon("img/1.png");
		characterImg2 = new ImageIcon("img/2.png");
		characterImg3 = new ImageIcon("img/3.png");

		frame = new JFrame();
		frame.setLayout(null);
		textArea = new TextArea(15, 40);
		textField = new TextField(50);
		label = new Label(" Game  ");
		panel = new Panel();
		sendBt = new Button("전송");
		characterBt1 = new JButton(characterImg1);
		characterBt2 = new JButton(characterImg2);
		characterBt3 = new JButton(characterImg3);

		frame.setBounds(200, 200, 320, 520);
		label.setBounds(10, 0, 300, 50);
		textField.setBounds(0, 450, 300, 30);
		textArea.setBounds(0, 300, 300, 150);
		characterBt1.setBounds(0, 170, 100, 130);
		characterBt2.setBounds(100, 170, 100, 130);
		characterBt3.setBounds(200, 170, 100, 130);
		sendBt.addActionListener(this);
		sendBt.setBackground(Color.yellow);

		panel.add(sendBt);
		panel.add(textField);
		frame.add(BorderLayout.SOUTH, panel);
		frame.add(label);
		frame.add(textArea);
		frame.add(textField);
		frame.add(characterBt1);
		frame.add(characterBt2);
		frame.add(characterBt3);

		textField.addActionListener(this);
		textField.requestFocus();

		frame.setVisible(true);
		
		
		characterBt1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GameFrame("img/1.png");
			}
		});
		
		characterBt2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GameFrame("img/2.png");
			}
		});
		characterBt3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GameFrame("img/3.png");
			}
		});

		try {
			socket = new Socket("127.0.0.1", 7777);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {
			System.out.println("error : " + e.getMessage());
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while ((Client_Chat = br.readLine()) != null) {
						textArea.append(Client_Chat + "\n");// textarea에 첨부한다
					}
				} catch (Exception e) {	System.out.println("error : " + e.getMessage()); }
			}
		}).start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		str = textField.getText();
		textField.setText("");
		printWriter.println(str);
	}
}
