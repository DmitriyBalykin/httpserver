package server;

import server.diff.TimeServerHandler;
import server.handler.Aggregator;
import server.handler.ChunkedWriteHandlerProxy;
import server.handler.DecoderProxy;
import server.handler.HttpRequestHandler;
import server.handler.ResponseEncoderProxy;
import server.handler.StatCollectorHandler;
import server.utils.StatCollector;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;

public class PipelineFactory {
	
	
	public ChannelPipeline getPipeline(Channel channel, StatCollector statCollector){
		ChannelPipeline pipeline = channel.pipeline();
		
/*		pipeline.addLast(new StatCollectorHandler(statCollector));
		pipeline.addLast(new DecoderProxy());
		pipeline.addLast(new Aggregator());
		pipeline.addLast(new ResponseEncoderProxy());
		pipeline.addLast(new ChunkedWriteHandlerProxy());
		
		pipeline.addLast(new HttpRequestHandler(statCollector));*/
		
		pipeline.addLast(new TimeServerHandler());
		
		return pipeline;
	}
}
