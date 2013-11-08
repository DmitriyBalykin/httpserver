package server.utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import server.utils.ProcessedConnection.ConnectionParameter;

public class StatCollector {
	
	
	private volatile int requests = 0;
	private volatile int openedConnections = 0;
	private volatile Set<ProcessedRequest> uniqIPRequests = new HashSet<ProcessedRequest>();	//IP -> (requests number, last request time)
	private volatile Map<String, Integer> redirects = new HashMap<String, Integer>();
	private volatile LinkedList<ProcessedConnection> processedConnections = new LinkedList<ProcessedConnection>();
	
	private void addRequest(String ip) {
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

	public void increaseOpenedConnections() {
		openedConnections ++;
	}
	
	public synchronized void decreaseOpenedConnections() throws Exception {
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
	
	public void addProcessedConnection(Channel channel, ConnectionParameter param, String strValue, long digValue) {

		ProcessedConnection newConn = new ProcessedConnection(channel);
		boolean isNewConn = true;
		
		for(int i = 0; i < processedConnections.size(); i++){
			ProcessedConnection conn = processedConnections.get(i);
			if(newConn.equals(conn)){
				if(!strValue.equals("")){
					conn.addParameter(param, strValue);
					
					if(param.equals(ConnectionParameter.IPADDRESS)){
						addRequest(strValue);
					}
				}
				if(digValue != 0){
					conn.addParameter(param, digValue);
				}
				isNewConn = false;
			}
		}
		if(isNewConn){
			processedConnections.push(newConn);
		}
		
		if (processedConnections.size() > 16) {
			processedConnections.pollLast();
		}
	}
	
	public void addProcessedConnection(Channel channel, ConnectionParameter param, long value){
		addProcessedConnection(channel, param, "", value);
	}
	
	public void addProcessedConnection(Channel channel, ConnectionParameter param, String value){
		addProcessedConnection(channel, param, value, 0);
}
	
	public LinkedList<ProcessedConnection> getProcessedConnections() {
		return processedConnections;
	}


}
