package net.server;


public class ServerManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("starting server");
		HttpServer server = new HttpServer(80);
		server.startServer();
		System.out.println("server started");
	}

}
