package server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;

import server.utils.ProcessedConnection.ConnectionParameter;
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
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, HttpRequest request) throws Exception {
		
		QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
		String decodedUri = decoder.path();
		
		statCollector.addProcessedConnection(ctx.channel(), ConnectionParameter.URI, decodedUri);
		
		//	response Hello world
		if(decodedUri.equals(HELLO_REQUEST)){

			HttpResponse response = new DefaultFullHttpResponse(
					HttpVersion.HTTP_1_1,
					HttpResponseStatus.OK,
					packString(ctx, HELLO_RESPONSE));
//			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
			new DelayedResponse(ctx, response, 10000);
		}
		
		//	redirect
		else if(decodedUri.equals(REDIRECT_REQUEST)){
			List<String> redirectUrlList = decoder.parameters().get("url");
			String redirectUrl = null;
			if(redirectUrlList != null && redirectUrlList.size() > 0){
				redirectUrl = "http://" + redirectUrlList.get(0);
			}
			statCollector.addRedirect(redirectUrl);

			HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.MOVED_PERMANENTLY);
			HttpHeaders.setHeader(response, "Location", redirectUrl);
			System.out.println(response);
			
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
		}
		
		//	status table	
		else if(decodedUri.equals(STAT_REQUEST)){
			
			String statTableString = new StatFormatter().formatHTMLTable(statCollector);
			HttpResponse response = new DefaultFullHttpResponse(
					HttpVersion.HTTP_1_1,
					HttpResponseStatus.OK,
					packString(ctx, statTableString));
			
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
		}
		
		//	default
		else {
			ctx.close();
		}
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
			ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		}
		
	}
}
