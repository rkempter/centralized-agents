package template;

import java.util.ArrayList;
import java.util.List;

import logist.simulation.Vehicle;

public class capacityClass {
	private ArrayList<ArrayList<Integer>> capacities;
	private List<Vehicle> vehicleList;


	public capacityClass(List<Vehicle> _vehicleList){
		capacities= new ArrayList<ArrayList<Integer>>(_vehicleList.size());
		for(int i=0; i< _vehicleList.size(); i++){
			capacities.add(new ArrayList<Integer>());
		}
		vehicleList= _vehicleList;
	}
	public void addCapacitySequentially(int vehicleId, int capacity){		//method used only by Initialization
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
	public void updateCapacitiesAfterUpdate(Vehicle v, int time, currentAction action, Integer taskWeight){
		if(action.equals(currentAction.REMOVE)){
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
		else{
			int previousCapacity= -1;
			int oldWeight= 0;
			if(time==0){
				previousCapacity= v.capacity();
			}	
			else{
				previousCapacity= capacities.get(v.id()).get(time-1);
			}
			capacities.get(v.id()).add(time, previousCapacity- taskWeight);
			for(int i= time+1; i< capacities.get(v.id()).size(); i++){
				oldWeight= capacities.get(v.id()).get(i)- previousCapacity;
				previousCapacity= capacities.get(v.id()).get(i);
				capacities.get(v.id()).set(i, capacities.get(v.id()).get(i-1)-oldWeight);
			}
		}
	}

	public void printCapacities(){
		System.out.println(capacities);
	}
}
