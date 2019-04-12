package org.netty.discard.server.adapter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
	/* 
	 * 方法将会在连接被建立并且准备进行通信时被调用
	 */
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		final ByteBuf time = ctx.alloc().buffer(4); // (2)32位的整数缓冲
		time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

		final ChannelFuture f = ctx.writeAndFlush(time); // (3)f代表一个还没有发生的I/O操作，所以请求不是立马执行,netty里面所有的操作都是异步的
		f.addListener(new ChannelFutureListener() {//添加监听器，监控写请求完成时通知
			public void operationComplete(ChannelFuture future) {
				assert f == future;
				ctx.close();//使用完毕通过简体关闭频道
			}
		}); // (4)
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
