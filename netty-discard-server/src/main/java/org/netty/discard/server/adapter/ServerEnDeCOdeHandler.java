package org.netty.discard.server.adapter;

import java.io.File;
import java.io.FileOutputStream;

import org.netty.discard.bean.Request;
import org.netty.discard.bean.Response;
import org.netty.discard.util.GzipUtils;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerEnDeCOdeHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//ctx上下文中持有DefaultChannelPipeline pipeline。context使handler和pipeline可以相互作用，大部分是通知pipeline中下一个的handler
		Request request = (Request) msg;
		System.out.println("Server:" + request.getId() + "," + request.getName() + "," + request.getReqeustMessag());
		Response response = new Response();
		response.setId(request.getId());
		response.setName("response " + request.getId());
		response.setResponseMessage("响应内容：" + request.getReqeustMessag());
		byte[] unGizpData = GzipUtils.unGzip(request.getAttachment());
		char separator = File.separatorChar;
		FileOutputStream outputStream = new FileOutputStream(System.getProperty("user.dir") + separator + "recieve" + separator + request.getFileName()+"."+request.getFileSuffix());
		outputStream.write(unGizpData);
		outputStream.flush();
		outputStream.close();
		//ctx.write(msg)方法不会将数据写入到通道，而是在缓存上只有调用flush方法才会写入channel上
		//Nio是异步的，ChannelFuture代表一个还么有发生的异步操作，writeAndFlush还没有发送前ch.close()可能已经执行了。
		ChannelFuture future = ctx.writeAndFlush(response);//写数据不用手工是否msg，单纯的读必须手工释放
		ctx.close();//context一旦被关闭将不能再接收到请求
		System.out.println(future.isSuccess());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Server channelActive");
		super.channelActive(ctx);
	}
}
