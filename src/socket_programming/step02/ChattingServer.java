package socket_programming.step02;

public class ChattingServer {
	public static void main(String[] args) {
		Server server = new Server();
		try{
			server.start();
		}catch(IllegalThreadStateException e){
			
		}
	}
}
