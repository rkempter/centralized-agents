package template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class timeClass {
	private Map<Integer,Integer> timeM = new HashMap<Integer, Integer>();
	
	public timeClass(){
	}

	public void addKeyValue(int key, Integer value){
		timeM.put(key, value);
	}
	
	public Integer getValue(int key){
		return timeM.get(key);
	}
	
	/**
	 * @TODO Returns tasks for a specific time
	 * 
	 * @param time
	 * @return tasks
	 */
	public ArrayList<ArrayList<Object>> getTasks(Integer time) {
		return null;
	}
	
}
