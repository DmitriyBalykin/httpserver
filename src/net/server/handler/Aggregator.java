package net.server.handler;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class Aggregator extends HttpObjectAggregator{
	private static final int MAX_CONTENT_LENGTH = 65536;
	
	public Aggregator(){
		this(MAX_CONTENT_LENGTH);
	}
	
	public Aggregator(int maxContentLength) {
		super(maxContentLength);
	}

	@Override
	protected void decode(ChannelHandlerContext arg0, HttpObject arg1, List<Object> arg2) throws Exception {
		System.out.println("Aggregator called");
		super.decode(arg0, arg1, arg2);
	}
}
