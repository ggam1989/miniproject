/*package net.project.mini.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

import db.LogInfo;

public class Server {
	ServerSocket ss;
	Socket s;
	Vector v;
//GameDAO gd = new GameDAO();
	
	public Server() {
		v = new Vector();
		System.out.println("서버 구동!");
		
		try {
			ss = new ServerSocket(7777);// 서버소켓설정
			while (true) {
				s = ss.accept();
				ServerThread st = new ServerThread();
				addThread(st);
				st.start();
			}

		} catch (Exception e) {
			System.out.println("생성자 실행중 예외 : " + e);
		}
	}

	public void addThread(ServerThread sthread) {
		v.add(sthread);
	}

	public void removeThread(ServerThread stread_remove) {
		v.remove(stread_remove);
	}

	public void Chatting(String str) {
		for (int i = 0; i < v.size(); i++) {
			ServerThread st = (ServerThread) v.elementAt(i);// 인덱스를이용해서 데이타를 얻어옴
			st.send(str);
		}
	}

	public static void main(String[] args) {
		new Server();
	}

	public class ServerThread extends Thread {
		BufferedReader br;
		PrintWriter pw_ta;
		String send_Chat;
		String name;

		public ServerThread() {
			try {
				br = new BufferedReader(new InputStreamReader(
						s.getInputStream()));
				pw_ta = new PrintWriter(s.getOutputStream(), true);
			} catch (Exception e) {
				System.out.println("ServerThread 생성중 예외 : " + e);
			}
		}

		public void send(String str) {
			pw_ta.println(str);
		}
	
		@Override
		public void run() {
			try {
				// pw_ta.println("대화창에 대화명을 입력하세요.");
				// name = br.readLine(); // 한줄씩 읽어오기
				Chatting("[" + LogInfo.getId() + "] 님이 입장하였습니다.");
				while ((send_Chat = br.readLine()) != null) {
					Chatting("[" + LogInfo.getId() + "]" + send_Chat);
					System.out.println("[" + LogInfo.getId() + "]" + send_Chat);
				}
			} catch (Exception e) {
				System.out.println("쓰레드 run 예외발생 : " + e);
				removeThread(this);
				Chatting("[" + LogInfo.getId() + "] 님이 퇴장했습니다.");
				System.out.println(s.getInetAddress() + "와의 연결 종");
			}
		}
	}
}
*/