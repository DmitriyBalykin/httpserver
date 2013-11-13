package server.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class LimitedHashMap<K,V>{
	private LinkedList<K> keysList;
	private LinkedList<V> valuesList;
	private HashMap<K,V> map;
	private final int targetCapacity;
	
	LimitedHashMap(int capacity){
		targetCapacity = capacity;
		map = new HashMap<K,V>(capacity);
		keysList = new LinkedList<K>();
		valuesList = new LinkedList<V>();
	}
	
	public synchronized V add(K key, V value) {
		//store new key-value pair if it is new else return link to existed value
		V result = null;
		if(map.containsValue(value)){
			
			result = map.get(key);
		} else {
			
			map.put(key, value);
			keysList.push(key);
			valuesList.push(value);
		}
		
		if(keysList.size() > targetCapacity){
			//remove from map and lists key and value that older than targetCapacity
			map.remove(keysList.pollLast());
			valuesList.pollLast();
		}
		return result;
	}
	
	public List<V> getValues(){
		return (List<V>) valuesList.clone();
	}
	
	public int size(){
		return keysList.size();
	}
	
}
