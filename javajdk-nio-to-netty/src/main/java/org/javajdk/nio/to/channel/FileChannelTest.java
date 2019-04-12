package org.javajdk.nio.to.channel;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
	public static void main(String[] args) throws Exception {
		RandomAccessFile aFile = new RandomAccessFile("D:\\soft_install\\Nio.txt", "rw");
		FileChannel channel = aFile.getChannel();
		String newData = "New String to write to file..." + System.currentTimeMillis();
		ByteBuffer buf = ByteBuffer.allocate(48);
		buf.clear();
		buf.put(newData.getBytes());
		buf.flip();
		while (buf.hasRemaining()) {
			channel.write(buf);
		}
		channel.close();
	}
}
