package main;

import net.project.mini.game.Login;
import net_p.Multi_Server;

public class Main {
	public static void main(String[] args) {
		new Login();
		//new Server();
		Multi_Server ms = new Multi_Server();
		try {
			ms.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
