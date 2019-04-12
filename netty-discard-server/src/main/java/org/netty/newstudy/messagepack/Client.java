package org.netty.newstudy.messagepack;

import java.io.UnsupportedEncodingException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class Client {
	public static void main(String[] args) throws Exception {
		receiveMsg();
	}

	public static void receiveMsg() throws InterruptedException, UnsupportedEncodingException {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(workerGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				/*************************设置特殊分隔符****************************/
				//防止TCP粘包，拆包问题，字节流是连续的TCP无法识别会根据缓存大小差分成不同的包或小包组合一起|添加后无法接受数据
//				ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
//				socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(2048, buf));//分隔符类
//				socketChannel.pipeline().addLast(new StringDecoder());
				socketChannel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535, 0, 2 ,0, 2));
				socketChannel.pipeline().addLast("msgpack decoder",new MsgpackDecoder());
				socketChannel.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
				socketChannel.pipeline().addLast("msgpack encoder",new MsgpackEncoder());
				socketChannel.pipeline().addLast(new ClientHandler1(1000));
			}
		});
		ChannelFuture future = bootstrap.connect("127.0.0.1", 8080).sync();
		
		future.channel().closeFuture().sync();
		workerGroup.shutdownGracefully();
	}
}
