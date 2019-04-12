package netty.study.java.nio.time;

public class TimeClient {
	public static void main(String[] args) {
		int port = 8080;
		new Thread(new TimeClientHandle("127.0.0.1",8080),"TimeClient-001").start();
	}
}
