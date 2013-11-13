package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;
import server.handler.HttpRequestHandler;
import server.handler.StatCollectorInboundHandler;
import server.handler.StatCollectorOutboudHandler;
import server.utils.StatCollector;

public class PipelineFactory {
	
	public ChannelPipeline getPipeline(Channel channel, StatCollector statCollector){
		ChannelPipeline pipeline = channel.pipeline();

		
		pipeline.addLast("inputStatisticHandler",new StatCollectorInboundHandler(statCollector));
		
		pipeline.addLast("outputStatisticHandler", new StatCollectorOutboudHandler(statCollector));
		
		pipeline.addLast("codec",new HttpServerCodec());
	
		pipeline.addLast("logicHandler",new HttpRequestHandler(statCollector));

		return pipeline;
	}
}