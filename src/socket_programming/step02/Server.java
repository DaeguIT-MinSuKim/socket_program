package socket_programming.step02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server extends Thread {
	private ServerSocket ss;
	private Socket soc;
	private Vector<UserInfo> users = new Vector<>();
	
	public Server() {
		try {
			ss = new ServerSocket(12345);
			System.out.println("Server Ready...");
			start();
		} catch (IOException e) {
			System.err.println("해당 포트를 열수 없습니다");
			System.exit(1);
		}
	}
	
	@Override
	public void run() {
		while(true){
			try {
				soc = ss.accept();
				UserInfo ui = new UserInfo(soc);
				System.out.printf("접근자(%s) : %s%n",ui.getNickName(), soc.toString());
				users.add(ui);
			} catch (IOException e) {
				System.out.println("accept error " + e);
			}
		}
	}
	
	class UserInfo extends Thread{
		private Socket socket;
		private OutputStream out;
		private BufferedReader in;
		private String nickName;
		
		public UserInfo(Socket soc) {
			socket = soc;
			try {
				out = socket.getOutputStream();
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				nickName = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}	
			start();
		}
		
		@Override
		public void run() {
			while(true){
				//클라이언트와 통신;
			}
		}

		public String getNickName() {
			return nickName;
		}
		
	}
}
