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
	
	public Initialization(TaskSet taskDist, List<Vehicle> vehicleList){
		actionStates[] actions= {actionStates.PICKUP, actionStates.DELIVER};
		nT = new nextTask();
		timeM = new timeClass();
		vehicleM = new vehicleClass();		

		//copy iterator to List because can not go back and forth on an iterator!!!
		Iterator<Task> taskIter= taskDist.iterator();
		List<Task> TaskSet = new ArrayList<Task>();
		while (taskIter.hasNext())
			TaskSet.add(taskIter.next());
		//
		int t_idx= 0;
		Vehicle lastVehicle = null;
		Integer lastKey = null;

		for(int i=0; i< vehicleList.size(); i++){
			System.out.println("vehicle:"+ vehicleList.get(i).id());
			int currentVehicleCapacity= vehicleList.get(i).capacity();
			timeM.addKeyValue(vehicleList.get(i).id(), 0);
			int time= 1;

			while (t_idx<TaskSet.size() && TaskSet.get(t_idx).weight<= currentVehicleCapacity){	
				System.out.println("adding task: "+ TaskSet.get(t_idx)+ " to vehicle "+ vehicleList.get(i).id());
				//adding the entry vehicle in nextT 
				if(!nT.checkKey(vehicleList.get(i).id())){
					ArrayList<Object> firstTaskAction= new ArrayList<Object>();
					firstTaskAction.add(TaskSet.get(t_idx));
					firstTaskAction.add(actionStates.PICKUP);
					nT.addKeyValue(vehicleList.get(i).id(), firstTaskAction);
				}
				//updating the previous entry in nextT
				if(lastVehicle!= null && !lastKey.equals(null)  && vehicleList.get(i).equals(lastVehicle)){
					ArrayList<Object> taskAction= new ArrayList<Object>();
					taskAction.add(TaskSet.get(t_idx));
					taskAction.add(actionStates.PICKUP);
					nT.addKeyValue(lastKey, taskAction);
				}				
				currentVehicleCapacity-= TaskSet.get(t_idx).weight;
				for(int j=0; j< actions.length; j++){
					if(actions[j].equals(actionStates.PICKUP)){
						ArrayList<Object> taskAction= new ArrayList<Object>();
						taskAction.add(TaskSet.get(t_idx));
						taskAction.add(actionStates.DELIVER);
						//System.out.println("adding link:"+ TaskSet.get(t_idx).toString()+ actions[j]+" to " + taskAction);
						nT.addKeyValue((TaskSet.get(t_idx).toString()+ actions[j]).hashCode(), taskAction);
					}
					else{
						lastKey= (TaskSet.get(t_idx).toString()+ actions[j]).hashCode();		//save key to retrieve it on the next loop
						nT.addKeyValue(lastKey, null);
					}
					//adding entry in timeM
					timeM.addKeyValue((TaskSet.get(t_idx).toString()+ actions[j]).hashCode(), time);
					time++;
					//adding entry in vehicleM
					vehicleM.addKeyValue((TaskSet.get(t_idx).toString()+ actions[j]).hashCode(), vehicleList.get(i));
				}
				lastVehicle= vehicleList.get(i);				//save vehicle to compare to the next currentVehicle on the next loop
				t_idx++;
			}
			if(!nT.checkKey(vehicleList.get(i).id())){
				nT.addKeyValue(vehicleList.get(i).id(), null);
			}
		}
		//System.out.println(nT.getNextTask());
	}
	
	public nextTask getNextTask() {
		return nT;
	}
	
	public timeClass getTimeArray() {
		return timeM;
	}

	public vehicleClass getVehicleArray() {
		return vehicleM;
	}
	
	public ArrayList<Task> getPlanVehicle(Vehicle v){
		ArrayList<Task> vehiclePlan= new ArrayList<Task>();			//when get the plan test with .isEmpty() not with .equals(null)
		int key= v.id();
		
		while(nT.getValue(key)!= null){
			if(((actionStates)nT.getValue(key).get(1)).equals(actionStates.PICKUP)){		//in the plan just need to save one of the two action. It matters the task
				System.out.println("test");
				vehiclePlan.add((Task)nT.getValue(key).get(0));
				key= (nT.getValue(key).get(0).toString()+ actionStates.DELIVER).hashCode();
			}
		}
		return vehiclePlan;
	}
	
	//just  a debug function to make sure everything works
	public ArrayList<Integer> getTimeVehicle(Vehicle v){
		ArrayList<Integer> timeV= new ArrayList<Integer>();

		ArrayList<Object> startValue = nT.getValue(v.id());
		if(startValue!=null){
			//generate key from value
			int key= (((Task)startValue.get(0)).toString()+ (actionStates)startValue.get(1)).hashCode();
			timeV.add(timeM.getValue(key));
			while(nT.getValue(key)!=null){

				ArrayList<Object> nextValue= nT.getValue(key);
				//System.out.println(nextValue);
				key= (((Task)nextValue.get(0)).toString()+ (actionStates)nextValue.get(1)).hashCode();	

				timeV.add(timeM.getValue(key));
			}
		}
		return timeV;
	}
	
}
