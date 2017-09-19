package socket_programming.step01;

public class ChattingServer {
	public static void main(String[] args) {
		Server server = new Server();
		try{
			server.start();
		}catch(IllegalThreadStateException e){
			
		}
	}
}
