package org.netty.discard.client.time.protocol;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;

import org.netty.discard.bean.Request;
import org.netty.discard.client.time.handler.ClientEnDeCodeHandler;
import org.netty.discard.util.GzipUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ClientEnDeCode {
	public static void main(String[] args) {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(workerGroup).handler(new LoggingHandler(LogLevel.INFO)).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)//用了创建接收进来的连接
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							// socketChannel.pipeline().addLast(new ReadTimeoutHandler(5)); //5秒后未与服务器通信，则断开连接。
							socketChannel.pipeline().addLast(new ObjectEncoder());
							socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
							socketChannel.pipeline().addLast(new ClientEnDeCodeHandler());
						}
					});
			ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8765)).sync();
				
				// 传输图片
				char separator = File.separatorChar;
				File file = new File(System.getProperty("user.dir") + separator + "source" + separator);
				if(file.isDirectory()){
					File[] myFiles = file.listFiles();
					for(int i=0;i<myFiles.length;i++){
						Request request = new Request();
						request.setId(i);
						request.setName("pro" + i);
						request.setReqeustMessag("数据信息" + i);
						String[] token = myFiles[i].getName().split("\\.");
						request.setFileName(token[0]);
						request.setFileSuffix(token[1]);
						FileInputStream inputStream = new FileInputStream(myFiles[i]);
						byte[] data = new byte[inputStream.available()];
						inputStream.read(data);
						inputStream.close();
						byte[] gzipData = GzipUtils.gzip(data);
						request.setAttachment(gzipData);
						future.channel().writeAndFlush(request);
					}
				}
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
		}

	}
}
