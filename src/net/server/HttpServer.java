package net.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;


public class HttpServer implements Runnable{
	private int port;
	private StatCollector statContainer = new StatCollector();
	private PipelineFactory pipeFactory = new PipelineFactory();
	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private	EventLoopGroup workerGroup = new NioEventLoopGroup();
	private ServerState serverState = new ServerState();
	
	public HttpServer(int port) {
		this.port = port;
	}
	
	class ServerState{
		private boolean workerStopped = false;
		private boolean bossStopped = false;
		
		public void setWorkerStopped() {
			this.workerStopped = true;
			announceState();
		}

		public void setBossStopped() {
			this.bossStopped = true;
			announceState();
		}
		
		private void announceState(){
			if(workerStopped & bossStopped){
				System.out.println("Server stopped");
			}
		}
	}

	@Override
	public void run() {
	
		try{
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					pipeFactory.getPipeline(channel, statContainer);
					}
			});
			bootstrap.option(ChannelOption.SO_BACKLOG, 128);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			System.out.println("Server started");
			
			ChannelFuture future = bootstrap.bind(port).sync();
			
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public void stop(){
		Future<?> workerStopFuture = workerGroup.shutdownGracefully();
		Future<?> bossStopFuture = bossGroup.shutdownGracefully();
		
		workerStopFuture.addListener(new GenericFutureListener<Future<?>>() {

			@Override
			public void operationComplete(Future<?> arg0) throws Exception {
				serverState.setWorkerStopped();
			}
		});
		bossStopFuture.addListener(new GenericFutureListener<Future<?>>() {

			@Override
			public void operationComplete(Future<?> arg0) throws Exception {
				serverState.setBossStopped();
			}
		});
	}
	
}
