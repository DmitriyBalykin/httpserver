package net.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class TimeServerHandler extends ChannelInboundHandlerAdapter{
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		final ByteBuf time = ctx.alloc().buffer(4);
		Date date = new Date();
		time.writeInt((int)(System.currentTimeMillis()/1000L + 2208988800L));
//		time.writeBytes(date.toString().getBytes());
		final ChannelFuture future = ctx.writeAndFlush(time);
		future.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future2) throws Exception {
				assert future == future2;
				ctx.close();
			}
		});
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)	throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
