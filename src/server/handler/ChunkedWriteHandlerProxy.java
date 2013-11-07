package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ChunkedWriteHandlerProxy extends ChunkedWriteHandler{
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println("ChunkWriterHandlerProxy called");
		super.write(ctx, msg, promise);
	}
}
