package server.utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
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
	
	Map<String, String> valuesMap = new LinkedHashMap<String, String>(6);
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
		
		valuesMap.put(ConnectionParameter.IPADDRESS.toString(), "");
		valuesMap.put(ConnectionParameter.URI.toString(), "");
		valuesMap.put(ConnectionParameter.TIME_STAMP.toString(), timestamp);
		valuesMap.put(ConnectionParameter.SENT_BYTES.toString(), "");
		valuesMap.put(ConnectionParameter.RECEIVED_BYTES.toString(), "");
		valuesMap.put(ConnectionParameter.SPEED.toString(), "");
	}

	public Set<String> getHeader(){
		return valuesMap.keySet();
	}

	public Collection<String> getRow(){
		return valuesMap.values();
	}

	public void addParameter(ConnectionParameter parameter, String value){
		valuesMap.put(parameter.toString(), value);
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
		valuesMap.put(parameter.toString(), Long.toString(value));
	}

	public boolean equals(Object o){
		return (o instanceof ProcessedConnection) && ((ProcessedConnection)o).channel.equals(channel);
	}
	
	public int hashcode(){
		return channel.hashCode();
	}
}
