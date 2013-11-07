package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;
import server.handler.HttpRequestHandler;
import server.handler.StatCollectorHandler;
import server.utils.StatCollector;

public class PipelineFactory {
	
	
	public ChannelPipeline getPipeline(Channel channel, StatCollector statCollector){
		ChannelPipeline pipeline = channel.pipeline();
		
		pipeline.addLast(new StatCollectorHandler(statCollector));
/*		pipeline.addLast(new DecoderProxy());
		pipeline.addLast(new Aggregator());
		pipeline.addLast(new ResponseEncoderProxy());
		pipeline.addLast(new ChunkedWriteHandlerProxy());*/
		pipeline.addLast(new HttpServerCodec());
		
		pipeline.addLast(new HttpRequestHandler(statCollector));
		
//		pipeline.addLast(new TimeServerHandler());
		
		return pipeline;
	}
}

