package net_p;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;

import db.GameVo;

public class Chat_frame extends JFrame {
	TextField tf;
	static TextArea ta;
	Socket socket;
	String name;
	Object ob;
	JFrame f1;
	
	public Chat_frame(GameVo vo) throws Exception {

		super("채팅창");
		setBounds(240, 580, 310, 200);
		setLayout(new BorderLayout());
		tf = new TextField();
		ta = new TextArea();
		ta.setEditable(false);
		tf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ob = e.getSource();
			}
		});
		add("Center", ta);
		add("South", tf);
		this.name = vo.getId();
		socket = new Socket("127.0.0.1", 4567);
		Mul_Sender ss = new Mul_Sender(socket, tf, name);
		Mul_Receive rr = new Mul_Receive(socket);
		ss.start();
		rr.start();
		System.out.println("접속");
		
		setVisible(true);
	}
	
	class Mul_Sender extends Thread{
		Socket socket;
		ObjectOutputStream out;
		TextField tf;
		String name;

		public Mul_Sender(Socket socket, TextField tf, String name) throws Exception {
			this.socket = socket;
			out = new ObjectOutputStream(socket.getOutputStream());
			this.tf = tf;
			this.name=name;
		}

		@Override
		public void run() {

			try {
				if (out != null) {
					out.writeObject(name);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			while (out != null) {
				try {
					if(tf.getText().length() > 0 && tf == ob) {
						out.writeObject("[" + name + "]" + tf.getText() + "\n");
						System.out.println("Client : [" + name + "]" + tf.getText() + "\n");
						tf.setText("");
						ob = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}


class Mul_Receive extends Thread {
	Socket socket;
	ObjectInputStream in;

	public Mul_Receive(Socket socket) throws Exception {
		this.socket = socket;
		in = new ObjectInputStream(socket.getInputStream());
	}

	@Override
	public void run() {

		while (in != null) {
			try {
				Chat_frame.ta.append((String) in.readObject());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
