//for nextT map the keys are: for task+ action = (task.toString()+ "P (or D)").hashCode(). 
//for vehicle the key is vehicle.id() (because the object vehicle does change)

package template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class nextTask {
	private Map<Object,ArrayList<Object>> nextT = new HashMap<Object, ArrayList<Object>>();
	
	public nextTask(){	}
	/**
	 * By converting current Task into a Hashcode and use
	 * as the key, we find the next task
	 * 
	 * @param task
	 * @return next
	 */
	public ArrayList<Object> getValue(int key) {	
		return nextT.get(key);
	}

	public Map<Object,ArrayList<Object>> getNextTask() {
		return nextT;
	}

	public boolean checkKey(int id){
		if (nextT.containsKey(id))
				return true;
		else
			return false;
	}
	
	public void addKeyValue(int key, ArrayList<Object> value){
		nextT.put(key, value);
	}
	
	public void printState() {
		System.out.println(nextT);
	}
}


