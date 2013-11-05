package net.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class ResponseEncoderProxy extends HttpResponseEncoder{

	@Override
	protected void encodeInitialLine(ByteBuf buf, HttpResponse response) throws Exception {
		System.out.println("ResponseEncoderProxy called");
		super.encodeInitialLine(buf, response);
	}
}
