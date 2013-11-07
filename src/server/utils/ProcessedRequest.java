package server.utils;

import java.util.Date;

public class ProcessedRequest {
	private String ipaddress;
	private volatile int requestsNumber = 0;
	private volatile Date lastRequestDate;
	
	public ProcessedRequest(String ipAddr) {
		ipaddress = ipAddr;
	}
	public String getIPAddress(){
		return ipaddress;
	}
	public int getRequestsNumber() {
		return requestsNumber;
	}
	public Date getLastRequestDate(){
		return lastRequestDate;
	}
	public synchronized void updateRequest() {
		requestsNumber++;
		lastRequestDate = new Date();
	}
	
	public int hashcode(){
		return ipaddress.hashCode();
	}
	
	public boolean equals(Object o){
		return (o instanceof ProcessedRequest) && ((ProcessedRequest)o).ipaddress.equals(ipaddress);
	}
	
	public String toString(){
		return ipaddress;
	}
}
