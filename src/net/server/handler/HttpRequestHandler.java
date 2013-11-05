package net.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.rtsp.RtspMethods;

import java.util.List;
import java.util.Map;

public class HttpRequestHandler extends SimpleChannelInboundHandler<HttpRequest>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws Exception {
		QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
		System.out.println("Decoded path: "+decoder.path());
		if(decoder.path().equals("hello")){
//			redirect
			request.setMethod(RtspMethods.REDIRECT);
			request.setUri("http://localhost/helloworld.html");
			ctx.writeAndFlush(request);
		}
		Map<String,List<String>> parameters = decoder.parameters();
		for(String paramName:parameters.keySet()){
			System.out.println(String.format("Parameter %s has values %s", paramName, parameters.get(paramName)));
		}
	}
	
	
	
}
