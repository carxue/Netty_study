package org.javajdk.nio.to.buffer;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BufferTest {
	public static void main(String[] args) throws Exception {
		RandomAccessFile aFile = new RandomAccessFile("D:\\soft_install\\Nio.txt", "rw");
		FileChannel inChannel = aFile.getChannel();
		// create buffer with capacity of 48 bytes
		ByteBuffer buf = ByteBuffer.allocate(1);
		int bytesRead = inChannel.read(buf); // read into buffer.
		while (bytesRead != -1) {
			buf.flip(); // make buffer ready for read
			while (buf.hasRemaining()) {
				System.out.print((char) buf.get()); // read 1 byte at a t ime
			}
			buf.clear(); // make buffer ready for writing
			bytesRead = inChannel.read(buf);
		}
		aFile.close();
		System.out.println("===="+add());
		
		set();
	}	
	
	public static int  add(){
		int i=0;
		return i++;
	}
	
	public static void  set(){
		ByteBuffer buf = ByteBuffer.allocate(12);
		buf.putInt(100);
		buf.flip();
		char resul = (char) buf.get();
		System.out.println("----"+resul);
		
		
	}
}
