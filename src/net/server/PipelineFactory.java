package net.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.server.handler.Aggregator;
import net.server.handler.ChunkedWriteHandlerProxy;
import net.server.handler.DecoderProxy;
import net.server.handler.HttpRequestHandler;
import net.server.handler.ResponseEncoderProxy;
import net.server.handler.StatCollectorHandler;

public class PipelineFactory {
	
	
	public ChannelPipeline getPipeline(Channel channel, StatCollector statContainer){
		ChannelPipeline pipeline = channel.pipeline();
		
		pipeline.addLast(new StatCollectorHandler(statContainer));
		pipeline.addLast(new DecoderProxy());
		pipeline.addLast(new Aggregator());
		pipeline.addLast(new ResponseEncoderProxy());
		pipeline.addLast(new ChunkedWriteHandlerProxy());
		
		pipeline.addLast(new HttpRequestHandler());
		
		return pipeline;
	}
}
