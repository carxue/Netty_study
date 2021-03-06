package org.netty.discard.server.run;

import org.netty.discard.server.adapter.ServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.concurrent.AbstractFuture;

public class Server {
	private int port;

	public Server(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)用来接收进来的连接
		EventLoopGroup workerGroup = new NioEventLoopGroup();// 用来处理已经被接收的连接,进行网络通信(读写)
		try {
			ServerBootstrap b = new ServerBootstrap(); // (2)启动 NIO 服务的辅助启动类,用于服务器通道的一系列配置
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class) // (3)一个新的Channel如何接收进来的连接——指定NIO模式,指定NioServerSocketChannel生成工厂
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)配置具体的数据处理方式,当TCP链路注册成功后调用initChannel设置用户的handler
					@Override
					public void initChannel(SocketChannel socketChannel) throws Exception {
					
						/*************************设置特殊分隔符****************************/
						//防止TCP粘包，拆包问题，字节流是连续的TCP无法识别会根据缓存大小差分成不同的包或小包组合一起|添加后无法接受数据
						ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
						socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));//分隔符类
						//设置字符串形成的解码 StringDecoder在通信时就不需要转成ByteBuf类了
						socketChannel.pipeline().addLast(new StringDecoder());
						/*************************设置特殊分隔符****************************/
						
						socketChannel.pipeline().addLast(new ServerHandler());
//						socketChannel.pipeline().addLast(newReadTimeoutHandler(5)); //5秒后未与服务器通信，则断开连接。
					}
				})
				.option(ChannelOption.SO_BACKLOG, 128) //设置TCP缓冲区  
//                .option(ChannelOption.SO_SNDBUF, 32 * 1024) //设置发送数据缓冲大小  
//                .option(ChannelOption.SO_RCVBUF, 32 * 1024) //设置接受数据缓冲大小  
                .childOption(ChannelOption.SO_KEEPALIVE, true); //保持连接 

			// 绑定端口，开始接收进来的连接
			ChannelFuture f = b.bind(port).sync(); // (7)绑定端口等待服务器 socket 关闭 。
			// 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
			f.channel().closeFuture().sync();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		new Server(port).run();
	}
}
