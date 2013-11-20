package server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import server.utils.StatCollector;
import static server.utils.ProcessedConnection.ConnectionParameter.*;

public class StatCollectorOutboudHandler extends ChannelOutboundHandlerAdapter{
	StatCollector statCollector;
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		
		ByteBuf buf = (ByteBuf)msg;
		long sentBytes = buf.readableBytes();
		String remoteIp = StatCollector.ipAddressToString(ctx.channel().remoteAddress());
		//measuring connection speed
		long startWriteTime = System.nanoTime();
		
		ctx.write(msg, promise);
		
		double readTime = (System.nanoTime() - startWriteTime) / 10E+9;
		double speed = 0;
		if(readTime > 0){
			speed = sentBytes / readTime;
		}
			
		statCollector.addProcessedConnection(ctx.channel(), IPADDRESS, remoteIp);
		statCollector.addProcessedConnection(ctx.channel(), SENT_BYTES, sentBytes);
		statCollector.addProcessedConnection(ctx.channel(), SPEED, Math.round(speed));
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}
	
	public StatCollectorOutboudHandler(StatCollector collector) {
		statCollector = collector;
	}
}
