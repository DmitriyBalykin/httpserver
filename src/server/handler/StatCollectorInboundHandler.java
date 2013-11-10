package server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import server.utils.ProcessedConnection.ConnectionParameter;
import server.utils.StatCollector;

public class StatCollectorInboundHandler extends ChannelInboundHandlerAdapter{
	private StatCollector statCollector;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		statCollector.increaseOpenedConnections();
		super.channelRegistered(ctx);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		statCollector.decreaseOpenedConnections();
		super.channelUnregistered(ctx);
	}
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ByteBuf buf = (ByteBuf)msg;
		long receivedBytes = buf.readableBytes();
		String remoteIp = ctx.channel().remoteAddress().toString();
		remoteIp = remoteIp.substring(1,remoteIp.indexOf(":"));
		//measuring connection speed
		
		statCollector.addProcessedConnection(ctx.channel(), ConnectionParameter.IPADDRESS, remoteIp);
		statCollector.addProcessedConnection(ctx.channel(), ConnectionParameter.RECEIVED_BYTES, receivedBytes);		
		
		long startReadTime = System.nanoTime();
		
		super.channelRead(ctx, msg);
		
		double readTime = (System.nanoTime() - startReadTime) / 10E+9;
		double speed = 0;
		if(readTime > 0){
			speed = receivedBytes / readTime;
		}
			
		statCollector.addProcessedConnection(ctx.channel(), ConnectionParameter.SPEED, Math.round(speed));
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
