package template;

import java.util.ArrayList;
import java.util.List;

import logist.simulation.Vehicle;

public class capacityClass {
	private ArrayList<ArrayList<Integer>> capacities;
	private List<Vehicle> vehicleList;


	public capacityClass(List<Vehicle> _vehicleList){
		capacities= new ArrayList<ArrayList<Integer>>(_vehicleList.size());
		vehicleList= _vehicleList;
	}
	public void addCapacitySequentially(int vehicleId, int capacity){		//method used only by Initialization
		if(capacities.size()<= vehicleId){
			capacities.add(new ArrayList<Integer>());
		}
		capacities.get(vehicleId).add(capacity);
	}
	public Integer getCapacityAtTime(int vehicleId, int time){
		if(capacities.get(vehicleId).size()>= time){
			return capacities.get(vehicleId).get(time);
		}
		else{		//should never return this
			return null;
		}
	}
	
	public void setCapacityAtTime(int vehicleId, int time, int newCapacity){
		if(capacities.get(vehicleId).size()>=time){
			capacities.get(vehicleId).set(time, newCapacity);
		}
	}
	public void updateCapacitiesAfterRemove(Vehicle v, int time, actionStates action){
		if(action.equals(actionStates.PICKUP)){
			int previousCapacity= 0;
			if(time==0){
				previousCapacity= v.capacity();
			}
			else{
				previousCapacity= capacities.get(v.id()).get(time-1);
			}
			for(int i= time+1; i< capacities.get(v.id()).size(); i++){
				int newCapacityI= previousCapacity + capacities.get(v.id()).get(i)- capacities.get(v.id()).get(i-1);
				capacities.get(v.id()).set(i, newCapacityI);
				previousCapacity= newCapacityI;
			}
			capacities.get(v.id()).remove(time);
		}
		//if it s a deliver i only have to delete it from the caopacities and do not need to update anything
		else{
			capacities.get(v.id()).remove(time);

		}

	}

	public void printCapacities(){
		System.out.println(capacities);
	}
}
