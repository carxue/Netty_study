package org.netty.newstudy.messagepack;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler1 extends ChannelInboundHandlerAdapter{
	int counter =0;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
//			Person person = (Person) msg;
			//StringDecoder服务器已经添加了该解码器不需要在解析ByteBuf了
            System.out.println("This is "+(++counter)+" Server: " + msg);  
            //写给客户端  
//            ByteBuf pingMessage = Unpooled.copiedBuffer("服务器发送的数据APPLE".getBytes());
            ChannelFuture future = ctx.writeAndFlush(msg);  
//            System.out.println(future.isSuccess());
    }  
	
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
    	cause.printStackTrace(); 
        ctx.close();  
    } 
}
