package org.netty.discard.server.run;

import java.io.UnsupportedEncodingException;

import org.netty.discard.Person;
import org.netty.discard.client.time.handler.ClientHandler;

import com.google.gson.Gson;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

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
				ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
				socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(2048, buf));//分隔符类
				
				socketChannel.pipeline().addLast(new StringDecoder());
				socketChannel.pipeline().addLast(new ClientHandler());
			}
		});
		ChannelFuture future = bootstrap.connect("127.0.0.1", 8080).sync();
		
		//发送10次测试粘包问题,如果没有加DelimiterBasedFrameDecoder会出现粘包可能只发送了2次
		Gson gson = new Gson();
		Person person = new Person("关小西",Boolean.TRUE);
		person.setAge(22).setDescribe("发斯蒂芬斯蒂芬所发生的个胜多负少的故事电饭锅电饭锅");
		String request = gson.toJson(person);
		for(int i=0;i<10;i++)//meiyo
			future.channel().writeAndFlush(Unpooled.copiedBuffer((request+"$_").getBytes("UTF-8")));
		
		
		future.channel().closeFuture().sync();
		workerGroup.shutdownGracefully();
	}
}
