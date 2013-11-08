package server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import server.utils.ProcessedConnection.ConnectionParameter;
import server.utils.StatCollector;

public class StatCollectorOutboudHandler extends ChannelOutboundHandlerAdapter{
	StatCollector statCollector;
	
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		
		ByteBuf buf = (ByteBuf)msg;
		long sentBytes = buf.readableBytes();
		String remoteIp = ctx.channel().remoteAddress().toString();
		remoteIp = remoteIp.substring(1,remoteIp.indexOf(":"));
		//measuring connection speed
		long startWriteTime = System.currentTimeMillis();
		
		super.write(ctx, msg, promise);
		
		long writeTime = System.currentTimeMillis() - startWriteTime;
		long speed = 1000 * sentBytes / writeTime;
			
		statCollector.addProcessedConnection(ctx.channel(), ConnectionParameter.IPADDRESS, remoteIp);
		statCollector.addProcessedConnection(ctx.channel(), ConnectionParameter.SENT_BYTES, sentBytes);
		statCollector.addProcessedConnection(ctx.channel(), ConnectionParameter.SPEED, speed);
		
	}
	
	public StatCollectorOutboudHandler(StatCollector collector) {
		statCollector = collector;
	}
}
