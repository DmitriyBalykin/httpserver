package server.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class StatCollector {
	private volatile int requests = 0;
	private volatile int openedConnections = 0;
	private volatile Set<ProcessedRequest> uniqIPRequests = new HashSet<ProcessedRequest>();	//IP -> (requests number, last request time)
	private volatile Map<String, Integer> redirects = new HashMap<String, Integer>();
	private volatile LinkedList<ProcessedConnection> processedConnections = new LinkedList<ProcessedConnection>();
	
	public synchronized void addRequest(String ip) {
		requests ++;
		ProcessedRequest request = new ProcessedRequest(ip);
		for(ProcessedRequest req:uniqIPRequests){
			if(req.equals(request)){
				req.updateRequest();
				request = req;
			}
		}
		uniqIPRequests.add(request);
	
	}
	
	public int getRequests() {
		return requests;
	}

	public void addOpenedConnection() {
		openedConnections ++;
	}
	
	public synchronized void deleteOpenedConnection() throws Exception {
		openedConnections --;
		if(openedConnections < 0){
			throw new Exception("Error in calculating opened connections number. Number cannot be less that zero.");
		}
	}	
	
	public int getOpenedConnections() {
		return openedConnections;
	}

	public Set<ProcessedRequest> getUniqIPRequests() {
		return uniqIPRequests;
	}
	public int getUniqIPRequestsNumber() {
		return uniqIPRequests.size();
	}
	
	public synchronized void addRedirect(String url) {
		redirects.put(url, redirects.get(url) == null ? 1 : redirects.get(url) + 1);
	}
	
	public Map<String, Integer> getRedirects() {
		return redirects;
	}
	
	public void addProcessedConnections(String sourceIP, String uri, long sentBytes, long receivedBytes, long speed) {
		
		Calendar date = new GregorianCalendar();
		String timestamp = String.format("%d-%d-%d-%d-%d-%d", date.YEAR, date.MONTH, date.DAY_OF_MONTH, date.HOUR_OF_DAY, date.MINUTE, date.SECOND);
		
		processedConnections.push(
				new ProcessedConnection(
						sourceIP,
						uri,
						timestamp,
						sentBytes,
						receivedBytes,
						speed
						));
		if (processedConnections.size() > 16) {
			processedConnections.pollLast();
		}
	}
	
	public LinkedList<ProcessedConnection> getProcessedConnections() {
		return processedConnections;
	}


}
