package org.netty.http.file;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
	
	int counter =0;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
//		if(!request.getDecoderResult().isSuccess()){
//			sendError(ctx,BAD_REQUEST);
//			return;
//		}
//		if(!request.getMethod()!=GET){
//			sendError(ctx,METHOD_NOT_ALLOWED);
//			return;
//		}
//		final String uri = request.getUri();
//		final String path = sanitizeUri(uri);
//		if()
		
	} 
	
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
    	cause.printStackTrace(); 
        ctx.close();  
    }
}
