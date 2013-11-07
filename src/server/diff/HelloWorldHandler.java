package server.diff;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class HelloWorldHandler extends ChannelInboundHandlerAdapter{
private static final byte[] RESPONSE = "Hello, World".getBytes();

	public void channelReadComplete(io.netty.channel.ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	};
	
	@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			if(msg instanceof HttpRequest){
				HttpRequest req = (HttpRequest) msg;
				
				if(HttpHeaders.is100ContinueExpected(req)){
					ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
				}
			}
		}
}
