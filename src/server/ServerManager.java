package server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class ServerManager {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void start(String[] args){
	int port = 80;
		
		if(args.length == 1){
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
		else if(args.length > 1){
			System.out.println("Invalid parameters number. Parameters format: [port]");
			return;
		}

		HttpServer server = new HttpServer(port);
		
		Thread t = new Thread(server);
		t.start();
		
		InputStream in = System.in;
		Scanner input = new Scanner(in);
		
		String line;
		while((line = input.nextLine()) != null && line.length() != 0){
			if(line.equals("stop")){
				server.stop();
				return;
			} else {
				System.out.println("Unknown command: "+line);
			}
		}
	}
	
	public static void main(String[] args){
		ServerManager.start(args);
	}
}
