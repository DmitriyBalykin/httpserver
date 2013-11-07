package server.utils;

public class StatCollector {
	private int receivedBytes = 0;
	
	public void addBytes(int bytes){
		receivedBytes += bytes;
	}
	
	public int getReceivedBytes(){
		return receivedBytes;
	}
	
	public String toString(){
		return String.format("Total bytes received: %d", receivedBytes);
	}
}
