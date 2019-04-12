package org.netty.discard.client.time.encoder;

import org.netty.discard.client.time.pojo.UnixTime;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 简化版的编码器
 *
 */
public class TimeEncoderSimple extends MessageToByteEncoder<UnixTime> {
	@Override
    protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) {
        out.writeInt((int)msg.value());
    }
}
	