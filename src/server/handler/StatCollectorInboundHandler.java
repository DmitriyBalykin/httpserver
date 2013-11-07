package server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import server.utils.StatCollector;

public class StatCollectorInboundHandler extends ChannelInboundHandlerAdapter{
	private StatCollector statCollector;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		statCollector.addOpenedConnection();
		super.channelRegistered(ctx);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		statCollector.deleteOpenedConnection();
		super.channelUnregistered(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ByteBuf buf = (ByteBuf)msg;
		long receivedBytes = buf.readableBytes();
		String remoteIp = ctx.channel().remoteAddress().toString();
		remoteIp = remoteIp.substring(1,remoteIp.indexOf(":"));
		String uri = "";
		
		long startReadTime = System.currentTimeMillis();
		
		super.channelRead(ctx, msg);
		
		long readTime = System.currentTimeMillis() - startReadTime;
		long speed = 1000 * receivedBytes / readTime;
			
		if(msg instanceof HttpRequest){
			HttpRequest req = (HttpRequest)msg;	
			uri = req.getUri();
		}
		statCollector.addRequest(remoteIp);
		statCollector.addProcessedConnections(remoteIp, uri, 0, receivedBytes, speed);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	public StatCollectorInboundHandler(StatCollector cont) {
		statCollector = cont;
	}
}
