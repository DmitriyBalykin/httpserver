package server.utils;

public class HtmlBuilder{
	StringBuilder sb;
	private boolean htmlopened = false;
	public HtmlBuilder() {
		sb = new StringBuilder("<!doctype HTML>");
	}
	public HtmlBuilder addHtml(){
		sb.append("<html>");
		htmlopened = true;
		return this;
	}
	public HtmlBuilder addHeader(){
		sb.append("<body>");
		htmlopened = true;
		return this;
	}
	public HtmlBuilder addBody(){
		sb.append("<html>");
		htmlopened = true;
		return this;
	}
	public HtmlBuilder addTable(){
		sb.append("<html>");
		htmlopened = true;
		return this;
	}
	public HtmlBuilder addRow(){
		sb.append("<html>");
		htmlopened = true;
		return this;
	}
	public HtmlBuilder addColumn(){
		sb.append("<html>");
		htmlopened = true;
		return this;
	}
}
