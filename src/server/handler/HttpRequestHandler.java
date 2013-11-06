package server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.rtsp.RtspMethods;

import java.util.List;

import server.utils.StatCollector;
import server.utils.StatFormatter;

public class HttpRequestHandler extends SimpleChannelInboundHandler<HttpRequest>{
	
	private static final String HELLO_RESPONSE = "Hello World";
	private static final String HELLO_REQUEST = "/hello";
	private static final String REDIRECT_REQUEST = "/redirect";
	private static final String STAT_REQUEST = "/status";
	
	StatCollector statCollector;
	
	public HttpRequestHandler(StatCollector statCollector) {
		this.statCollector = statCollector;
	}
	
	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, HttpRequest request) throws Exception {
		
		QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
		String decodedPath = decoder.path();
		System.out.println("Decoded path: "+decodedPath);

		//	response Hello world
		if(decoder.path().equals(HELLO_REQUEST)){
			byte[] response = HELLO_RESPONSE.getBytes();
			ByteBuf buf = ctx.alloc().buffer(response.length);
			buf.writeBytes(response);
			new DelayedResponse(ctx, buf, 10000);
		}
		
		//	redirect
		else if(decoder.path().equals(REDIRECT_REQUEST)){
			List<String> redirectUrlList = decoder.parameters().get("url");
			String redirectUrl = null;
			if(redirectUrlList != null && redirectUrlList.size() > 0){
				redirectUrl = redirectUrlList.get(0);
			}
			HttpRequest request2 = new DefaultHttpRequest(HttpVersion.HTTP_1_1, RtspMethods.REDIRECT, redirectUrl);
			
			postResponse(ctx, request2);
		}
		
		//	status table	
		else if(decoder.path().equals(STAT_REQUEST)){
			
			String statTableString = StatFormatter.formatHTMLTable(statCollector);
			byte[] statTableArray = statTableString.getBytes();
			ByteBuf buf = ctx.alloc().buffer(statTableArray.length);

			postResponse(ctx, buf);
		}
		
		//	default
		else {
			ctx.close();
		}
	}
	
	private void postResponse(ChannelHandlerContext ctx, Object msg){
		final ChannelFuture future = ctx.channel().writeAndFlush(msg);
		future.addListener(new ChannelFutureListenerImpl(ctx, future));
	}
	
	private class DelayedResponse implements Runnable{
		Object response;
		ChannelHandlerContext ctx;
		int delay;
		
		public DelayedResponse(ChannelHandlerContext ctx, Object response, int delay) {
			this.response = response;
			this.ctx = ctx;
			this.delay = delay;
			new Thread(this).start();
		}
		@Override
		public void run() {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			postResponse(ctx, response);
		}
		
	}
	
	private class ChannelFutureListenerImpl implements ChannelFutureListener{
		ChannelFuture future;
		final ChannelHandlerContext ctx;
		public ChannelFutureListenerImpl(ChannelHandlerContext ctx, ChannelFuture future) {
			this.future = future;
			this.ctx = ctx;
		}
		
		@Override
		public void operationComplete(ChannelFuture future2) throws Exception {
			if(future.equals(future2)){
				ctx.close();
			}
		}
		
	}
}
