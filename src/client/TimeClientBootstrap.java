package client;

public class TimeClientBootstrap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TimeClient client = new TimeClient();
		client.start("localhost", 80);
	}

}
