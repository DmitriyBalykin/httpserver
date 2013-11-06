package server;


public class ServerManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int port = 80;
		
		if(args.length == 0 || args.length > 2){
			System.out.println("Invalid parameters number. Parameters format: start/stop [port]");
			return;
		}
		if(args.length == 2){
			try {
				port = Integer.valueOf(args[0]);
				
				if(port < 0 || port > 65536){
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid port value");
				return;
			}
		}
		HttpServer server = new HttpServer(port);
		
		if(args[0].equals("start")){
			Thread t = new Thread(server);
			t.start();
		} 
		else if(args[0].equals("stop")){
			server.stop();
		} else {
			System.out.println("Unknown command: "+args[0]);
		}
	}
}
