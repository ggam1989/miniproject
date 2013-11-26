package net_p;

import java.io.*;
import java.net.*;
import java.util.*;

public class Multi_Server {

	HashMap<String, ObjectOutputStream> clientmap;
	ServerSocket server;

	public Multi_Server() {
		clientmap = new HashMap<String, ObjectOutputStream>();

		Collections.synchronizedMap(clientmap);
	}

	public void connect() throws Exception {
		server = new ServerSocket(4567);
		System.out.println("");
		Socket client = null;

		while (true) {
			client = server.accept();
			ServerReceiver sr = new ServerReceiver(client);
			sr.start();

		}
	}

	public void closed() {
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class ServerReceiver extends Thread {
		Socket socket;
		ObjectOutputStream out;
		ObjectInputStream in;

		public ServerReceiver(Socket socket) throws Exception {
			this.socket = socket;

			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		}

		@Override
		public void run() {

			String name = "";

			try {

				name = (String) in.readObject();
				sendToAll(name + " 입장하였습니다. \n");
				clientmap.put(name, out);

				while (in != null) {
					sendToAll(in.readObject());
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} finally {
				sendToAll(name + " 퇴장하셨습니다. \n");
				clientmap.remove(name);
			}
		}
	}

	void sendToAll(Object object) {
		Iterator<String> it = clientmap.keySet().iterator();
		while (it.hasNext()) {
			ObjectOutputStream out = clientmap.get(it.next());

			try {
				out.writeObject(object);
				System.out.println(object);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
	}
}
