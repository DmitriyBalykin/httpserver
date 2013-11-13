package test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class PerfDummyHttpRequestHandler extends SimpleChannelInboundHandler<HttpRequest>{
	
	private static final String HELLO_RESPONSE = "Hello World";
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, HttpRequest request) throws Exception {
		
		HttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK,
				packString(ctx, HELLO_RESPONSE));

		ctx.write(response).addListener(ChannelFutureListener.CLOSE);

	}
	
	private ByteBuf packString(ChannelHandlerContext ctx, String str){
		byte[] statTableArray = str.getBytes();
		ByteBuf buf = ctx.alloc().buffer(statTableArray.length);
		buf.writeBytes(statTableArray);
		return buf;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
