import io.netty.bootstrap.ChannelFactory;
import io.netty.bootstrap.ServerBootstrap;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class HttpServer {
	
	public static void startServer(){
		Executor bossPool = Executors.newFixedThreadPool(1);
		Executor workerPool = Executors.newFixedThreadPool(2);
		
		ChannelFactory channelFactory = new NioClientSocketChannelFactory(){}
		
		ServerBootstrap bootstrap = new ServerBootstrap();
		
	}
	
}
