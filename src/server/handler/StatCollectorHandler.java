package server.handler;

import server.utils.StatCollector;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class StatCollectorHandler extends ChannelInboundHandlerAdapter{
	private StatCollector container;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf)msg;
		container.addBytes(buf.readableBytes());
		System.out.println(container);
		super.channelRead(ctx, msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	public StatCollectorHandler(StatCollector cont) {
		container = cont;
	}
}
