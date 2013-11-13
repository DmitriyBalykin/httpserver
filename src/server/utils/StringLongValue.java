package server.utils;

public class StringLongValue {
	private long digValue;
	private String strValue;
	
	public StringLongValue(String value) {
		strValue = value;
	}
	public StringLongValue (long value) {
		digValue = value;
	}
	
	public long getLong(){
		return digValue;
	}
	
	public String getString(){
		if(digValue == 0){
			return strValue;
		} else {
			return Long.toString(digValue);
		}
	}
	
	public void setString(String str){
		strValue = str;
	}
}
