package server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpRequest;
import server.utils.StatCollector;

public class StatCollectorOutboudHandler extends ChannelOutboundHandlerAdapter{
	StatCollector statCollector;
	
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		
		ByteBuf buf = (ByteBuf)msg;
		long sentBytes = buf.readableBytes();
		String remoteIp = ctx.channel().remoteAddress().toString();
		remoteIp = remoteIp.substring(1,remoteIp.indexOf(":"));
		String uri = "";
		
		long startWriteTime = System.currentTimeMillis();
		
		super.write(ctx, msg, promise);
		
		long writeTime = System.currentTimeMillis() - startWriteTime;
		long speed = 1000 * sentBytes / writeTime;
			
		if(msg instanceof HttpRequest){
			HttpRequest req = (HttpRequest)msg;
			uri = req.getUri();
		}

		statCollector.addRequest(remoteIp);
		statCollector.addProcessedConnections(remoteIp, uri, sentBytes, 0, speed);
		
	}
	
	public StatCollectorOutboudHandler(StatCollector collector) {
		statCollector = collector;
	}
}
