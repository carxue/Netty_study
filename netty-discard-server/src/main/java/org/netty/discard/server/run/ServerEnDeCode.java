package org.netty.discard.server.run;

import org.netty.discard.server.adapter.ServerEnDeCOdeHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ServerEnDeCode {
	public ServerEnDeCode(int port) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();//最底层继承自scheduleexcutorSercie做定时线程池使用,用于执行ChannelHandler的方法，ChannelPipeline中解释
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap
			.group(bossGroup, workerGroup)
			//添加NioServerSocketChannel的反射工厂ChannelFactory。 指定NIO的模式.NioServerSocketChannel对应TCP, NioDatagramChannel对应UDP
			//每个channel都有他自己的pipeline，pipeline是在channel创建时被自动创建==>abstractChannel构造方法中pipeline = newChannelPipeline();
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			//In/out boundhandler用于在ChannelPipeline频道管道内处理I/O，通过在ChannelHandlerContext的fireChannelRead/write方法依次调用
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					//输入的handler处理顺序是由上而下，输出顺序是由下而上，输入只经过实现in开头的处理器，输出只经过实现out开头的处理器
					//如果即实现了in也实现了out则两个都经过
					//socketChannel.pipeline().addLast(new ReadTimeoutHandler(5)); //5秒后未与服务器通信，则断开连接。
					//来自channelpipeline中的说明<li>Protocol Decoder - translates binary data (e.g. {@link ByteBuf}) into a Java object.</li>
					//<li>Protocol Encoder - translates a Java object into binary data.</li>
					socketChannel.pipeline().addLast(new ObjectEncoder());
					socketChannel.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null))); // 最大长度
					socketChannel.pipeline().addLast(new ServerEnDeCOdeHandler());
				}
			})
			.option(ChannelOption.SO_BACKLOG, 1024)// 设置TCP缓冲区大小1024即服务A/B队列的长度
			.option(ChannelOption.SO_RCVBUF, 32 * 1024)// 设置发送缓冲大小
			.option(ChannelOption.SO_SNDBUF, 32 * 1024)// 这是接收缓冲大小
			.option(ChannelOption.SO_KEEPALIVE, true);
			
			//绑定端口, bind返回future(异步), 加上sync阻塞在获取连接处
			ChannelFuture future = bootstrap.bind(port).sync();
			//可以绑定多个端口
			//ChannelFuture future = bootstrap.bind(port).sync();
			//等待关闭, 加上sync阻塞在关闭请求处
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		new ServerEnDeCode(8765);
	}
}
