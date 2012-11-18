package template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class timeClass {
	private Map<Integer,Integer> timeM = new HashMap<Integer, Integer>();
	
	public Map<Integer, Integer> getTimeM() {
		return timeM;
	}

	public timeClass(){
	}
	
	public timeClass(Map<Integer, Integer> map){
		for(Map.Entry<Integer, Integer> entry : map.entrySet()){
			timeM.put(entry.getKey(), entry.getValue());
		}
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
	public ArrayList<Integer> getTaskSameTime(Integer time) {
		ArrayList<Integer> keys= new ArrayList<Integer>();
		for (Map.Entry<Integer, Integer> entry : timeM.entrySet()){
			if(entry.getValue().equals(time)){
				keys.add(entry.getKey());
			}
		}
		return keys;
	}
}
