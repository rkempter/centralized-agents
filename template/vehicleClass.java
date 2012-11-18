package template;

import java.util.HashMap;
import java.util.Map;

import logist.simulation.Vehicle;

public class vehicleClass {
	private Map<Integer,Vehicle> vehicleM =new HashMap<Integer, Vehicle>();

	public Map<Integer, Vehicle> getVehicleM() {
		return vehicleM;
	}

	public vehicleClass(){
	}

	public vehicleClass(Map<Integer, Vehicle> map){
		for(Map.Entry<Integer, Vehicle> entry : map.entrySet()){
			vehicleM.put(entry.getKey(), entry.getValue());
		}
	}
	
	public void addKeyValue(int key, Vehicle value){
		vehicleM.put(key, value);
	}
	
	public Vehicle getValue(int key) {	
		return vehicleM.get(key);
	}

}
