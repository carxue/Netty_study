package org.netty.discard.server.adapter;

import org.netty.discard.client.time.pojo.UnixTime;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimePojoServerHandler extends ChannelInboundHandlerAdapter {
	/* 
	 * 方法将会在连接被建立并且准备进行通信时被调用
	 */
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		 ChannelFuture f = ctx.writeAndFlush(new UnixTime());
		 f.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
