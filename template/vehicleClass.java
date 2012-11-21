package template;

import java.util.HashMap;
import java.util.Map;

import logist.simulation.Vehicle;

public class vehicleClass {
	private Map<Integer,Vehicle> vehicleM =new HashMap<Integer, Vehicle>();

	public vehicleClass(){
	}

	public vehicleClass(Map<Integer, Vehicle> map){
		for(Map.Entry<Integer, Vehicle> entry : map.entrySet()){
			vehicleM.put(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Adds or updates a key, value combination
	 * @param key
	 * @param value
	 */
	public void addKeyValue(int key, Vehicle value){
		vehicleM.put(key, value);
	}
	
	/**
	 * Returns the vehicle of task (task as hash)
	 * 
	 * @param key
	 * @return
	 */
	public Vehicle getValue(int key) {	
		return vehicleM.get(key);
	}
	
	/**
	 * Getter function of vehicle M
	 * @return
	 */
	public Map<Integer, Vehicle> getVehicleM() {
		return vehicleM;
	}

}
