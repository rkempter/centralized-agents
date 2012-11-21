package template;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import logist.simulation.Vehicle;
import logist.task.Task;
import logist.task.TaskSet;

public class Initialization {
	private nextTask nT;
	private timeClass timeM;
	private vehicleClass vehicleM;
	private capacityClass capacitiesC;

	/**
	 * Initialization method
	 * 
	 * @param taskDist
	 * @param vehicleList
	 */
	public Initialization(TaskSet taskDist, List<Vehicle> vehicleList){
		actionStates[] actions= {actionStates.PICKUP, actionStates.DELIVER};
		nT = new nextTask();
		timeM = new timeClass();
		vehicleM = new vehicleClass();
		capacitiesC= new capacityClass(vehicleList);
		
		//copy iterator to List because can not go back and forth on an iterator!!!
		Iterator<Task> taskIter = taskDist.iterator();
		List<Task> TaskSet = new ArrayList<Task>();
		while (taskIter.hasNext())
			TaskSet.add(taskIter.next());
		
		int t_idx = 0; // Task
		Vehicle lastVehicle = null;
		Integer lastKey = null;
		
		//random percentage for each car
		double sum=0;
		ArrayList<Double> fillPercentage = new ArrayList<Double>();
		double maxValue = 1 / (double) vehicleList.size();
		double minValue = maxValue/2;
		for(int i = 0; i < vehicleList.size()-1; i++){ 
			fillPercentage.add(minValue + (Math.random() * (maxValue - minValue)));
			sum += fillPercentage.get(i);
		}
		fillPercentage.add(1-sum);
		int last_idx=0;

		for(int i=0; i< vehicleList.size(); i++){
			int currentVehicleCapacity = vehicleList.get(i).capacity();
			int time= 0;
			last_idx = t_idx;

			while (t_idx < TaskSet.size() && TaskSet.get(t_idx).weight<= currentVehicleCapacity && ((float)(t_idx-last_idx)/(float)TaskSet.size()) < fillPercentage.get(i)){
				//adding the entry vehicle in nextT 
				if(!nT.checkKey(vehicleList.get(i).id())){
					ArrayList<Object> firstTaskObject = localSearchNode.createTaskObject(TaskSet.get(t_idx), actionStates.PICKUP);
					nT.addKeyValue(vehicleList.get(i).id(), firstTaskObject);
				}
				//updating the previous entry in nextT
				if(lastVehicle != null && !lastKey.equals(null)  && vehicleList.get(i).equals(lastVehicle)){
					ArrayList<Object> taskObject = localSearchNode.createTaskObject(TaskSet.get(t_idx), actionStates.PICKUP);
					nT.addKeyValue(lastKey, taskObject);
				}
				
				for(int j=0; j< actions.length; j++){
					if(actions[j].equals(actionStates.PICKUP)){
						currentVehicleCapacity-= TaskSet.get(t_idx).weight;
						ArrayList<Object> taskObject= localSearchNode.createTaskObject(TaskSet.get(t_idx), actionStates.DELIVER);
						nT.addKeyValue((TaskSet.get(t_idx).toString()+ actions[j]).hashCode(), taskObject);
					}
					else{
						currentVehicleCapacity += TaskSet.get(t_idx).weight;
						lastKey = (TaskSet.get(t_idx).toString() + actions[j]).hashCode();		//save key to retrieve it on the next loop
						nT.addKeyValue(lastKey, null);
					}
					//updating capacities
					capacitiesC.addCapacitySequentially(vehicleList.get(i).id(), currentVehicleCapacity);

					//adding entry in timeM
					timeM.addKeyValue((TaskSet.get(t_idx).toString()+ actions[j]).hashCode(), time);
					time++;
					//adding entry in vehicleM
					vehicleM.addKeyValue((TaskSet.get(t_idx).toString()+ actions[j]).hashCode(), vehicleList.get(i));
				}
				lastVehicle= vehicleList.get(i);
				t_idx++;
			}
			if(!nT.checkKey(vehicleList.get(i).id())){
				nT.addKeyValue(vehicleList.get(i).id(), null);
			}
		}
		capacitiesC.printCapacities();
	}
	
	/*
	 * Getter methods
	 */

	public nextTask getNextTask() {
		return nT;
	}

	public timeClass getTimeArray() {
		return timeM;
	}

	public vehicleClass getVehicleArray() {
		return vehicleM;
	}
	public capacityClass getCapacities(){
		return capacitiesC;
	}
}
