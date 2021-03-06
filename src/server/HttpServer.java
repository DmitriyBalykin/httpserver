package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.BindException;

import server.utils.StatCollector;


public class HttpServer implements Runnable{
	private int port;
	private StatCollector statCollector = new StatCollector();
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
				System.out.println("Done.");
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
					pipeFactory.getPipeline(channel, statCollector);
					}
			});
			bootstrap.option(ChannelOption.SO_BACKLOG, 128);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			System.out.println("Server started");
			System.out.println("Listening port "+port);
			
			ChannelFuture future = bootstrap.bind(port).sync();
			
			future.channel().closeFuture().sync();
		} catch (InterruptedException e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			stop();
		}
	}

	public void stop(){
		
		System.out.print("Server is stopping... ");
		
		final Future<Channel> workerStopFuture = (Future<Channel>) workerGroup.shutdownGracefully();
		final Future<Channel> bossStopFuture = (Future<Channel>) bossGroup.shutdownGracefully();
		
		workerStopFuture.addListener(new GenericFutureListener<Future<Channel>>() {

			@Override
			public void operationComplete(Future<Channel> future) throws Exception {
				if(future.equals(workerStopFuture)){
					serverState.setWorkerStopped();
				}
			}
		});
		bossStopFuture.addListener(new GenericFutureListener<Future<Channel>>() {

			@Override
			public void operationComplete(Future<Channel> future) throws Exception {
				if(future.equals(bossStopFuture)){
					serverState.setBossStopped();
				}
			}
		});
	}
	
}
