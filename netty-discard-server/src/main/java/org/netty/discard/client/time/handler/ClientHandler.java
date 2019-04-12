package org.netty.discard.client.time.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelInboundHandlerAdapter {
	
	
	/*public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Gson gson = new Gson();
		Person person = new Person("关小西",Boolean.TRUE);
		person.setAge(22).setDescribe("发斯蒂芬斯蒂芬所发生的个胜多负少的故事电饭锅电饭锅");
		String request = gson.toJson(person);
		for(int i=0;i<10;i++)//meiyo
			ctx.writeAndFlush(Unpooled.copiedBuffer((request+"$_").getBytes("UTF-8")));
	};*/
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			String request = (String)msg;
			System.out.println("Client::: " + request);  
			/*ByteBuf buf = (ByteBuf) msg;
			byte[] data = new byte[buf.readableBytes()];
			buf.readBytes(data);
			System.out.println("Client：" + new String(data,"UTF-8").trim());*/
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
