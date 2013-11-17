package main;


import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Client implements Runnable, ActionListener {

	TextArea ta;
	TextField tf;
	Socket s;
	BufferedReader br;
	PrintWriter pw;
	String str;
	int num;
	String Client_Chat;
	JFrame f;
	Label lb1;
	Panel p;
	
	Button btn_send;
	JButton btn1;
	JButton btn2;
	JButton btn3;
	ImageIcon img1, img2, img3;
	//Image im1,im2,im3;
	//Toolkit tk = Toolkit.getDefaultToolkit();
	
	public Client(Logf pp) {
//		im1=tk.getImage("img\\Player_1.png");
//		im2=tk.getImage("img\\Player_2.png");
//		im3=tk.getImage("img\\Player_3.png");
//		img1 = new ImageIcon(im1);
//		img2 = new ImageIcon(im2);
//		img3 = new ImageIcon(im3);
		
		img1 = new ImageIcon("img\\1.png");
		img2 = new ImageIcon("img\\2.png");
		img3 = new ImageIcon("img\\3.png");
		
		
		f = new JFrame();
		f.setLayout(null);
		ta = new TextArea(15, 40);
		tf = new TextField(50);
		lb1 = new Label(" Game  ");
		p = new Panel();
		btn_send = new Button("전송");
		btn1 = new JButton(img1);
		btn2 = new JButton(img2);
		btn3 = new JButton(img3);
		
		f.setBounds(200, 200, 320, 520);
		lb1.setBounds(10, 0, 300, 50);
		tf.setBounds(0, 450, 300, 30);
		ta.setBounds(0, 300, 300, 150);
		btn1.setBounds(0, 170, 100, 130);
		btn2.setBounds(100, 170, 100, 130);
		btn3.setBounds(200, 170, 100, 130);
		btn_send.addActionListener(this);
		btn_send.setBackground(Color.yellow);

		p.add(btn_send);
		p.add(tf);
		f.add(BorderLayout.SOUTH, p);
		f.add(lb1);
		f.add(ta);
		f.add(tf);
		f.add(btn1);
		f.add(btn2);
		f.add(btn3);
		
		tf.addActionListener(this);
		tf.requestFocus();
		
		f.setVisible(true);
		
		btn1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btn1.addActionListener(this);
				
				if(e.getSource()==btn1){
					new Game_f("img\\1.png");
				}
			}
		});
	btn2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btn2.addActionListener(this);
								
				if(e.getSource()==btn2){
					new Game_f("img\\2.png");
				}
				
			}
		});
	btn3.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			btn3.addActionListener(this);
			
			if(e.getSource()==btn3){
				new Game_f("img\\3.png");
			}
			
		}
	});
		

		try {
			s = new Socket("127.0.0.1", 7777);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream(), true); 

		} catch (Exception e) {

			System.out.println("에러발생 : " + e);

		}

		Thread ct = new Thread(this);

		ct.start();

	}
	

	public void actionPerformed(ActionEvent e) {

		str = tf.getText();
		tf.setText("");
		pw.println(str);

	}

	@Override
	public void run() {

		try {
			while ((Client_Chat = br.readLine()) != null) {
				ta.append(Client_Chat + "\n");//textarea에 첨부한다.
			}

		} catch (Exception e) {
			System.out.println("소켓을 통해 채팅내용을 읽어오는 중 에러발생");
		}
	}

	public static void main(String[] args) {
	}
}
