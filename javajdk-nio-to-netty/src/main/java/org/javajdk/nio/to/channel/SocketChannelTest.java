package org.javajdk.nio.to.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 连接到网络上的套机制通道
 *
 */
public class SocketChannelTest {
	public static void main(String[] args) throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress("www.baidu.com", 80));
		ByteBuffer buf = ByteBuffer.allocate(48);
		// int bytesRead = socketChannel.read(buf);
		while (socketChannel.finishConnect()) {
			socketChannel.read(buf);
			while (buf.hasRemaining()) {
				char c = (char) buf.get();
				System.out.println("----" + c);
			}
		}
		socketChannel.close();

	}

	public static void writeDatagram() throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		String newData = "New String to write to file..." + System.currentTimeMillis();
		ByteBuffer buf = ByteBuffer.allocate(48);
		buf.clear();
		buf.put(newData.getBytes());
		buf.flip();
		//write()方法的调用是在一个while循环中的。Write()方法无法保证能写多少字节到
		while (buf.hasRemaining()) {
			socketChannel.write(buf);
		}
	}

}
