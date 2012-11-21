package template;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import logist.simulation.Vehicle;

public class capacityClass {
	private ArrayList<ArrayList<Integer>> capacities;

	/**
	 * Constructor
	 * 
	 * @param _vehicleList
	 */
	public capacityClass(List<Vehicle> _vehicleList){
		capacities= new ArrayList<ArrayList<Integer>>(_vehicleList.size());
		for(int i=0; i< _vehicleList.size(); i++){
			capacities.add(new ArrayList<Integer>());
		}
	}
	
	/**
	 * Constructor
	 * 
	 * @param _vehicleList
	 * @param _capacities
	 */
	public capacityClass(List<Vehicle> _vehicleList, ArrayList<ArrayList<Integer>> _capacities){
		capacities= new ArrayList<ArrayList<Integer>>(_vehicleList.size());
		for(int i=0; i< _capacities.size(); i++){
			capacities.add(new ArrayList<Integer>());
			for(int j=0; j< _capacities.get(i).size(); j++){
				Integer newIntObject = new Integer(_capacities.get(i).get(j));
				capacities.get(i).add(newIntObject);
			}
			
		}
	}
	
	/**
	 * Method used during the Initialization. Adds capacitiy sequentially
	 * 
	 * @param vehicleId
	 * @param capacity
	 */
	public void addCapacitySequentially(int vehicleId, int capacity) {
		capacities.get(vehicleId).add(capacity);
	}
	
	/**
	 * Returns the capacity of a vehicle at a certain time
	 * @param vehicleId
	 * @param time
	 * @return
	 */
	public Integer getCapacityAtTime(int vehicleId, int time){
		if(capacities.get(vehicleId).size()>= time){
			return capacities.get(vehicleId).get(time);
		}
		else{				//should never return this
			return null;
		}
	}

	/**
	 * Sets a capacity at a specific time and vehicle
	 * 
	 * @param vehicleId
	 * @param time
	 * @param newCapacity
	 */
	public void setCapacityAtTime(int vehicleId, int time, int newCapacity){
		if(capacities.get(vehicleId).size()>=time){
			capacities.get(vehicleId).set(time, newCapacity);
		}
	}
	
	/**
	 * Updates capacities
	 * @param v
	 * @param time
	 * @param action
	 * @param taskWeight
	 * @param taskAction
	 */
	public void updateCapacitiesAfterUpdate(Vehicle v, int time, currentAction action, Integer taskWeight, actionStates taskAction){
		if(action.equals(currentAction.REMOVE)){
			int previousCapacity= 0;
			if(time==0){
				previousCapacity= v.capacity();
			}
			else{
				previousCapacity= capacities.get(v.id()).get(time-1);
			}
			int previousValue= capacities.get(v.id()).get(time);
			for(int i= time+1; i< capacities.get(v.id()).size(); i++){
				int newCapacityI= previousCapacity + capacities.get(v.id()).get(i)- previousValue;
				previousValue= capacities.get(v.id()).get(i);
				capacities.get(v.id()).set(i, newCapacityI);
				previousCapacity= newCapacityI;
			}
			capacities.get(v.id()).remove(time);
		}
		else{
			int previousCapacity= -1;
			int oldWeight= 0;
			if(time==0)
				previousCapacity= v.capacity();
			else
				previousCapacity= capacities.get(v.id()).get(time-1);
			if(taskAction.equals(actionStates.PICKUP))
				capacities.get(v.id()).add(time, previousCapacity- taskWeight);
			else
				capacities.get(v.id()).add(time, previousCapacity+ taskWeight);

			for(int i= time+1; i< capacities.get(v.id()).size(); i++){
				oldWeight= capacities.get(v.id()).get(i)- previousCapacity;
				previousCapacity= capacities.get(v.id()).get(i);
				capacities.get(v.id()).set(i, capacities.get(v.id()).get(i-1)+oldWeight);
			}
		}
	}
	/**
	 * Enforces capacity constraint
	 * 
	 * @param v
	 * @return return true if the capacity drops below zero
	 */
	public boolean checkIfBelowZero(Vehicle v){
		boolean belowZero= false;
		Iterator<Integer> capcityIt= capacities.get(v.id()).iterator();
		while(capcityIt.hasNext() && !belowZero){
			if(capcityIt.next()< 0){
				belowZero= true;
			}
		}
		return belowZero;
	}
	
	/**
	 * Getter method for capacities
	 * 
	 * @return
	 */
	public ArrayList<ArrayList<Integer>> getCapacities() {
		return capacities;
	}

	/**
	 * Debugging method
	 */
	public void printCapacities(){
		System.out.println(capacities);
	}
}
