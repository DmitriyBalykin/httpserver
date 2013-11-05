package net.server;


public class ServerManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HttpServer server = new HttpServer(80);
		server.startServer();
	}

}
