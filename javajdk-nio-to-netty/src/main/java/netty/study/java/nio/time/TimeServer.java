package netty.study.java.nio.time;

public class TimeServer {
	public static void main(String[] args) {
		int port = 8080;
		MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
		new Thread(timeServer,"NIO-MultiplexerTimeServer-001").start();
	}
}
