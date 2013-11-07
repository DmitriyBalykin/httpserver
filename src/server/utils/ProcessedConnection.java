package server.utils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ProcessedConnection {
	
	Map<String, String> valuesMap = new LinkedHashMap<String, String>();
	
	public ProcessedConnection(String sourceIP, String uri, String timestamp, long sentBytes, long receivedBytes, long speed) {
		valuesMap.put("Source IP address", sourceIP);
		valuesMap.put("URI", uri);
		valuesMap.put("Time stamp", timestamp);
		valuesMap.put("Sent bytes", Long.toString(sentBytes));
		valuesMap.put("Received bytes", Long.toString(receivedBytes));
		valuesMap.put("Speed, bytes/s", Long.toString(speed));
	}
	public Set<String> getHeader(){
		return valuesMap.keySet();
	}
	public Collection<String> getRow(){
		return valuesMap.values();
	}
	
}
