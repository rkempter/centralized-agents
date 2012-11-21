//for nextT map the keys are: for task+ action = (task.toString()+ "P (or D)").hashCode(). 
//for vehicle the key is vehicle.id() (because the object vehicle does change)

package template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class nextTask {
	private Map<Object,ArrayList<Object>> nextT = new HashMap<Object, ArrayList<Object>>();
	
	public nextTask(){ }
	
	
	public nextTask(Map<Object, ArrayList<Object>> map){
		for(Map.Entry<Object, ArrayList<Object>> entry : map.entrySet()){
			nextT.put(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Gets a key and looks for its corresponding value in the map.
	 * 
	 * @param task
	 * @return next
	 */
	public ArrayList<Object> getValue(int key) {	
		return nextT.get(key);
	}

	/**
	 * Method checks if a key exists. Returns true in that case.
	 * @param id
	 * @return
	 */
	public boolean checkKey(int id){
		if (nextT.containsKey(id))
			return true;
		else
			return false;
	}
	
	/**
	 * Adds key, value pair to the map if key doesn't exist. Otherwise
	 * the existing value in the map is replaced by the new one.
	 * 
	 * @param key
	 * @param value
	 */
	public void addKeyValue(int key, ArrayList<Object> value){
		nextT.put(key, value);
	}
	
	/**
	 * Getter method for nextT
	 * 
	 * @return
	 */
	public Map<Object, ArrayList<Object>> getNextT() {
		return nextT;
	}
	
	public void printState() {
		System.out.println(nextT);
	}
}


