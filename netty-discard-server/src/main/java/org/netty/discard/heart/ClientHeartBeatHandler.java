package org.netty.discard.heart;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ClientHeartBeatHandler extends ChannelInboundHandlerAdapter {
	private ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> heartBeat;
	private InetAddress address;
	private static final String SUCCESS_KEY = "auth_success_key";

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		address = InetAddress.getLocalHost();
		String ip = address.getHostAddress();
		String key = "1234";
		String auth = ip + "," + key;
		ctx.writeAndFlush(auth);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		if (heartBeat != null) {
			heartBeat.cancel(true);
			heartBeat = null;
		}
		ctx.fireExceptionCaught(cause);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			if (msg instanceof String) {
				String data = (String) msg;
				if (SUCCESS_KEY.equals(data)) {
					heartBeat = scheduled.scheduleWithFixedDelay(new HeartBeatTask(ctx), 0, 5, TimeUnit.SECONDS);
					System.out.println(msg);
				} else {
					System.out.println(msg);
				}
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}

	}

	private class HeartBeatTask implements Runnable {
		private final ChannelHandlerContext ctx;

		public HeartBeatTask(ChannelHandlerContext ctx) {
			this.ctx = ctx;
		}

		public void run() {
			try {
				RequestInfo requestInfo = new RequestInfo();
				requestInfo.setIp(address.getHostAddress());
//				Sigar sigar = new Sigar();
//				CpuPerc cpuPerc = sigar.getCpuPerc();
//				Map<String, Object> cpuPercMap = new HashMap<String, Object>();
//				cpuPercMap.put("combined", cpuPerc.getCombined());
//				cpuPercMap.put("user", cpuPerc.getUser());
//				cpuPercMap.put("sys", cpuPerc.getSys());
//				cpuPercMap.put("wait", cpuPerc.getWait());
//				cpuPercMap.put("idle", cpuPerc.getIdle());
//
//				Mem mem = sigar.getMem();
//				Map<String, Object> memoryMap = new HashMap<String, Object>();
//				memoryMap.put("total", mem.getTotal() / (1024 * 1024));
//				memoryMap.put("used", mem.getUsed() / (1024 * 1024));
//				memoryMap.put("free", mem.getFree() / (1024 * 1024));

//				requestInfo.setCpuPercMap(cpuPercMap);
//				requestInfo.setMemoryMap(memoryMap);
				ctx.writeAndFlush(requestInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}
