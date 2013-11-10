package server.utils;

import java.util.Date;
import java.util.List;

public class StatFormatter {
	private StringBuilder sb;
	public StatFormatter() {
		sb = new StringBuilder();
	}
	public String formatHTMLTable(StatCollector stat){
		
		add("<!doctype HTML>");
		add("<html>");
		add("<body>");
		add("<table border=1>");
		add("<tr>Server statistic:");
		
		// Total requests
		add("<tr><td>Total requests<td>");
		add(stat.getRequests());
		
		// Unique requests number
		add("<tr><td>Unique IP requests<td>");
		add(stat.getUniqIPRequestsNumber());
		
		//Unique requests table
		add("<tr><td>Unique IP requests details");
		add("<td><table cellpadding=10 width=\"100%\">");
		if(stat.getUniqIPRequests().size() > 0){
			add("<tr><td>IP address<td>Requests number<td>Last request date");
		}
		for(ProcessedRequest req:stat.getUniqIPRequests()){
			add("<tr><td>");
				add(req.getIPAddress());
			add("<td>");
				add(req.getRequestsNumber());
			add("<td>");
				add(req.getLastRequestDate());
		}
		add("</table>");
		
		//Redirections table
		add("<tr><td>Redirections table");
		add("<td><table cellpadding=10>");
		if(stat.getRedirects().keySet().size() > 0){
			add("<tr><td>Redirect URL<td>Redirects number");
		}
		for(String url:stat.getRedirects().keySet()){
			add("<tr><td>");
				add(url);
			add("<td>");
				add(stat.getRedirects().get(url));
		}	
		add("</table>");
		
		//Opened connections number
		add("<tr><td>Opened connections number<td>");
		add(stat.getOpenedConnections());
		
		//Last processed connections table
		add("<tr><td>Last processed connections");
		add("<td><table cellpadding=10>");
		List<ProcessedConnection> connections = stat.getProcessedConnections();
		if(connections.size() > 0){
			//print row with columns headers
			add("<tr>");
			for(String colName:connections.get(0).getHeader()){
				add("<td>");
				add(colName);
			}
			//print cell values, skipping first row, that relates to current connection and is incomplete
			for(ProcessedConnection conn:connections.subList(1, connections.size())){
				add("<tr>");
				for(String cellValue:conn.getValues()){
					add("<td>");
					add(cellValue);
				}
			}	
		}
		add("</table>");
		
		//end
		add("</table>");
		add("</body>");
		add("</html>");
		
		return sb.toString();
	}
	
	private void add(String str){
		sb.append(str).append("\n");
	}
	
	private void add(int str){
		sb.append(str).append("\n");
	}
	
	private void add(Date str){
		sb.append(str).append("\n");
	}
}
