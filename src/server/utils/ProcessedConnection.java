package server.utils;

import io.netty.channel.Channel;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ProcessedConnection {
	
	public enum ConnectionParameter {
		
		IPADDRESS("Source IP address"),
		URI("URI"),
		TIME_STAMP("Time stamp"),
		SENT_BYTES("Sent bytes"),
		RECEIVED_BYTES("Received bytes"),
		SPEED("Speed, bytes/s");
		
		private final String header;
		private ConnectionParameter(String header) {this.header = header;}
		public String toString() {return header;}
		
	}
	
	Map<ConnectionParameter, String> valuesMap = new LinkedHashMap<ConnectionParameter, String>(6);
	Channel channel;
	
	public ProcessedConnection(Channel channel) {
		this.channel = channel;
		Calendar calendar = new GregorianCalendar();
		
		String timestamp = String.format("%d%02d%02d-%02d%02d%02d",
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND)
				);
		
		valuesMap.put(ConnectionParameter.IPADDRESS, "");
		valuesMap.put(ConnectionParameter.URI, "");
		valuesMap.put(ConnectionParameter.TIME_STAMP, timestamp);
		valuesMap.put(ConnectionParameter.SENT_BYTES, "");
		valuesMap.put(ConnectionParameter.RECEIVED_BYTES, "");
		valuesMap.put(ConnectionParameter.SPEED, "");
	}

	public Set<String> getHeader(){
		Set<String> headersSet = new LinkedHashSet<String>();
		for(ConnectionParameter param:valuesMap.keySet()){
			headersSet.add(param.toString());
		}
		return headersSet;
	}

	public Collection<String> getValues(){
		return valuesMap.values();
	}

	public void addParameter(ConnectionParameter parameter, String value){
		valuesMap.put(parameter, value);
	}

	public void addParameter(ConnectionParameter parameter, long value){
		if(parameter != ConnectionParameter.SPEED){
			//speed sets one time, other parameters accumulates value
			long val = 0;
			try {
				val = new Long(valuesMap.get(parameter.toString()));
			} catch (NumberFormatException e) {
				val = 0;
			}
			 
			value = value + val;
		}
		valuesMap.put(parameter, Long.toString(value));
	}

	public boolean equals(Object o){
		return (o instanceof ProcessedConnection) && ((ProcessedConnection)o).channel.equals(channel);
	}
	
	public int hashcode(){
		return channel.hashCode();
	}
	
	public String getURI(){
		return valuesMap.get(ConnectionParameter.URI);
	}
}
