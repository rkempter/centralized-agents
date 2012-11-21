package template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class timeClass {
	private Map<Integer,Integer> timeM = new HashMap<Integer, Integer>();
	
	public timeClass(){
	}
	
	public timeClass(Map<Integer, Integer> map){
		for(Map.Entry<Integer, Integer> entry : map.entrySet()){
			timeM.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Method adds or updates a key, value pair.
	 * @param key
	 * @param value
	 */
	public void addKeyValue(int key, Integer value){
		timeM.put(key, value);
	}
	
	/**
	 * Returns the time of a taskObject (taskObject expressed as hash)
	 * @param key
	 * @return
	 */
	public Integer getValue(int key){
		return timeM.get(key);
	}
	
	/**
	 * Returns the list of tasks providing a time
	 * 
	 * @param time
	 * @return tasks
	 */
	public ArrayList<Integer> getTaskSameTime(Integer time) {
		ArrayList<Integer> keys= new ArrayList<Integer>();
		for (Map.Entry<Integer, Integer> entry : timeM.entrySet()){
			if(entry.getValue().equals(time)){
				keys.add(entry.getKey());
			}
		}
		return keys;
	}
	
	/**
	 * Getter method for timeM Map
	 * 
	 * @return
	 */
	public Map<Integer, Integer> getTimeM() {
		return timeM;
	}
}
