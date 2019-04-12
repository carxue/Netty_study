package org.netty.http.file;

import org.netty.discard.Person;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler2 extends ChannelInboundHandlerAdapter {
	
	private final int sendNumber;
	public ClientHandler2(int sendNumber) {
		this.sendNumber = sendNumber;
	}
	
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Person[] persons = getPersons();
		for(Person person:persons){
			ctx.write(person);
		}
		ctx.flush();
	};
	
	
	private Person[] getPersons(){
		Person[] persons = new Person[sendNumber];
		Person person = null;
		for(int i=0;i<sendNumber;i++){
			person = new Person("关小西",Boolean.TRUE);
			person.setDescribe("发斯蒂芬斯蒂芬所发生的个胜多负少的故事电饭锅电饭锅");
			person.setAge(i);
			persons[i]=person;
		}
		return persons;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		try {
			System.out.println("Client::: " + msg);
			ctx.write(msg);
//		} finally {
//			ReferenceCountUtil.release(msg);
//		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
