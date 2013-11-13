package server.utils;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProcessedConnection {
	
	public static enum ConnectionParameter {
		
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
	
	private static int DEFINED_PARAMETERS = 6;
	private Map<ConnectionParameter, StringLongValue> valuesMap = new LinkedHashMap<ConnectionParameter, StringLongValue>(DEFINED_PARAMETERS);
	private Channel channel;
	
	public ProcessedConnection(Channel channel) {
		this.channel = channel;
		
		Calendar calendar = new GregorianCalendar();
		StringBuilder sb = new StringBuilder();

		sb
		.append(calendar.get(Calendar.YEAR))
		.append(calendar.get(Calendar.MONTH))
		.append(calendar.get(Calendar.DAY_OF_MONTH))
		.append("-")
		.append(calendar.get(Calendar.HOUR_OF_DAY))
		.append(calendar.get(Calendar.MINUTE))
		.append(calendar.get(Calendar.SECOND));
		
		valuesMap.put(ConnectionParameter.IPADDRESS, new StringLongValue(""));
		valuesMap.put(ConnectionParameter.URI, new StringLongValue(""));
		valuesMap.put(ConnectionParameter.TIME_STAMP,  new StringLongValue(sb.toString()));
		valuesMap.put(ConnectionParameter.SENT_BYTES,  new StringLongValue(0));
		valuesMap.put(ConnectionParameter.RECEIVED_BYTES,  new StringLongValue(0));
		valuesMap.put(ConnectionParameter.SPEED,  new StringLongValue(0));
	}
	
	public Set<String> getHeader(){
		Set<String> headersSet = new LinkedHashSet<String>();
		for(ConnectionParameter param:valuesMap.keySet()){
			headersSet.add(param.toString());
		}
		return headersSet;
	}

	public Collection<String> getValues(){
		List<String> list = new ArrayList<String>(DEFINED_PARAMETERS);
		for(StringLongValue value: valuesMap.values()){
			list.add(value.getString());
		}
		return list;
	}

	public void addParameter(ConnectionParameter parameter, StringLongValue value){
		valuesMap.put(parameter, value);
	}

	public boolean equals(Object o){
		return (o instanceof ProcessedConnection) && ((ProcessedConnection)o).channel.equals(channel);
	}
	
	public int hashcode(){
		return channel.hashCode();
	}
	
	public String getURI(){
		return valuesMap.get(ConnectionParameter.URI).getString();
	}
}
