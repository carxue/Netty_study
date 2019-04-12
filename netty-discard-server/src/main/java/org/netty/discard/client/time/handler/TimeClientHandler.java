package org.netty.discard.client.time.handler;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {
	
	 private ByteBuf buf;

	    @Override
	    public void handlerAdded(ChannelHandlerContext ctx) {
	        buf = ctx.alloc().buffer(4); // (1)
	    }

	    @Override
	    public void handlerRemoved(ChannelHandlerContext ctx) {
	        buf.release(); // (1)
	        buf = null;
	    }
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
		//这种地方方式会出现字节数据读取不完整
        /*ByteBuf m = (ByteBuf) msg; // (1)tcp/ip中netty将读到的消息放入到ByteBuf中
        try {
            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        } finally {
            m.release();
        }*/
        
        ByteBuf m = (ByteBuf) msg;
        buf.writeBytes(m); // (2)所有接收的数据都应该被累积在 buf 变量里。
        m.release();

        if (buf.readableBytes() >= 4) { // (3)必须检查 buf 变量是否有足够的数据
            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
