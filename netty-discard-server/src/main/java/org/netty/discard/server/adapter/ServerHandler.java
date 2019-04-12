package org.netty.discard.server.adapter;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter{
	int counter =0;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
			
            /*ByteBuf buf = (ByteBuf)msg;  
            byte[] data = new byte[buf.readableBytes()];  
            buf.readBytes(data); 
            String request = new String(data, "UTF-8");*/
            
		
			//StringDecoder服务器已经添加了该解码器不需要在解析ByteBuf了
			String request = (String)msg;
            System.out.println("This is"+(++counter)+" Server: " + request);  
            //写给客户端  
            ByteBuf pingMessage = Unpooled.copiedBuffer("服务器发送的数据APPLE.$_".getBytes());
            ChannelFuture future = ctx.writeAndFlush(pingMessage);  
//            .addListener(ChannelFutureListener.CLOSE);  
            
            System.out.println(future.isSuccess());
    }  
	
	private ByteBuf getSendByteBuf(String message) throws UnsupportedEncodingException {

	    byte[] req = message.getBytes("UTF-8");
	    ByteBuf pingMessage = Unpooled.copiedBuffer(message.getBytes());
	    pingMessage.writeBytes(req);

	    return pingMessage;
	}
  
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
        cause.printStackTrace();  
        ctx.close();  
    } 
}
