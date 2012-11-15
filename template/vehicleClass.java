package template;

import java.util.HashMap;
import java.util.Map;

import logist.simulation.Vehicle;

public class vehicleClass {
	private Map<Object,Vehicle> vehicleM =new HashMap<Object, Vehicle>();

	public vehicleClass(){
	}
	
	public void addKeyValue(int key, Vehicle value){
		vehicleM.put(key, value);
	}
	
	public Vehicle getValue(int key) {	
		return vehicleM.get(key);
	}

}
