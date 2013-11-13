package test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PerfDummyStatCollectorInboundHandler extends ChannelInboundHandlerAdapter{

	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		super.channelRead(ctx, msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
