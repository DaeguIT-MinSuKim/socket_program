package socket_programming.step01;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	private ServerSocket ss;
	private Socket soc;

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
				System.out.println("접근자 : " + soc.toString());
			} catch (IOException e) {
				System.out.println("accept error " + e);
			}
		}
	}
	
}
