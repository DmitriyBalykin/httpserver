package server.handler;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequestDecoder;

public class DecoderProxy extends HttpRequestDecoder{
	@Override
	protected void decode(ChannelHandlerContext arg0, ByteBuf arg1, List<Object> arg2) throws Exception {
		System.out.println("DecoderProxy called");
		super.decode(arg0, arg1, arg2);
	}
}
