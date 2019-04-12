package org.netty.discard.server.run;

import java.io.UnsupportedEncodingException;

public class Test {
	public static void main(String[] args) throws UnsupportedEncodingException {
		String name = "welcome to sz薛";//[119, 101, 108, 99, 111, 109, 101, 32, 116, 111, 32, 115, 122]
		int length = name.getBytes().length;
		byte[] nameByte = new byte[length];
		byte[] copyByte = new byte[length];
		nameByte = name.getBytes();
		System.arraycopy(nameByte, 13, copyByte, 0, 3);
		String copyName = new String(copyByte,"UTF-8");
		System.out.println(copyName);
	}
}
