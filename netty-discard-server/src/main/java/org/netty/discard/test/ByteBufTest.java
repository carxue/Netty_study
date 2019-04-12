package org.netty.discard.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class ByteBufTest {
	public static void main(String[] args) throws InterruptedException {
		ByteBuf poolBuffer = null;
		for(int i=0;i<3000000;i++){
			poolBuffer = PooledByteBufAllocator.DEFAULT.directBuffer(1024);
			poolBuffer.writeByte(1);
			Thread.sleep(3000);
			poolBuffer.release();
		}
	}
}
