package server.utils;

import io.netty.channel.Channel;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import server.utils.ProcessedConnection.ConnectionParameter;

public class StatCollector {
	
	private volatile int requests = 0;
	private volatile int openedConnections = 0;
	private volatile Set<ProcessedRequest> uniqIPRequests = new HashSet<ProcessedRequest>();	//IP -> (requests number, last request time)
	private volatile Map<String, Integer> redirects = new HashMap<String, Integer>();
	private volatile LinkedList<ProcessedConnection> processedConnections = new LinkedList<ProcessedConnection>();
	private static final int MAX_CONNECTIONS_STORED = 16;
	
	public static String ipAddressToString(SocketAddress address){
		String strAddr = address.toString();
		return strAddr.substring(1, strAddr.lastIndexOf(":"));
	}
	
	public int getRequests() {
		return requests;
	}

	public synchronized void increaseOpenedConnections() {
		openedConnections ++;
	}
	
	public synchronized void decreaseOpenedConnections() throws Exception {
		openedConnections --;
		if(openedConnections < 0){
			throw new Exception("Error in counting opened connections: number cannot be less than 0");
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
	
	public synchronized void addProcessedConnection(Channel channel, ConnectionParameter param, String strValue, long digValue) {

		ProcessedConnection newConn = new ProcessedConnection(channel);

		//delete starting slash from uri string
		if(param.equals(ConnectionParameter.URI)){
			strValue = strValue.substring(1);
		}
		
		synchronized(this){
			boolean isNewConn = true;
			for(int i = 0; i < processedConnections.size(); i++){
				ProcessedConnection conn = processedConnections.get(i);
				if(newConn.equals(conn)){
					fillConnection(conn, param, strValue, digValue);
					isNewConn = false;
				}
			}
			if(isNewConn){
				fillConnection(newConn, param, strValue, digValue);
				processedConnections.push(newConn);
			}
			if(processedConnections.size() > MAX_CONNECTIONS_STORED){
				processedConnections.pollLast();
			}
		}
		
		if(param.equals(ConnectionParameter.IPADDRESS)){
			addRequest(strValue);
		}
	}
	
	public void addProcessedConnection(Channel channel, ConnectionParameter param, long value){
		addProcessedConnection(channel, param, "", value);
	}
	
	public void addProcessedConnection(Channel channel, ConnectionParameter param, String value){
		addProcessedConnection(channel, param, value, 0);
	}
	
	public List<ProcessedConnection> getProcessedConnections() {
		return (LinkedList<ProcessedConnection>) processedConnections.clone();
	}

	private void fillConnection(ProcessedConnection conn, ConnectionParameter param, String strValue, long digValue){
		if(!strValue.equals("")){
			conn.addParameter(param, strValue);
		}
		if(digValue != 0){
			conn.addParameter(param, digValue);
		}
	}
	
	private synchronized void addRequest(String ip) {
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
}
